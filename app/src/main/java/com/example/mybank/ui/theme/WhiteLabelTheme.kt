package com.example.mybank.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

enum class WhiteLabelBrand {
    ClientA,
    ClientB
}

private val clientAColors = lightColorScheme(
    primary = BrandADeep,
    onPrimary = BrandALight,
    background = BrandALight,
    onBackground = BrandADeep,
    surface = BrandALight,
    onSurface = BrandADeep,
    secondary = BrandAAttenuated,
    onSecondary = BrandALight,
    error = BrandError,
    onError = BrandALight
)

private val clientBColors = darkColorScheme(
    primary = BrandBAccent,
    onPrimary = BrandALight,
    background = BrandBAccent,
    onBackground = BrandALight,
    surface = BrandBAccent,
    onSurface = BrandALight,
    secondary = BrandBBorder,
    onSecondary = BrandBAccent,
    error = BrandError,
    onError = BrandALight
)

@Composable
fun WhiteLabelTheme(
    brand: WhiteLabelBrand = WhiteLabelBrand.ClientA,
    content: @Composable () -> Unit
) {
    val colors: ColorScheme = when (brand) {
        WhiteLabelBrand.ClientA -> clientAColors
        WhiteLabelBrand.ClientB -> clientBColors
    }
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}

