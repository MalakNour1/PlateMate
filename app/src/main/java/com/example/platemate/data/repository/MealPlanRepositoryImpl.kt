package com.example.platemate.data.repository

import com.example.platemate.data.local.dao.MealPlanDao
import com.example.platemate.data.local.dao.RecipeDao
import com.example.platemate.data.local.entity.MealPlanEntity
import com.example.platemate.domain.model.MealPlan
import com.example.platemate.domain.model.MealSource
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.MealPlanRepository
import com.example.platemate.domain.util.WeekUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MealPlanRepositoryImpl(
    private val mealPlanDao: MealPlanDao,
    private val recipeDao: RecipeDao
) : MealPlanRepository {

    private val days = listOf(
        "Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"
    )

    override fun observeMealPlan(): Flow<List<MealPlan>> {
        val weekStart = WeekUtils.currentWeekStartEpochDay()
        return mealPlanDao.observeForWeek(weekStart).map { entities ->
            entities.map { entity ->
                MealPlan(
                    id = entity.id,
                    weekStartEpochDay = entity.weekStartEpochDay,
                    day = entity.day,
                    recipe = Recipe(
                        id = entity.recipeId,
                        title = entity.recipeTitle,
                        imageUrl = entity.recipeImageUrl,
                        category = null,
                        ingredients = emptyList(),
                        steps = emptyList()
                    ),
                    source = MealSource.valueOf(entity.source),
                    assignedAt = entity.assignedAt
                )
            }
        }
    }

    override suspend fun ensureWeeklyRandomFill() {
        val weekStart = WeekUtils.currentWeekStartEpochDay()
        val cachedRecipes = recipeDao.observeRecipes().first()
        if (cachedRecipes.isEmpty()) return // nothing cached yet — nothing to assign

        val shuffled = cachedRecipes.shuffled()

        days.forEachIndexed { index, day ->
            val id = "$weekStart-$day"
            val existing = mealPlanDao.getById(id)

            // Week is baked into the id, so a fresh week never has an existing row.
            // No need to check "isManual" here — if it exists at all, this week's
            // slot is already decided (manually or automatically) and we leave it alone.
            if (existing == null) {
                val recipe = shuffled[index % shuffled.size]
                mealPlanDao.upsert(
                    MealPlanEntity(
                        id = id,
                        weekStartEpochDay = weekStart,
                        day = day,
                        recipeId = recipe.id,
                        recipeTitle = recipe.title,
                        recipeImageUrl = recipe.imageUrl,
                        source = MealSource.AUTO.name,
                        assignedAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    override suspend fun assignManually(day: String, recipe: Recipe) {
        val weekStart = WeekUtils.currentWeekStartEpochDay()
        val id = "$weekStart-$day"
        mealPlanDao.upsert(
            MealPlanEntity(
                id = id,
                weekStartEpochDay = weekStart,
                day = day,
                recipeId = recipe.id,
                recipeTitle = recipe.title,
                recipeImageUrl = recipe.imageUrl,
                source = MealSource.MANUAL.name,
                assignedAt = System.currentTimeMillis()
            )
        )
    }
}