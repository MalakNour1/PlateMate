package com.example.platemate.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.RecipeRepository
import com.example.platemate.state.RecipeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
    private val uiState : StateFlow<RecipeUiState> = repository
        .getRecipes()
        .map { recipes ->
            if(recipes.isEmpty())
            {
                RecipeUiState.Empty
            } else {
                RecipeUiState.Success(recipes)
            }
        }
        .catch { error->
            emit(RecipeUiState.Error(error.message?:"Something went wrong"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RecipeUiState.Loading
        )


}