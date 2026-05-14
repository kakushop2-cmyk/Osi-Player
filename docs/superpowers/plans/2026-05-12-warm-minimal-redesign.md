# Warm Minimal Theme Redesign — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the cold blue-accented dark theme with a warm minimal dark theme using gold (`#CCA770`) accent colors, Inter typography, and consistent design tokens across all 11 screen files.

**Architecture:** Two-phase approach. Phase 1 rewrites `Color.kt` and `Theme.kt` with backward-compatible deprecated aliases so the app compiles immediately. Phase 2 systematically replaces all deprecated usages in each screen file with the new color names.

**Tech Stack:** Kotlin 2.0, Jetpack Compose, Material 3, Google Fonts (Inter)

---

## Color Name Migration Map

| Old Name | New Name | Old Hex | New Hex |
|----------|----------|---------|---------|
| `DarkBackground` | `WarmBackground` | `#121212` | `#0D0D0B` |
| `SurfaceDark` | `WarmSurface` | `#1E1E1E` | `#1A1A17` |
| `SurfaceLight` | `WarmSurfaceElevated` | `#2C2C2C` | `#252522` |
| `AccentBlue` | `WarmGold` | `#3D8BFD` | `#CCA770` |
| `AccentGreen` | `WarmGold` (same) | `#4CAF50` | `#CCA770` |
| `TextPrimary` | `WarmTextPrimary` | `#E0E0E0` | `#E8E3D7` |
| `TextSecondary` | `WarmTextSecondary` | `#9E9E9E` | `#8A857C` |
| `StarYellow` | `WarmGold` (same) | `#FFC107` | `#CCA770` |
| `ErrorRed` | `WarmError` | `#CF6679` | `#E05555` |

**Two special cases:**
- `AccentBlue.copy(alpha = 0.2f)` → `WarmGoldDim` (new constant: `WarmGold.copy(alpha = 0.12f)`)
- `AccentGreen` in section headers → `WarmGold` (unified accent)

---

### Task 1: Rewrite Color.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/theme/Color.kt`

- [ ] **Step 1: Replace entire Color.kt with new warm gold tokens + deprecated aliases**

```kotlin
package com.nativeplayer.ui.theme

import androidx.compose.ui.graphics.Color

// ── New Semantic Warm Gold Palette ────────────────────────
val WarmBackground = Color(0xFF0D0D0B)
val WarmSurface = Color(0xFF1A1A17)
val WarmSurfaceElevated = Color(0xFF252522)
val WarmGold = Color(0xFFCCA770)
val WarmTextPrimary = Color(0xFFE8E3D7)
val WarmTextSecondary = Color(0xFF8A857C)
val WarmTextOnGold = Color(0xFF0D0D0B)
val WarmError = Color(0xFFE05555)
val WarmGoldDim = WarmGold.copy(alpha = 0.12f)
val WarmBorder = Color(0x14E8E3D7)   // rgba(232,227,215,0.08)
val WarmProgressTrack = WarmSurfaceElevated

// ── Deprecated Backward-Compatibility Aliases ──────────────
// Remove these imports from all screen files before final merge.
@Deprecated("Use WarmBackground", ReplaceWith("WarmBackground"))
val DarkBackground = WarmBackground

@Deprecated("Use WarmSurface", ReplaceWith("WarmSurface"))
val SurfaceDark = WarmSurface

@Deprecated("Use WarmSurfaceElevated", ReplaceWith("WarmSurfaceElevated"))
val SurfaceLight = WarmSurfaceElevated

@Deprecated("Use WarmGold", ReplaceWith("WarmGold"))
val AccentBlue = WarmGold

@Deprecated("Use WarmGold", ReplaceWith("WarmGold"))
val AccentGreen = WarmGold

@Deprecated("Use WarmTextPrimary", ReplaceWith("WarmTextPrimary"))
val TextPrimary = WarmTextPrimary

@Deprecated("Use WarmTextSecondary", ReplaceWith("WarmTextSecondary"))
val TextSecondary = WarmTextSecondary

@Deprecated("Use WarmGold", ReplaceWith("WarmGold"))
val StarYellow = WarmGold

@Deprecated("Use WarmError", ReplaceWith("WarmError"))
val ErrorRed = WarmError
```

- [ ] **Step 2: Verify Color.kt compiles**

Run: `./gradlew :app:compileDebugKotlin 2>&1 | tail -20`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/theme/Color.kt
git commit -m "feat: add warm gold color palette with deprecated backward-compat aliases"
```

---

### Task 2: Rewrite Theme.kt with Inter Typography

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/theme/Theme.kt`

- [ ] **Step 1: Replace entire Theme.kt**

```kotlin
package com.nativeplayer.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Warm Dark Color Scheme ────────────────────────────────
private val WarmDarkColorScheme = darkColorScheme(
    primary = WarmGold,
    onPrimary = WarmTextOnGold,
    primaryContainer = WarmGoldDim,
    secondary = WarmGold,
    onSecondary = WarmTextOnGold,
    background = WarmBackground,
    onBackground = WarmTextPrimary,
    surface = WarmSurface,
    onSurface = WarmTextPrimary,
    surfaceVariant = WarmSurfaceElevated,
    onSurfaceVariant = WarmTextSecondary,
    error = WarmError,
    onError = WarmTextOnGold,
    outline = WarmBorder
)

// ── Inter Typography ──────────────────────────────────────
// Note: Inter must be bundled via downloadable fonts or asset.
// Font family is set to sans-serif fallback until Inter is loaded.
private val DefaultFontFamily = androidx.compose.ui.text.font.FontFamily.Default

private val WarmTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = (-0.3).sp
    ),
    titleMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = DefaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
)

// ── New Theme Composable ──────────────────────────────────
@Composable
fun WarmNativePlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WarmDarkColorScheme,
        typography = WarmTypography,
        content = content
    )
}

// ── Deprecated Alias ──────────────────────────────────────
@Deprecated("Use WarmNativePlayerTheme", ReplaceWith("WarmNativePlayerTheme"))
@Composable
fun NativePlayerTheme(content: @Composable () -> Unit) {
    WarmNativePlayerTheme(content)
}
```

- [ ] **Step 2: Verify Theme.kt compiles**

Run: `./gradlew :app:compileDebugKotlin 2>&1 | tail -20`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/theme/Theme.kt
git commit -m "feat: add WarmNativePlayerTheme with Inter typography and warm gold color scheme"
```

---

### Task 3: Update MainActivity.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/MainActivity.kt`

- [ ] **Step 1: Replace theme and color references**

Replace the content with:

```kotlin
package com.nativeplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.nativeplayer.ui.navigation.NavGraph
import com.nativeplayer.ui.theme.WarmBackground
import com.nativeplayer.ui.theme.WarmNativePlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WarmNativePlayerTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = WarmBackground) {
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}
```

The key changes from old `MainActivity.kt`:
- `NativePlayerTheme` → `WarmNativePlayerTheme`
- `DarkBackground` → `WarmBackground`

- [ ] **Step 2: Build debug APK to verify**

Run: `./gradlew :app:assembleDebug 2>&1 | tail -5`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/MainActivity.kt
git commit -m "feat: switch MainActivity to WarmNativePlayerTheme"
```

---

### Task 4: Update SplashScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/splash/SplashScreen.kt`

**Replacements (search-and-replace, order matters):**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `TextPrimary` → `WarmTextPrimary`
7. `TextSecondary` → `WarmTextSecondary`
8. `ErrorRed` → `WarmError`

- [ ] **Step 1: Apply all 8 search-and-replace operations**

Use the replaceAll=true flag for each edit operation on SplashScreen.kt.

- [ ] **Step 2: Build to verify**

Run: `./gradlew :app:compileDebugKotlin 2>&1 | tail -5`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/splash/SplashScreen.kt
git commit -m "refactor: migrate SplashScreen to warm gold colors"
```

---

### Task 5: Update HomeScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt`

**Replacements (search-and-replace):**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `TextPrimary` → `WarmTextPrimary`
6. `TextSecondary` → `WarmTextSecondary`

- [ ] **Step 1: Apply all 6 replacements**

- [ ] **Step 2: Build to verify**

Run: `./gradlew :app:compileDebugKotlin 2>&1 | tail -5`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "refactor: migrate HomeScreen to warm gold colors"
```

---

### Task 6: Update LiveScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `AccentBlue.copy(alpha = 0.2f)` → `WarmGoldDim`  (occurs 2 times)
7. `TextPrimary` → `WarmTextPrimary`
8. `TextSecondary` → `WarmTextSecondary`
9. `ErrorRed` → `WarmError`

- [ ] **Step 1: Apply all replacements in order**

- [ ] **Step 2: Build to verify**

Run: `./gradlew :app:compileDebugKotlin 2>&1 | tail -5`

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt
git commit -m "refactor: migrate LiveScreen to warm gold colors"
```

---

### Task 7: Update MoviesScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `AccentBlue.copy(alpha = 0.2f)` → `WarmGoldDim`
7. `TextPrimary` → `WarmTextPrimary`
8. `TextSecondary` → `WarmTextSecondary`
9. `StarYellow` → `WarmGold`
10. `ErrorRed` → `WarmError`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt
git commit -m "refactor: migrate MoviesScreen to warm gold colors"
```

---

### Task 8: Update MovieDetailScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MovieDetailScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `TextPrimary` → `WarmTextPrimary`
6. `TextSecondary` → `WarmTextSecondary`
7. `ErrorRed` → `WarmError`
8. `StarYellow` → `WarmGold`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/movies/MovieDetailScreen.kt
git commit -m "refactor: migrate MovieDetailScreen to warm gold colors"
```

---

### Task 9: Update SeriesScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `AccentBlue.copy(alpha = 0.2f)` → `WarmGoldDim`
7. `TextPrimary` → `WarmTextPrimary`
8. `TextSecondary` → `WarmTextSecondary`
9. `StarYellow` → `WarmGold`
10. `ErrorRed` → `WarmError`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt
git commit -m "refactor: migrate SeriesScreen to warm gold colors"
```

---

### Task 10: Update SeriesDetailScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesDetailScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `TextPrimary` → `WarmTextPrimary`
6. `TextSecondary` → `WarmTextSecondary`
7. `ErrorRed` → `WarmError`
8. `StarYellow` → `WarmGold`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/series/SeriesDetailScreen.kt
git commit -m "refactor: migrate SeriesDetailScreen to warm gold colors"
```

---

### Task 11: Update SearchScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/search/SearchScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `TextPrimary` → `WarmTextPrimary`
7. `TextSecondary` → `WarmTextSecondary`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/search/SearchScreen.kt
git commit -m "refactor: migrate SearchScreen to warm gold colors"
```

---

### Task 12: Update PlayerScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `TextPrimary` → `WarmTextPrimary`
6. `TextSecondary` → `WarmTextSecondary`
7. `ErrorRed` → `WarmError`

**Additional PlayerScreen changes (gold seekbar, blurred overlay):**
The player screen currently uses `AccentBlue` for the seek bar track/thumb. After replacement, the seek bar will automatically use `WarmGold`. No additional code changes needed for the color — Material3 `Slider` picks up `primary` from the theme.

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git commit -m "refactor: migrate PlayerScreen to warm gold colors"
```

---

### Task 13: Update FavoritesScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/favorites/FavoritesScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `AccentGreen` → `WarmGold`
6. `TextPrimary` → `WarmTextPrimary`
7. `TextSecondary` → `WarmTextSecondary`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/favorites/FavoritesScreen.kt
git commit -m "refactor: migrate FavoritesScreen to warm gold colors"
```

---

### Task 14: Update SettingsScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt`

**Replacements:**
1. `DarkBackground` → `WarmBackground`
2. `SurfaceDark` → `WarmSurface`
3. `SurfaceLight` → `WarmSurfaceElevated`
4. `AccentBlue` → `WarmGold`
5. `TextPrimary` → `WarmTextPrimary`
6. `TextSecondary` → `WarmTextSecondary`
7. `ErrorRed` → `WarmError`

- [ ] **Step 1: Apply all replacements**

- [ ] **Step 2: Build**

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt
git commit -m "refactor: migrate SettingsScreen to warm gold colors"
```

---

### Task 15: Remove Deprecated Aliases & Final Cleanup

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/theme/Color.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/theme/Theme.kt`

Now that all screens use the new names, we can remove the backward-compat aliases.

- [ ] **Step 1: Remove deprecated aliases from Color.kt**

Replace Color.kt with a clean version (no aliases):

```kotlin
package com.nativeplayer.ui.theme

import androidx.compose.ui.graphics.Color

val WarmBackground = Color(0xFF0D0D0B)
val WarmSurface = Color(0xFF1A1A17)
val WarmSurfaceElevated = Color(0xFF252522)
val WarmGold = Color(0xFFCCA770)
val WarmTextPrimary = Color(0xFFE8E3D7)
val WarmTextSecondary = Color(0xFF8A857C)
val WarmTextOnGold = Color(0xFF0D0D0B)
val WarmError = Color(0xFFE05555)
val WarmGoldDim = WarmGold.copy(alpha = 0.12f)
val WarmBorder = Color(0x14E8E3D7)
val WarmProgressTrack = WarmSurfaceElevated
```

- [ ] **Step 2: Remove deprecated alias from Theme.kt**

Replace Theme.kt — remove the `@Deprecated NativePlayerTheme` function. Keep only `WarmNativePlayerTheme`.

- [ ] **Step 3: Full build and verify zero deprecated usage warnings**

Run: `./gradlew :app:assembleDebug 2>&1 | grep -i "deprecated" || echo "No deprecation warnings — clean"`
Expected: "No deprecation warnings — clean"

- [ ] **Step 4: Verify no old colors remain**

Run: `grep -r "AccentBlue\|AccentGreen\|DarkBackground\|SurfaceDark\|SurfaceLight\|TextPrimary\|TextSecondary\|StarYellow\|ErrorRed" app/src/main/java/ --include="*.kt" || echo "No old colors found"`
Expected: "No old colors found"

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/theme/Color.kt app/src/main/java/com/nativeplayer/ui/theme/Theme.kt
git commit -m "refactor: remove deprecated color aliases, finalize warm gold theme"
```

---

### Task 16: Final Verification

- [ ] **Step 1: Clean build**

Run: `./gradlew clean :app:assembleDebug 2>&1 | tail -10`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Verify APK exists**

Run: `ls -lh app/build/outputs/apk/debug/app-debug.apk`
Expected: File exists, size > 5MB

- [ ] **Step 3: Lint check**

Run: `./gradlew :app:lintDebug 2>&1 | tail -15`
Expected: No new errors introduced

- [ ] **Step 4: Commit final state**

```bash
git add -A
git commit -m "chore: final verification - warm minimal theme complete"
```
