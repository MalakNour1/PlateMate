package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plan")
data class MealPlanEntity(
    @PrimaryKey val id: String,            // "$weekStartEpochDay-$day" — matches MealPlan.id exactly
    val weekStartEpochDay: Long,
    val day: String,
    val recipeId: Int,
    val recipeTitle: String,
    val recipeImageUrl: String?,
    val source: String,                    // MealSource.name — "AUTO" or "MANUAL"
    val assignedAt: Long
)