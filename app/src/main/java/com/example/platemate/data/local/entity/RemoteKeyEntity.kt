package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey
    val recipeId: Int,       // same id as the recipe it's tracking
    val prevPage: Int?,      // null = this is the first page
    val nextPage: Int?       // null = no more pages (reached the end)
)