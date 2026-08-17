package com.example.platemate.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.platemate.components.RecipeCard
import com.example.platemate.state.RecipeUiState
import com.example.platemate.viewmodel.RecipeViewModel

@Composable
fun HomeScreen(
    onSearchClick : () -> Unit,
    onFavouriteClick : () -> Unit,
    onRecipeClick : (Int) -> Unit,
    viewModel: RecipeViewModel=viewModel())
{
    val searchQuery = remember{
        mutableStateOf("")
    }
    val uiState = viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PlateMate")
        Text("Find your next meal")
        OutlinedTextField(
            value= searchQuery.value,
            onValueChange = {
                searchQuery.value=it
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Search recipes")
            },
            singleLine = true
        )

        Text("Popular Recipes")
        when (val state = uiState.value) {
            RecipeUiState.Loading -> {
                Text("Loading recipes...⏳⏳⏳")
            }

            is RecipeUiState.Success -> {
                state.recipes.forEach { recipe ->
                    RecipeCard(
                        title = recipe.title,
                        summary = recipe.summary,
                        onClick = {
                            onRecipeClick(recipe.id)
                        }
                    )
                }
            }

            RecipeUiState.Empty ->{
                Text("No recipes found.")
            }
            is RecipeUiState.Error ->{
                Text(state.message)
            }
        }



    }
}