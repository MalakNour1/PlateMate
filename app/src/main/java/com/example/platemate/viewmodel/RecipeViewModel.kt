package com.example.platemate.viewmodel

import androidx.lifecycle.ViewModel
import com.example.platemate.model.Recipe
import com.example.platemate.state.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RecipeUiState>(
        RecipeUiState.Success(
        listOf(
            Recipe(
                id = 1,
                title = "chicken Pasta",
                image = null,
                summary = "creamy chicken pasta"
            ),
            Recipe(
                id = 2,
                title = "beef rice",
                image = null,
                summary = "beef rice"
            ),
            Recipe(
                id = 3,
                title = "Pancakes",
                image = null,
                summary = "fluffy homemade pancakes"
            )
        ) )
    )
    val uiState : StateFlow<RecipeUiState> = _uiState.asStateFlow()
}