package com.example.platemate.data.repository

import com.example.platemate.data.local.dao.RecipeDao
import com.example.platemate.data.mapper.toDomain
import com.example.platemate.data.mapper.toEntity
import com.example.platemate.data.remote.api.RecipeApi
import com.example.platemate.domain.repository.RecipeRepository
import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
    private val recipeDao: RecipeDao,
    private val apiKey: String
) : RecipeRepository {

    override fun getRecipes(): Flow<List<Recipe>> =
        recipeDao.observeRecipes().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshRecipes() {
        val response = recipeApi.searchRecipes(apiKey = apiKey)
        val entities = response.results.map { it.toEntity(cachedAt = System.currentTimeMillis()) }
        recipeDao.insertRecipes(entities)
    }

    override fun getRecipeById(id: Int): Flow<Recipe?> =
        recipeDao.observeRecipeById(id).map { it?.toDomain() }
}