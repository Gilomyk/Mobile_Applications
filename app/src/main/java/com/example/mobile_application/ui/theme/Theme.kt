package com.example.mobile_application.ui.theme

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Material3 dark and light color schemes
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = DarkBlue,
    surface = DarkBlue,
    onBackground = White,
    onSurface = CrewTextGray,
    // możesz dopisać inne pola
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = White,
    surface = White,
    onBackground = DarkBlue,
    onSurface = MetaGray,
    // itd.
)

@Composable
fun Mobile_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    Log.d("ThemeCheck", "Mobile_ApplicationTheme applied. Dark theme: $darkTheme")
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

