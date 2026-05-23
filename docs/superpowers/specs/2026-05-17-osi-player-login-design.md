# Osi Player — Login Screen & Branding

**Date:** 2026-05-17
**Status:** Approved
**Context:** `(b1)`

---

## 1. Overview

Add a branded login screen between Splash and Home screens. The app is renamed from "OSI IPTV" to **Osi Player** with a custom visual identity: the app emblem is a gold Play-Circle (▶ in a circle) that serves as the letter "O" in the wordmark.

### Objectives
- Beautiful first-impression login screen with the new brand identity
- Two login methods: Xtream Codes (server + username + password) and M3U Playlist (URL)
- Only shown on first launch — skip to Home if an account/m3u URL is already configured
- Follows Warm Dark design system (OLED-optimized, gold accent #CCA770, no pure black/white)

---

## 2. Navigation Changes

### Current flow
```
Splash → Home (success) or Settings (no account)
```

### New flow
```
Splash → Login (no account) → Home (success)
Splash → Home (account already configured)
```

### Changes to `Screen.kt`
Add a new route:
```kotlin
data object Login : Screen("login")
```

### Changes to `NavGraph.kt`
- Add `composable(Screen.Login.route)` destination
- Change `SplashState.NoAccount` navigation target from `Screen.Settings` to `Screen.Login`
- Remove the `popUpTo(Splash)` behavior for Login — Login replaces Splash in backstack

### Changes to `SplashScreen.kt`
- On `NoAccount` state: navigate to `Screen.Login` instead of `Screen.Settings`

---

## 3. Visual Design

### 3.1 Brand Identity

**Emblem:** Play-Circle — a gold (#CCA770) circle with a dark (WarmBackground) play triangle inside.

**Wordmark:** The Play-Circle replaces the letter "O" in "Osi Player":
```
[▶ Play-Circle] si Player
```
- Play-Circle: gold circle with play triangle
- "si": gold (#CCA770), bold weight
- "Player": WarmTextPrimary (#E8E3D7), bold weight
- All on one line, centered
- Font: Inter (or SansSerif fallback), weight 700–800

### 3.2 Login Screen Layout

**Design D — Logo-Fokus** with the following structure:

```
┌─────────────────────────────────┐
│                                 │
│     [Ambient Gold Glow BG]      │
│                                 │
│        [▶] si Player            │  ← Emblem + Wordmark
│                                 │
│   ┌──────────┬──────────┐       │
│   │ Xtream   │  M3U     │       │  ← Segmented Control
│   └──────────┴──────────┘       │
│                                 │
│   ┌─────────────────────────┐   │
│   │ Server URL              │   │
│   ├─────────────────────────┤   │
│   │ Benutzername            │   │  ← Xtream Codes fields
│   ├─────────────────────────┤   │
│   │ Passwort                │   │
│   └─────────────────────────┘   │
│                                 │
│   ┌─────────────────────────┐   │
│   │    Verbinden            │   │  ← Gold button
│   └─────────────────────────┘   │
│                                 │
│   Error message (conditional)   │
│                                 │
│   Weiter ohne Login (optional)  │
│                                 │
```

**M3U tab** shows only one field:
```
   ┌─────────────────────────┐
   │ M3U Playlist URL        │
   └─────────────────────────┘
```

### 3.3 Colors & Spacing

All values from `design-system/osi-iptv/MASTER.md`:

| Element | Value |
|---------|-------|
| Background | WarmBackground (#0D0D0B) |
| Card (fields container) | No card — fields direct on background |
| Input fields | WarmSurfaceElevated (#252522), radius 10dp |
| Active tab | WarmSurfaceElevated (#252522) + WarmTextPrimary |
| Inactive tab | transparent + WarmTextSecondary |
| Segmented control bg | WarmSurface (#1A1A17), radius 10dp, padding 4dp |
| Button | WarmGold (#CCA770), radius 10dp |
| Button text | WarmBackground (#0D0D0B), bold |
| Error text | WarmError (#E05555) |
| Emblem glow | Radial gradient, rgba(204,167,112, 0.08), center |
| Spacing | md=16dp, lg=24dp between sections |
| Corner radius | md=10dp (inputs, button, segmented control) |

### 3.4 Animations
- Entrance: Fade in (Crossfade, 300ms) for the whole content
- Tab switch: Crossfade between Xtream form and M3U form (150ms)
- Button state: loading spinner replaces text (Crossfade 150ms)
- Keyboard: content scrolls/pans, logo stays visible

---

## 4. LoginScreen Composable

### File: `app/src/main/java/com/nativeplayer/ui/login/LoginScreen.kt`

### 4.1 Component Tree
```
LoginScreen(viewModel: LoginViewModel, onLoginSuccess: () -> Unit)
├── Box (background = WarmBackground)
│   ├── AmbientGlow (radial gradient behind logo)
│   └── Column (centered, padded)
│       ├── AppLogo() ← PlayCircle + "si Player"
│       ├── Spacer(24.dp)
│       ├── SegmentedControl(selectedTab, onTabChange)
│       ├── Spacer(16.dp)
│       ├── AnimatedContent(tab) {
│       │   ├── XtreamFields (server, username, password)
│       │   └── M3UField (url)
│       │   }
│       ├── Spacer(24.dp)
│       ├── ConnectButton (state: idle/loading)
│       ├── Spacer(16.dp)
│       └── ErrorBanner (conditional)
└── Keyboard handling (imePadding + scroll)
```

### 4.2 AppLogo Composable

The logo is a `Row` with:
1. **PlayCircleIcon**: `Box` with `CircleShape`, gold background, `Canvas` drawing a play triangle (or use icon)
2. **"si" text**: `Text("si", color = WarmGold, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp)`
3. **" Player" text**: `Text(" Player", color = WarmTextPrimary, fontWeight = FontWeight.Bold, fontSize = 28.sp)`

PlayCircle implementation:
```kotlin
@Composable
fun PlayCircleLogo(modifier: Modifier = Modifier, size: Dp = 46.dp) {
    Box(
        modifier = modifier
            .size(size)
            .background(WarmGold, CircleShape)
            .shadow(8.dp, CircleShape, ambientColor = WarmGold.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        // Slight left padding to visually center the triangle
        Canvas(modifier = Modifier.size(size * 0.4f).padding(start = 1.dp)) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w * 0.15f, h * 0.1f)
                lineTo(w * 0.85f, h * 0.5f)
                lineTo(w * 0.15f, h * 0.9f)
                close()
            }
            drawPath(path, color = WarmBackground)
        }
    }
}
```

### 4.3 SegmentedControl

A `Row` with `Surface` background (WarmSurface, rounded), containing two `Box` composables. Active tab has `WarmSurfaceElevated` background, inactive shows `WarmTextSecondary`. Clipping with `RoundedCornerShape(8.dp)` on active indicator.

### 4.4 Input Fields

Use Material3 `OutlinedTextField` with custom `darkTextFieldColors()` (following existing pattern from `SettingsScreen.kt`):
- Container: `WarmSurfaceElevated`
- Border: `rgba(232,227,215, 0.06)` (faint warm border)
- Text: `WarmTextPrimary`
- Placeholder: `WarmTextSecondary`
- Password field: `visualTransformation = PasswordVisualTransformation()`
- SingleLine = true

### 4.5 States

| State | UI |
|-------|-----|
| `Idle` | Form visible, button enabled |
| `Loading` | Form disabled, button shows `CircularProgressIndicator` (WarmGold, small) |
| `Success` | Navigate to Home via `onLoginSuccess` callback |
| `Error(message)` | Error banner below button, form re-enabled |
| `NoAccount` | (initial state — show form) |

---

## 5. LoginViewModel

### File: `app/src/main/java/com/nativeplayer/ui/login/LoginViewModel.kt`

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val iptvRepository: IptvRepository
) : ViewModel() {

    // Tab state
    var selectedTab: LoginTab by mutableStateOf(LoginTab.XTREAM)
    
    // Xtream fields
    var serverUrl by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    
    // M3U field
    var m3uUrl by mutableStateOf("")
    
    // Connection state
    var state: LoginState by mutableStateOf(LoginState.Idle)

    fun connect() {
        when (selectedTab) {
            LoginTab.XTREAM -> connectXtream()
            LoginTab.M3U -> connectM3u()
        }
    }
    
    private fun connectXtream() {
        // Validate fields
        // Parse server URL to extract domain components
        // Create Account object
        // Call iptvRepository.loadAllFromXtream()
        // On success: save account via preferencesManager, update state to Success
        // On failure: update state to Error
    }
    
    private fun connectM3u() {
        // Validate URL
        // Call iptvRepository.fetchPlaylist(url)
        // On success: save m3uUrl via preferencesManager.setM3uUrl()
        // On failure: update state to Error
    }
}

enum class LoginTab { XTREAM, M3U }

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Error(val message: String) : LoginState()
    data object Success : LoginState()
}
```

### 5.1 Xtream Codes Logic
1. Parse `serverUrl` — strip `http://` / `https://`, extract base domain
2. Create `Account` with auto-generated ID, given username/password
3. Call existing `IptvRepository.loadAllFromXtream()` or a new `authenticate()` method
4. On success: `preferencesManager.addAccount(account)` + `preferencesManager.setActiveAccount(account.id)`
5. On failure: surface API error message

### 5.2 M3U Logic
1. Validate URL (non-empty, starts with http/https)
2. Call `IptvRepository.fetchPlaylist(m3uUrl)` — this already exists
3. On success: `preferencesManager.setM3uUrl(m3uUrl)`
4. On failure: surface exception message

### 5.3 Validation
- Xtream: server URL must not be empty, username must not be empty, password must not be empty
- M3U: URL must not be empty and must start with `http://` or `https://`
- Show inline validation errors before attempting connection

---

## 6. App Name & Icon Changes

### 6.1 strings.xml
**File:** `app/src/main/res/values/strings.xml`
```xml
<string name="app_name">Osi Player</string>
```

### 6.2 Launcher Icon
- The Play-Circle emblem on a WarmBackground (#0D0D0B) canvas
- Generate new PNG assets for all mipmap densities (mdpi–xxxhdpi)
- Round icon variant: same design, circular crop

### 6.3 Splash Background
- Current: `splash_preload.png`
- New: WarmBackground with centered Play-Circle emblem (or keep existing + just rename)

---

## 7. Edge Cases & Error Handling

| Case | Behavior |
|------|----------|
| Empty fields | Show inline error "Server URL erforderlich" etc. below the field |
| Invalid server URL (Xtream) | Show error "Server nicht erreichbar" — try both with/without port |
| HTTP not HTTPS | Allow cleartext (already enabled in manifest) |
| Invalid credentials (401) | Show error "Login fehlgeschlagen — Benutzername oder Passwort falsch" |
| Network timeout | Show error "Keine Verbindung — Server antwortet nicht" |
| Invalid M3U URL | Show error "Ungültige URL" |
| Empty M3U playlist (0 channels) | Accept but show warning "Keine Kanäle gefunden" |
| Account already exists | Skip login screen entirely (SplashScreen handles this) |
| Keyboard open | Content scrolls, logo stays visible, button above keyboard via `imePadding()` |
| Screen rotation | State preserved in ViewModel, fields retain values |
| Back press on Login | Exit app (Login is entry point, no back destination) |

---

## 8. Testing

### 8.1 Unit Tests (LoginViewModel)
- `connectXtream` with valid fields → Success
- `connectXtream` with empty server → Error
- `connectXtream` with invalid credentials → Error
- `connectM3u` with valid URL → Success
- `connectM3u` with empty URL → Error
- Tab switching preserves field values
- `connect()` dispatches to correct tab method

### 8.2 UI Tests (Compose)
- Logo renders Play-Circle + correct text
- Segmented control switches tabs
- Xtream fields show/hide based on tab
- M3U field shows/hides based on tab
- Error shows when validation fails
- Loading state disables form and shows spinner

---

## 9. Files Changed / Created

| File | Action |
|------|--------|
| `app/src/main/res/values/strings.xml` | Change `app_name` to "Osi Player" |
| `app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt` | Add `Login` route |
| `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt` | Add Login composable destination |
| `app/src/main/java/com/nativeplayer/ui/splash/SplashScreen.kt` | Route NoAccount → Login |
| `app/src/main/java/com/nativeplayer/ui/login/LoginScreen.kt` | **New** — Login UI |
| `app/src/main/java/com/nativeplayer/ui/login/LoginViewModel.kt` | **New** — Login logic |
| `app/src/main/java/com/nativeplayer/ui/login/LoginState.kt` | **New** — State sealed classes |
| `app/src/main/java/com/nativeplayer/ui/login/AppLogo.kt` | **New** — Reusable logo composable |
| `app/src/main/res/mipmap-*/ic_launcher*.png` | Replace with Play-Circle icon |
| `app/src/main/java/com/nativeplayer/di/AppModule.kt` | No changes needed (IptvRepository, PreferencesManager already provided) |
