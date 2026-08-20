package com.example.platemate.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RecipeCard(title: String, category : String?, onClick:() -> Unit )
{
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable{onClick()})
    {
        Column(modifier = Modifier.padding(16.dp))
        {
            Text(text=title)
            Text(text=category?:"Unknown category", modifier = Modifier.padding(8.dp))
        }
    }
}