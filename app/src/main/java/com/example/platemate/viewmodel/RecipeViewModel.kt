package com.example.platemate.viewmodel

import android.R
import androidx.lifecycle.ViewModel
import com.example.platemate.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow(
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
        )
    )
    val recipes : StateFlow<List<Recipe>> =_recipes
}