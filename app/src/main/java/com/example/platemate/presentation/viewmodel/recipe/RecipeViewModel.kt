package com.example.platemate.presentation.viewmodel.recipe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.platemate.data.connectivity.NetworkMonitor
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.repository.RecipeRepository
import com.example.platemate.presentation.state.RecipeUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository: RecipeRepository,
    networkMonitor: NetworkMonitor

) : ViewModel() {

    val isConnected: StateFlow<Boolean> = networkMonitor.isConnected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            initialValue = true
        )
        .also { flow ->
            viewModelScope.launch {
                flow.collect { value ->
                    Log.d("ViewModel", "isConnected changed to: $value")
                }
            }
        }
    val pagedRecipes: Flow<PagingData<Recipe>> =
        repository.getPagedRecipes().cachedIn(viewModelScope)

    val uiState: StateFlow<RecipeUiState> = repository
        .getAllRecipes()
        .map { recipes ->
            if (recipes.isEmpty()) RecipeUiState.Empty else RecipeUiState.Success(
                recipes
            )
        }
        .catch { error -> emit(RecipeUiState.Error(error.message ?: "Something went wrong")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RecipeUiState.Loading)

    // Sourced from Room via the repository now, instead of an in-memory
    // set -- survives process death and app restarts.
    val favoriteIds: StateFlow<Set<Int>> = repository.observeFavoriteIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggleFavorite(recipeId: Int) {
        Log.d("ViewModel", "toggleFavorite called for id=$recipeId")
        viewModelScope.launch {
            val isCurrentlyFavorite = favoriteIds.value.contains(recipeId)
            Log.d(
                "ViewModel",
                "recipeId=$recipeId currently favorite=$isCurrentlyFavorite, setting to ${!isCurrentlyFavorite}"
            )
            try {
                repository.setFavorite(recipeId, !isCurrentlyFavorite)
                Log.d("ViewModel", "setFavorite completed for id=$recipeId")
            } catch (e: Exception) {
                Log.e("ViewModel", "setFavorite FAILED for id=$recipeId: ${e.message}", e)
            }
        }
    }

    fun onPullToRefresh() {
        Log.d("ViewModel", "Pull to refresh triggered!")
        repository.requestForceRefresh()
    }

    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail.asStateFlow()
    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()
    fun fetchRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            // 1. Show whatever's already cached immediately — works fully offline,
            //    since this recipe came from a list/favorites screen that's already in Room.
            val cached = repository.getRecipeById(recipeId).first()
            if (cached != null) {
                _recipeDetail.value = cached
                _isLoadingDetail.value = false
            } else {
                _recipeDetail.value = null
                _isLoadingDetail.value = true
            }

            // 2. Try to fetch full details (ingredients/steps) and upgrade in place.
            //    If this fails (offline, API error), keep showing whatever we already
            //    had from step 1 instead of wiping it.
            try {
                Log.d("ViewModel", "Fetching detail for recipe $recipeId")
                repository.fetchAndCacheRecipeDetails(recipeId)
                val enriched = repository.getRecipeById(recipeId).first()
                if (enriched != null) {
                    _recipeDetail.value = enriched
                    Log.d("ViewModel", "Recipe detail loaded: ${enriched.title}")
                }
            } catch (e: Exception) {
                Log.e(
                    "ViewModel",
                    "Could not refresh recipe detail, showing cached data if any: ${e.message}"
                )
                // Intentionally NOT clearing _recipeDetail here — offline should show
                // whatever was already cached, not "Recipe not found".
            } finally {
                _isLoadingDetail.value = false
            }
        }
    }
}