package com.youthtalk.designsystem

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme =
    darkColorScheme(
        primary = primaryColor,
        onPrimary = onPrimary,
        background = backgroundColor,
        onBackground = onBackground,
        onPrimaryContainer = Color.White,
        primaryContainer = Color.White,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,
        errorContainer = accent,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = primaryColor,
        onPrimary = onPrimary,
        background = backgroundColor,
        onBackground = onBackground,
        onPrimaryContainer = Color.White,
        primaryContainer = Color.White,
        surface = surface,
        onSurface = onSurface,
        error = error,
        onError = onError,
        errorContainer = accent,
    )

@Composable
fun YongProjectTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme =
        when {
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.navigationBarColor = Color.White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
