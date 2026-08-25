package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.platemate.presentation.components.RecipeCard
import com.example.platemate.domain.model.Recipe

@Composable
fun SearchScreen(
    recipes: List<Recipe>,
    onRecipeClick:(Int)-> Unit,
    onBackClick:()-> Unit)
{
    var searchQuery by remember{
        mutableStateOf("")
    }
    var selectedCategory by remember {
        mutableStateOf("All")
    }
    val categories = listOf(
        "All",
        "Main",
        "Dessert",
        "Breakfast",
        "Appetizer",
        "Drinks")
    val filteredRecipes = recipes.filter { recipe ->
        val matchesSearch =
            recipe.title.contains(
                searchQuery,
                ignoreCase = true)
        val matchesCategory =
            selectedCategory=="All" ||
                    recipe.category == selectedCategory
        matchesSearch && matchesCategory

    }
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ){
        Text("Search Recipes")
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search")
            },
            singleLine = true
        )
        Text("Category")
        categories.forEach { category ->
            Button(onClick =
                {
                    selectedCategory= category
                }
            ) {
                Text(category)
            }
        }
        if(filteredRecipes.isEmpty())
        {
            Text("No recipes found")
        } else {
            filteredRecipes.forEach { recipe ->
                RecipeCard(
                    title = recipe.title,
                    category = recipe.category,
                    onClick =
                        {
                            onRecipeClick(recipe.id)
                        }
                )
            }
        }

        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}