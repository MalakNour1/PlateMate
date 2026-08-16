package com.example.platemate

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.platemate.screens.FavoritesScreen
import com.example.platemate.screens.HomeScreen
import com.example.platemate.screens.RecipeDetailScreen
import com.example.platemate.screens.SearchScreen


@Composable
fun PlateMateNavGraph(navController: NavHostController)
{
    NavHost(navController=navController,
        startDestination="home")
    {
        composable("home")
        {
            HomeScreen(
                onSearchClick = {
                    navController.navigate("search")
                },
                onRecipeClick = {
                    navController.navigate("details")
                },
                onFavouriteClick = {
                    navController.navigate("favorites")
                }
            )
        }
        composable("search")
        {
            SearchScreen(
                onRecipeClick = {
                    navController.navigate("details")
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
        composable("details")
        {
            RecipeDetailScreen(
                onBackCLick = {
                    navController.popBackStack()
                }
            )
        }
    }

}