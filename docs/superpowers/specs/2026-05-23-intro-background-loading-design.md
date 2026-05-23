# IntroScreen Background Data Loading — Design Spec

## Ziel
Daten laden im Hintergrund, während das Intro-Video abspielt — statt vorher einen Lade-Spinner auf dem SplashScreen anzuzeigen. Der User sieht sofort das Video, die App wirkt schneller.

## Status
- **Erstellt:** 2026-05-23
- **Genehmigt:** Ja
- **Ansatz:** A — IntroViewModel (empfohlen)

## Aktuelles Problem
Derzeit läuft der App-Start sequentiell ab:

```
SplashScreen (loadAllFromXtream → Spinner)
    → IntroScreen (Video)
    → HomeScreen
```

Der User wartet auf dem SplashScreen, bis alle Daten von der Xtream-API geladen sind (Live-Kanäle, VOD-Filme, Serien). Erst dann startet das Intro-Video. Das fühlt sich langsam an.

## Neuer Ablauf
```
IntroScreen (Video startet SOFORT + gleichzeitiges Datenladen)
    → HomeScreen (bei Video-Ende, egal ob Daten fertig sind)
    └─ bei Fehler: Error-Overlay auf IntroScreen nach Video
    └─ bei NoAccount: LoginScreen nach Video
```

Der SplashScreen entfällt komplett.

## Betroffene Dateien

| Datei | Änderung |
|-------|----------|
| `ui/splash/IntroScreen.kt` | Wird Start-Destination, beobachtet `IntroViewModel`, Error-Overlay |
| `ui/splash/IntroViewModel.kt` | **NEU** — Data Loading, IntroState sealed class |
| `ui/splash/SplashScreen.kt` | **Löschen** |
| `ui/splash/SplashViewModel.kt` | `loadData()` entfernen, Account-Helper bleiben |
| `ui/navigation/NavGraph.kt` | `startDestination = Screen.Intro.route` |
| `ui/navigation/Screen.kt` | `Screen.Splash` entfernen |
| `ui/settings/SettingsScreen.kt` | Keine Änderung — importiert weiterhin `SplashViewModel` (nur ohne `loadData()`) |

## Architektur

```
IntroViewModel (@Hilt)
  ├── state: StateFlow<IntroState>
  │   ├── Loading        (initial / retry)
  │   ├── Success        (loadAllFromXtream OK)
  │   ├── Error(message) (Fehler beim Laden)
  │   └── NoAccount      (kein Account konfiguriert)
  ├── init → loadDataInBackground()
  └── retry()

IntroScreen
  ├── ExoPlayer (Video) — läuft sofort bei Composititon
  ├── IntroViewModel.state — beobachtet parallel
  ├── LaunchedEffect(videoEnded, dataState):
  │   ├── Success  → HomeScreen
  │   ├── Loading  → HomeScreen (pollingFlow wartet auf isLoaded)
  │   ├── NoAccount → LoginScreen
  │   └── Error    → bleibe auf IntroScreen, zeige Error-Overlay
  └── Error-Overlay:
      ├── Space-Hintergrund (Sterne + Nebel) bleibt
      ├── Video-Layer wird ausgeblendet
      ├── Error-Icon + Nachricht
      ├── "Erneut versuchen" Button
      └── "Einstellungen" Button
```

## Datenfluss

1. **App start** → `NavGraph` öffnet `Screen.Intro.route`
2. **IntroScreen**:
   - ExoPlayer startet `R.raw.app_intro` sofort
   - `IntroViewModel.loadDataInBackground()` startet in `init`
3. **IntroViewModel**:
   - Prüft `PreferencesManager.getActiveAccount()`
   - Wenn kein Account → `IntroState.NoAccount`
   - Wenn Account → `repository.loadAllFromXtream(account)`
     - Erfolg → `IntroState.Success`
     - Fehler → `IntroState.Error(message)`
4. **Video endet** (oder User klickt "Überspringen"):
   - Bei `Success` → Navigate Home (Daten sind bereit)
   - Bei `Loading` → Navigate Home (HomeViewModel.pollingFlow wartet auf `isLoaded`)
   - Bei `Error` → Bleibe auf IntroScreen, zeige Fehler-Overlay
   - Bei `NoAccount` → Navigate Login

## IntroViewModel — State & API

```kotlin
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
    val state: StateFlow<IntroState>
    fun retry()
}
```

## IntroScreen — Error-Overlay (nach Video bei Fehler)

- Space-Hintergrund (Canvas mit Nebel + Sternen) bleibt sichtbar
- Video-Player wird **released** (Ressourcen sparen)
- Bei "Erneut versuchen" wird der Player neu erstellt + gestartet
- Zentrierte Error-UI (wie bisheriger SplashScreen Error-State):
  - Error-Icon (48dp, `WarmError`)
  - Nachricht (14sp, `WarmError`)
  - "Erneut versuchen" OutlinedButton
  - "Einstellungen" OutlinedButton (navigiert zu Settings)

## SplashViewModel / SettingsScreen

- `SplashViewModel.loadData()` entfällt (wandert zu `IntroViewModel`)
- `SplashViewModel` bleibt als **reiner Account-ViewModel** erhalten:
  - `getActiveAccount()`, `getAccounts()`, `saveAccount()`, `setActiveAccount()`, `deleteAccount()`
  - `clearWatchHistory()`
- `SettingsScreen` importiert weiterhin `SplashViewModel` — **keine Änderung nötig**
- Optional: später in `AccountViewModel` umbenennen (reine Kosmetik, nicht Teil dieses Projekts)

## Edge Cases

### Fall 1: Kein Internet / Server nicht erreichbar
- Video läuft ganz normal ab
- Nach Video-Ende: Error-Overlay mit "Verbindungsfehler"
- User kann "Erneut versuchen" klicken (viewModel.retry())
- Oder "Einstellungen" → Account-Daten prüfen

### Fall 2: Account wurde gelöscht während App im Hintergrund
- `getActiveAccount()` gibt null zurück
- IntroState.NoAccount → nach Video zu LoginScreen

### Fall 3: Video dauert länger als Datenladen
- Daten sind bereits in `Success`-State, wenn Video endet
- Normaler Navigations-Fall

### Fall 4: Daten laden länger als Video
- Video endet, Daten sind noch in `Loading`
- Trotzdem zu HomeScreen navigieren
- `HomeViewModel.pollingFlow` wartet auf `isLoaded` (prüft alle 200ms, max 30s)
- HomeScreen zeigt ggf. leere Sektionen kurz, die sich dann füllen

## Nicht-Ziele
- Keine Änderung am HomeScreen-Logik
- Keine Änderung am ExoPlayer-Verhalten im IntroScreen
- Kein neuer Error-Screen (Error bleibt Overlay auf IntroScreen)
