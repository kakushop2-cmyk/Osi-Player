package com.nativeplayer.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════════
// OSI NativePlayer Design System — Schwarz / Cyan / Sternengelb
// Variant: MINIMAL (pure black, clean borders, no glow)
// ═══════════════════════════════════════════════════════════════

// ── Backgrounds (OLED-friendly) ─────────────────────────────
val OsiBlack          = Color(0xFF000000)   // Pure OLED background
val OsiSurface        = Color(0xFF000000)   // Cards, Panels (Minimal = same as bg)
val OsiSurfaceHigh    = Color(0xFF0A0A0A)   // Dialogs, TopBar
val OsiSurfaceBright  = Color(0xFF111111)   // Highest elevation

// ── Cyan — Interactive & Focus ──────────────────────────────
val OsiCyan           = Color(0xFF19EAFB)   // Primary: focus, links, active
val OsiCyanDim        = Color(0x1F19EAFB)   // ~12% — subtle background tint
val OsiCyanGlow       = Color(0x4D19EAFB)   // ~30% — glow effects (reserved)
val OsiCyanBorder     = Color(0x6619EAFB)   // ~40% — focus border

// ── Sternengelb — Highlights & CTAs ─────────────────────────
val OsiStarYellow     = Color(0xFFFFD700)   // Stars, Play buttons, badges
val OsiStarYellowDim  = Color(0x1FFFD700)   // ~12% — background tint
val OsiStarYellowGlow = Color(0x4DFFD700)   // ~30% — glow (reserved)

// ── Text ────────────────────────────────────────────────────
val OsiTextPrimary    = Color(0xFFEAEAEA)   // Main text
val OsiTextSecondary  = Color(0xFF7A7A7A)   // Subtitles, meta
val OsiTextMuted      = Color(0xFF4A4A4A)   // Placeholders, disabled

// ── Semantic ────────────────────────────────────────────────
val OsiError          = Color(0xFFE05555)   // Errors, destructive actions
val OsiBorder         = Color(0x14FFFFFF)   // ~8% subtle border
val OsiDivider        = Color(0x0DFFFFFF)   // ~5% divider
val OsiOverlay        = Color(0xCC000000)   // 80% black overlay
val OsiOverlayLight   = Color(0x66000000)   // 40% black overlay

// ── Legacy Aliases (backward compatibility during migration) ─
val WarmBackground      = OsiBlack
val WarmSurface         = OsiSurface
val WarmSurfaceElevated = OsiSurfaceHigh
val WarmGold            = OsiCyan              // Primary accent is now Cyan
val WarmTextPrimary     = OsiTextPrimary
val WarmTextSecondary   = OsiTextSecondary
val WarmError           = OsiError
val WarmGoldDim         = OsiCyanDim
val WarmTextOnGold      = Color(0xFF000000)    // Dark text on cyan
val WarmBorder          = OsiBorder
val WarmProgressTrack   = OsiSurface
val NetflixRed          = Color(0xFFE50914)    // Kept for any remaining usage

// ── Carousel Tokens ─────────────────────────────────────────
val CarouselBackground  = OsiBlack
val CarouselSurface     = Color(0xFF0C0C0C)
val CarouselOverlay     = OsiOverlay
val CarouselBadgeBg     = OsiOverlay
