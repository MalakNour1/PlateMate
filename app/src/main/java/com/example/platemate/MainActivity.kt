package com.example.platemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.platemate.data.connectivity.NetworkMonitorImpl
import com.example.platemate.data.local.database.PlateMateDatabase
import com.example.platemate.data.remote.NetworkModule
import com.example.platemate.data.repository.MealPlanRepositoryImpl
import com.example.platemate.data.repository.RecipeRepositoryImpl
import com.example.platemate.data.repository.ShoppingListRepositoryImpl
import com.example.platemate.presentation.theme.PlateMateTheme
import com.example.platemate.presentation.viewmodel.mealPlan.MealPlanViewModel
import com.example.platemate.presentation.viewmodel.mealPlan.MealPlanViewModelFactory
import com.example.platemate.presentation.viewmodel.recipe.RecipeViewModel
import com.example.platemate.presentation.viewmodel.recipe.RecipeViewModelFactory
import com.example.platemate.presentation.viewmodel.shoppingList.ShoppingListViewModel
import com.example.platemate.presentation.viewmodel.shoppingList.ShoppingListViewModelFactory
import com.example.platemate.presentation.viewmodel.theme.ThemeViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the DB
        val database = PlateMateDatabase.getInstance(applicationContext)

        // Build the repository from real API + real DAO
        val repository = RecipeRepositoryImpl(
            recipeApi = NetworkModule.recipeApi,
            database = database,
            apiKey = BuildConfig.SPOONACULAR_API_KEY
        )

        val networkMonitor = NetworkMonitorImpl(applicationContext)

        val mealPlanRepository = MealPlanRepositoryImpl(
            mealPlanDao = database.mealPlanDao(),
            recipeDao = database.recipeDao()
        )

        val shoppingListRepository = ShoppingListRepositoryImpl(database.shoppingListDao())

        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            PlateMateTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                val viewModel: RecipeViewModel = viewModel(
                    factory = RecipeViewModelFactory(
                        repository,
                        networkMonitor
                    )
                )

                val mealPlanViewModel: MealPlanViewModel = viewModel(
                    factory = MealPlanViewModelFactory(mealPlanRepository)
                )

                val shoppingListViewModel: ShoppingListViewModel = viewModel(
                    factory = ShoppingListViewModelFactory(shoppingListRepository)
                )

                PlateMateNavGraph(
                    navController = navController,
                    viewModel = viewModel,
                    mealPlanViewModel = mealPlanViewModel,
                    shoppingListViewModel = shoppingListViewModel
                )
            }
        }
    }
}