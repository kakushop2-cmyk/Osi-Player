# Timeline 30s Seeking Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Erweitere die Timeline im PlayerScreen mit Fokus-Navigation und 30-Sekunden Vor-/Zurueck-Spulen via Links/Rechts Pfeiltasten.

**Architecture:** Erweiterung des bestehenden Sliders in PlayerScreen.kt mit zusaetzlichem Focus-Handling. Der Slider erhaelt einen FocusRequester und reagiert auf DirectionLeft/Right KeyEvents mit +/- 30 Sekunden Seeking.

**Tech Stack:** Jetpack Compose, Compose TV Focus-System, ExoPlayer

---

## File Structure

| File | Action | Responsibility |
|------|--------|----------------|
| `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt` | Modify | Enhanced Slider mit Focus-Handling und 30s Seeking |

---

## Task 1: Focus-Requester fuer Timeline hinzufuegen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt:139`

- [ ] **Step 1: Timeline-FocusRequester hinzufuegen**

Fuege nach Zeile 139 einen neuen FocusRequester hinzu:

```kotlin
    val timelineSliderFocus = remember { FocusRequester() }  // NEU: Fuer die Timeline
```

- [ ] **Step 2: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git commit -m "feat: add timeline slider focus requester"
```

---

## Task 2: Slider mit Focus-Handling und 30s Seeking erweitern

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt:713-738`

- [ ] **Step 1: Ersetze den bestehenden Slider durch enhanced Version**

Ersetze den Slider (Zeilen 713-738) durch:

```kotlin
                // Enhanced Timeline Slider mit 30s Seeking
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .focusable()
                        .focusRequester(timelineSliderFocus)
                        .focusProperties { 
                            down = playPauseFocus
                        }
                        .onKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                            if (duration <= 0) return@onKeyEvent false
                            
                            when (event.key) {
                                Key.DirectionLeft -> {
                                    val newPos = (currentPosition - 30_000).coerceAtLeast(0)
                                    playerManager?.seekTo(newPos)
                                    currentPosition = newPos
                                    resetHideTimer()
                                    true
                                }
                                Key.DirectionRight -> {
                                    val newPos = (currentPosition + 30_000).coerceAtMost(duration)
                                    playerManager?.seekTo(newPos)
                                    currentPosition = newPos
                                    resetHideTimer()
                                    true
                                }
                                else -> false
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Slider(
                        value = if (duration > 0) {
                            (currentPosition.toFloat() / duration).coerceIn(0f, 1f)
                        } else 0f,
                        onValueChange = { fraction ->
                            val pos = (fraction * duration).toLong()
                            currentPosition = pos
                            isDragging = true
                        },
                        onValueChangeFinished = {
                            playerManager?.seekTo(currentPosition)
                            isDragging = false
                            resetHideTimer()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = WarmGold,
                            activeTrackColor = WarmGold,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )
                }
```

- [ ] **Step 2: Teste Build**

```bash
./gradlew :app:compileDebugKotlin --no-daemon
```

Erwartet: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git commit -m "feat: add 30s seek to timeline with focus handling"
```

---

## Task 3: Focus-Navigation zwischen Steuerungselementen aktualisieren

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt`

- [ ] **Step 1: Rewind-Button Focus-Properties aktualisieren (ca. Zeile 770)**

Aendere die focusProperties des rewindFocus:

```kotlin
                            .focusProperties {
                                right = playPauseFocus
                                up = timelineSliderFocus  // NEU
                            }
```

- [ ] **Step 2: Play/Pause-Button Focus-Properties aktualisieren (ca. Zeile 793)**

Aendere die focusProperties des playPauseFocus:

```kotlin
                            .focusProperties {
                                left = rewindFocus
                                right = forwardFocus
                                up = timelineSliderFocus  // NEU
                            }
```

- [ ] **Step 3: Forward-Button Focus-Properties aktualisieren (ca. Zeile 820)**

Aendere die focusProperties des forwardFocus:

```kotlin
                            .focusProperties {
                                left = playPauseFocus
                                right = speedFocus
                                up = timelineSliderFocus  // NEU
                            }
```

- [ ] **Step 4: Speed-Button Focus-Properties aktualisieren (ca. Zeile 837)**

Aendere die focusProperties des speedFocus:

```kotlin
                            .focusProperties {
                                left = forwardFocus
                                right = stopFocus
                                up = timelineSliderFocus  // NEU
                            }
```

- [ ] **Step 5: Stop-Button Focus-Properties aktualisieren (ca. Zeile 915)**

Aendere die focusProperties des stopFocus:

```kotlin
                            .focusProperties {
                                left = speedFocus
                                up = timelineSliderFocus  // NEU
                            }
```

- [ ] **Step 6: Teste Build**

```bash
./gradlew :app:compileDebugKotlin --no-daemon
```

Erwartet: BUILD SUCCESSFUL

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git commit -m "feat: connect timeline focus navigation to control buttons"
```

---

## Task 4: Testen der Implementierung

**Files:**
- Keine neuen Dateien

- [ ] **Step 1: Build testen**

```bash
./gradlew :app:assembleDebug --no-daemon
```

Erwartet: BUILD SUCCESSFUL

- [ ] **Step 2: Manuelle Tests durchfuehren**

Teste folgende Szenarien:

1. **Navigation zur Timeline:**
   - Starte einen Film/Serie
   - Druecke D-Pad Down von Play/Pause
   - Erwartet: Focus wechselt zum Timeline-Slider

2. **30s Zurueckspulen:**
   - Fokus auf Timeline
   - Druecke D-Pad Left
   - Erwartet: Position -30 Sekunden

3. **30s Vorspulen:**
   - Fokus auf Timeline
   - Druecke D-Pad Right
   - Erwartet: Position +30 Sekunden

4. **Grenzfaelle:**
   - Bei Position 0: Left sollte nicht unter 0 gehen
   - Bei Position = Duration: Right sollte nicht ueber Duration gehen

- [ ] **Step 3: Finaler Commit**

```bash
git commit -m "feat: complete timeline 30s seeking implementation"
```

---

## Self-Review

**Spec Coverage:**
- Focus-Requester fuer Timeline: Task 1
- 30s Seeking via Links/Rechts: Task 2
- Focus-Navigation Verbindungen: Task 3
- Tests: Task 4

**Keine Placeholders:** Alle Code-Beispiele sind vollstaendig.

**Type Consistency:** Alle FocusRequester und KeyEvents verwenden konsistente Compose TV APIs.
