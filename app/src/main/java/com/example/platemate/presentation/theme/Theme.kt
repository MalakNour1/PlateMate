package com.example.platemate.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

// ---------- Brand palette (warm, food-app feel) ----------
private val Orange40 = Color(0xFFE2622B)   // primary - appetizing warm orange
private val Orange80 = Color(0xFFFFB599)
private val Orange20 = Color(0xFF7A2E0A)

private val Green40 = Color(0xFF4C6B3A)    // secondary - fresh/healthy green
private val Green80 = Color(0xFFB2D397)
private val Green20 = Color(0xFF1F3510)

private val Gold40 = Color(0xFF8A5D00)     // tertiary - accent for favorites/ratings
private val Gold80 = Color(0xFFFFDD9C)

private val LightBackground = Color(0xFFFFFBF7)
private val LightSurface = Color(0xFFFFFBF7)
private val LightSurfaceVariant = Color(0xFFF3E0D5)

private val DarkBackground = Color(0xFF1C1410)
private val DarkSurface = Color(0xFF1C1410)
private val DarkSurfaceVariant = Color(0xFF4E3B2F)

private val LightColorScheme = lightColorScheme(
    primary = Orange40,
    onPrimary = Color.White,
    primaryContainer = Orange80,
    onPrimaryContainer = Orange20,
    secondary = Green40,
    onSecondary = Color.White,
    secondaryContainer = Green80,
    onSecondaryContainer = Green20,
    tertiary = Gold40,
    tertiaryContainer = Gold80,
    background = LightBackground,
    onBackground = Color(0xFF201A16),
    surface = LightSurface,
    onSurface = Color(0xFF201A16),
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF52443B),
    error = Color(0xFFBA1A1A),
    outline = Color(0xFF85736A)
)

private val DarkColorScheme = darkColorScheme(
    primary = Orange80,
    onPrimary = Orange20,
    primaryContainer = Color(0xFFB44A1D),
    onPrimaryContainer = Orange80,
    secondary = Green80,
    onSecondary = Green20,
    secondaryContainer = Color(0xFF354F26),
    onSecondaryContainer = Green80,
    tertiary = Gold80,
    tertiaryContainer = Color(0xFF6B4700),
    background = DarkBackground,
    onBackground = Color(0xFFEDE0D9),
    surface = DarkSurface,
    onSurface = Color(0xFFEDE0D9),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFD7C3B6),
    error = Color(0xFFFFB4AB),
    outline = Color(0xFF9F8D82)
)

private val PlateMateTypography = Typography(
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 28.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelLarge = TextStyle(fontWeight = FontWeight.Medium, fontSize = 14.sp)
)

/**
 * App-wide theme. Respects the system dark/light setting by default.
 * Pass `darkTheme` explicitly if you build a manual in-app toggle
 * (e.g. driven by a ThemeViewModel + DataStore preference).
 *
 * @param dynamicColor Set false (default) to keep PlateMate's warm brand
 * palette consistent across devices. Set true to use Android 12+ wallpaper
 * based dynamic color instead.
 */
@Composable
fun PlateMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = PlateMateTypography,
        content = content
    )
}