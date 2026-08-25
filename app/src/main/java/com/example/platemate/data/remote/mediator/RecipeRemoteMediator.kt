package com.example.platemate.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.platemate.data.local.database.PlateMateDatabase
import com.example.platemate.data.local.entity.RecipeEntity
import com.example.platemate.data.local.entity.RemoteKeyEntity
import com.example.platemate.data.mapper.toEntity
import com.example.platemate.data.remote.api.RecipeApi

@OptIn(ExperimentalPagingApi::class)
class RecipeRemoteMediator(
    private val recipeApi: RecipeApi,
    private val database: PlateMateDatabase,
    private val apiKey: String
) : RemoteMediator<Int, RecipeEntity>() {

    private val recipeDao = database.recipeDao()
    private val remoteKeyDao = database.remoteKeyDao()

    companion object {
        private const val CACHE_TIMEOUT_MS = 15 * 60 * 1000L // 15 minutes
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {
        return try {
            // 1. Figure out which page to fetch
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val lastCacheTime = recipeDao.getLatestCacheTime()
                    val isCacheStillFresh = lastCacheTime != null &&
                            (System.currentTimeMillis() - lastCacheTime) < CACHE_TIMEOUT_MS

                    if (isCacheStillFresh) {
                        // Cache is still good -> don't hit the network at all.
                        // Room already has the data, PagingSource will read it directly.
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    0 // no cache, or it's stale -> fetch page 0 fresh
                }

                LoadType.PREPEND ->
                    // Spoonacular pages forward only, nothing to load "before" page 0
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val remoteKey = remoteKeyDao.getRemoteKeyByRecipeId(lastItem.id)
                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // 2. Actually call the API for that page
            val pageSize = state.config.pageSize
            val offset = page * pageSize

            val response = recipeApi.searchRecipes(
                number = pageSize,
                offset = offset,
                apiKey = apiKey
            )

            val endOfPaginationReached =
                response.results.isEmpty() ||
                        (offset + response.results.size) >= response.totalResults

            // 3. Save everything (recipes & their remote keys) as one atomic unit
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    recipeDao.clearRecipes()
                    remoteKeyDao.clearRemoteKeys()
                }

                val entities = response.results.map {
                    it.toEntity(cachedAt = System.currentTimeMillis())
                }
                val keys = response.results.map {
                    RemoteKeyEntity(
                        recipeId = it.id,
                        prevPage = if (page == 0) null else page - 1,
                        nextPage = if (endOfPaginationReached) null else page + 1
                    )
                }

                recipeDao.insertRecipes(entities)
                remoteKeyDao.insertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            android.util.Log.e("RecipeMediator", "Load failed", e)
            MediatorResult.Error(e)
        }
    }
}