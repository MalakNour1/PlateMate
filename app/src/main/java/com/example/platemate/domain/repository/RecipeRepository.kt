package com.example.platemate.domain.repository

import androidx.paging.PagingData
import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getPagedRecipes(): Flow<PagingData<Recipe>>   // for Home screen
    fun getAllRecipes(): Flow<List<Recipe>>            // for Favorites/Detail — reads full cache
    fun getRecipeById(id: Int): Flow<Recipe?>
    fun requestForceRefresh()
    suspend fun fetchAndCacheRecipeDetails(recipeId: Int)

    fun observeFavoriteIds():Flow<Set<Int>>
    suspend fun setFavorite(recipeId: Int, isfavorite: Boolean)
}