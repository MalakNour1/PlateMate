package com.example.platemate.domain.model

enum class MealSource { AUTO, MANUAL }

data class MealPlan(
    val id: String,              // "$weekStartEpochDay-$day"
    val weekStartEpochDay: Long,
    val day: String,
    val recipe: Recipe,
    val source: MealSource,
    val assignedAt: Long
)