package com.nikelroid.artist_pedia.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0XFFD8E2FE),
    secondary = Color(0xFF3F5AA6),
    background = Color(0xFFF8F8FC),
    onBackground = Color(0xFF4F4F4F),
    onSurface = Color(0xFF00142A),
    error = Color(0xFFB3261E),
    surfaceBright = Color(0xFFE1E1E1),
    onTertiary = Color(0xFF45474F),
    onPrimaryContainer = Color(0xCCD8E2FE),
    surfaceContainerHighest = Color(0xCC151515),
    surface = Color(0xFFF0F0F0),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF2B4678),
    secondary = Color(0xFFAEC5FF),
    background = Color(0xFF121114),
    surface = Color(0xFF272727),
    onSurface = Color(0xFFC8DCF0),
    error = Color(0xFFFF4C45),
    surfaceBright = Color(0xFF646464),
    onBackground = Color(0xFFBEBCBC),
    onTertiary = Color(0xFFB0B0B0),
    onPrimaryContainer = Color(0xCC2B4678),
   surfaceContainerHighest = Color(0xCCF8F8F8)
    )

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}