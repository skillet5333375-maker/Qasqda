package com.example.medalgorithms.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6AA9FF),
    secondary = Color(0xFF8CCBFF),
    background = Color(0xFFF4FAFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF0F1A2A),
    onBackground = Color(0xFF0F1A2A),
    onSurface = Color(0xFF0F1A2A)
)

@Composable
fun MedAlgorithmsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
