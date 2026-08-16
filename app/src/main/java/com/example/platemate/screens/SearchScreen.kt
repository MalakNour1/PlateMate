package com.example.platemate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    onRecipeClick:()-> Unit,
    onBackClick:()-> Unit)
{
    Column(
        modifier= Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ){
        Text("Search Recipes")
        Text("Search for a recipe")
        Button(onClick = onRecipeClick) {
            Text("View recipe")
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}