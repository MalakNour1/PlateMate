package com.example.platemate.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.platemate.data.local.database.PlateMateDatabase
import com.example.platemate.data.local.entity.FavoriteEntity
import com.example.platemate.data.mapper.toDomain
import com.example.platemate.data.mapper.toEntity
import com.example.platemate.data.remote.api.RecipeApi
import com.example.platemate.data.remote.mediator.RecipeRemoteMediator
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
    private val database: PlateMateDatabase,
    private val apiKey: String
) : RecipeRepository {

    private val recipeDao = database.recipeDao()
    private val favoriteDao = database.favoriteDao()
    private val mediator = RecipeRemoteMediator(recipeApi, database, apiKey)

    override fun getPagedRecipes(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = mediator,
            pagingSourceFactory = { recipeDao.pagingSource() }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override fun getAllRecipes(): Flow<List<Recipe>> =
        recipeDao.observeRecipes().map { entities -> entities.map { it.toDomain() } }

    override fun getRecipeById(id: Int): Flow<Recipe?> =
        recipeDao.observeRecipeById(id).map { it?.toDomain() }

    override fun requestForceRefresh() {
        mediator.forceRefresh = true
        android.util.Log.d("Repository", "Force refresh requested!")

    }

    override suspend fun fetchAndCacheRecipeDetails(recipeId: Int) { //Fetch and Cache Details
        try {
            android.util.Log.d("Repository", "Fetching details for recipe ID: $recipeId")

            val detailDto = recipeApi.getRecipeDetails(
                id = recipeId,
                apiKey = apiKey
            )

            android.util.Log.d("Repository", "API response received")
            android.util.Log.d("Repository", "Title: ${detailDto.title}")
            android.util.Log.d("Repository", "Ingredients: ${detailDto.ingredients.size}")
            android.util.Log.d("Repository", "Steps: ${detailDto.instructions.size}")

            val entity = detailDto.toEntity(cachedAt = System.currentTimeMillis())

            recipeDao.insertRecipes(listOf(entity))

            android.util.Log.d("Repository", "Recipe details cached for ID: $recipeId")

        } catch (e: Exception) {
            android.util.Log.e("Repository", "Failed to fetch recipe details for ID: $recipeId")
            android.util.Log.e("Repository", "Error: ${e.message}")
            throw e
        }
    }

    override fun observeFavoriteIds(): Flow<Set<Int>> =
        favoriteDao.observeFavoritesIds().map { it.toSet() }

    override suspend fun setFavorite(recipeId: Int, isfavorite: Boolean) {
        if(isfavorite)
        {
            favoriteDao.addFavorite((FavoriteEntity(recipeId)))
        }else{
            favoriteDao.removeFavorite(recipeId)
        }
    }
}