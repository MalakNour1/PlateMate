package com.example.platemate.data.remote.api

import com.example.platemate.data.remote.dto.RecipeDetailDto
import com.example.platemate.data.remote.dto.RecipeDto
import com.example.platemate.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
interface RecipeApi {

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String? = null,
        @Query("number") number: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("addRecipeInformation") addRecipeInformation : Boolean =true,
        @Query("apiKey") apiKey: String
    ): SearchResponseDto

    @GET("recipes/{id}/information")
    suspend fun getRecipeDetails(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String
    ): RecipeDetailDto
}

