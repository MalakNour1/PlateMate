package com.example.platemate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.platemate.components.RecipeCard
import com.example.platemate.domain.model.Recipe

@Composable
fun FavoritesScreen(
    recipes:List<Recipe>,
    favoriteIds: Set<Int>,
    onRecipeClick :(Int) -> Unit,
    onBackClick :()-> Unit)
{
    val favoriteRecipes = recipes.filter { it.id in favoriteIds }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ) {
        Text("Favourites")
        if(favoriteRecipes.isEmpty())
        {
            Text("Saved recipe will appear here")
        }
        else
        {
            favoriteRecipes.forEach { recipe->
                RecipeCard(
                    title = recipe.title,
                    category = recipe.category,
                    onClick = {
                        onRecipeClick(recipe.id)
                    }
                )
            }
        }
        Button(onClick = onBackClick)
        {
            Text("Back")
        }
    }
}