package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val recipeId: Int
)
// Deliberately separate from RecipeEntity: recipe rows get REPLACE-d on
// every re-cache (refresh, re-fetching details), which would silently
// wipe a favorite flag stored on that same row. Keeping favorites in
// their own table means re-caching a recipe never touches this data.