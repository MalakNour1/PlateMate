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
import com.example.platemate.domain.model.Recipe


@Composable
fun RecipeDetailScreen(
    recipeId:Int,
    recipes:List<Recipe>,
    onBackCLick:() -> Unit
){
    val recipe = recipes.find{it.id == recipeId}
    if (recipe ==null)
    {
     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(16.dp)
     ) {
         Text("Recipe not found")
         Button(onClick = onBackCLick) { Text("Back") }
     }
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement= Arrangement.spacedBy(16.dp)
    ) {
        Text(text = recipe.title)
        Text("Recipe Id:$recipeId")
        Text(text = recipe.category ?: "Unknown category")
        Text("ingredients")
        recipe.ingredients.forEach { ingredient ->
            Text(text = " •${ingredient.name}" + (ingredient.amount?.let { " - $it " } ?: ""))
        }
        Text("Instructions")
        recipe.steps.forEachIndexed { index, step ->
            Text(text = "${index + 1}. $step")
        }


        Button(onClick = onBackCLick)
        {
            Text("Back")
        }
    }
}