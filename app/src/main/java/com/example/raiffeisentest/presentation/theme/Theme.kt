package com.example.raiffeisentest.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme =
    lightColorScheme(
        primary = AppRed,
        onPrimary = Color.White,
        background = AppBackground,
        onBackground = AppTextPrimary,
        surface = AppBackground,
        onSurface = AppTextPrimary,
        onSurfaceVariant = AppTextSecondary,
        outlineVariant = AppDivider,
    )

private val DarkColorScheme =
    darkColorScheme(
        primary = AppRedDark,
    )

@Composable
fun RaiffeisenTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
