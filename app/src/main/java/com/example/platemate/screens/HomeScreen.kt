package com.example.platemate.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button

@Composable
fun HomeScreen(
    onSearchClick : () -> Unit,
    onFavouriteClick : () -> Unit,
    onRecipeClick : () -> Unit )
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("PlateMate")
        Text("Popular Recipes")
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