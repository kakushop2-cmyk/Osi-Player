# AspectRatio UI Redesign – Design Spec
**Datum:** 2026-05-16  
**Projekt:** Kotlin Native IPTV Player App  
**Status:** Approved

---

## Überblick

Redesign des Aspect Ratio Controls für bessere UX:
1. **Settings Screen:** Ersetze Dropdown durch horizontale Segmented-Button-Gruppe
2. **Player Screen:** Neuer AspectRatio-Button in der Seekbar für Live-Umschaltung

---

## Scope

**In Scope:**
- Ersetze `DropdownRow` durch `SegmentedRow` für AspectRatio in PlayerSettingsScreen
- Neuer AspectRatio-Button in PlayerScreen Kontrolleiste
- Zyklische AspectRatio-Umschaltung beim Tippen
- Live-Update des Aspect Ratio während Wiedergabe
- Visuelle Rückmeldung (WarmGold Icon bei nicht-Standard)

**Out of Scope:**
- Speicherung der Live-Änderung (nur explizites Speichern in Settings)
- Keyboard-Navigation für AspectRatio-Button

---

## Datenmodell (existierend)

```kotlin
enum class AspectRatio {
    FIT, FILL, STRETCH, RATIO_4_3, RATIO_16_9;

    fun displayName(): String = when (this) {
        FIT -> "Angepasst"
        FILL -> "Füllen"
        STRETCH -> "Strecken"
        RATIO_4_3 -> "4:3"
        RATIO_16_9 -> "16:9"
    }
}
```

### Neue State-Variable im PlayerScreen

```kotlin
var currentAspectRatio by remember { mutableStateOf(AspectRatio.FIT) }
```

Initial wird diese aus `playerSettings.defaultAspectRatio` geladen.

---

## UI – PlayerSettingsScreen

### Änderung: DropdownRow → SegmentedRow

**Vorher:**
```kotlin
DropdownRow(
    label = "Standard-Seitenverhältnis",
    options = AspectRatio.entries.map { it to it.displayName() },
    selected = settings.defaultAspectRatio,
    onSelect = { viewModel.update(settings.copy(defaultAspectRatio = it)) }
)
```

**Nachher:**
```kotlin
SegmentedRow(
    label = "Standard-Seitenverhältnis",
    options = AspectRatio.entries.map { it to it.displayName() },
    selected = settings.defaultAspectRatio,
    onSelect = { viewModel.update(settings.copy(defaultAspectRatio = it)) }
)
```

### SegmentedRow Spezifikation

- **Layout:** Label über einer Zeile von 5 Buttons
- **Button-Größe:** Uniform, angepasst an Bildschirmbreite (Wrap oder Scroll für ≤5 Items)
- **Aktiver Button:** WarmGold Hintergrund
- **Inaktive Buttons:** WarmSurface mit WarmTextSecondary Text
- **Spacing:** 8dp horizontal zwischen Buttons

---

## UI – PlayerScreen (Seekbar)

### Neue AspectRatio-Button in Kontrolleiste

**Position:** Nach Speed-Button, vor Stop-Button

**Layout:**
```
[Rewind] [Play/Pause] [Forward] [Speed] [AspectRatio] [Stop] [Subtitle] [Audio]
```

### Button-Verhalten

1. **Icon:** Material3 Icon für Aspect Ratio (z.B. `Icons.Filled.Aspect*` oder custom)
2. **State:**
   - Farbe: WarmGold wenn `currentAspectRatio != AspectRatio.FIT`, sonst Weiß
   - Tooltip: Zeige aktuelles Verhältnis (optional)
3. **On Click:**
   - Zyklus durch AspectRatio-Reihenfolge
   - State aktualisieren: `currentAspectRatio = next`
   - PlayerManager.setAspectRatio(next) aufrufen
   - Hide-Timer zurücksetzen (`resetHideTimer()`)
4. **Focus:** Mit `focusable()` für D-Pad-Navigation

### Cycle-Logik

```kotlin
val aspectRatioCycle = listOf(
    AspectRatio.FIT,
    AspectRatio.FILL,
    AspectRatio.STRETCH,
    AspectRatio.RATIO_4_3,
    AspectRatio.RATIO_16_9
)

fun nextAspectRatio(current: AspectRatio): AspectRatio {
    val currentIndex = aspectRatioCycle.indexOf(current)
    val nextIndex = (currentIndex + 1) % aspectRatioCycle.size
    return aspectRatioCycle[nextIndex]
}
```

---

## Technische Details

### State-Management

**PlayerScreen:**
```kotlin
// Initial
LaunchedEffect(Unit) {
    playerSettings = viewModel.getPlayerSettings()
    currentAspectRatio = playerSettings.defaultAspectRatio
}

// On AspectRatio Button Click
val nextRatio = aspectRatioCycle[(aspectRatioCycle.indexOf(currentAspectRatio) + 1) % aspectRatioCycle.size]
currentAspectRatio = nextRatio
playerManager?.setAspectRatio(nextRatio)
resetHideTimer()
```

### PlayerManager Integration

- `PlayerManager.setAspectRatio(ratio: AspectRatio)` Methode existiert bereits
- Wird aufgerufen bei jeder Live-Änderung im Player

### Focus Navigation

```kotlin
var aspectRatioFocus = remember { FocusRequester() }

// In AspectRatio Button
.focusRequester(aspectRatioFocus)
.focusProperties {
    left = speedFocus
    right = stopFocus
    up = sliderFocus
}

// In Speed Button
.focusProperties {
    right = aspectRatioFocus
}

// In Stop Button
.focusProperties {
    left = aspectRatioFocus
}
```

### Speichern-Verhalten

- **Live-Änderung:** Nicht automatisch gespeichert
- **Persistent speichern:** Nur über "Einstellungen speichern"-Button im PlayerSettingsScreen
- Das aktuelle `currentAspectRatio` wird beim PlayerSettings-Save reflektiert

---

## Fehlerbehandlung

| Situation | Behandlung |
|---|---|
| AspectRatio nicht in PlayerManager unterstützt | Fallback auf FIT, Fehler loggen |
| Zyklus-Index out of bounds | Modulo-Operator schützt vor diesem Fall |
| PlayerManager null beim Click | Button disabled oder no-op |

---

## Dateien zum Ändern

1. **PlayerSettingsScreen.kt:** DropdownRow → SegmentedRow (eine Zeile)
2. **PlayerScreen.kt:** 
   - Neue State: `currentAspectRatio`
   - Neue Button in Kontrolleiste
   - Focus Navigation anpassen
   - Cycle-Logik implementieren

---

## Testing

**Manuell:**
1. Öffne PlayerSettings, stelle AspectRatio auf "16:9"
2. Speichern, Player öffnen – sollte "16:9" sein
3. Tippe AspectRatio-Button: sollte zu "Füllen" wechseln
4. Weitere Taps sollten zyklisch durchschalten
5. Nach Speichern sollte neue Einstellung persistent sein

