package com.example.platemate.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.platemate.data.local.database.PlateMateDatabase
import com.example.platemate.data.mapper.toDomain
import com.example.platemate.data.remote.api.RecipeApi
import com.example.platemate.data.remote.mediator.RecipeRemoteMediator
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
    private val database: PlateMateDatabase,   // <-- was recipeDao, now needs the whole database
    private val apiKey: String
) : RecipeRepository {

    private val recipeDao = database.recipeDao()

    override fun getPagedRecipes(): Flow<PagingData<Recipe>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = RecipeRemoteMediator(recipeApi, database, apiKey),
            pagingSourceFactory = { recipeDao.pagingSource() }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }

    override fun getAllRecipes(): Flow<List<Recipe>> =
        recipeDao.observeRecipes().map { entities -> entities.map { it.toDomain() } }

    override fun getRecipeById(id: Int): Flow<Recipe?> =
        recipeDao.observeRecipeById(id).map { it?.toDomain() }
}