package com.example.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDetailDto(
    val id: Int,
    val title: String,
    val image: String?,

    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,

    val servings: Int?,

    @SerializedName("extendedIngredients")
    val ingredients: List<IngredientDto>,

    @SerializedName("analyzedInstructions")
    val instructions: List<InstructionDto>,

    val plainInstructions: String?,

    @SerializedName("dishTypes")
    val dishTypes:List<String>?=null
)
