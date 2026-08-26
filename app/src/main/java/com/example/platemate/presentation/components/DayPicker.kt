package com.example.platemate.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.dp

@Composable
fun DayPicker(
    onDaySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val days = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Choose a day")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                days.forEach { day ->

                    TextButton(
                        onClick = {
                            onDaySelected(day)
                        }
                    ) {
                        Text(day)
                    }
                }
            }
        },
        confirmButton = {}
    )
}