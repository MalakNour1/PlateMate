package com.example.platemate.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.platemate.domain.model.MealPlan

@Composable
fun MealPlannerScreen(
    mealPlans: List<MealPlan>,
    onBackClick:()->Unit
)
{
    val days = listOf(
        "Saturday",
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday"
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Text("Meal Planner")
        days.forEach {
            day ->
            val meal = mealPlans.find { it.day==day }
            Card {
                Column(modifier =
                    Modifier.padding(12.dp))
                {
                    Text(day)
                    if (meal!= null)
                    {
                        Text(text="\uD83C\uDF7D  ${meal.recipe.title}")
                    } else
                    {
                        Text("No meal Planned")
                    }
                }
            }

        }
        Button(onClick = onBackClick)
        {
            Text("Back")
        }
    }
}

