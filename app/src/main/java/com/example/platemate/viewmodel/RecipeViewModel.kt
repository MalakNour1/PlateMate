package com.example.platemate.viewmodel

import android.R
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
                imageUrl = "",
                category = "",
                ingredients = TODO(),
                steps = TODO()
            ),
            Recipe(
                id = 2,
                title = "beef rice",
                imageUrl = "",
                category = "",
                ingredients = TODO(),
                steps = TODO()
            ),
            Recipe(
                id = 3,
                title = "Pancakes",
                imageUrl = "",
                category = "",
                ingredients = TODO(),
                steps = TODO()
            )
        ) )
    )
    val uiState : StateFlow<RecipeUiState> = _uiState.asStateFlow()
}