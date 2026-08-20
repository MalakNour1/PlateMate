package com.example.platemate

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.platemate.components.BottomNavigationBar
import com.example.platemate.screens.FavoritesScreen
import com.example.platemate.screens.HomeScreen
import com.example.platemate.screens.RecipeDetailScreen
import com.example.platemate.screens.SearchScreen


@Composable
fun PlateMateNavGraph(navController: NavHostController)
{
    val navController = rememberNavController()
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
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
                FavoritesScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
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
                RecipeDetailScreen(
                    recipeId=recipeId,
                    onBackCLick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

}