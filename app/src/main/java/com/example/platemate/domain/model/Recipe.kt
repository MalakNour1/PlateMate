package com.example.platemate.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val category: String?,
    val ingredients: List<Ingredient>,
    val steps: List<Step>
)