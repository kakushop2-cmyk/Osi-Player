# Hero Trailer Visibility Control Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Hero-Trailer stoppen sofort wenn Sektion <80% sichtbar ist, starten nur wenn >80% sichtbar UND fokussiert.

**Architecture:** Focus-basierte Steuerung (bestehend) + Visibility-Check mit 80% Threshold via `onGloballyPositioned`. Kombinierte Logik in `LaunchedEffect(hasFocus, isVisible80Percent)`.

**Tech Stack:** Jetpack Compose, ExoPlayer (Media3), Android TV D-Pad Navigation

---

## File Structure

| Datei | Änderung |
|-------|----------|
| `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt` | Visibility-Tracking pro Sektion (Hero, Hero2, Hero3) |
| `app/src/main/java/com/nativeplayer/ui/home/HeroVideoPlayer.kt` | Neuer Parameter `isVisible80Percent`, kombinierte Logik |

---

## Task 1: HeroVideoPlayer - Neuer Parameter hinzufügen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HeroVideoPlayer.kt:41-49`

- [ ] **Step 1: Parameter `isVisible80Percent` hinzufügen**

```kotlin
@Composable
fun HeroVideoPlayer(
    movie: Movie?,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {},
    hasFocus: Boolean = false,
    isVisible80Percent: Boolean = true,  // NEU: Visibility-Check
    badgeText: String = "NEU HINZUGEFÜGT",
    getOrCreatePlayer: (String) -> ExoPlayer,
    prepareAndPlay: (String) -> Unit,
    pausePlayer: (String) -> Unit
)
```

- [ ] **Step 2: LaunchedEffect anpassen**

Ändere Zeile 85 von:
```kotlin
LaunchedEffect(hasFocus) {
```
zu:
```kotlin
LaunchedEffect(hasFocus, isVisible80Percent) {
```

- [ ] **Step 3: Bedingung anpassen**

Ändere Zeile 86 von:
```kotlin
if (hasFocus && movie.streamUrl.isNotEmpty()) {
```
zu:
```kotlin
if (hasFocus && isVisible80Percent && movie.streamUrl.isNotEmpty()) {
```

- [ ] **Step 4: Else-Block anpassen**

Ändere Zeile 166 von:
```kotlin
} else if (!hasFocus) {
```
zu:
```kotlin
} else if (!hasFocus || !isVisible80Percent) {
```

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HeroVideoPlayer.kt
git commit -m "feat: add isVisible80Percent parameter to HeroVideoPlayer"
```

---

## Task 2: HomeScreen - Visibility-Tracking für Hero-Sektion

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt:148-244`

- [ ] **Step 1: Visibility-State für Hero-Sektion hinzufügen**

Füge nach Zeile 148 (vor `item(key = "hero_section")`) hinzu:
```kotlin
var heroSectionVisible80 by remember { mutableStateOf(true) }
```

- [ ] **Step 2: onGloballyPositioned Modifier hinzufügen**

Ändere Zeile 150-154 von:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(WarmBackground)
) {
```
zu:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(WarmBackground)
        .onGloballyPositioned { coordinates ->
            val windowBounds = coordinates.ownerWindowBounds
            val sectionBounds = coordinates.boundsInWindow()
            val visibleHeight = minOf(sectionBounds.bottom, windowBounds.bottom) -
                               maxOf(sectionBounds.top, windowBounds.top)
            val totalHeight = sectionBounds.height
            val visibilityPercent = if (totalHeight > 0) visibleHeight / totalHeight else 0f
            heroSectionVisible80 = visibilityPercent >= 0.8f
        }
) {
```

- [ ] **Step 3: isVisible80Percent an HeroVideoPlayer übergeben**

Ändere Zeile 194-201 von:
```kotlin
HeroVideoPlayer(
    movie = heroItem.movie,
    modifier = Modifier.fillMaxSize(),
    hasFocus = pagerState.currentPage == page,
    badgeText = "NEU HINZUGEFÜGT",
    getOrCreatePlayer = { url -> viewModel.getOrCreateHeroPlayer(url) },
    prepareAndPlay = { url -> viewModel.prepareAndPlayHero(url) },
    pausePlayer = { url -> viewModel.pauseHero(url) },
    onDetailsClick = {
        navController.navigate(Screen.MovieDetail.createRoute(heroItem.movie.id))
    }
)
```
zu:
```kotlin
HeroVideoPlayer(
    movie = heroItem.movie,
    modifier = Modifier.fillMaxSize(),
    hasFocus = pagerState.currentPage == page,
    isVisible80Percent = heroSectionVisible80,
    badgeText = "NEU HINZUGEFÜGT",
    getOrCreatePlayer = { url -> viewModel.getOrCreateHeroPlayer(url) },
    prepareAndPlay = { url -> viewModel.prepareAndPlayHero(url) },
    pausePlayer = { url -> viewModel.pauseHero(url) },
    onDetailsClick = {
        navController.navigate(Screen.MovieDetail.createRoute(heroItem.movie.id))
    }
)
```

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat: add visibility tracking for Hero section"
```

---

## Task 3: HomeScreen - Visibility-Tracking für Hero2-Sektion

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt:396-460`

- [ ] **Step 1: Visibility-State für Hero2-Sektion hinzufügen**

Füge nach Zeile 396 (vor `private fun LazyItemScope.Hero2Section(`) hinzu:
```kotlin
var hero2SectionVisible80 by remember { mutableStateOf(true) }
```

**HINWEIS:** Diese Variable muss IN der Hero2Section Composable deklariert werden, nicht davor.

- [ ] **Step 2: onGloballyPositioned Modifier hinzufügen**

Ändere Zeile 417-421 von:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(CarouselBackground)
        .padding(vertical = 16.dp)
) {
```
zu:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(CarouselBackground)
        .padding(vertical = 16.dp)
        .onGloballyPositioned { coordinates ->
            val windowBounds = coordinates.ownerWindowBounds
            val sectionBounds = coordinates.boundsInWindow()
            val visibleHeight = minOf(sectionBounds.bottom, windowBounds.bottom) -
                               maxOf(sectionBounds.top, windowBounds.top)
            val totalHeight = sectionBounds.height
            val visibilityPercent = if (totalHeight > 0) visibleHeight / totalHeight else 0f
            hero2SectionVisible80 = visibilityPercent >= 0.8f
        }
) {
```

- [ ] **Step 3: isVisible80Percent an HeroVideoPlayer übergeben**

Ändere Zeile 441-450 von:
```kotlin
HeroVideoPlayer(
    movie = heroItem.movie,
    modifier = Modifier.fillMaxSize(),
    hasFocus = pagerState.currentPage == page,
    badgeText = heroItem.category,
    getOrCreatePlayer = getOrCreatePlayer,
    prepareAndPlay = prepareAndPlay,
    pausePlayer = pausePlayer,
    onDetailsClick = { navController.navigate(Screen.MovieDetail.createRoute(heroItem.movie.id)) }
)
```
zu:
```kotlin
HeroVideoPlayer(
    movie = heroItem.movie,
    modifier = Modifier.fillMaxSize(),
    hasFocus = pagerState.currentPage == page,
    isVisible80Percent = hero2SectionVisible80,
    badgeText = heroItem.category,
    getOrCreatePlayer = getOrCreatePlayer,
    prepareAndPlay = prepareAndPlay,
    pausePlayer = pausePlayer,
    onDetailsClick = { navController.navigate(Screen.MovieDetail.createRoute(heroItem.movie.id)) }
)
```

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat: add visibility tracking for Hero2 section"
```

---

## Task 4: HomeScreen - Visibility-Tracking für Hero3-Sektion

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt:465-530`

- [ ] **Step 1: Visibility-State für Hero3-Sektion hinzufügen**

Füge in der Hero3Section Composable hinzu:
```kotlin
var hero3SectionVisible80 by remember { mutableStateOf(true) }
```

- [ ] **Step 2: onGloballyPositioned Modifier hinzufügen**

Ändere die Column in Hero3Section von:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(CarouselBackground)
) {
```
zu:
```kotlin
Column(
    modifier = Modifier
        .fillParentMaxSize()
        .background(CarouselBackground)
        .onGloballyPositioned { coordinates ->
            val windowBounds = coordinates.ownerWindowBounds
            val sectionBounds = coordinates.boundsInWindow()
            val visibleHeight = minOf(sectionBounds.bottom, windowBounds.bottom) -
                               maxOf(sectionBounds.top, windowBounds.top)
            val totalHeight = sectionBounds.height
            val visibilityPercent = if (totalHeight > 0) visibleHeight / totalHeight else 0f
            hero3SectionVisible80 = visibilityPercent >= 0.8f
        }
) {
```

- [ ] **Step 3: isVisible80Percent an HeroVideoPlayer übergeben**

Finde den HeroVideoPlayer Aufruf in Hero3Section und füge hinzu:
```kotlin
isVisible80Percent = hero3SectionVisible80,
```

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat: add visibility tracking for Hero3 section"
```

---

## Task 5: Import hinzufügen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt:1-46`

- [ ] **Step 1: Import für onGloballyPositioned hinzufügen**

Füge nach Zeile 23 hinzu:
```kotlin
import androidx.compose.ui.layout.onGloballyPositioned
```

- [ ] **Step 2: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt
git commit -m "feat: add onGloballyPositioned import"
```

---

## Task 6: Testen und Verifizieren

- [ ] **Step 1: App kompilieren**

```bash
./gradlew assembleDebug
```

Expected: BUILD SUCCESSFUL

- [ ] **Step 2: Manuell testen**

1. App auf Android TV starten
2. Hero-Sektion sichtbar → Trailer sollte abspielen
3. Zu "Top Bewertet" scrollen → Trailer sollte sofort stoppen
4. Zurück zu Hero scrollen → Trailer sollte wieder starten
5. Gleichen Test für Hero2 und Hero3 wiederholen

- [ ] **Step 3: Finale Commits zusammenfassen**

```bash
git add -A
git commit -m "feat: hero trailer visibility control with 80% threshold"
```

---

## Edge Cases

- **Schnelles Scrollen:** Visibility-Check ist sofort, Player stoppt sofort
- **Sektion teilweise sichtbar:** Nur starten wenn >80% sichtbar
- **Focus ohne Visibility:** Player startet nicht
- **Visibility ohne Focus:** Player startet nicht
- **LazyColumn Dispose:** DisposableEffect stoppt Player zusätzlich

## Performance

- `onGloballyPositioned` wird nur bei Layout-Änderungen aufgerufen
- Keine kontinuierliche Überwachung
- Minimaler Overhead
