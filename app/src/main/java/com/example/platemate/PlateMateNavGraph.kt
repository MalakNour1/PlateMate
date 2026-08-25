package com.example.platemate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.platemate.components.BottomNavigationBar
import com.example.platemate.components.OfflineBanner
import com.example.platemate.screens.FavoritesScreen
import com.example.platemate.screens.HomeScreen
import com.example.platemate.screens.RecipeDetailScreen
import com.example.platemate.screens.SearchScreen
import com.example.platemate.screens.ShoppingListScreen
import com.example.platemate.state.RecipeUiState
import com.example.platemate.viewmodel.RecipeViewModel


@Composable
fun PlateMateNavGraph(
    navController: NavHostController,
    viewModel: RecipeViewModel
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    // Hoisted once here, reused by every screen below — no need for each
    // composable to collect its own copy.
    val uiState by viewModel.uiState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    Scaffold(
        bottomBar = {
            if (
                currentRoute == "home" ||
                currentRoute == "search" ||
                currentRoute == "favorites"
            ) {

                BottomNavigationBar(
                    currentRoute = currentRoute, onHomeClick = {
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },
                    onSearchClick = {
                        navController.navigate("search") {
                            launchSingleTop = true
                        }
                    },
                    onFavoritesClick = {
                        navController.navigate("favorites") {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize()) {
            OfflineBanner(
                isConnected = isConnected,
                durationMillis = 5000
            )
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
                    HomeScreen(
                        onSearchClick = { navController.navigate("search") },
                        onRecipeClick = { recipeId -> navController.navigate("details/$recipeId") },
                        onFavouriteClick = { navController.navigate("favorites") },
                        viewModel = viewModel
                    )
                }
                composable("search") {
                    when (val state = uiState) {
                        is RecipeUiState.Success -> {
                            SearchScreen(
                                recipes = state.recipes,
                                onRecipeClick = { recipeId ->
                                    navController.navigate("details/$recipeId")
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        RecipeUiState.Loading -> {
                            Text("Loading...⏳⏳⏳")
                        }

                        RecipeUiState.Empty -> {
                            Text("Recipes not found")
                        }

                        is RecipeUiState.Error -> {
                            Text(state.message)
                        }
                    }
                }
                composable("favorites") {
                    when (val state = uiState) {
                        is RecipeUiState.Success -> {
                            FavoritesScreen(
                                recipes = state.recipes,
                                favoriteIds = favoriteIds,
                                onRecipeClick = { recipeId ->
                                    navController.navigate("details/$recipeId")
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        RecipeUiState.Loading -> {
                            Text("Loading...⏳⏳⏳")
                        }

                        RecipeUiState.Empty -> {
                            Text("Recipes not found")
                        }

                        is RecipeUiState.Error -> {
                            Text(state.message)
                        }
                    }
                }
                composable(
                    "details/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.IntType
                    })
                ) { backStackEntry ->
                    val recipeId =
                        backStackEntry.arguments?.getInt("recipeId") ?: return@composable
                    when (val state = uiState) {
                        is RecipeUiState.Success -> {
                            RecipeDetailScreen(
                                recipeId = recipeId,
                                recipes = state.recipes,
                                favoriteIds = favoriteIds,
                                onFavoriteClick = { id ->
                                    viewModel.toggleFavorite(id)
                                },
                                onAddToShoppingList = {
                                    val recipe = state.recipes.find { it.id == recipeId }
                                    if (recipe != null) {
                                        viewModel.addRecipeToShoppingList(recipe)
                                        navController.navigate("shopping_list")
                                    }
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        RecipeUiState.Loading -> {
                            Text("Loading...⏳⏳⏳")
                        }

                        RecipeUiState.Empty -> {
                            Text("Recipes not found")
                        }

                        is RecipeUiState.Error -> {
                            Text(state.message)
                        }
                    }
                }
                composable("shopping_list") {
                    val items by viewModel.shoppingList.collectAsState()
                    ShoppingListScreen(
                        items = items,
                        onItemClick = { item ->
                            viewModel.toggleShoppingItem(item)
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}