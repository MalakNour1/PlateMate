package com.example.platemate.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.platemate.data.local.dao.RecipeDao
import com.example.platemate.data.local.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = true
)
abstract class PlateMateDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
}