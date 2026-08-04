package com.institutional.tradingjournal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF09090B)
val SurfaceDark = Color(0xFF121215)
val SurfaceCard = Color(0xFF1C1C21)
val GoldPrimary = Color(0xFFFFD700)
val GoldVariant = Color(0xFFC5A000)
val ProfitGreen = Color(0xFF00E676)
val LossRed = Color(0xFFFF5252)
val TextWhite = Color(0xFFF4F4F5)
val TextMuted = Color(0xFFA1A1AA)
val BorderGlass = Color(0xFF27272A)

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    secondary = GoldVariant,
    background = DarkBackground,
    surface = SurfaceDark,
    onPrimary = Color.Black,
    onBackground = TextWhite,
    onSurface = TextWhite
)

@Composable
fun OrderflowTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

