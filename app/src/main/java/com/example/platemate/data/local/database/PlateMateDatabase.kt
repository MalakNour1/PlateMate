package com.example.platemate.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.platemate.data.local.dao.MealPlanDao
import com.example.platemate.data.local.dao.RecipeDao
import com.example.platemate.data.local.dao.RemoteKeyDao
import com.example.platemate.data.local.entity.MealPlanEntity
import com.example.platemate.data.local.entity.RecipeEntity
import com.example.platemate.data.local.entity.RemoteKeyEntity

@Database(
    entities = [RecipeEntity::class, RemoteKeyEntity::class, MealPlanEntity::class],
    version = 3,
    exportSchema = true
)
abstract class PlateMateDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun remoteKeyDao(): RemoteKeyDao
    abstract fun mealPlanDao(): MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: PlateMateDatabase? = null

        fun getInstance(context: Context): PlateMateDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PlateMateDatabase::class.java,
                    "platemate_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}