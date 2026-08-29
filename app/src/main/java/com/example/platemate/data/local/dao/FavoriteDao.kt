package com.example.platemate.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.platemate.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite:FavoriteEntity)

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun removeFavorite(recipeId: Int)

    @Query("SELECT recipeId FROM favorites")
    fun observeFavoritesIds(): Flow<List<Int>>
}