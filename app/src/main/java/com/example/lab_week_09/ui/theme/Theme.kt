package com.example.lab_week_09.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//Dark color scheme for purple-black theme
private val DarkColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    secondary = PurpleLight,
    onSecondary = Color.White,
    background = BlackBackground,
    onBackground = WhiteText,
    surface = DarkGraySurface,
    onSurface = WhiteText
)

//Light color scheme (optional, but we keep it subtle purple)
private val LightColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = Color.White,
    secondary = PurpleLight,
    onSecondary = Color.Black,
    background = Color(0xFFF8F4FF), // very light purple tint
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black
)

@Composable
fun LAB_WEEK_09Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // disable dynamic for consistency
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
