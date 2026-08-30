package com.example.platemate.data.repository

import com.example.platemate.data.local.dao.ShoppingListDao
import com.example.platemate.data.mapper.toDomain
import com.example.platemate.data.mapper.toEntity
import com.example.platemate.domain.model.ShoppingListItem
import com.example.platemate.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShoppingListRepositoryImpl (
    private val shoppingListDao :ShoppingListDao
): ShoppingListRepository{

    override fun observeShoppingList(): Flow<List<ShoppingListItem>> =
        shoppingListDao.observeItems().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addToShoppingList(items: List<ShoppingListItem>) {
        shoppingListDao.insertAll(items.map { it.toEntity() })
    }

    override suspend fun toggleShoppingItem(item: ShoppingListItem) {
        val id = "${item.name}-${item.amount}"
        shoppingListDao.setChecked(id, !item.isChecked)
    }
}