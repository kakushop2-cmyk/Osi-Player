# NativePlayer - IPTV Player App

<div align="center">

![Android](https://img.shields.io/badge/Android-24%2B-green?style=flat-square&logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue?style=flat-square&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-purple?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

**Ein moderner IPTV-Player für Android mit Xtream-Codes-API-Integration**

[Features](#features) • [Installation](#installation) • [Benutzung](#benutzung) • [Architektur](#architektur) • [API](#api-dokumentation) • [Contributing](#contributing)

</div>

---

## Features

### 📺 Live TV
- **Xtream-Codes-API** - Vollständige Integration mit Xtream-Codes IPTV-Anbietern
- **M3U-Playlist-Support** - Import von M3U/M3U8-Playlists
- **EPG (Electronic Program Guide)** - TV-Programm mit Zeitverschiebung
- **Kategorien** - Sender nach Kategorien sortiert
- **Favoriten** - Lieblingssender speichern

### 🎬 Filme & Serien
- **VOD (Video on Demand)** - Filme und Serien abrufen
- **Serien-Verwaltung** - Staffeln und Episoden
- **Detailansicht** - Poster, Bewertungen, Beschreibungen
- **Weiterschauen** - Fortschritt speichern und fortsetzen

### 🎨 Benutzeroberfläche
- **Material Design 3** - Modernes, dunkles Theme
- **Tablet-optimiert** - Responsive Layout für große Bildschirme
- **Android TV Support** - Leanback-Launcher für TV-Geräte
- **Intuitive Navigation** - Einfache Menüführung

### ⚙️ Technisches
- **Hilt Dependency Injection** - Saubere Architektur
- **Room Database** - Lokale Datenspeicherung
- **Coroutines** - Asynchrone Netzwerk- und Datenbankoperationen
- **Coil** - Effizientes Bildladen mit Caching

---

## Installation

### Voraussetzungen

- **Android Studio** Hedgehog (2023.1.1) oder neuer
- **JDK 17** oder neuer
- **Android SDK 34** (compileSdk)
- **Mindestens Android 7.0** (API 24)

### Schritt-für-Schritt-Anleitung

1. **Repository klonen**
   ```bash
   git clone https://github.com/yourusername/nativeplayer.git
   cd nativeplayer
   ```

2. **In Android Studio öffnen**
   - File → Open → Projektverzeichnis auswählen
   - Warten, bis Gradle-Sync abgeschlossen ist

3. **App erstellen**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Auf Gerät installieren**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Als Release erstellen

```bash
# Signing konfigurieren (siehe docs/signing.md)
./gradlew assembleRelease
```

---

## Benutzung

### Erste Schritte

1. **App starten** - NativePlayer auf Ihrem Gerät öffnen
2. **Anbieter hinzufügen** - In den Einstellungen Ihren IPTV-Anbieter konfigurieren:
   - **Xtream-Codes**: Domain, Benutzername, Passwort eingeben
   - **M3U-Playlist**: URL oder Datei importieren
3. **Inhalte browsen** - Live TV, Filme oder Serien auswählen
4. **Genießen!** - Stream starten und schauen

### Hauptmenü

| Funktion | Beschreibung |
|----------|--------------|
| **Live TV** | Alle Live-Sender nach Kategorien |
| **Filme** | Video-on-Demand Filme |
| **Serien** | Serien mit Staffeln und Episoden |
| **Favoriten** | Gespeicherte Lieblingssender |
| **Weiterschauen** | Zuletzt gesehene Inhalte fortsetzen |

### Einstellungen

- **Anbieter-Verwaltung** - Mehrere IPTV-Anbieter verwalten
- **Player-Einstellungen** - Video-Decoder, Audio-Track
- **Kategorie-Reihenfolge** - Benutzerdefinierte Sortierung
- **Cache-Verwaltung** - Daten und Bilder löschen

---

## Architektur

### Übersicht

```
com.nativeplayer/
├── data/                    # Datenschicht
│   ├── api/                 # Netzwerk-API (Retrofit)
│   ├── model/               # Datenmodelle
│   ├── parser/              # M3U-Parser
│   └── repository/          # Repository-Pattern
├── di/                      # Dependency Injection (Hilt)
├── player/                  # Video-Player (ExoPlayer)
└── ui/                      # Benutzeroberfläche (Compose)
    ├── components/          # Wiederverwendbare UI-Komponenten
    ├── home/                # Startbildschirm
    ├── live/                # Live TV
    ├── movies/              # Filme
    ├── series/              # Serien
    ├── favorites/           # Favoriten
    ├── resume/              # Weiterschauen
    ├── search/              # Suche
    ├── settings/            # Einstellungen
    ├── login/               # Anbieter-Login
    ├── splash/              # Startbildschirm
    ├── player/              # Player-UI
    ├── navigation/          # Navigation (Compose)
    ├── theme/               # Material3 Theme
    ├── util/                # Hilfsklassen
    └── viewmodel/           # ViewModels (MVVM)
```

### Technologie-Stack

| Komponente | Technologie | Zweck |
|------------|-------------|-------|
| **UI** | Jetpack Compose + Material3 | Moderne, deklarative UI |
| **DI** | Hilt | Dependency Injection |
| **Netzwerk** | Retrofit + OkHttp | API-Kommunikation |
| **Datenbank** | Room | Lokale Datenspeicherung |
| **Player** | ExoPlayer (Media3) | Video-Wiedergabe |
| **Bilder** | Coil | Bildladung mit Caching |
| **Navigation** | Navigation Compose | Bildschirm-Navigation |

### Design-Prinzipien

- **MVVM** - Model-View-ViewModel für saubere Trennung
- **Repository-Pattern** - Einheitliche Datenschicht
- **Single Source of Truth** - Ein zentraler Datenpfad
- **Unidirectional Data Flow** - Daten fließen in eine Richtung

---

## API-Dokumentation

Die vollständige API-Dokumentation finden Sie in [`docs/API.md`](docs/API.md).

### Kurzübersicht

Die App verwendet die **Xtream-Codes-API** für die Kommunikation mit IPTV-Anbietern.

**Basis-URL:** `https://your-provider.com/player_api.php`

**Authentifizierung:**
```
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS
```

**Hauptendpunkte:**

| Endpunkt | Beschreibung |
|----------|--------------|
| `authenticate` | Benutzer authentifizieren |
| `get_live_streams` | Live-TV-Sender abrufen |
| `get_vod_streams` | Filme abrufen |
| `get_series` | Serien abrufen |
| `get_epg` | TV-Programm abrufen |

---

## Contributing

Wir freuen uns über Beiträge! Bitte lesen Sie unsere [Contributing-Richtlinien](docs/CONTRIBUTING.md).

### Entwicklungs-Setup

1. **Fork** das Repository
2. **Branch** erstellen: `git checkout -b feature/my-feature`
3. **Änderungen** machen
4. **Tests** ausführen: `./gradlew test`
5. **Commit**: `git commit -m "Add my feature"`
6. **Push**: `git push origin feature/my-feature`
7. **Pull Request** erstellen

### Code-Standards

- **Kotlin** - Offizielle Kotlin-Codestyle
- **Compose** - Material3 Guidelines
- **Tests** - Mindestens 80% Testabdeckung für neue Features
- **Dokumentation** - KDoc für öffentliche APIs

---

## Lizenz

Dieses Projekt ist unter der **MIT License** lizenziert. Siehe [LICENSE](LICENSE) für Details.

---

## Danksagungen

- [ExoPlayer](https://github.com/google/Exoplayer) - Video-Wiedergabe
- [Retrofit](https://square.github.io/retrofit/) - HTTP-Client
- [Hilt](https://dagger.dev/hilt/) - Dependency Injection
- [Jetpack Compose](https://developer.android.com/jetpack/compose) - UI-Toolkit

---

## Support

- **Issues** - [GitHub Issues](https://github.com/yourusername/nativeplayer/issues)
- **Discussions** - [GitHub Discussions](https://github.com/yourusername/nativeplayer/discussions)
- **Email** - support@nativeplayer.example.com

---

<div align="center">

**Made with ❤️ for IPTV enthusiasts**

</div>
