package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,

    val title: String,

    val imageUrl: String?,

    val category: String?,

    val ingredientsJson: String,

    val stepsJson: String,

    val cachedAt: Long
)