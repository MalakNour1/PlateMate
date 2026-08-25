package com.example.platemate.data.remote.dto

data class IngredientDto(
    val name: String,
    val amount: Double?,
    val unit: String?,
    val meta: List<String>?
)
