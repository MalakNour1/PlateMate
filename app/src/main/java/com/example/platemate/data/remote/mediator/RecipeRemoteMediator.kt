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

    @Volatile
    var forceRefresh: Boolean = false
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
            // Figure out which page to fetch
            android.util.Log.d("RecipeMediator", "load() called with loadType: $loadType")
            android.util.Log.d("RecipeMediator", "forceRefresh: $forceRefresh")
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    if (forceRefresh) {
                        android.util.Log.d("RecipeMediator", "FORCE REFRESH triggered!")
                        forceRefresh = false   // consume the flag, one-time use
                        0
                    }
                    else {
                        val lastCacheTime = recipeDao.getLatestCacheTime()
                        val isCacheStillFresh = lastCacheTime != null &&
                                (System.currentTimeMillis() - lastCacheTime) < CACHE_TIMEOUT_MS

                        android.util.Log.d("RecipeMediator", "Cache check: lastCacheTime=$lastCacheTime, isFresh=$isCacheStillFresh")

                        if (isCacheStillFresh) {
                            android.util.Log.d("RecipeMediator", "Cache is still fresh, returning from cache")
                            // Room already has the data, PagingSource will read it directly.
                            return MediatorResult.Success(endOfPaginationReached = true)
                        }
                        android.util.Log.d("RecipeMediator", "Cache expired, fetching from API")
                        0 // no cache, or it's stale -> fetch page 0 fresh
                    }
                }

                LoadType.PREPEND ->
                    // Spoonacular pages forward only, nothing to load "before" page 0
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    android.util.Log.d("RecipeMediator", "APPEND: loading next page")
                    val lastItem = state.lastItemOrNull() // type: RecipeEntity
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    val remoteKey = remoteKeyDao.getRemoteKeyByRecipeId(lastItem.id)
                    remoteKey?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            // Call the API for that page
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

            // Save everything (recipes & their remote keys) as one atomic unit
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
                android.util.Log.d("RecipeMediator", "🕐 API call at: ${System.currentTimeMillis()}")
                android.util.Log.d("RecipeMediator", "🕐 Cache time: ${recipeDao.getLatestCacheTime()}")
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            android.util.Log.e("RecipeMediator", "Load failed", e)
            MediatorResult.Error(e)
        }
    }
}