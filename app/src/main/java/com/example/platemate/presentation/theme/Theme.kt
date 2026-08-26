package com.example.platemate.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PlateGreen,
    onPrimary = Color.White,

    secondary = PlateGreenDark,
    onSecondary = Color.White,

    background = PlateCream,
    onBackground = PlateText,

    surface = PlateSurface,
    onSurface = PlateText,

    surfaceVariant = Color(0xFFEFF1EB),
    onSurfaceVariant = PlateSecondaryText
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8BCF63),
    onPrimary = Color(0xFF17300D),

    secondary = Color(0xFF72B84E),
    onSecondary = Color(0xFF102308),

    background = PlateDarkBackground,
    onBackground = PlateDarkText,

    surface = PlateDarkSurface,
    onSurface = PlateDarkText,

    surfaceVariant = PlateDarkCard,
    onSurfaceVariant = PlateDarkSecondaryText
)

@Composable
fun PlateMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}