package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.platemate.presentation.components.RecipeCard
import com.example.platemate.presentation.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    viewModel: RecipeViewModel   // no default — must be passed the shared instance from the nav graph
) {
    val searchQuery = remember { mutableStateOf("") }
    val recipes = viewModel.pagedRecipes.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PlateMate")
        Text("Find your next meal")
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search recipes") },
            singleLine = true
        )

        Text("Popular Recipes")

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recipes.itemCount) { index ->
                val recipe = recipes[index]
                if (recipe != null) {
                    RecipeCard(
                        title = recipe.title,
                        category = recipe.category,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
            }

            // Loading/error state at the BOTTOM of the list (first page, e.g. spinner while scrolling)
            when (recipes.loadState.append) {
                is LoadState.Loading -> item { Text("Loading more...⏳") }
                is LoadState.Error -> item { Text("Couldn't load more recipes") }
                else -> Unit
            }
        }

        // Loading/error/empty state for the FIRST page load
        when (val refresh = recipes.loadState.refresh) {
            is LoadState.Loading -> Text("Loading recipes...⏳")
            is LoadState.Error -> Text(refresh.error.message ?: "Failed to load recipes")
            else -> {
                if (recipes.itemCount == 0) {
                    Text("No recipes found.")
                }
            }
        }
    }
}