package com.example.mobile_application.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Definicje kolorów (Twoje poprzednie)
private val Primary = Color(0xFF1E88E5)
private val PrimaryVariant = Color(0xFF1565C0)
private val Secondary = Color(0xFF90CAF9)
private val Background = Color(0xFF121212)
private val Surface = Color(0xFF1E1E1E)
private val OnPrimary = Color.White
private val OnBackground = Color(0xFFE0E0E0)

// Material3 dark and light color schemes
private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Secondary, // opcjonalnie można dopasować inny kolor
    background = Background,
    surface = Surface,
    onPrimary = OnPrimary,
    onBackground = OnBackground,
    onSurface = OnBackground
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Secondary,
    background = Color.White,
    surface = Color.White,
    onPrimary = OnPrimary,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun Mobile_ApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true, // opcjonalnie dynamiczne kolory Android 12+
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // zostawiam Twój obecny typografię
        content = content
    )
}
