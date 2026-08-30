package com.example.platemate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OfflineBanner(
    isConnected: Boolean?,
    durationMillis: Long = 5000
) {
    var showBanner by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(isConnected) {
        job?.cancel()

        if (isConnected == null) {
            showBanner = false
            return@LaunchedEffect
        }

        showBanner = true

        job = scope.launch {
            delay(durationMillis)
            showBanner = false
        }
    }

    if (showBanner && isConnected != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (isConnected) Color(0xFF2E7D32) else Color(0xFFC62828),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isConnected) "You're online" else "You're offline - showing cached recipes",
                color = Color.White
            )
        }
    }
}