package com.example.datingappkmp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Pink,
    onPrimary = White,
    primaryContainer = PinkDark,
    onPrimaryContainer = PinkLight,

    secondary = Blue,
    onSecondary = White,
    secondaryContainer = BlueDark,
    onSecondaryContainer = BlueLight,

    tertiary = CategoryAdventure,
    onTertiary = White,

    background = Dark,
    onBackground = Light,

    surface = DarkGray,
    onSurface = Light,
    surfaceVariant = MediumGray,
    onSurfaceVariant = LightGray,

    error = Error,
    onError = White,

    outline = MediumGray,
    outlineVariant = DarkGray
)

private val LightColorScheme = lightColorScheme(
    primary = Pink,
    onPrimary = White,
    primaryContainer = PinkLight,
    onPrimaryContainer = PinkDark,

    secondary = Blue,
    onSecondary = White,
    secondaryContainer = BlueLight,
    onSecondaryContainer = BlueDark,

    tertiary = CategoryAdventure,
    onTertiary = White,

    background = Light,
    onBackground = Dark,

    surface = White,
    onSurface = Dark,
    surfaceVariant = VeryLightGray,
    onSurfaceVariant = MediumGray,

    error = Error,
    onError = White,

    outline = LightGray,
    outlineVariant = VeryLightGray
)

@Composable
fun ParyTalkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
