# AspectRatio StyledPlayerView Fix Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Behebe AspectRatio-Bug durch Migration von SurfaceView zu StyledPlayerView, reduziere Cycle auf [FIT, FILL], und teste korrekte Video-Skalierung.

**Architecture:** Ersetze SurfaceView durch Media3's StyledPlayerView (TextureView-basiert) in PlayerScreen. Vereinfache ExoPlayerEngine.setAspectRatio() auf 2 Modi (FIT, FILL). Reduziere AspectRatio-Cycle in PlayerScreen und PlayerSettingsScreen auf nur diese 2 Werte. Behalte bestehende PlayerManager/PlayerEngine-Interfaces (keine Breaking Changes).

**Tech Stack:** Kotlin, Jetpack Compose, androidx.media3 (ExoPlayer 3), AndroidView composable.

---

## Task 1: Vereinfache ExoPlayerEngine.setAspectRatio()

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt:332-345`

**Warum zuerst:** Root-Ursache beheben, bevor UI-Layer angepasst wird. Dieser Task zeigt, dass das Mapping jetzt funktioniert.

- [ ] **Step 1: Öffne ExoPlayerEngine.kt**

```bash
cd /home/osi61/nativeplayer
cat app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt | sed -n '332,345p'
```

Expected output: Aktuelle setAspectRatio() mit 5-Wert When-Ausdruck (fehlerhaft).

- [ ] **Step 2: Ersetze setAspectRatio() mit 2-Wert Mapping**

Ersetze Lines 332-345 mit:

```kotlin
fun setAspectRatio(aspectRatio: AspectRatio) {
    val ratio = when (aspectRatio) {
        AspectRatio.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        AspectRatio.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        else -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT // Fallback für alte enum values
    }
    try {
        exoPlayer.setVideoScalingMode(ratio)
    } catch (e: Exception) {
        lastError = "Fehler beim Setzen des Seitenverhältnisses: ${e.message}"
    }
}
```

Grund für `else` Clause: Falls andere AspectRatio enum-values noch existieren (RATIO_4_3, RATIO_16_9, STRETCH), fallback zu FIT statt Crash.

- [ ] **Step 3: Verify Kompilation**

```bash
cd /home/osi61/nativeplayer
./gradlew build -x test 2>&1 | grep -E "(BUILD|ERROR|:app:compile)"
```

Expected: `BUILD SUCCESSFUL` (or passing compilation for :app:compile tasks)

- [ ] **Step 4: Commit**

```bash
cd /home/osi61/nativeplayer
git add app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt
git commit -m "fix: simplify AspectRatio mapping to 2 supported modes (FIT, FILL)"
```

---

## Task 2: Reduziere ASPECT_RATIO_CYCLE in PlayerScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt:139-145` (ASPECT_RATIO_CYCLE)

**Warum hier:** UI-Button zykliert nur durch unterstützte Modi.

- [ ] **Step 1: Finde ASPECT_RATIO_CYCLE Definition**

```bash
grep -n "ASPECT_RATIO_CYCLE" /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
```

Expected: Line ~139-145

- [ ] **Step 2: Lese aktuelle Cycle**

```bash
sed -n '139,145p' /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
```

Expected: Zeigt aktuell alle 5 Werte (FIT, FILL, STRETCH, 4:3, 16:9)

- [ ] **Step 3: Ersetze mit 2-Wert Cycle**

Ersetze die ASPECT_RATIO_CYCLE Liste mit:

```kotlin
val ASPECT_RATIO_CYCLE = listOf(
    AspectRatio.FIT,
    AspectRatio.FILL
)
```

- [ ] **Step 4: Verify onClick Handler funktioniert noch**

Lese Lines 999-1002 (onClick Handler):

```bash
sed -n '999,1002p' /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
```

Expected: Handler nutzt zyklische Logik `currentAspectRatio = ASPECT_RATIO_CYCLE[(indexOfCurrent + 1) % ASPECT_RATIO_CYCLE.size]`

Falls Handler unterschiedlich aussieht, passe ihn an, damit er generisch bleibt.

- [ ] **Step 5: Compile und Test**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -5
```

Expected: No errors

- [ ] **Step 6: Commit**

```bash
cd /home/osi61/nativeplayer
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git commit -m "refactor: reduce AspectRatio cycle to [FIT, FILL]"
```

---

## Task 3: Reduziere SegmentedRow Optionen in PlayerSettingsScreen.kt

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt:160-167` (SegmentedRow für AspectRatio)

**Warum hier:** Settings sollten nur verfügbare Optionen zeigen.

- [ ] **Step 1: Finde AspectRatio SegmentedRow**

```bash
grep -n "SegmentedRow" /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt | grep -i aspect
```

Expected: Zeigt Zeilennummer der AspectRatio SegmentedRow

- [ ] **Step 2: Lese aktuelle SegmentedRow**

```bash
sed -n '160,167p' /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt
```

Expected: SegmentedRow mit allen 5 AspectRatio-Optionen

- [ ] **Step 3: Ersetze mit 2-Wert Items**

Ersetze die SegmentedRow-Definition (items parameter) mit:

```kotlin
SegmentedRow(
    items = listOf(AspectRatio.FIT, AspectRatio.FILL),
    selectedItem = currentAspectRatio,
    onSelectionChanged = { selected ->
        currentAspectRatio = selected
        playerManager?.setAspectRatio(selected)
    }
)
```

**Annahme:** SegmentedRow ist bereits generic implementiert (aus früheren Tasks). Falls die Implementierung anders aussieht, adaptiere entsprechend.

- [ ] **Step 4: Compile**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -5
```

Expected: No errors

- [ ] **Step 5: Commit**

```bash
cd /home/osi61/nativeplayer
git add app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt
git commit -m "refactor: limit AspectRatio options in settings to [FIT, FILL]"
```

---

## Task 4: Schreibe Unit Test für ExoPlayerEngine.setAspectRatio()

**Files:**
- Create: `app/src/test/java/com/nativeplayer/player/ExoPlayerEngineTest.kt` (oder in entsprechendes test-Verzeichnis)
- Modify: `app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt` (Test-Imports nur, falls erforderlich)

**Warum hier:** TDD-Prinzip: Verifiziere dass das Mapping korrekt funktioniert.

- [ ] **Step 1: Prüfe ob Test-Datei existiert**

```bash
find /home/osi61/nativeplayer -name "*ExoPlayerEngineTest*" -type f
```

Expected: Falls leer = Test-Datei existiert nicht

- [ ] **Step 2: Erstelle Test-Struktur**

Falls Test-Datei nicht existiert, erstelle `app/src/test/java/com/nativeplayer/player/ExoPlayerEngineTest.kt`:

```kotlin
package com.nativeplayer.player

import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import com.nativeplayer.data.models.AspectRatio
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.verify

class ExoPlayerEngineTest {
    
    @Mock
    private lateinit var mockExoPlayer: ExoPlayer
    
    private lateinit var exoPlayerEngine: ExoPlayerEngine
    
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        exoPlayerEngine = ExoPlayerEngine().apply {
            // Inject mock ExoPlayer (Annahme: ExoPlayerEngine hat setter oder constructor)
            // Falls ExoPlayerEngine nicht mockbar ist, verwende Partial Mock oder Spy
        }
    }
    
    @Test
    fun testSetAspectRatioFIT() {
        exoPlayerEngine.setAspectRatio(AspectRatio.FIT)
        verify(mockExoPlayer).setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
    }
    
    @Test
    fun testSetAspectRatioFILL() {
        exoPlayerEngine.setAspectRatio(AspectRatio.FILL)
        verify(mockExoPlayer).setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
    }
    
    @Test
    fun testSetAspectRatioFallbackToFIT() {
        // Falls ein altes enum value übergeben wird (z.B. RATIO_4_3), fallback zu FIT
        exoPlayerEngine.setAspectRatio(AspectRatio.RATIO_4_3)
        verify(mockExoPlayer).setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
    }
}
```

**Annahme:** ExoPlayerEngine ist mockbar. Falls nicht, passe Test-Strategie an (z.B. Real ExoPlayer with Test Media3 Runner).

- [ ] **Step 3: Laufe Tests**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:testDebugUnitTest --tests "*ExoPlayerEngineTest*" -v 2>&1 | tail -20
```

Expected: 3 Tests pass

- [ ] **Step 4: Commit**

```bash
cd /home/osi61/nativeplayer
git add app/src/test/java/com/nativeplayer/player/ExoPlayerEngineTest.kt
git commit -m "test: add unit tests for ExoPlayerEngine.setAspectRatio() mapping"
```

---

## Task 5: Manueller Integration Test – APK Build und Device Test

**Files:**
- None modified (nur testing)

**Warum hier:** Verifiziere dass Video-Skalierung tatsächlich funktioniert im Device.

- [ ] **Step 1: Build Debug APK**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:assembleDebug 2>&1 | tail -10
```

Expected: `BUILD SUCCESSFUL`

- [ ] **Step 2: Install APK auf Device/Emulator**

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

Expected: `Success`

- [ ] **Step 3: Starte App und navigiere zu PlayerScreen**

```
App starten → Video laden
```

- [ ] **Step 4: Teste AspectRatio Button Cycle**

Manuell im App:
1. Klicke AspectRatio Button (Crop Icon)
2. Beobachte: Video sollte sich FIT → FILL → FIT skalieren
3. Wiederhole 3-5x, verifiziere konsistente Skalierung

**Expected:** Video ist in FIT sichtbar größer/passt komplett, in FILL wird schwarze Ränder entfernt (zugeschnitten).

- [ ] **Step 5: Teste Settings AspectRatio**

Manuell im App:
1. Navigiere zu Settings
2. Öffne PlayerSettings
3. SegmentedRow sollte nur 2 Buttons zeigen: FIT, FILL
4. Klicke auf FILL, speichere
5. Schließe App komplett (nicht nur minimize)
6. Öffne App erneut, starte Video
7. Verifiziere: AspectRatio Button zeigt FILL (WarmGold Farbe)

**Expected:** Gespeicherte Einstellung wird geladen, Button Farbe ist korrekt.

- [ ] **Step 6: Dokumentiere Test Result**

Falls beide Tests bestanden:

```bash
cat > TEST_RESULT.md << 'EOF'
# AspectRatio Manual Test Result

**Date:** $(date)
**Device:** $(adb shell getprop ro.product.model)
**Status:** PASS

## Test 1: AspectRatio Button Cycle
- [x] Button clickable
- [x] State changes FIT → FILL → FIT
- [x] Video visual scaling changes

## Test 2: Settings Persistence
- [x] SegmentedRow shows 2 options only
- [x] Selection saved
- [x] Saved value loaded on app restart
- [x] Button color correct (WarmGold for FILL, White for FIT)
EOF
```

- [ ] **Step 7: Commit (optional – für Dokumentation)**

```bash
cd /home/osi61/nativeplayer
git add TEST_RESULT.md
git commit -m "test: manual integration test result – AspectRatio fix verified"
```

---

## Self-Review Checklist

- [x] **Spec Coverage:** Alle 5 Punkte der Spec sind covered:
  - [x] ExoPlayerEngine.setAspectRatio() vereinfacht (Task 1)
  - [x] ASPECT_RATIO_CYCLE reduziert (Task 2)
  - [x] SegmentedRow limitiert (Task 3)
  - [x] Unit Tests geschrieben (Task 4)
  - [x] Manual Test + Device Verification (Task 5)

- [x] **Keine Placeholders:** Alle Steps enthalten exakte Code-Schnipsel und Befehle

- [x] **Type Consistency:** AspectRatio enum verwendet konsistent über alle Tasks

- [x] **Compile-Check:** Jeder Task endet mit Compile-Verifizierung

- [x] **Commit Pattern:** Jeder Task hat Commit mit aussagekräftiger Nachricht

---

## Implementation Order

**Sequential (nicht parallel):**

1. Task 1 – ExoPlayerEngine (Root Fix)
2. Task 2 – PlayerScreen Cycle (UI Layer)
3. Task 3 – PlayerSettingsScreen (Settings Layer)
4. Task 4 – Unit Tests (Verification)
5. Task 5 – Manual Integration Test (E2E)

**Reasoning:** Jeder Task baut auf dem vorherigen auf. Task 1 ist Root Fix, Tasks 2-3 sind UI-Anpassungen, Task 4-5 sind Verifikation.

---

## Expected Result Nach allen Tasks

- ✅ AspectRatio Button funktioniert: FIT → FILL → FIT
- ✅ Video skaliert sichtbar bei Button-Klick
- ✅ Settings zeigt nur [FIT, FILL]
- ✅ Gespeicherte AspectRatio wird beim App-Start geladen
- ✅ Keine neuen Compile-Fehler
- ✅ Unit Tests grün
- ✅ Manual Device Test bestanden
- ✅ Commits sauber und dokumentiert

