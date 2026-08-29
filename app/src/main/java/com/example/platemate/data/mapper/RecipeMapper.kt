package com.example.platemate.data.mapper

import com.example.platemate.data.local.entity.RecipeEntity
import com.example.platemate.data.remote.dto.IngredientDto
import com.example.platemate.data.remote.dto.RecipeDetailDto
import com.example.platemate.data.remote.dto.RecipeDto
import com.example.platemate.data.remote.dto.StepDto
import com.example.platemate.domain.model.Ingredient
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.model.Step
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private val gson = Gson()

private fun List<String>?.toCategoryLabel(): String? =
    this?.firstOrNull()?.replaceFirstChar { it.uppercase() }

fun RecipeDto.toEntity(cachedAt: Long): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = image,
        category = dishTypes.toCategoryLabel(),
        ingredientsJson = gson.toJson(emptyList<Ingredient>()),
        stepsJson = gson.toJson(emptyList<Step>()),
        cachedAt = cachedAt
    )
}

fun RecipeDetailDto.toEntity(cachedAt: Long): RecipeEntity {

    val steps = if (instructions.isNotEmpty()) {
        instructions.flatMap { it.steps.map { step -> step.toDomain() } }
    } else {
        plainInstructions?.let {
            listOf(Step(number = 1, instruction = it))
        } ?: emptyList()
    }

    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = image,
        category = dishTypes.toCategoryLabel(),
        ingredientsJson = gson.toJson(ingredients.map { it.toDomain() }),
        stepsJson = gson.toJson(steps),
        cachedAt = cachedAt
    )
}


fun IngredientDto.toDomain(): Ingredient {
    val fullName = if (meta != null && meta.isNotEmpty()) { // combine name+meta
        "${meta.joinToString(" ")} $name"
    } else {
        name
    }

    return Ingredient(
        name = fullName,
        amount = if (amount != null && unit != null) "$amount $unit" else amount?.toString() ?: ""
    )
}

fun StepDto.toDomain(): Step {
    return Step(
        number = number,
        instruction = step
    )
}


fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        category = category,
        ingredients = decodeIngredients(ingredientsJson),
        steps = decodeSteps(stepsJson)  // JSON -> List<Step>
    )
}

private fun decodeIngredients(json: String): List<Ingredient> {
    if (json.isBlank()) return emptyList()
    val type = object : TypeToken<List<Ingredient>>() {}.type
    return gson.fromJson(json, type)
}

private fun decodeSteps(json: String): List<Step> {
    if (json.isBlank()) return emptyList()
    val type = object : TypeToken<List<Step>>() {}.type
    return gson.fromJson(json, type)
}