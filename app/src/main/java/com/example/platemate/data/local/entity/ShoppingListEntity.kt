package com.example.platemate.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list")
data class ShoppingListEntity(
    @PrimaryKey val id: String,   // "$name-$amount" — same key used by the old distinctBy logic
    val name: String,
    val amount: String?,
    val isChecked: Boolean
)