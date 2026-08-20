package com.example.platemate.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.platemate.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

//all our db operations
@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
    fun observeRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun observeRecipeById(recipeId: Int): Flow<RecipeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM recipes")
    suspend fun clearRecipes()

    @Query("SELECT MAX(cachedAt) FROM recipes")
    suspend fun getLatestCacheTime(): Long?
}