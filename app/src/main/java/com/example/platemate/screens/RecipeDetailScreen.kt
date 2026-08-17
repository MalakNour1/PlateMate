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
    recipeId:Int,
    onBackCLick:() -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ) {
        Text("Recipe details")
        Text("Recipe Id:$recipeId")
        Text("ingredients")
        Text("Instructions")


        Button(onClick = onBackCLick)
        {
            Text("Back")
        }
    }
}