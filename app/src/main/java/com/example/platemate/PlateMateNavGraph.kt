package com.example.platemate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.platemate.presentation.components.BottomNavigationBar
import com.example.platemate.presentation.components.OfflineBanner
import com.example.platemate.presentation.screens.FavoritesScreen
import com.example.platemate.presentation.screens.HomeScreen
import com.example.platemate.presentation.screens.MealPlannerScreen
import com.example.platemate.presentation.screens.RecipeDetailScreen
import com.example.platemate.presentation.screens.SearchScreen
import com.example.platemate.presentation.screens.ShoppingListScreen
import com.example.platemate.presentation.state.RecipeUiState
import com.example.platemate.presentation.viewmodel.MealPlanViewModel
import com.example.platemate.presentation.viewmodel.RecipeViewModel
import com.example.platemate.presentation.viewmodel.ThemeViewModel


@Composable
fun PlateMateNavGraph(
    navController: NavHostController,
    viewModel: RecipeViewModel,
    mealPlanViewModel: MealPlanViewModel
) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    // Hoisted once here, reused by every screen below — no need for each
    // composable to collect its own copy.
    val uiState by viewModel.uiState.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    // Single shared instance so the toggle button's state is the same one
    // MainActivity's PlateMateTheme(darkTheme = ...) reads from.
    val themeViewModel: ThemeViewModel = viewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

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
                    onAddToShoppingList = {
                        navController.navigate("shopping_list") {
                            launchSingleTop = true
                        }
                    },
                    onMealPlannerClick = {
                        navController.navigate("meal_planner") {
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
                        viewModel = viewModel,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = { themeViewModel.toggleTheme() }
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

                composable("meal_planner") {
                    val mealPlans by mealPlanViewModel.mealPlans.collectAsState()

                    MealPlannerScreen(
                        mealPlans = mealPlans,
                        onBackClick = { navController.popBackStack() },
                        onDayClick = { day ->
                            // go to recipe details
                            val meal = mealPlans.find { it.day == day }
                            meal?.recipe?.id?.let { recipeId ->
                                navController.navigate("details/$recipeId")
                            }
                        }
                    )
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
                    val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable

                    LaunchedEffect(recipeId) {
                        viewModel.fetchRecipeDetail(recipeId)
                    }

                    val recipeDetail by viewModel.recipeDetail.collectAsState()
                    val isLoadingDetail by viewModel.isLoadingDetail.collectAsState()
                    val favoriteIds by viewModel.favoriteIds.collectAsState()

                    when {
                        isLoadingDetail -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        recipeDetail != null -> {
                            RecipeDetailScreen(
                                recipe = recipeDetail!!,
                                favoriteIds = favoriteIds,
                                onFavoriteClick = { id ->
                                    viewModel.toggleFavorite(id)
                                },
                                onAddToShoppingList = {
                                    val recipe = recipeDetail
                                    if (recipe != null) {
                                        viewModel.addRecipeToShoppingList(recipe)
                                        navController.navigate("shopping_list")
                                    }
                                },
                                onAddToMealPlanner = { day ->
                                    mealPlanViewModel.assignMeal(day = day, recipe = recipeDetail!!)   // was: viewModel.assignRecipeToDay(...)
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        else -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Recipe not found")
                            }
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