package com.example.platemate.data.remote.api

import com.example.platemate.data.remote.dto.RecipeDto
import retrofit2.http.GET
import retrofit2.http.Query

// is this right? shouldn't it be only contracts ? // call type response?
interface RecipeApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String? = null,
        @Query("number") number: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("apiKey") apiKey: String
    ): SearchResponseDto
}

data class SearchResponseDto(
    val results: List<RecipeDto>,
    val offset: Int,
    val number: Int,
    val totalResults: Int
)