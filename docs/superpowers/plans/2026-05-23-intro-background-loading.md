# IntroScreen Background Data Loading Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Data loads in background while intro video plays, instead of blocking on a SplashScreen first.

**Architecture:** New `IntroViewModel` (Hilt) handles all data loading from `IptvRepository.loadAllFromXtream()`. `IntroScreen` becomes NavGraph startDestination — video starts immediately, data loads in background. On video end: navigate to HomeScreen (regardless of load state; HomeViewModel.pollingFlow handles waiting), or show error overlay on IntroScreen if loading failed. `SplashScreen.kt` deleted; `SplashViewModel` keeps only account helpers for `SettingsScreen`.

**Tech Stack:** Kotlin, Jetpack Compose, Hilt ViewModel, ExoPlayer, NavController

**Spec:** `docs/superpowers/specs/2026-05-23-intro-background-loading-design.md`

---

### Task 1: Create IntroViewModel

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/splash/IntroViewModel.kt`
- Test: Build must succeed

- [ ] **Step 1: Create IntroViewModel with sealed IntroState**

Create `/home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/splash/IntroViewModel.kt`:

```kotlin
package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class IntroState {
    data object Loading : IntroState()
    data object Success : IntroState()
    data class Error(val message: String) : IntroState()
    data object NoAccount : IntroState()
}

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow<IntroState>(IntroState.Loading)
    val state: StateFlow<IntroState> = _state.asStateFlow()

    init {
        loadDataInBackground()
    }

    private fun loadDataInBackground() {
        viewModelScope.launch {
            _state.value = IntroState.Loading

            val activeAccount = preferencesManager.getActiveAccount()

            if (activeAccount == null || activeAccount.domain.isBlank()) {
                _state.value = IntroState.NoAccount
                return@launch
            }

            try {
                val success = repository.loadAllFromXtream(activeAccount)
                if (success) {
                    _state.value = IntroState.Success
                } else {
                    _state.value = IntroState.Error(
                        "Authentifizierung fehlgeschlagen. Zugangsdaten prüfen."
                    )
                }
            } catch (e: Exception) {
                _state.value = IntroState.Error(
                    "Verbindungsfehler: ${e.message ?: "Unbekannter Fehler"}"
                )
            }
        }
    }

    fun retry() {
        loadDataInBackground()
    }
}
```

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 2: Modify IntroScreen to use IntroViewModel + Error Overlay

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/splash/IntroScreen.kt`
- Test: Build must succeed

The IntroScreen needs to:
1. Accept `IntroViewModel` via `hiltViewModel()`
2. Observe `viewModel.state` to decide post-video navigation
3. Show error overlay (with space background visible) when loading fails after video ends
4. On retry: recreate ExoPlayer + reload data
5. "Einstellungen" button navigates to SettingsScreen

Key changes to IntroScreen:
- Add `viewModel: IntroViewModel = hiltViewModel()` parameter
- Collect `state` via `val state by viewModel.state.collectAsState()`
- Replace direct `LaunchedEffect(videoEnded)` → `navController.navigate(HOME)` with state-aware navigation
- Add `showError` state variable
- Use `playerKey` counter for ExoPlayer recreation on retry
- Add error overlay composable layer

- [ ] **Step 1: Rewrite IntroScreen**

Replace the contents of `/home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/splash/IntroScreen.kt`:

```kotlin
package com.nativeplayer.ui.splash

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.nativeplayer.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.nativeplayer.R
import com.nativeplayer.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val phase: Float,
    val speed: Float
)

@OptIn(UnstableApi::class)
@Composable
fun IntroScreen(
    navController: NavController,
    viewModel: IntroViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var videoEnded by remember { mutableStateOf(false) }
    var showSkip by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var playerKey by remember { mutableIntStateOf(0) }

    // Stars once
    val stars = remember {
        List(200) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2.2f + 0.4f,
                phase = Random.nextFloat() * Math.PI.toFloat() * 2f,
                speed = Random.nextFloat() * 1.5f + 0.5f
            )
        }
    }

    // Tick for star twinkle (~60fps)
    var tick by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            tick += 0.016f
        }
    }

    // Navigation when video ends (only if not already showing error)
    LaunchedEffect(videoEnded) {
        if (videoEnded && !showError) {
            when (state) {
                is IntroState.Error -> {
                    showError = true
                }
                is IntroState.NoAccount -> {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
                else -> {
                    // Success or Loading → HomeScreen (pollingFlow waits for isLoaded)
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            }
        }
    }

    // Show skip button after 2s
    LaunchedEffect(Unit) {
        delay(2000)
        showSkip = true
    }

    // ExoPlayer — recreated when playerKey changes (e.g., on retry)
    val exoPlayer = remember(playerKey) {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.app_intro}")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED && !showError) {
                        videoEnded = true
                    }
                }
            })
        }
    }

    DisposableEffect(playerKey) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OsiBlack)
    ) {
        // Layer 1: Space nebula
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x1A3A1A6E),
                        Color(0x0D0D0D2A),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.4f),
                    radius = size.width * 0.7f
                ),
                radius = size.width * 0.7f,
                center = Offset(size.width * 0.5f, size.height * 0.4f)
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x121A1A4A), Color.Transparent),
                    center = Offset(size.width * 0.8f, size.height * 0.2f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
        }

        // Layer 2: Animated stars (always visible)
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                val alpha = ((sin((tick * star.speed + star.phase).toDouble()) + 1.0) / 2.0)
                    .toFloat() * 0.75f + 0.25f
                drawCircle(
                    color = OsiTextPrimary.copy(alpha = alpha),
                    radius = star.radius,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        // Layer 3: Video (hidden when error overlay is shown)
        AnimatedVisibility(
            visible = !showError,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        setBackgroundColor(android.graphics.Color.TRANSPARENT)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Layer 4: Skip button
        AnimatedVisibility(
            visible = showSkip && !showError,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            TextButton(onClick = { videoEnded = true }) {
                Text(
                    "Überspringen ›",
                    color = OsiTextPrimary.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
        }

        // Layer 5: Error overlay (shown after video ends when state is Error)
        if (showError) {
            val errorMessage = (state as? IntroState.Error)?.message ?: "Unbekannter Fehler"

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Filled.Error,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = WarmError
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        errorMessage,
                        color = WarmError,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                // Retry: reload data, recreate player, hide error
                                showError = false
                                videoEnded = false
                                playerKey++
                                viewModel.retry()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WarmGold
                            )
                        ) {
                            Text("Erneut versuchen")
                        }
                        OutlinedButton(
                            onClick = {
                                navController.navigate(Screen.Settings.route)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WarmTextSecondary
                            )
                        ) {
                            Text("Einstellungen")
                        }
                    }
                }
            }
        }
    }
}
```

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 3: Update NavGraph — Intro becomes startDestination, remove Splash route

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt`
- Test: Build must succeed

- [ ] **Step 1: Update NavGraph**

In `/home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt`:

1. Remove `import com.nativeplayer.ui.splash.SplashScreen`
2. Change `startDestination = Screen.Splash.route` to `startDestination = Screen.Intro.route`
3. Remove the `composable(Screen.Splash.route) { SplashScreen(navController = navController) }` block

The updated NavGraph start should look like:

```kotlin
NavHost(navController = navController, startDestination = Screen.Intro.route) {
    composable(Screen.Intro.route) { IntroScreen(navController = navController) }
```

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 4: Remove Screen.Splash from Screen sealed class

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt`
- Test: Build must succeed

- [ ] **Step 1: Remove `Screen.Splash` entry**

In `/home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt`, remove the line:

```kotlin
    data object Splash : Screen("splash")
```

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 5: Delete SplashScreen.kt

**Files:**
- Delete: `app/src/main/java/com/nativeplayer/ui/splash/SplashScreen.kt`
- Test: Build must succeed

- [ ] **Step 1: Delete SplashScreen.kt**

Run: `rm /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/splash/SplashScreen.kt`

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 6: Remove loadData() from SplashViewModel

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/splash/SplashViewModel.kt`
- Test: Build must succeed

- [ ] **Step 1: Remove loadData() method and clean up unused imports**

Edit `/home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/splash/SplashViewModel.kt`:

1. Remove the `init { loadData() }` block
2. Remove the entire `fun loadData(account: Account? = null) { ... }` method
3. Remove unused imports: `viewModelScope`, `launch`, `IptvRepository` (if no longer needed directly — but check: `IptvRepository` is only used for `loadData()`, so remove it too)
4. Remove sealed class states that are no longer used: `Loading`, `LoadingWithProgress`, `Success`, `Error`, `NoAccount` — BUT they may still be referenced if any other file uses SplashState. Let's check: only SplashScreen.kt references SplashState, and we're deleting it. So yes, remove the entire sealed class.

Wait — actually, if we remove the sealed class from SplashViewModel, does any other file reference it? The `SplashState` is only used in SplashScreen.kt (which is deleted) and SplashViewModel.kt itself. So we can safely remove it.

But wait — we're also changing the `SettingsScreen` import to use a different ViewModel. Let me handle that in Task 7.

Actually, let me keep SplashViewModel lean — just the account management helpers. Remove `SplashState`, `loadData()`, `IptvRepository` dependency, `WatchHistoryRepository`, and `viewModelScope`/`launch` imports.

Resulting SplashViewModel:

```kotlin
package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    fun getActiveAccount(): Account? = preferencesManager.getActiveAccount()
    fun getAccounts(): List<Account> = preferencesManager.getAccounts()
    fun saveAccount(account: Account) = preferencesManager.saveAccount(account)
    fun setActiveAccount(accountId: String) = preferencesManager.setActiveAccount(accountId)
    fun deleteAccount(accountId: String) = preferencesManager.deleteAccount(accountId)
}
```

Also remove `WatchHistoryRepository` import — we removed `clearWatchHistory()`. Wait, the spec says SplashViewModel keeps `clearWatchHistory()`:

> `clearWatchHistory()`

Let me keep that method. Add `WatchHistoryRepository` back:

```kotlin
package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {

    fun getActiveAccount(): Account? = preferencesManager.getActiveAccount()
    fun getAccounts(): List<Account> = preferencesManager.getAccounts()
    fun saveAccount(account: Account) = preferencesManager.saveAccount(account)
    fun setActiveAccount(accountId: String) = preferencesManager.setActiveAccount(accountId)
    fun deleteAccount(accountId: String) = preferencesManager.deleteAccount(accountId)

    fun clearWatchHistory() {
        viewModelScope.launch {
            watchHistoryRepository.clearAll()
        }
    }
}
```

- [ ] **Step 2: Build check**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 7: Update SettingsScreen — redirect navigations from Splash to Intro

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt`
- Test: Build must succeed

Three places in SettingsScreen navigate to `Screen.Splash.route` — all need to change to `Screen.Intro.route`:

1. **Line 71** ("Neu laden" button): `navController.navigate(Screen.Splash.route)`
2. **Line 105** (account click): `navController.navigate(Screen.Splash.route)`
3. **Line 464** (add account dialog): `navController.navigate(Screen.Splash.route)`

Also, calls to `viewModel.loadData(...)` must be removed (the method no longer exists on SplashViewModel).

- [ ] **Step 1: Fix "Neu laden" button (lines 69-76)**

Change:
```kotlin
TextButton(onClick = {
    viewModel.loadData()
    navController.navigate(Screen.Splash.route) {
        popUpTo(Screen.Home.route) { inclusive = true }
    }
}) {
```

To:
```kotlin
TextButton(onClick = {
    navController.navigate(Screen.Intro.route) {
        popUpTo(Screen.Home.route) { inclusive = true }
    }
}) {
```

- [ ] **Step 2: Fix account click (lines 103-107)**

Change:
```kotlin
viewModel.setActiveAccount(account.id)
viewModel.loadData(account)
navController.navigate(Screen.Splash.route) {
    popUpTo(Screen.Settings.route) { inclusive = true }
}
```

To:
```kotlin
viewModel.setActiveAccount(account.id)
navController.navigate(Screen.Intro.route) {
    popUpTo(Screen.Settings.route) { inclusive = true }
}
```

- [ ] **Step 3: Fix add account dialog (lines 461-466)**

Change:
```kotlin
viewModel.loadData(account)
navController.navigate(Screen.Splash.route) {
    popUpTo(Screen.Settings.route) { inclusive = true }
}
```

To:
```kotlin
navController.navigate(Screen.Intro.route) {
    popUpTo(Screen.Settings.route) { inclusive = true }
}
```

- [ ] **Step 4: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 8: Update LoginScreen — redirect navigation from Splash to Intro

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/login/LoginScreen.kt`
- Test: Build must succeed

- [ ] **Step 1: Fix LoginScreen post-login navigation (lines 184-186)**

Change:
```kotlin
navController.navigate(Screen.Home.route) {
    popUpTo(Screen.Splash.route) { inclusive = true }
}
```

To:
```kotlin
navController.navigate(Screen.Intro.route) {
    popUpTo(Screen.Intro.route) { inclusive = true }
}
```

- [ ] **Step 2: Verify it builds**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

---

### Task 9: Full build and verify

- [ ] **Step 1: Clean build**

Run: `./gradlew clean :app:assembleDebug`
Expected: BUILD SUCCESSFUL in 0s

- [ ] **Step 2: Verify navigation flow**

The new startup sequence should be:

```
App launch
  → NavGraph (startDestination = Screen.Intro)
  → IntroScreen:
      ├── ExoPlayer plays R.raw.app_intro IMMEDIATELY
      ├── IntroViewModel.loadDataInBackground() runs in parallel:
      │     ├── getActiveAccount() → null?
      │     │   └── NoAccount → waits for video end
      │     ├── loadAllFromXtream() → Success?
      │     │   └── Success state set
      │     └── loadAllFromXtream() → Exception?
      │         └── Error state set with message
      └── Video ends:
          ├── State = Success → navigate Home
          ├── State = Loading → navigate Home (pollingFlow waits)
          ├── State = Error → show overlay (retry / settings)
          └── State = NoAccount → navigate Login
```

- [ ] **Step 3: Commit all changes**

```bash
git add -A
git commit -m "feat: background data loading on IntroScreen

- Create IntroViewModel with IntroState sealed class (Loading/Success/Error/NoAccount)
- IntroScreen becomes NavGraph startDestination, video starts immediately
- Data loads in background via IntroViewModel.loadDataInBackground()
- Error overlay on IntroScreen when loading fails after video ends
- SplashScreen deleted, SplashViewModel reduced to account helpers
- SettingsScreen/LoginScreen navigations redirected from Splash to Intro

Closes #..."
```
