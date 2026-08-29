package com.example.platemate.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    val id: Int,
    val title: String,
    val image: String?,
    //spoonacular has no single category field
    @SerializedName("dishTypes")
    val dishTypes: List<String>?=null
)