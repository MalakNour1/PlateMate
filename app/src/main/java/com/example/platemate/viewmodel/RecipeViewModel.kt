package com.example.platemate.viewmodel


import androidx.lifecycle.ViewModel
import com.example.platemate.domain.model.Ingredient
import com.example.platemate.domain.model.Recipe
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
                    title = "Chicken Pasta",
                    imageUrl = null,
                    category = "Main",
                    ingredients = listOf(
                        Ingredient("Pasta", "200 g"),
                        Ingredient("Chicken", "300 g"),
                        Ingredient("Salt", null)
                    ),
                    steps = listOf(
                        "Boil the pasta",
                        "Cook the chicken",
                        "Mix the pasta and chicken"
                    )
                ),

                Recipe(
                    id = 2,
                    title = "Beef Rice",
                    imageUrl = null,
                    category = "Main",
                    ingredients = listOf(
                        Ingredient("Beef", "300 g"),
                        Ingredient("Rice", "200 g")
                    ),
                    steps = listOf(
                        "Cook the rice",
                        "Cook the beef",
                        "Serve together"
                    )
                ),

                Recipe(
                    id = 3,
                    title = "Pancakes",
                    imageUrl = null,
                    category = "Breakfast",
                    ingredients = listOf(
                        Ingredient("Flour", "200 g"),
                        Ingredient("Milk", "250 ml"),
                        Ingredient("Egg", "1")
                    ),
                    steps = listOf(
                        "Mix the ingredients",
                        "Heat the pan",
                        "Cook the pancakes"
                    )
                )
            )
        )
    )

    val uiState: StateFlow<RecipeUiState> =
        _uiState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()
    fun toggleFavorite(recipeId: Int)
    {
        val currentFavorites =_favoriteIds.value.toMutableSet()

        if(currentFavorites.contains(recipeId))
        {
            currentFavorites.remove(recipeId)
        } else
        {
            currentFavorites.add(recipeId)
        }
        _favoriteIds.value = currentFavorites
    }

}
//till the repository is connected to viewmodel
/*class RecipeViewModel(private val repository: RecipeRepository) : ViewModel() {
     val uiState : StateFlow<RecipeUiState> = repository
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
        private val _favoriteIds = MutableStateFlow<Set<Int>>(emptySet())
    val favoriteIds: StateFlow<Set<Int>> = _favoriteIds.asStateFlow()
    fun toggleFavorite(recipeId: Int)
    {
        val currentFavorites =_favoriteIds.value.toMutableSet()

        if(currentFavorites.contains(recipeId))
        {
            currentFavorites.remove(recipeId)
        } else
        {
            currentFavorites.add(recipeId)
        }
        _favoriteIds.value = currentFavorites
    }


}*/