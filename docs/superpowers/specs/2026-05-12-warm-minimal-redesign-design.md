# UI Redesign: Warm Minimal Dark Theme — Design Specification

**Date:** 2026-05-12
**Project:** OSI IPTV (NativePlayer)
**Scope:** Complete visual redesign — Theme, all Screens, Components
**Design System:** `design-system/osi-iptv/MASTER.md`

---

## 1. Overview

Replace the current cold blue-accented dark theme with a warm minimal dark theme featuring gold (`#CCA770`) accents, Inter typography, and consistent design tokens across all 8 screens.

**What changes:**
- `ui/theme/Color.kt` — new semantic color tokens
- `ui/theme/Theme.kt` — new `WarmNativePlayerTheme` with custom typography + color scheme
- All screen composables — restyled with new tokens, borders, radii
- Loading/empty/error states — skeleton loading, improved empty states
- Player overlay — blurred glassmorphism overlay

**What does NOT change:**
- App architecture (Hilt, Room, Retrofit, Navigation)
- Video player engine (ExoPlayer)
- Business logic & ViewModels
- No light mode support (dark-only by design)
- Screen structure & navigation

---

## 2. Color Migration Map

| Old Token | New Token | Old Hex | New Hex |
|-----------|-----------|---------|---------|
| `DarkBackground` | `WarmBackground` | `#121212` | `#0D0D0B` |
| `SurfaceDark` | `WarmSurface` | `#1E1E1E` | `#1A1A17` |
| `SurfaceLight` | `WarmSurfaceElevated` | `#2C2C2C` | `#252522` |
| `AccentBlue` | `WarmGoldAccent` | `#3D8BFD` | `#CCA770` |
| `AccentGreen` | (removed — use Gold) | `#4CAF50` | — |
| `TextPrimary` | `WarmTextPrimary` | `#E0E0E0` | `#E8E3D7` |
| `TextSecondary` | `WarmTextSecondary` | `#9E9E9E` | `#8A857C` |
| `StarYellow` | `WarmGoldStar` | `#FFC107` | `#CCA770` |
| `ErrorRed` | `WarmError` | `#CF6679` | `#E05555` |

**New tokens added:**
- `WarmGoldAccentDim` = `rgba(204,167,112,0.12)`
- `WarmTextOnGold` = `#0D0D0B`
- `WarmBorder` = `rgba(232,227,215,0.08)`
- `WarmProgressTrack` = `#252522`

---

## 3. Theme.kt Redesign

The existing `NativePlayerTheme` will be replaced with `WarmNativePlayerTheme`:

```kotlin
@Composable
fun WarmNativePlayerTheme(content: @Composable () -> Unit) {
    val colorScheme = warmDarkColorScheme()
    val typography = warmTypography()
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}
```

**Key changes in Theme.kt:**
1. Map semantic tokens to Material3 `darkColorScheme` roles
2. Define `warmTypography()` with Inter-based `Typography` object
3. Export `WarmNativePlayerTheme` as the new theme composable

**Material3 Color Mapping:**
- `primary` → `WarmGoldAccent`
- `onPrimary` → `WarmTextOnGold`
- `primaryContainer` → `WarmGoldAccentDim`
- `background` → `WarmBackground`
- `onBackground` → `WarmTextPrimary`
- `surface` → `WarmSurface`
- `onSurface` → `WarmTextPrimary`
- `surfaceVariant` → `WarmSurfaceElevated`
- `onSurfaceVariant` → `WarmTextSecondary`
- `error` → `WarmError`
- `outline` → `WarmBorder`

---

## 4. Typography Tokens (Inter)

```kotlin
val WarmTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Bold,      // 700
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.SemiBold,  // 600
        fontSize = 20.sp,
        letterSpacing = (-0.3).sp
    ),
    titleMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,    // 500
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,    // 400
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Medium,    // 500
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = InterFamily,
        fontWeight = FontWeight.Normal,    // 400
        fontSize = 10.sp
    )
)
```

---

## 5. Component Restyling (per screen)

### 5.1 Theme Files (Color.kt, Theme.kt)
**Changes:** Complete rewrite. Remove old colors, add new semantic tokens + Inter typography.

### 5.2 SplashScreen
- Background gradient: `WarmBackground` → `WarmSurface`
- Text/icon: `WarmGoldAccent` branding
- Spinner: Replace with skeleton or themed `CircularProgressIndicator` (gold track)

### 5.3 HomeScreen
- TopAppBar: `WarmSurface` bg, gold title color
- Tab indicators: `animateColorAsState` from `WarmGoldAccent` to gold
- Tab unselected: `WarmTextSecondary`
- Cards (channels/poster grids): `WarmSurface` bg, `md` radius, `WarmBorder` hairline

### 5.4 LiveScreen
- Category panel: `WarmSurface` bg, selected category highlighted in `WarmGoldAccentDim`
- Channel cards: `WarmSurface` bg, `md` radius

### 5.5 MoviesScreen / SeriesScreen
- Poster cards: `md` radius, `WarmBorder`, lazy loading skeleton
- Category sidebar: Same styling as LiveScreen

### 5.6 MovieDetailScreen / SeriesDetailScreen
- Poster: `md` radius, soft shadow
- Resume banner: `WarmGoldAccentDim` bg, gold progress bar, gold icon
- Play button: Full-width gold background, dark text
- Plot text: `WarmTextSecondary`

### 5.7 SearchScreen
- Search field: `WarmSurfaceElevated` bg, gold focus border
- Result cards: Same card styling as other screens
- Empty state: Themed icon + text

### 5.8 PlayerScreen (largest change)
- Controls overlay: Blurred background (`rgba(13,13,11,0.85)` + software blur approximation)
- Seekbar: Gold track + 12dp thumb with glow
- Buttons: Gold accent active, `WarmTextSecondary` inactive
- Speed selector: `WarmSurfaceElevated` dropdown
- Time text: `WarmTextSecondary`

### 5.9 FavoritesScreen
- List items: `md` radius, `WarmBorder`
- Empty state: Gold icon + themed empty text
- Delete button: `WarmError` tinted

### 5.10 SettingsScreen
- Text fields: `WarmSurfaceElevated` bg, gold focus border
- Dialogs: MD3 `AlertDialog` with custom `WarmSurfaceElevated` bg
- Account cards: Card styling consistent with rest of app

---

## 6. Loading & Empty States

**Current:** CircularProgressIndicator (blue default) or no loading state at all.

**New:**
- **Skeleton loading:** Shimmer effect on `WarmSurface` placeholder shapes matching content layout (poster rectangles, text lines)
- **Empty states:** Gold icon + `WarmTextPrimary` title + `WarmTextSecondary` description
- **Error states:** Red icon + retry button in gold

---

## 7. Implementation Order

1. **Theme Layer** (`Color.kt`, `Theme.kt`) — foundation for everything
2. **HomeScreen** — Most visible, sets the tone
3. **LiveScreen** — Complex layout, immediate visibility
4. **MoviesScreen / SeriesScreen** — Similar card patterns
5. **MovieDetailScreen / SeriesDetailScreen** — Detail pages
6. **PlayerScreen** — Most complex UI component
7. **FavoritesScreen** — List-based
8. **SearchScreen** — Search components
9. **SplashScreen** — Entry point
10. **SettingsScreen** — Account management

---

## 8. Success Criteria

- [x] All 8 screens use the new warm dark theme consistently
- [x] No old blue accent (`#3D8BFD`) remains anywhere
- [x] Inter font loaded and applied to all text
- [x] All colors are semantic tokens (no raw hex in screen code)
- [x] Loading states use skeleton instead of spinners
- [x] Player overlay has blurred background
- [x] App compiles and runs without regressions
- [x] Design tokens documented in `design-system/osi-iptv/MASTER.md`
