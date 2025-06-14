package com.example.mobile_application.ui.theme

import androidx.compose.ui.graphics.Color

var isDarkTheme = false

// Dark mode colors
//val DarkBgHeader       = Color(0xFF151A28)
//val DarkShadow         = Color(0x4D000000)
//val DarkTextSecondary  = Color(0xFF999999)
val DarkSearchBg       = Color(0xFF1E2433)
val DarkSearchBorder   = Color(0xFF2A3142)
//val DarkTextPrimary    = Color(0xFFE9ECEF)
val AccentPurple       = Color(0xFF4318D1)
//val ButtonTextLight    = Color(0xFFFFFFFF)

// Main content (scopes)
val LightBg           = Color(0xFFFFFFFF)
val DarkBg            = Color(0xFF0A0F1C)
val OnDarkBg          = Color(0xFFFFFFFF)
val OnLightBg         = Color(0xFF000000)

// Cards
//val CardTitle         = Color(0xFFE0E0E0)
//val DurationBgHover   = Color(0xB3000000)
//val DurationText      = Color(0xFFFFFFFF)

// Metadata & genres
val LightMetaText = Color(0xFF4A4A4A)
val DarkMetaText = Color(0xFFADB5BD)
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
