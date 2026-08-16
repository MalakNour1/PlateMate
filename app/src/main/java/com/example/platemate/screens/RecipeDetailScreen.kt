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
fun RecipeDetailScreen(
    onBackCLick:() -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ) {
        Text("Recipe details")
        Text("Chicken pasta")
        Text("ingredients")
        Text("Chicken,pasta,cheese")
        Text("cook the pasta while preparing the sauce")

        Button(onClick = onBackCLick)
        {
            Text("Back")
        }
    }
}