package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.platemate.domain.model.Recipe
import com.example.platemate.presentation.components.DayPicker

@Composable
fun RecipeDetailScreen(
    recipe: Recipe,
    favoriteIds: Set<Int>,
    onFavoriteClick: (Int) -> Unit,
    onAddToShoppingList: () -> Unit,
    onAddToMealPlanner: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val isFavorite = recipe.id in favoriteIds
    var showDayPicker by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title
        Text(text = recipe.title, style = MaterialTheme.typography.headlineSmall)

        // Image
        if (recipe.imageUrl != null) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Category
        if (recipe.category != null) {
            Text(
                text = "Category: ${recipe.category}",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Ingredients
        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        if (recipe.ingredients.isEmpty()) {
            Text("No ingredients available")
        } else {
            recipe.ingredients.forEach { ingredient ->
                Text("• ${ingredient.name} ${ingredient.amount ?: ""}")
            }
        }

        // Instructions
        Text("Instructions", style = MaterialTheme.typography.titleMedium)
        if (recipe.steps.isEmpty()) {
            Text("No instructions available")
        } else {
            recipe.steps.forEachIndexed { index, step ->
                Text("${index + 1}. $step")
            }
        }

        // Buttons
        Button(
            onClick = { onFavoriteClick(recipe.id) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isFavorite) "❤️ Remove from Favorites" else "♡ Add to Favorites")
        }

        Button(
            modifier = Modifier.fillMaxWidth()
            ,
            onClick = {
                showDayPicker = true
            }
        ) {
            Text(" \uD83C\uDF7D Add to Meal Planner")
        }
        Button(
            onClick = onAddToShoppingList,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🛒 Add to Shopping List")
        }
        Button(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back")
        }
    }
    if (showDayPicker) {

        DayPicker(
            onDaySelected = { day ->

                onAddToMealPlanner(day)

                showDayPicker = false
            },

            onDismiss = {
                showDayPicker = false
            }
        )
    }
}