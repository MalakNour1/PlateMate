package com.example.platemate.domain.repository

import com.example.platemate.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeShoppingList(): Flow<List<ShoppingListItem>>
    suspend fun addToShoppingList(items: List<ShoppingListItem>)
    suspend fun toggleShoppingItem(item: ShoppingListItem)
}