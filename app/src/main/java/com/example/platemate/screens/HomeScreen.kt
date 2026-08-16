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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.platemate.components.RecipeCard

@Composable
fun HomeScreen(
    onSearchClick : () -> Unit,
    onFavouriteClick : () -> Unit,
    onRecipeClick : () -> Unit )
{
    val searchQuery = remember{
        mutableStateOf("")
    }
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

        RecipeCard(
            title="beef rice",
            summary="beef with rice",
            onClick = onRecipeClick)
        RecipeCard(
            title="Chicken pasta",
            summary="Creamy chicken pasta",
            onClick=onRecipeClick)

        Button(onClick = onSearchClick)
        {
            Text("Search Recipes")
        }
        Button(onClick = onFavouriteClick)
        {
            Text("Saved Recipes")
        }
        Button(onClick = onRecipeClick)
        {
            Text("View Recipes")
        }
    }
}