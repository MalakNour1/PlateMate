package com.example.platemate.presentation.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(currentRoute: String?,
                        onHomeClick:() -> Unit,
                        onSearchClick:()-> Unit,
                        onAddToShoppingList: () -> Unit,
                        onMealPlannerClick:()-> Unit,
                        onFavoritesClick:() -> Unit)
{
    NavigationBar {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,
            icon =
                {
                    Text("⌂")
                },
            label = {
                    Text("Home")
                })
        NavigationBarItem(
            selected = currentRoute == "search",
            onClick = onSearchClick,
            icon =
                {
                    Text("⌕")
                },
            label = {
                Text("Search")
            })
        NavigationBarItem(
            selected = currentRoute =="shopping_list",
            onClick = onAddToShoppingList,
            icon =
                {
                    Text("🛒")
                },
            label ={
                Text("Groceries")
            }
        )
        NavigationBarItem(
            selected = currentRoute =="meal_planner",
            onClick = onMealPlannerClick,
            icon = {
                Text("\uD83C\uDF7D")
            },
            label={
                Text("MealPlan")
            }
        )
        NavigationBarItem(
            selected = currentRoute == "favorites",
            onClick = onFavoritesClick,
            icon =
                {
                    Text("♥")
                },
            label = {
                Text("Saved")
            })
    }
}