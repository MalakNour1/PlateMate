package com.example.platemate.presentation.state

import com.example.platemate.domain.model.Recipe

sealed interface RecipeUiState {
    data object Loading : RecipeUiState
    data class Success(
        val recipes:List<Recipe>
    ) : RecipeUiState

    data class Error(
        val message : String
    ) : RecipeUiState
    data object  Empty : RecipeUiState

}