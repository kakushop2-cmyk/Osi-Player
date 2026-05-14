package com.nativeplayer.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Inter Typography ────────────────────────────────────────
// TODO: Add Inter font files to res/font/ and load via FontFamily:
//   val Inter = FontFamily(
//       Font(R.font.inter_regular, FontWeight.Normal),
//       Font(R.font.inter_medium, FontWeight.Medium),
//       Font(R.font.inter_bold, FontWeight.Bold),
//   )
private val WarmTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp
    )
)

// ── Warm Dark Color Scheme ──────────────────────────────────
private val WarmDarkColorScheme = darkColorScheme(
    primary = WarmGold,
    onPrimary = WarmTextOnGold,
    primaryContainer = WarmGold.copy(alpha = 0.12f),
    onPrimaryContainer = WarmGold,
    secondary = WarmGold,
    onSecondary = WarmTextOnGold,
    secondaryContainer = WarmGold.copy(alpha = 0.08f),
    onSecondaryContainer = WarmGold,
    tertiary = WarmGold,
    onTertiary = WarmTextOnGold,
    background = WarmBackground,
    onBackground = WarmTextPrimary,
    surface = WarmSurface,
    onSurface = WarmTextPrimary,
    surfaceVariant = WarmSurfaceElevated,
    onSurfaceVariant = WarmTextSecondary,
    outline = WarmBorder,
    outlineVariant = WarmBorder,
    error = WarmError,
    onError = WarmTextPrimary
)

// ── Theme ────────────────────────────────────────────────────

@Composable
fun WarmNativePlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WarmDarkColorScheme,
        typography = WarmTypography,
        content = content
    )
}
