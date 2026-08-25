package com.example.platemate.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.platemate.domain.model.ShoppingListItem

@Composable
fun ShoppingListScreen(
    items: List<ShoppingListItem>,
    onItemClick:(ShoppingListItem) -> Unit,
    onBackClick:() -> Unit
)
{
    Column( modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(12.dp)
    )
    {
    Text("Shopping List \uD83D\uDED2")
        if (items.isEmpty())
        {
            Text("Your shopping list is empty.")
        } else
        {
            items.forEach { item ->
                Text(
                    text = if(item.isChecked){
                        "☑ ${item.name}" + (item.amount?.let { " - $it" } ?: "")
                    } else
                    {
                        "☐ ${item.name}" + (item.amount?.let { " - $it" } ?: "")
                    },
                    modifier = Modifier.clickable{
                        onItemClick(item)
                    }
                )
            }
        }
        Button(onClick = onBackClick) {
            Text("Back")
        }
    }
}