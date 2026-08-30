package com.example.platemate.presentation.viewmodel.shoppingList

import androidx.lifecycle.ViewModel
import com.example.platemate.domain.model.ShoppingListItem
import com.example.platemate.domain.repository.MealPlanRepository
import com.example.platemate.domain.repository.ShoppingListRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope
import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.launch

class ShoppingListViewModel (private val repository: ShoppingListRepository)  : ViewModel() {
    val shoppingList: StateFlow<List<ShoppingListItem>> = repository.observeShoppingList()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun addRecipeToShoppingList(recipe: Recipe) {
        viewModelScope.launch {
            val newItems = recipe.ingredients.map { ingredient ->
                ShoppingListItem(name = ingredient.name, amount = ingredient.amount)
            }
            repository.addToShoppingList(newItems)
        }
    }

    fun toggleShoppingItem(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.toggleShoppingItem(item)
        }
    }
}