package com.example.mobile_application.ui.theme

import androidx.compose.ui.graphics.Color

var isDarkTheme = false

// Główne kolory aplikacji
val AccentPurple = Color(0xFF4318D1)

// Jasny motyw
val LightBg = Color(0xFFF5F7FA)
val OnLightBg = Color(0xFF1F2937)
val LightMetaText = Color(0xFF6C757D)
val LightSearchBorder = Color(0xFFE0E0E0)

// Ciemny motyw
val DarkBg = Color(0xFF0A0F1C)
val OnDarkBg = Color(0xFFE4E4E4)
val DarkMetaText = Color(0xFFADB5BD)
val DarkSearchBg = Color(0xFF151A28)
val DarkSearchBorder = Color(0xFF2C2F3A)
val GenrePurple       = AccentPurple

// Crew info
val CrewBg            = Color(0x0AFFFFFF)
val CrewBorderAccent  = Color(0xFFFACC15)

// Seats
val JustTakenColor = Color(0xFFff5555)
val TakenColor = Color(0xFF73138b)
val SelectedColor = Color(0xFF4318d1)
val AvailableColor = Color(0xFF1e2433)

fun getMetaTextColor(darkTheme: Boolean): Color =
    if (darkTheme) DarkMetaText else LightMetaText
