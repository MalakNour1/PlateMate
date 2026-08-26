package com.example.platemate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.platemate.data.connectivity.NetworkMonitorImpl
import com.example.platemate.data.local.database.PlateMateDatabase
import com.example.platemate.data.remote.NetworkModule
import com.example.platemate.data.repository.RecipeRepositoryImpl
import com.example.platemate.presentation.theme.PlateMateTheme
import com.example.platemate.presentation.viewmodel.RecipeViewModel
import com.example.platemate.presentation.viewmodel.RecipeViewModelFactory
import com.example.platemate.presentation.viewmodel.ThemeViewModel

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

                PlateMateNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}