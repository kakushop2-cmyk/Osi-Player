package com.nativeplayer.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Typography ──────────────────────────────────────────────
private val OsiTypography = Typography(
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

// ── OSI Dark Color Scheme (Minimal Variant) ─────────────────
private val OsiDarkColorScheme = darkColorScheme(
    primary = OsiCyan,
    onPrimary = Color(0xFF000000),
    primaryContainer = OsiCyanDim,
    onPrimaryContainer = OsiCyan,
    secondary = OsiStarYellow,
    onSecondary = Color(0xFF000000),
    secondaryContainer = OsiStarYellowDim,
    onSecondaryContainer = OsiStarYellow,
    tertiary = OsiStarYellow,
    onTertiary = Color(0xFF000000),
    background = OsiBlack,
    onBackground = OsiTextPrimary,
    surface = OsiSurface,
    onSurface = OsiTextPrimary,
    surfaceVariant = OsiSurfaceHigh,
    onSurfaceVariant = OsiTextSecondary,
    outline = OsiBorder,
    outlineVariant = OsiDivider,
    error = OsiError,
    onError = OsiTextPrimary
)

// ── Theme Composable ────────────────────────────────────────

@Composable
fun WarmNativePlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = OsiDarkColorScheme,
        typography = OsiTypography,
        content = content
    )
}
