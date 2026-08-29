package com.example.platemate.data.mapper

import com.example.platemate.data.local.entity.MealPlanEntity
import com.example.platemate.domain.model.MealPlan
import com.example.platemate.domain.model.MealSource
import com.example.platemate.domain.model.Recipe
fun MealPlanEntity.toDomain() = MealPlan(
    id = id,
    weekStartEpochDay = weekStartEpochDay,
    day = day,
    recipe = Recipe(
        id = recipeId,
        title = recipeTitle,
        imageUrl = recipeImageUrl,
        category = null,
        ingredients = emptyList(),
        steps = emptyList()
    ),
    source = MealSource.valueOf(source),
    assignedAt = assignedAt
)

fun MealPlan.toEntity() = MealPlanEntity(
    id = id, weekStartEpochDay = weekStartEpochDay, day = day,
    recipeId = recipe.id, source = source.name, assignedAt = assignedAt,
    recipeTitle = recipe.title,
    recipeImageUrl = recipe.imageUrl
)