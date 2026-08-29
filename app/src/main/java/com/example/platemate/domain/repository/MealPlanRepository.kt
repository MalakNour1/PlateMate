package com.example.platemate.domain.repository

import com.example.platemate.domain.model.MealPlan
import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface MealPlanRepository {
    fun observeMealPlan(): Flow<List<MealPlan>>
    suspend fun ensureWeeklyRandomFill()
    suspend fun assignManually(day: String, recipe: Recipe)
}