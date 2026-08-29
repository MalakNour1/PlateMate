package com.example.platemate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.platemate.domain.model.Recipe
import com.example.platemate.domain.model.ShoppingListItem
import com.example.platemate.domain.repository.RecipeRepository
import com.example.platemate.presentation.state.RecipeUiState
import com.example.platemate.data.connectivity.NetworkMonitor
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
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = true)
    val pagedRecipes: Flow<PagingData<Recipe>> =
        repository.getPagedRecipes().cachedIn(viewModelScope)

    val uiState: StateFlow<RecipeUiState> = repository
        .getAllRecipes()
        .map { recipes -> if (recipes.isEmpty()) RecipeUiState.Empty else RecipeUiState.Success(recipes) }
        .catch { error -> emit(RecipeUiState.Error(error.message ?: "Something went wrong")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RecipeUiState.Loading)

    // Sourced from Room via the repository now, instead of an in-memory
    // set -- survives process death and app restarts.
    val favoriteIds: StateFlow<Set<Int>> = repository.observeFavoriteIds()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggleFavorite(recipeId: Int) {
        android.util.Log.d("ViewModel", "toggleFavorite called for id=$recipeId")
        viewModelScope.launch {
            val isCurrentlyFavorite = favoriteIds.value.contains(recipeId)
            android.util.Log.d(
                "ViewModel",
                "recipeId=$recipeId currently favorite=$isCurrentlyFavorite, setting to ${!isCurrentlyFavorite}"
            )
            try {
                repository.setFavorite(recipeId, !isCurrentlyFavorite)
                android.util.Log.d("ViewModel", "setFavorite completed for id=$recipeId")
            } catch (e: Exception) {
                android.util.Log.e("ViewModel", "setFavorite FAILED for id=$recipeId: ${e.message}", e)
            }
        }
    }

    private val _shoppingList = MutableStateFlow<List<ShoppingListItem>>(emptyList())
    val shoppingList: StateFlow<List<ShoppingListItem>> = _shoppingList.asStateFlow()

    fun addRecipeToShoppingList(recipe: Recipe) {
        val newItems = recipe.ingredients.map { ingredient ->
            ShoppingListItem(
                name = ingredient.name,
                amount = ingredient.amount
            )
        }
        _shoppingList.value =
            (_shoppingList.value + newItems).distinctBy { "${it.name}-${it.amount}" }
        // distinctBy prevents the same item from being added again
    }

    fun toggleShoppingItem(item: ShoppingListItem) {
        _shoppingList.value = _shoppingList.value.map {
            if (it == item) {
                it.copy(isChecked = !it.isChecked)
                // why copy? because ShoppingListItem is a data class,
                // we don't mutate it directly, we create a new version
            } else {
                it
            }
        }
    }

    fun onPullToRefresh() {
        android.util.Log.d("ViewModel", "Pull to refresh triggered!")
        repository.requestForceRefresh()
    }

    private val _recipeDetail = MutableStateFlow<Recipe?>(null)
    val recipeDetail: StateFlow<Recipe?> = _recipeDetail.asStateFlow()
    private val _isLoadingDetail = MutableStateFlow(false)
    val isLoadingDetail: StateFlow<Boolean> = _isLoadingDetail.asStateFlow()

    fun fetchRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            try {
                // Remove the previously displayed recipe
                _recipeDetail.value = null

                // Show loading screen
                _isLoadingDetail.value = true

                android.util.Log.d(
                    "ViewModel",
                    "Fetching detail for recipe $recipeId"
                )

                // Fetch details from API and save them to Room
                repository.fetchAndCacheRecipeDetails(recipeId)

                // Get the newly cached recipe
                val recipe = repository
                    .getRecipeById(recipeId)
                    .first()

                // Make sure we display the requested recipe
                if (recipe?.id == recipeId) {
                    _recipeDetail.value = recipe

                    android.util.Log.d(
                        "ViewModel",
                        "Recipe detail loaded: ${recipe.title}"
                    )
                }

            } catch (e: Exception) {

                _recipeDetail.value = null

                android.util.Log.e(
                    "ViewModel",
                    "Failed to fetch recipe detail: ${e.message}"
                )

            } finally {
                _isLoadingDetail.value = false
            }
        }
    }
    }

    // Meal planning now lives entirely in MealPlanViewModel + MealPlanRepository
    // (Room-backed). This class no longer owns any meal-plan state.
