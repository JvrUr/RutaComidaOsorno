package com.example.osornogourmet.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable

// Esquema de colores oscuro con tema gastronómico
private val DarkColorScheme = darkColorScheme(
    primary = OrangeMain,
    onPrimary = TextWhite,
    primaryContainer = OrangeDark,
    onPrimaryContainer = CreamWhite,
    secondary = GoldAccent,
    onSecondary = TextDark,
    secondaryContainer = WarmBrown,
    onSecondaryContainer = CreamWhite,
    tertiary = GreenSuccess,
    onTertiary = TextDark,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextGray,
    error = Color(0xFFFF6B6B),
    onError = TextWhite,
    outline = TextGray
)

// Tema principal de OsornoGourmet
@Composable
fun OsornoGourmetTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = OsornoGourmetTypography,
        content = content
    )
}
