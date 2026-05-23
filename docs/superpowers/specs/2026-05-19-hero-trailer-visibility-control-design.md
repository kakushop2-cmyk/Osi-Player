# Hero Trailer Visibility Control Design

**Date:** 2026-05-19
**Status:** Approved
**Author:** Build Agent

## Problem Statement

Die 3 Hero-Trailer (Hero, Hero2, Hero3) im HomeScreen stoppen nicht sofort, wenn der Benutzer zu einer anderen Sektion scrollt. Der Player läuft weiter, bis die LazyColumn-Sektion komplett disposed wird, was zu unerwünschtem Verhalten führt:

1. Trailer läuft weiter, obwohl Sektion nicht mehr sichtbar ist
2. Trailer startet, obwohl Sektion nicht fokussiert ist

## Solution: Focus + Visibility-basierte Steuerung

### Verhalten

| Zustand | Aktion |
|---------|--------|
| `hasFocus = true` UND Sektion >80% sichtbar | ▶️ Video starten |
| `hasFocus = false` ODER Sektion <80% sichtbar | ⏸️ Video stoppen |

### Technische Umsetzung

#### 1. Visibility-Tracking pro Sektion

**Modifier:** `onGloballyPositioned` auf jeder Hero-Sektion

**Berechnung:**
```kotlin
val isVisible80Percent = remember { mutableStateOf(false) }

Box(
    modifier = Modifier
        .onGloballyPositioned { coordinates ->
            val windowBounds = coordinates.ownerWindowBounds
            val sectionBounds = coordinates.boundsInWindow()
            
            // Berechne sichtbaren Anteil
            val visibleHeight = minOf(sectionBounds.bottom, windowBounds.bottom) - 
                               maxOf(sectionBounds.top, windowBounds.top)
            val totalHeight = sectionBounds.height
            val visibilityPercent = if (totalHeight > 0) visibleHeight / totalHeight else 0f
            
            isVisible80Percent.value = visibilityPercent >= 0.8f
        }
)
```

#### 2. Kombinierte Steuerung in HeroVideoPlayer

**Parameter:** `isVisible80Percent: Boolean` (neuer Parameter)

**LaunchedEffect:**
```kotlin
LaunchedEffect(hasFocus, isVisible80Percent) {
    if (hasFocus && isVisible80Percent && movie.streamUrl.isNotEmpty()) {
        // Player erstellen und starten
        // ...
    } else {
        // Player stoppen
        // ...
    }
}
```

#### 3. Sofortiger Stop beim Scrollen

- Wenn `isVisible80Percent` von `true` auf `false` wechselt → `pausePlayer()` sofort aufrufen
- Kein Warten bis LazyColumn disposed
- Keine unnötige CPU/GPU-Last

### Betroffene Dateien

| Datei | Änderung |
|-------|----------|
| `HomeScreen.kt` | Visibility-Tracking pro Sektion (Hero, Hero2, Hero3) |
| `HeroVideoPlayer.kt` | Neuer Parameter `isVisible80Percent`, kombinierte Logik |

### Implementierungs-Schritte

1. **HomeScreen.kt:** Visibility-Tracking für alle 3 Hero-Sektionen hinzufügen
2. **HeroVideoPlayer.kt:** Neuer Parameter `isVisible80Percent: Boolean`
3. **HeroVideoPlayer.kt:** `LaunchedEffect(hasFocus, isVisible80Percent)` kombiniert
4. **Testen:** Scrollen zwischen Sektionen, Verhalten prüfen

### Edge Cases

- **Schnelles Scrollen:** Visibility-Check ist sofort, Player stoppt sofort
- **Sektion teilweise sichtbar:** Nur starten wenn >80% sichtbar
- **Focus ohne Visibility:** Player startet nicht
- **Visibility ohne Focus:** Player startet nicht

### Performance

- `onGloballyPositioned` wird nur bei Layout-Änderungen aufgerufen
- Keine kontinuierliche Überwachung
- Minimaler Overhead

## Acceptance Criteria

- [ ] Trailer stoppt sofort wenn Sektion <80% sichtbar
- [ ] Trailer startet nur wenn Sektion >80% sichtbar UND fokussiert
- [ ] Kein Flackern oder unerwartetes Verhalten beim Scrollen
- [ ] Alle 3 Hero-Sektionen betroffen (Hero, Hero2, Hero3)
