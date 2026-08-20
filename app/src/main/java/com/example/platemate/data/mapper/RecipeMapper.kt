package com.example.platemate.data.mapper

import com.example.platemate.data.remote.dto.RecipeDto
import com.example.platemate.data.local.entity.RecipeEntity
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.model.Ingredient
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

private val gson = Gson()

fun RecipeDto.toEntity(cachedAt: Long): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        imageUrl = image,
        category = null,
        ingredientsJson = gson.toJson(emptyList<Ingredient>()), // "[]" for now
        stepsJson = gson.toJson(emptyList<String>()),
        cachedAt = cachedAt
    )
}

fun RecipeEntity.toDomain(): Recipe {
    return Recipe(
        id = id,
        title = title,
        imageUrl = imageUrl,
        category = category,
        ingredients = decodeIngredients(ingredientsJson),
        steps = decodeSteps(stepsJson)
    )
}

private fun decodeIngredients(json: String): List<Ingredient> {
    if (json.isBlank()) return emptyList()
    val type = object : TypeToken<List<Ingredient>>() {}.type
    return gson.fromJson(json, type)
}

private fun decodeSteps(json: String): List<String> {
    if (json.isBlank()) return emptyList()
    val type = object : TypeToken<List<String>>() {}.type
    return gson.fromJson(json, type)
}