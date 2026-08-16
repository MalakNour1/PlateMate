package com.example.platemate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavoritesScreen( onBackClick :()-> Unit)
{
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ) {
        Text("Saved recipe will appear here")
        Text("Favourites will appear here")
        Button(onClick = onBackClick)
        {
            Text("Back")
        }
    }
}