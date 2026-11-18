package com.example.oftapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ClinicalPrimary,
    secondary = ClinicalSecondary,
    background = Color(0xFF0D1117),
    onBackground = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val LightColorScheme = lightColorScheme(
    primary = ClinicalPrimary,
    onPrimary = Color.White,
    primaryContainer = OftAppTealContainer,
    onPrimaryContainer = OftAppTealDark,
    secondary = ClinicalSecondary,
    background = ClinicalBackground,
    surface = OftAppCardBg,
    onBackground = OftAppTextPrimary,
    onSurface = OftAppTextPrimary,
    onSurfaceVariant = ClinicalOnSurfaceVariant
)

/**
 * Tema canónico de OftApp. Paleta clínica unificada (Clinical* + OftApp*).
 * dynamicColor desactivado por defecto para preservar la identidad visual clínica.
 */
@Composable
fun OftAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

/**
 * Alias de compatibilidad para el nombre anterior del tema (OftappTheme).
 * Permite que las pantallas heredadas de las ramas sigan compilando sin cambios.
 */
@Composable
fun OftappTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    OftAppTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        content = content
    )
}
