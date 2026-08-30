package com.example.platemate.presentation.viewmodel.shoppingList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.platemate.domain.repository.ShoppingListRepository

class ShoppingListViewModelFactory(
    private val repository: ShoppingListRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShoppingListViewModel::class.java)) {
            return ShoppingListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}