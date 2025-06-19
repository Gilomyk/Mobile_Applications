package com.example.mobile_application.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary      = AccentPurple,
    secondary    = AccentPurple,
    background   = DarkBg,
    surface      = DarkSearchBg,
    onBackground = OnDarkBg,
    onSurface    = OnDarkBg,
    outline      = DarkSearchBorder,
)

private val LightColorScheme = lightColorScheme(
    primary      = AccentPurple,
    secondary    = AccentPurple,
    background   = LightBg,
    surface      = LightBg,
    onBackground = OnLightBg,
    onSurface    = OnLightBg,
    outline      = LightSearchBorder,
)

@Composable
fun Mobile_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    Log.d("ThemeCheck", "Mobile_ApplicationTheme applied. Dark theme: $darkTheme")

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
