package com.example.mobile_application.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val primary: Color,
    val header: Color,
    val text: Color,
    val icon: Color,
    val metaText: Color,
    val cardTitle: Color,
    val movieOverlay: Color,
    val searchBorder: Color,
    val textSecondary: Color,
    val background: Color,
    val heading: Color,
    val error: Color,
    val buttonText: Color,
    val cardBackground: Color,
    val surfaceVariant: Color,
    val disabledText: Color,
    val discountBg: Color,
    val takenSeat: Color,
    val selectedSeat: Color,
    val availableSeat: Color,
    val justTakenSeat: Color,
)

val DarkAppColors = AppColors(
    primary = AccentPurple,
    header = Color(0xFF1E1E2A),
    text = OnDarkBg,
    icon = OnDarkBg,
    metaText = Color(0xFFADB5BD),         // szarawy tekst pomocniczy
    cardTitle = Color(0xFFE0E0E0),        // tytuł w karcie
    movieOverlay = Color(0xCC000000),     // przezroczysty overlay
    searchBorder = DarkSearchBorder,
    textSecondary = DarkMetaText,
    background = DarkBg,
    heading = OnDarkBg,
    error = Color.Red,
    buttonText = Color.White,
    cardBackground = DarkSearchBg,
    surfaceVariant = Color(0xFF1E2433),
    disabledText = Color.Gray,
    discountBg = DarkDiscountBg,
    takenSeat = TakenColor,
    selectedSeat = SelectedColor,
    availableSeat = AvailableColor,
    justTakenSeat = JustTakenColor,
)

val LightAppColors = AppColors(
    primary = AccentPurple,
    header = Color(0xFFF2F2F9),
    text = OnLightBg,
    icon = OnLightBg,
    metaText = Color(0xFF6C757D),         // jasnoszary tekst pomocniczy
    cardTitle = Color(0xFF212529),        // ciemny tekst
    movieOverlay = Color(0x66000000),     // półprzezroczysty overlay
    searchBorder = LightSearchBorder,
    textSecondary = LightMetaText,
    background = LightBg,
    heading = OnLightBg,
    error = Color.Red,
    buttonText = Color.White,
    cardBackground = LightSearchBg,
    surfaceVariant = Color(0xFFE9ECEF),
    disabledText = Color.Gray,
    discountBg = LightDiscountBg,
    takenSeat = TakenColor,
    selectedSeat = SelectedColor,
    availableSeat = AvailableColor,
    justTakenSeat = JustTakenColor,
)


val LocalAppColors = staticCompositionLocalOf { LightAppColors } // fallback na jasny
