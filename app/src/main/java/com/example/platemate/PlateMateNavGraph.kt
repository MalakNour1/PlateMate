package com.example.platemate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.navigation.navArgument
import com.example.platemate.components.BottomNavigationBar
import com.example.platemate.domain.model.Recipe
import com.example.platemate.screens.FavoritesScreen
import com.example.platemate.screens.HomeScreen
import com.example.platemate.screens.RecipeDetailScreen
import com.example.platemate.screens.SearchScreen
import com.example.platemate.screens.ShoppingListScreen
import com.example.platemate.state.RecipeUiState
import com.example.platemate.viewmodel.RecipeViewModel


@Composable
fun PlateMateNavGraph(navController: NavHostController ,recipes: List<Recipe>)
{

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    val recipeViewModel : RecipeViewModel = viewModel()
    val favoriteIds by recipeViewModel.favoriteIds.collectAsState()
    Scaffold(
        bottomBar = {
            if (
                currentRoute=="home"||
                currentRoute=="search" ||
                currentRoute=="favorites"
            ){

                BottomNavigationBar(currentRoute=currentRoute, onHomeClick = {
                    navController.navigate("home"){
                        popUpTo("home"){
                            inclusive=false
                        }
                        launchSingleTop =true
                    }
                                                                             },
                    onSearchClick = {
                        navController.navigate("search"){
                            launchSingleTop = true
                        }
                                    },
                    onFavoritesClick = {
                        navController.navigate("favorites"){
                            launchSingleTop=true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController=navController,
            startDestination="home",
            modifier = Modifier.padding(innerPadding)) {
            composable("home")
            {
                HomeScreen(
                    onSearchClick = {
                        navController.navigate("search")
                                    },
                    onRecipeClick = { recipeId ->
                        navController.navigate("details/$recipeId")
                                    },
                    onFavouriteClick = {
                        navController.navigate("favorites")
                    }
                )
            }
            composable("search")
            {
                SearchScreen(
                    recipes = recipes
                    ,
                    onRecipeClick = { recipeId ->
                        navController.navigate("details/$recipeId")
                                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable("favorites")
            {
                val uiState = recipeViewModel.uiState.collectAsState()
                when( val state = uiState.value)
                {
                    is RecipeUiState.Success -> {
                        FavoritesScreen(
                            recipes = state.recipes,
                            favoriteIds =favoriteIds,
                            onRecipeClick =
                                {
                                    recipeId -> navController.navigate("details/$recipeId")
                                },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )

                    }
                    RecipeUiState.Loading ->{
                        Text("Loading...⏳⏳⏳")
                    }

                    RecipeUiState.Empty ->{
                        Text("Recipes not found")
                    }
                    is RecipeUiState.Error ->
                    {
                        Text(state.message)
                    }
                }

            }
            composable("details/{recipeId}",
                arguments =listOf(navArgument("recipeId")
                {
                    type = NavType.IntType
                }
                )
            )
            {
                backStackEntry ->
                val recipeId =
                    backStackEntry.arguments?.getInt("recipeId")?: return@composable
                val uiState = recipeViewModel.uiState.collectAsState()
                when ( val state = uiState.value) {
                    is RecipeUiState.Success -> {
                        RecipeDetailScreen(
                            recipeId = recipeId,
                            recipes = state.recipes,
                            favoriteIds =favoriteIds,
                            onFavoriteClick = {
                                id -> recipeViewModel.toggleFavorite(id)
                            },
                            onAddToShoppingList = {
                                val recipe = state.recipes.find {
                                    it.id==recipeId
                                }
                                if (recipe!=null)
                                {
                                    recipeViewModel.addRecipeToShoppingList(recipe)
                                    navController.navigate("shopping_list")
                                }
                            },
                            onBackClick = {
                                navController.popBackStack()
                            }
                        )
                    }

                    RecipeUiState.Loading ->{
                        Text("Loading...⏳⏳⏳")
                    }

                    RecipeUiState.Empty ->{
                        Text("Recipes not found")
                    }
                    is RecipeUiState.Error ->
                    {
                        Text(state.message)
                    }
                }

            }
            composable("shopping_list")
            {
                val items by recipeViewModel.shoppingList.collectAsState()
                ShoppingListScreen(
                    items=items,
                    onItemClick = {
                        item -> recipeViewModel.toggleShoppingItem(item)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}