# Player Settings – Design Spec
**Datum:** 2026-05-15  
**Projekt:** Kotlin Native IPTV Player App  
**Status:** Approved

---

## Überblick

Implementierung eines dedizierten Player-Einstellungen-Screens, der persistent gespeicherte Konfigurationsoptionen für den IPTV-Player bereitstellt. Die Einstellungen werden beim Start des Players angewendet – kein Live-Ändern während der Wiedergabe.

---

## Scope

**In Scope:**
- Wiedergabe-Basis: Resume-Offset, Standard-Geschwindigkeit
- Seek-Buttons: Vor- und Zurückspul-Dauer
- Verhalten: Auto-Hide-Timer, Auto-Play nächste Episode, Puffer-Größe
- Bild: Standard-Seitenverhältnis, Bildschirm-an Toggle
- Audio: Bevorzugte Audiosprache, Lautstärke-Normalisierung

**Out of Scope:**
- Helligkeit/Kontrast (ExoPlayer ohne GLES-Shader nicht trivial)
- Per-Kanal-Overrides
- Import/Export von Settings
- Live-Änderung von Settings während der Wiedergabe

---

## Datenmodell

### `data/PlayerSettings.kt`

```kotlin
data class PlayerSettings(
    // A – Wiedergabe-Basis
    val resumeOffsetMs: Long = 5_000L,        // 0–30.000ms, Standard: 5s zurückspringen
    val defaultPlaybackSpeed: Float = 1.0f,   // 0.5, 1.0, 1.5, 2.0

    // B – Seek-Buttons
    val seekForwardMs: Long = 30_000L,        // 5.000–300.000ms, Standard: 30s
    val seekBackwardMs: Long = 10_000L,       // 5.000–300.000ms, Standard: 10s

    // C1 – Wiedergabe-Verhalten
    val autoHideControlsMs: Long = 5_000L,   // 2.000–15.000ms
    val autoPlayNext: Boolean = false,
    val bufferSizeSeconds: Int = 30,          // 10–120s

    // C2 – Bild
    val defaultAspectRatio: AspectRatio = AspectRatio.FIT,
    val keepScreenOn: Boolean = true,

    // C3 – Audio
    val preferredAudioLanguage: String = "",  // ISO 639-1 Code, "" = keine Präferenz
    val loudnessNormalization: Boolean = false
)

enum class AspectRatio { FIT, FILL, STRETCH, RATIO_4_3, RATIO_16_9 }
```

### Persistence

- Key in `PreferencesManager`: `player_settings`
- Serialisierung: Gson (konsistent mit bestehenden Account-Settings)
- Neue Methoden: `getPlayerSettings(): PlayerSettings` und `savePlayerSettings(settings: PlayerSettings)`
- Fehlerbehandlung: Exception bei Deserialisierung → Fallback auf `PlayerSettings()` mit Defaults, nie Crash
- Rückwärtskompatibilität: Neue Felder nach App-Update nutzen Kotlin-Defaults automatisch

---

## Architektur

### Neue Dateien

| Datei | Zweck |
|---|---|
| `data/PlayerSettings.kt` | Data class + `AspectRatio` enum |
| `ui/settings/PlayerSettingsScreen.kt` | Compose UI |
| `ui/settings/PlayerSettingsViewModel.kt` | State + Persistence-Logik |
| `ui/settings/components/SliderRow.kt` | Wiederverwendbares Slider-Composable |
| `ui/settings/components/SegmentedRow.kt` | Segmentierte Button-Gruppe |
| `ui/settings/components/SwitchRow.kt` | Toggle-Zeile |
| `ui/settings/components/DropdownRow.kt` | Dropdown-Auswahl |
| `ui/settings/components/TextFieldRow.kt` | Texteingabe-Zeile |
| `ui/settings/components/SectionHeader.kt` | Abschnitts-Überschrift |

### Geänderte Dateien

| Datei | Änderung |
|---|---|
| `PreferencesManager.kt` | `getPlayerSettings()` + `savePlayerSettings()` hinzufügen |
| `SettingsScreen.kt` | Neue "Player"-Kachel mit Navigation |
| `NavGraph.kt` | Route `PlayerSettings` registrieren |
| `Screen.kt` | `Screen.PlayerSettings` Objekt hinzufügen |
| `PlayerScreen.kt` | Hardcoded-Werte durch Settings ersetzen |
| `ExoPlayerEngine.kt` | `currentSpeed` aus Settings statt hardcoded `1.0f` |

### ViewModel

```kotlin
@HiltViewModel
class PlayerSettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _settings = MutableStateFlow(preferencesManager.getPlayerSettings())
    val settings: StateFlow<PlayerSettings> = _settings.asStateFlow()

    fun update(settings: PlayerSettings) { _settings.value = settings }
    fun save() { preferencesManager.savePlayerSettings(_settings.value) }
}
```

### Datenfluss

```
SettingsScreen
    └─ tap "Player" → navigate(Screen.PlayerSettings)
            └─ PlayerSettingsScreen
                    ├─ liest: PlayerSettingsViewModel.settings (StateFlow)
                    ├─ Slider/Switch-Änderungen → ViewModel.update(newSettings)
                    └─ "Speichern"-Button → ViewModel.save() → PreferencesManager

PlayerScreen (beim Start)
    └─ LaunchedEffect(Unit) → PreferencesManager.getPlayerSettings()
            ├─ seekForwardMs / seekBackwardMs → Seek-Buttons
            ├─ autoHideControlsMs → Auto-Hide-Timer
            ├─ resumeOffsetMs → max(0, savedPosition - resumeOffsetMs)
            ├─ defaultPlaybackSpeed → ExoPlayerEngine.setPlaybackSpeed()
            ├─ defaultAspectRatio → Aspect-Ratio-Logik
            ├─ bufferSizeSeconds → ExoPlayer DefaultLoadControl
            ├─ keepScreenOn → Window-Flag
            ├─ preferredAudioLanguage → Track-Selektion
            └─ loudnessNormalization → ExoPlayer LoudnessEnhancer
```

---

## UI – PlayerSettingsScreen

### Layout

```
PlayerSettingsScreen
├── TopAppBar: "Player-Einstellungen" + Zurück-Button
└── LazyColumn
    ├── SectionHeader("Wiedergabe")
    │   ├── SliderRow("Rücksprung beim Fortsetzen", 0–30s, resumeOffsetMs)
    │   └── SegmentedRow("Standard-Geschwindigkeit", [0.5×, 1×, 1.5×, 2×])
    │
    ├── SectionHeader("Seek-Buttons")
    │   ├── SliderRow("Vorspulen", 5–300s, seekForwardMs)
    │   └── SliderRow("Zurückspulen", 5–300s, seekBackwardMs)
    │
    ├── SectionHeader("Verhalten")
    │   ├── SliderRow("Controls ausblenden nach", 2–15s, autoHideControlsMs)
    │   ├── SwitchRow("Nächste Episode auto-abspielen", autoPlayNext)
    │   └── SliderRow("Puffer-Größe", 10–120s, bufferSizeSeconds)
    │
    ├── SectionHeader("Bild")
    │   ├── DropdownRow("Standard-Seitenverhältnis", AspectRatio-Werte)
    │   └── SwitchRow("Bildschirm anlassen", keepScreenOn)
    │
    ├── SectionHeader("Audio")
    │   ├── TextFieldRow("Bevorzugte Audiosprache (z.B. 'de')", preferredAudioLanguage)
    │   └── SwitchRow("Lautstärke normalisieren", loudnessNormalization)
    │
    └── Button("Einstellungen speichern", WarmGold, onClick = viewModel.save())
```

### Composable-Spezifikationen

**`SliderRow`**
- Label links, Slider in der Mitte, Wert + Einheit rechts (z.B. "30 s")
- Konfigurierbare Step-Größe (z.B. 1s für autoHide, 5s für Seek)
- D-Pad-navigierbar via `FocusRequester` + `onKeyEvent` (Links/Rechts = ±1 Step)

**`SegmentedRow`**
- Label + horizontale Button-Gruppe für diskrete Float-Werte
- Aktiver Wert hervorgehoben mit `WarmGold`-Hintergrund

**`SwitchRow`**
- Label links, `Switch` rechts
- Volle Breite klickbar für bessere Touch/D-Pad-Ergonomie

**`DropdownRow`**
- Label + `ExposedDropdownMenuBox`
- Enum-Werte werden als lesbare Labels angezeigt (z.B. `RATIO_16_9` → "16:9")

**`TextFieldRow`**
- Label + einzeiliges `OutlinedTextField`
- `KeyboardOptions(imeAction = ImeAction.Done)`

**`SectionHeader`**
- Text in `WarmGold`, `MaterialTheme.typography.labelMedium`
- Horizontaler Divider darunter

### TV-Optimierung
- Alle Zeilen sind via `focusable()` D-Pad-navigierbar
- `Modifier.focusGroup()` pro Sektion für geordnete D-Pad-Navigation
- Slider reagiert auf Links/Rechts mit konfigurierbaren Steps

---

## Fehlerbehandlung & Edge Cases

| Situation | Behandlung |
|---|---|
| Gson-Deserialisierung schlägt fehl | Exception fangen → `PlayerSettings()` Defaults zurückgeben |
| Neue Felder nach App-Update | Gson ignoriert fehlende Keys → Kotlin-Defaults greifen |
| Slider-Wert außerhalb Range | `coerceIn(min, max)` vor dem Speichern |
| `preferredAudioLanguage` fehlerhaft | Auf 3 Zeichen trimmen, lowercase normieren |
| Resume-Position < 0 nach Offset | `max(0, savedPosition - resumeOffsetMs)` |
| `autoPlayNext` ohne nächste Episode | Ignorieren, kein Crash |

---

## Nicht implementiert (bewusst)

- **Helligkeit/Kontrast:** ExoPlayer benötigt GLES-Shader – zu aufwändig für diesen Scope
- **Per-Kanal-Overrides:** Zu komplex, kann später als Erweiterung folgen
- **Import/Export:** Nicht benötigt
