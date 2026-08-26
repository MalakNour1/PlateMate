package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.platemate.components.RecipeCard
import com.example.platemate.presentation.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    viewModel: RecipeViewModel
) {
    val searchQuery = remember { mutableStateOf("") }
    val recipes = viewModel.pagedRecipes.collectAsLazyPagingItems()
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading

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

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                android.util.Log.d("HomeScreen", "User pulled to refresh!")
                viewModel.onPullToRefresh()
                recipes.refresh()
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Popular Recipes",
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recipes.itemCount) { index ->
                        val recipe = recipes[index]
                        if (recipe != null) {
                            RecipeCard(
                                title = recipe.title,
                                category = recipe.category,
                                imageUrl = recipe.imageUrl,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }

                    when (recipes.loadState.append) {
                        is LoadState.Loading -> item {
                            Text(
                                text = "Loading more...⏳",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        is LoadState.Error -> item {
                            Text(
                                text = "Couldn't load more recipes",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        else -> Unit
                    }
                }


                when (val refresh = recipes.loadState.refresh) {
                    is LoadState.Loading -> {
                        Text(
                            text = "Loading recipes...⏳",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    is LoadState.Error -> {
                        Text(
                            text = refresh.error.message ?: "Failed to load recipes",
                            color = androidx.compose.ui.graphics.Color.Red,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    else -> {
                        if (recipes.itemCount == 0) {
                            Text(
                                text = "No recipes found.",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}