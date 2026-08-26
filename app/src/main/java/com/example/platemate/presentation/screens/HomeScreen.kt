package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.platemate.components.RecipeCard
import com.example.platemate.presentation.viewmodel.RecipeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onFavouriteClick: () -> Unit,
    onRecipeClick: (Int) -> Unit,
    viewModel: RecipeViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val recipes = viewModel.pagedRecipes.collectAsLazyPagingItems()
    val isRefreshing = recipes.loadState.refresh is LoadState.Loading

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "PlateMate", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = onFavouriteClick) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorites"
                        )
                    }
                    // Shows the mode you'll switch TO, not the current one -- the
                    // standard convention for a light/dark toggle icon.
                    IconButton(onClick = onToggleTheme) {
                        Text(
                            text = if (isDarkTheme) "\u2600\uFE0F" else "\uD83C\uDF19",
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Good morning \uD83D\uDC4B",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "What are you cooking today?",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            // Not a live-filtering field on this screen -- it's an entry point into
            // the dedicated SearchScreen (which does the real filtering by title
            // and category). The old OutlinedTextField collected typed text but
            // never used it, so nothing happened when you typed into it.
            SearchEntryBar(onClick = onSearchClick)

            Text(
                text = "Popular Recipes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    viewModel.onPullToRefresh()
                    recipes.refresh()
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (val refresh = recipes.loadState.refresh) {
                    is LoadState.Loading -> {
                        if (recipes.itemCount == 0) {
                            CenteredMessage {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            }
                        } else {
                            RecipeList(recipes = recipes, onRecipeClick = onRecipeClick)
                        }
                    }
                    is LoadState.Error -> {
                        CenteredMessage {
                            Text(
                                text = refresh.error.message ?: "Failed to load recipes",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.size(12.dp))
                            Button(onClick = { recipes.retry() }) {
                                Text("Retry")
                            }
                        }
                    }
                    else -> {
                        if (recipes.itemCount == 0) {
                            CenteredMessage {
                                Text(
                                    text = "No recipes found.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            RecipeList(recipes = recipes, onRecipeClick = onRecipeClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchEntryBar(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Search recipes...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RecipeList(
    recipes: androidx.paging.compose.LazyPagingItems<com.example.platemate.domain.model.Recipe>,
    onRecipeClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            count = recipes.itemCount,
            key = recipes.itemKey { it.id }
        ) { index ->
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is LoadState.Error -> item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Couldn't load more recipes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    TextButton(onClick = { recipes.retry() }) {
                        Text("Retry")
                    }
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun CenteredMessage(content: @Composable ColumnScope.() -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}