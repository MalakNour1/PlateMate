package com.example.platemate.data.remote.dto

data class SearchResponseDto(
    val results: List<RecipeDto>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)
