package com.example.platemate.data.remote

import com.example.platemate.data.remote.api.RecipeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val recipeApi: RecipeApi by lazy {
        retrofit.create(RecipeApi::class.java)
    }
}