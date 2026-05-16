# AspectRatio Fix: StyledPlayerView + Listener Pattern

**Date:** 2026-05-16  
**Status:** Design Approved  
**Priority:** High  

## Problem Statement

Aktuelle Implementierung nutzt ExoPlayer's `setVideoScalingMode()` mit unvollständigem AspectRatio-Mapping:
- `FILL` und `STRETCH` sind beide auf `SCALE_TO_FIT_WITH_CROPPING` gemappt → nicht differenzierbar
- `RATIO_4_3` und `RATIO_16_9` sind beide auf `SCALE_TO_FIT` gemappt → keine spezifische Skalierung

**Symptom:** Button wird geklickt, State ändert sich, aber Video zeigt keine visuelle Änderung.

**Root Cause:** ExoPlayer unterstützt nativ nur 2 Skalierungsmodi (SCALE_TO_FIT, SCALE_TO_FIT_WITH_CROPPING). Alle anderen AspectRatios können nicht implementiert werden.

## Solution Overview

Nutze Media3's `StyledPlayerView` mit AspectRatio-Listener-Pattern. Reduziere `ASPECT_RATIO_CYCLE` auf nur unterstützte Modi: **[FIT, FILL]**.

### Why StyledPlayerView?
- Bereits TextureView-basiert (bessere Rendering-Kontrolle als SurfaceView)
- Bietet AspectRatio-Listener für Rendering-Events
- Minimale UI-Layer-Änderungen erforderlich
- ExoPlayer managed interne Details

### Why Reduce to [FIT, FILL]?
- Diese 2 Modi sind die einzigen, die ExoPlayer nativ unterstützt
- Weitere Modi (STRETCH, 4:3, 16:9) würden komplexe LayoutParams-Manipulation erfordern
- Saubere, wartbare Lösung ohne Hacks

## Architecture

### Component Changes

#### 1. PlayerScreen.kt
**Current:** Nutzt `SurfaceView` direkt  
**New:** Nutzt `StyledPlayerView` aus Media3

```
AndroidView(factory = { context ->
    StyledPlayerView(context).apply {
        // StyledPlayerView configured with ExoPlayer
        // AspectRatio changes propagated via listener
    }
})
```

**State Updates:**
- `currentAspectRatio` bleibt State
- ASPECT_RATIO_CYCLE reduziert auf: `[AspectRatio.FIT, AspectRatio.FILL]`
- onClick Handler zykliert zwischen nur 2 Werten

#### 2. PlayerSettingsScreen.kt
**Current:** SegmentedRow zeigt 5 AspectRatio-Optionen  
**New:** SegmentedRow zeigt nur 2 Optionen [FIT, FILL]

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

#### 3. ExoPlayerEngine.kt
**Current:** 5-Wert When-Ausdruck mit falschem Mapping  
**New:** 2-Wert When-Ausdruck mit korrektem Mapping

```kotlin
fun setAspectRatio(aspectRatio: AspectRatio) {
    val ratio = when (aspectRatio) {
        AspectRatio.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
        AspectRatio.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    }
    try {
        exoPlayer.setVideoScalingMode(ratio)
    } catch (e: Exception) {
        lastError = "Fehler beim Setzen des Seitenverhältnisses: ${e.message}"
    }
}
```

#### 4. PlayerManager.kt
**No changes required.** Interface bleibt gleich, nur interne Implementierung ändert sich.

#### 5. PlayerEngine.kt (Interface)
**No changes required.** `setAspectRatio()` bleibt als abstrakte Methode.

## Data Flow

### AspectRatio Change (Button Click)
```
PlayerScreen onClick
  ↓
currentAspectRatio State = nächster Wert (FIT oder FILL)
  ↓
playerManager.setAspectRatio(nextAspectRatio)
  ↓
ExoPlayerEngine.setAspectRatio(aspectRatio)
  ↓
exoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_*)
  ↓
StyledPlayerView aktualisiert Rendering
  ↓
Video skaliert sichtbar
```

### App Start (Settings Laden)
```
LaunchedEffect
  ↓
playerSettings.defaultAspectRatio gelesen
  ↓
currentAspectRatio State = defaultAspectRatio
  ↓
playerManager.setAspectRatio(defaultAspectRatio) aufgerufen
```

## UI Behavior

### AspectRatio Button (Seekbar)
- **Icon:** Icons.Filled.Crop
- **Position:** Nach Speed-Button, vor Stop-Button
- **Farbe:** 
  - WarmGold wenn `currentAspectRatio != AspectRatio.FIT`
  - Weiß wenn `currentAspectRatio == AspectRatio.FIT` (Default)
- **Cycle:** FIT → FILL → FIT
- **Fokus:** Navigation mit Pfeiltasten unterstützt

### AspectRatio SegmentedRow (Settings)
- **Buttons:** [FIT, FILL]
- **Verhalten:** Live-Update ohne Auto-Speicherung
- **Speicherung:** Nutzer speichert manuell via Save-Button

## Error Handling

### Exception Scenarios
- ExoPlayer nicht initialisiert → Exception in `setVideoScalingMode()` → Fehler gecatched, zu `lastError` geloggt
- AspectRatio-Wert invalid → Default zu FIT
- StyledPlayerView Rendering-Fehler → Schwarzer Screen, kein Crash

### Edge Cases
- AspectRatio-Wechsel während Video lädt → State aktualisiert, setVideoScalingMode() später aufgerufen (asynchron, ok)
- Navigation weg vom PlayerScreen → State verworfen, kein Memory Leak

## Testing Strategy

### Unit Tests
- `setAspectRatio(FIT)` ruft `setVideoScalingMode(SCALE_TO_FIT)` auf
- `setAspectRatio(FILL)` ruft `setVideoScalingMode(SCALE_TO_FIT_WITH_CROPPING)` auf
- Verify Cycle: FIT → FILL → FIT

### Manual Tests
1. **Visual Change:** Click AspectRatio Button, verify Video-Skalierung ändert sich sichtbar
2. **Settings Persistence:** Stelle AspectRatio in Settings, schließe App, öffne erneut, verify gespeicherter Wert wird geladen
3. **Edge Case:** Während Video lädt, wechsle AspectRatio → verify kein Crash

## Implementation Checklist

- [ ] PlayerScreen.kt: SurfaceView → StyledPlayerView
- [ ] PlayerScreen.kt: ASPECT_RATIO_CYCLE reduzieren auf [FIT, FILL]
- [ ] PlayerScreen.kt: AspectRatio Button implementieren
- [ ] PlayerSettingsScreen.kt: SegmentedRow limitieren auf [FIT, FILL]
- [ ] ExoPlayerEngine.kt: setAspectRatio() simplify auf 2 Modi
- [ ] Unit Tests schreiben
- [ ] Manual Tests durchführen
- [ ] Verify: Video skaliert korrekt bei Button-Klick

## Migration Notes

**Breaking Changes:** Keine. Benutzer der API sehen keine Änderungen.

**Config Changes:** AspectRatio.RATIO_4_3, AspectRatio.RATIO_16_9, AspectRatio.STRETCH sind still in Enum, aber werden nicht mehr in Cycles genutzt. Können später entfernt werden.

**Settings Backward Compatibility:** Falls Nutzer gespeicherte Einstellung auf RATIO_4_3 hatte → wird bei App-Start zu FIT fallback (weil enum value nicht mehr existiert oder nicht gemappt wird).

## Dependencies

- `androidx.media3:media3-ui` (bereits in build.gradle)
- `androidx.media3:media3-exoplayer` (bereits in build.gradle)

Keine neuen Dependencies erforderlich.

## Timeline

- Phase 1 (Code): ~30 min (PlayerScreen, PlayerSettingsScreen, ExoPlayerEngine ändern)
- Phase 2 (Testing): ~15 min (Unit + Manual Tests)
- Phase 3 (Verification): ~10 min (Build, APK-Test auf Gerät)

**Total: ~55 min**
