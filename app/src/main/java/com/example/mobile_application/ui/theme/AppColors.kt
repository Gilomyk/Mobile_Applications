package com.example.mobile_application.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val metaText: Color,
    val cardTitle: Color,
    val movieOverlay: Color,
    val searchBorder: Color,
    val textSecondary: Color,
)

val DarkAppColors = AppColors(
    metaText = Color(0xFFADB5BD),         // szarawy tekst pomocniczy
    cardTitle = Color(0xFFE0E0E0),        // tytuł w karcie
    movieOverlay = Color(0xCC000000),     // przezroczysty overlay
    searchBorder = DarkSearchBorder,
    textSecondary = DarkMetaText
)

val LightAppColors = AppColors(
    metaText = Color(0xFF6C757D),         // jasnoszary tekst pomocniczy
    cardTitle = Color(0xFF212529),        // ciemny tekst
    movieOverlay = Color(0x66000000),     // półprzezroczysty overlay
    searchBorder = LightSearchBorder,
    textSecondary = LightMetaText
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors } // fallback na jasny
