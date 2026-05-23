# Hero Trailer Carousel — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace the single Hero Trailer with a swipeable carousel of 10 random movies from the full catalog, with pagination dots and optional 2-minute auto-rotation.

**Architecture:** Extend HomeViewModel with a `heroMovies` list (random 10 from all movies) and `selectedHeroIndex`. Replace the single `HeroVideoPlayer` call in HomeScreen with a `HorizontalPager` (Compose Foundation) and add a `PaginationDots` composable below it. HeroVideoPlayer remains unchanged.

**Tech Stack:** Kotlin, Jetpack Compose, Compose Foundation (HorizontalPager), Hilt ViewModel, ExoPlayer (Media3)

---

## File Structure

| File | Action | Responsibility |
|------|--------|----------------|
| `HomeViewModel.kt` | Modify | `heroMovies` StateFlow (random 10), `selectedHeroIndex`, `navigateHeroNext()` |
| `HomeScreen.kt` | Modify | HorizontalPager + PaginationDots integration |
| `HeroVideoPlayer.kt` | No change | Remains as-is — self-contained |

---

### Task 1: Extend HomeViewModel with heroMovies and selectedHeroIndex

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt`

- [ ] **Step 1: Add heroMovies StateFlow and selectedHeroIndex**

Add these members to `HomeViewModel`:

```kotlin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository,
    private val iptvRepository: IptvRepository
) : ViewModel() {

    // ... existing recentWatch, recommendations, sportChannels ...

    // ── Hero: Zufällige Filme aus dem gesamten Katalog ──────────
    val heroMovies: StateFlow<List<Movie>> = pollingFlow(5000L) {
        iptvRepository.getMovies().shuffled().take(10)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ── Hero: Aktueller Index im Karussell ──────────────────────
    private val _selectedHeroIndex = MutableStateFlow(0)
    val selectedHeroIndex: StateFlow<Int> = _selectedHeroIndex

    fun navigateHeroNext(movieCount: Int) {
        if (movieCount <= 0) return
        _selectedHeroIndex.update { current ->
            (current + 1) % movieCount
        }
    }

    fun navigateHeroPrevious(movieCount: Int) {
        if (movieCount <= 0) return
        _selectedHeroIndex.update { current ->
            (current - 1 + movieCount) % movieCount
        }
    }

    fun setHeroIndex(index: Int) {
        _selectedHeroIndex.value = index
    }
```

- [ ] **Step 2: Remove old latestContent StateFlow**

Delete or comment out the old `latestContent` line:

```kotlin
// DELETE this:
val latestContent: StateFlow<List<Movie>> = pollingFlow(5000L) {
    iptvRepository.getMovies().takeLast(5).reversed()
}.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
```

- [ ] **Step 3: Verify compilation**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt
git commit -m "feat(hero): add heroMovies random carousel with selectedHeroIndex"
```

---

### Task 2: Create PaginationDots composable

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/home/PaginationDots.kt`

- [ ] **Step 1: Create PaginationDots.kt**

```kotlin
package com.nativeplayer.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun PaginationDots(
    pageCount: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (pageCount <= 1) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage

            val dotColor by animateColorAsState(
                targetValue = if (isActive) WarmGold else WarmTextSecondary.copy(alpha = 0.4f),
                label = "dotColor"
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .size(if (isActive) 10.dp else 8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .clickable { onPageSelected(index) }
            )
        }
    }
}
```

- [ ] **Step 2: Verify compilation**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/PaginationDots.kt
git commit -m "feat(hero): add PaginationDots composable for carousel indicator"
```

---

### Task 3: Replace single HeroVideoPlayer with HorizontalPager in HomeScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt`

- [ ] **Step 1: Add imports**

Add these imports to the top of `HomeScreen.kt`:

```kotlin
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch
```

- [ ] **Step 2: Update state collection in HomeScreen**

Replace the `latestContent` collection with the new state variables:

```kotlin
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recentWatch by viewModel.recentWatch.collectAsState()
    val heroMovies by viewModel.heroMovies.collectAsState()
    val selectedHeroIndex by viewModel.selectedHeroIndex.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val sportChannels by viewModel.sportChannels.collectAsState()

    var selectedCategory by remember { mutableStateOf(HomeCategory.ALLE) }
    var showExitDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // ... rest unchanged until the hero section
```

- [ ] **Step 3: Replace Hero section in LazyColumn**

Find the hero section (lines 147-155) and replace it:

```kotlin
when (selectedCategory) {
    HomeCategory.ALLE -> {
        // ── HERO: Swipeable Carousel (Trailer) ──────────────
        item(key = "hero") {
            if (heroMovies.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    val pagerState = rememberPagerState(
                        initialPage = selectedHeroIndex.coerceIn(0, (heroMovies.size - 1).coerceAtLeast(0))
                    ) {
                        heroMovies.size
                    }

                    // Sync pager with ViewModel index
                    LaunchedEffect(selectedHeroIndex) {
                        if (selectedHeroIndex != pagerState.currentPage) {
                            pagerState.animateScrollToPage(selectedHeroIndex)
                        }
                    }

                    // Sync ViewModel when user swipes
                    LaunchedEffect(pagerState.currentPage) {
                        viewModel.setHeroIndex(pagerState.currentPage)
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp),
                        beyondViewportPageCount = 1
                    ) { page ->
                        val movie = heroMovies[page]
                        HeroVideoPlayer(
                            movie = movie,
                            modifier = Modifier.fillMaxSize(),
                            onDetailsClick = {
                                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                            }
                        )
                    }

                    // Pagination Dots
                    PaginationDots(
                        pageCount = heroMovies.size,
                        currentPage = pagerState.currentPage,
                        onPageSelected = { page ->
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page)
                            }
                        }
                    )
                }
            }
        }

        // ... rest of ALLE section (FÜR DICH, Quick Access, etc.) unchanged
```

- [ ] **Step 4: Remove old latestContent usage**

Make sure `latestContent` is no longer referenced. The `HomeCategory.NEU` section (line 322) still uses `latestContent` — this needs to be updated:

```kotlin
HomeCategory.NEU -> {
    item(key = "neu_header") {
        SectionHeader(
            title = "Neu hinzugefügt",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
    // Use recommendations or heroMovies as fallback for "Neu" tab
    if (heroMovies.isNotEmpty()) {
        items(heroMovies, key = { it.id }) { movie ->
            MovieListItem(
                movie = movie,
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = {
                    navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                }
            )
        }
    }
}
```

- [ ] **Step 5: Verify compilation**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 6: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat(hero): replace single hero with HorizontalPager carousel and dots"
```

---

### Task 4: Add Auto-Rotation Timer

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt`

- [ ] **Step 1: Add auto-rotation logic to HomeViewModel**

Add a `LaunchedEffect`-compatible flow for auto-rotation. Since this is a ViewModel, we expose a ticker:

```kotlin
// Add to HomeViewModel class:

// ── Auto-Rotation: 2 Minuten Timer ──────────────────────────────
private val _autoRotateEnabled = MutableStateFlow(true)
val autoRotateEnabled: StateFlow<Boolean> = _autoRotateEnabled

fun disableAutoRotate() {
    _autoRotateEnabled.value = false
}

fun enableAutoRotate() {
    _autoRotateEnabled.value = true
}

/**
 * Auto-rotation flow: emits every 120 seconds when enabled.
 * Call from a LaunchedEffect in the composable.
 */
fun autoRotationTick(intervalMs: Long = 120_000L): Flow<Unit> = flow {
    while (true) {
        delay(intervalMs)
        if (_autoRotateEnabled.value) {
            emit(Unit)
        }
    }
}
```

- [ ] **Step 2: Verify compilation**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt
git commit -m "feat(hero): add auto-rotation timer support to HomeViewModel"
```

---

### Task 5: Integrate Auto-Rotation in HomeScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt`

- [ ] **Step 1: Add auto-rotation LaunchedEffect inside the hero Column**

Inside the hero Column (created in Task 3), add auto-rotation logic:

```kotlin
Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
) {
    val pagerState = rememberPagerState(
        initialPage = selectedHeroIndex.coerceIn(0, (heroMovies.size - 1).coerceAtLeast(0))
    ) {
        heroMovies.size
    }

    // ... existing sync effects ...

    // Auto-rotation every 2 minutes
    LaunchedEffect(heroMovies.size) {
        viewModel.autoRotationTick().collect {
            if (pagerState.currentPage < heroMovies.size - 1) {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            } else {
                pagerState.animateScrollToPage(0)
            }
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        beyondViewportPageCount = 1
    ) { page ->
        val movie = heroMovies[page]
        HeroVideoPlayer(
            movie = movie,
            modifier = Modifier.fillMaxSize(),
            onDetailsClick = {
                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
            }
        )
    }

    // Pagination Dots
    PaginationDots(
        pageCount = heroMovies.size,
        currentPage = pagerState.currentPage,
        onPageSelected = { page ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(page)
            }
        }
    )
}
```

- [ ] **Step 2: Verify compilation**

Run: `./gradlew :app:compileDebugKotlin`
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat(hero): integrate 2-minute auto-rotation timer in carousel"
```

---

### Task 6: Final Verification and Cleanup

**Files:**
- Verify: All modified files

- [ ] **Step 1: Full build check**

Run: `./gradlew :app:assembleDebug`
Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Verify no references to old `latestContent`**

Run: `grep -rn "latestContent" app/src/main/java/com/nativeplayer/ui/home/`
Expected: No matches (removed in Task 1/3)

- [ ] **Step 3: Verify all new imports compile**

Run: `./gradlew :app:compileDebugKotlin --info 2>&1 | grep -i error`
Expected: No errors

- [ ] **Step 4: Final commit (if any cleanup needed)**

```bash
git add -A
git commit -m "chore(hero): cleanup and verify carousel implementation"
```

---

## Summary of Changes

| What | Before | After |
|------|--------|-------|
| **Film-Auswahl** | `takeLast(5).reversed()` (nur neu) | `shuffled().take(10)` (zufällig aus ALLEN) |
| **Anzeige** | Einzelner `HeroVideoPlayer` | `HorizontalPager` mit 10 Seiten |
| **Navigation** | Keine | Swipe + Pagination-Dots |
| **Auto-Rotation** | Keine | 2-Minuten-Timer (optional) |
| **Badge** | "NEU HINZUGEFÜGT" | Bleibt (kann später angepasst werden) |
