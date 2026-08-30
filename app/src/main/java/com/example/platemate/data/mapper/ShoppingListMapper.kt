package com.example.platemate.data.mapper

import com.example.platemate.data.local.entity.ShoppingListEntity
import com.example.platemate.domain.model.ShoppingListItem

fun ShoppingListItem.toEntity(): ShoppingListEntity {
    return ShoppingListEntity(
        id = "$name-$amount",
        name = name,
        amount = amount,
        isChecked = isChecked
    )
}

fun ShoppingListEntity.toDomain(): ShoppingListItem {
    return ShoppingListItem(
        name = name,
        amount = amount,
        isChecked = isChecked
    )
}