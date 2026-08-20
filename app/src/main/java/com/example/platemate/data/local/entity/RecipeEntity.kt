package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//fully functional db table
@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int, // use Spoonacular's real recipe id, no autoGenerate

    val title: String,

    val imageUrl: String?,

    val category: String?,

    val ingredientsJson: String,

    val stepsJson: String,

    val cachedAt: Long
)