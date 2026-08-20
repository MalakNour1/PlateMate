package com.example.platemate.domain.repository

import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(): Flow<List<Recipe>>
    suspend fun refreshRecipes()          // triggers the API call + cache write
    fun getRecipeById(id: Int): Flow<Recipe?>
}