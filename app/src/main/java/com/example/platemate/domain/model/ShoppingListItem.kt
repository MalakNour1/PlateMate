package com.example.platemate.domain.model

data class ShoppingListItem(
    val name: String,
    val amount: String?,
    val isChecked: Boolean = false
)
