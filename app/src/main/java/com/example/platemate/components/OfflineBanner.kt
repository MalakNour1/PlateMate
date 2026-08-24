package com.example.platemate.components

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
    var previousConnectionState by remember { mutableStateOf<Boolean?>(null) }
    var lastShownTime by remember { mutableStateOf(0L) }
    var showBanner by remember { mutableStateOf(false) }
    var hasShownFirstBanner by remember { mutableStateOf(false) }
    var isDismissedByUser by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var hideJob by remember { mutableStateOf<Job?>(null) }

    // Handle banner visibility based on state changes
    fun updateBannerVisibility(connected: Boolean?) {
        if (connected == null) {
            showBanner = false
            hideJob?.cancel()
            return
        }

        val currentTime = System.currentTimeMillis()
        val isConnectionChanged = previousConnectionState != connected
        val isCooldownOver = currentTime - lastShownTime > 30000

        val shouldShowBanner = (!hasShownFirstBanner || (isConnectionChanged && isCooldownOver))
                && !isDismissedByUser

        if (shouldShowBanner) {
            // Cancel existing timer
            hideJob?.cancel()

            showBanner = true
            previousConnectionState = connected
            lastShownTime = currentTime
            hasShownFirstBanner = true

            // Start new timer
            hideJob = scope.launch {
                delay(durationMillis)
                showBanner = false
                hideJob = null
            }
        }
    }

    // Trigger on connection changes
    LaunchedEffect(isConnected) {
        updateBannerVisibility(isConnected)
    }

    // Clean up on dispose
    DisposableEffect(Unit) {
        onDispose {
            hideJob?.cancel()
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
                text = if (isConnected) {
                    "You're online"
                } else {
                    "You're offline — showing cached recipes"
                },
                color = Color.White
            )
        }
    }
}