package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LuxuryGold,
    onPrimary = LuxuryBlack,
    primaryContainer = LuxurySurfaceVariant,
    onPrimaryContainer = LuxuryGoldLight,
    secondary = LuxuryElectricBlue,
    onSecondary = Color.White,
    secondaryContainer = LuxuryNavyDark,
    onSecondaryContainer = LuxuryIceBlue,
    tertiary = LuxuryGoldLight,
    onTertiary = LuxuryBlack,
    background = LuxuryBlack,
    onBackground = LuxuryTextPrimaryDark,
    surface = LuxuryNavyDark,
    onSurface = LuxuryTextPrimaryDark,
    surfaceVariant = LuxurySurface,
    onSurfaceVariant = LuxuryTextSecondaryDark,
    outline = LuxuryGoldDark,
    outlineVariant = LuxuryBorderDark
)

private val LightColorScheme = lightColorScheme(
    primary = LuxuryGoldDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF9C3),
    onPrimaryContainer = LuxuryGoldDark,
    secondary = LuxuryRoyalBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDBEAFE),
    onSecondaryContainer = LuxuryRoyalBlue,
    tertiary = LuxuryGold,
    onTertiary = Color.White,
    background = LuxuryLightBackground,
    onBackground = LuxuryTextPrimaryLight,
    surface = LuxuryLightSurface,
    onSurface = LuxuryTextPrimaryLight,
    surfaceVariant = LuxuryLightSurfaceVariant,
    onSurfaceVariant = LuxuryTextSecondaryLight,
    outline = LuxuryGold,
    outlineVariant = LuxuryBorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to stunning luxury dark aesthetic
    dynamicColor: Boolean = false, // Keep consistent luxury branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
