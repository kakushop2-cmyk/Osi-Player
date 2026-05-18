# Contributing zu NativePlayer

Vielen Dank für Ihr Interesse an NativePlayer! Diese Richtlinien helfen Ihnen, zum Projekt beizutragen.

---

## Inhaltsverzeichnis

1. [Erste Schritte](#erste-schritte)
2. [Entwicklungs-Setup](#entwicklungs-setup)
3. [Code-Standards](#code-standards)
4. [Pull-Request-Prozess](#pull-request-prozess)
5. [Bug-Reports](#bug-reports)
6. [Feature-Requests](#feature-requests)
7. [Kommunikation](#kommunikation)

---

## Erste Schritte

### Voraussetzungen

- **Android Studio** Hedgehog (2023.1.1) oder neuer
- **JDK 17** oder neuer
- **Git** installiert
- **GitHub-Konto**

### Repository forken

1. Gehen Sie zu [NativePlayer auf GitHub](https://github.com/yourusername/nativeplayer)
2. Klicken Sie auf **Fork** (oben rechts)
3. Klonen Sie Ihren Fork:
   ```bash
   git clone https://github.com/IHR_USERNAME/nativeplayer.git
   cd nativeplayer
   ```

### Upstream einrichten

```bash
git remote add upstream https://github.com/yourusername/nativeplayer.git
git fetch upstream
```

---

## Entwicklungs-Setup

### 1. Abhängigkeiten installieren

```bash
# Gradle-Wrapper ausführen
./gradlew build

# Oder in Android Studio: File → Sync Project with Gradle Files
```

### 2. Emulator einrichten

- **API-Level**: 24 (minSdk) oder höher
- **Empfohlen**: API 34 (targetSdk)
- **ABI**: x86_64 für bessere Performance

### 3. App ausführen

```bash
# Debug-Version erstellen und installieren
./gradlew installDebug

# Oder in Android Studio: Run → Run 'app'
```

### 4. Tests ausführen

```bash
# Unit-Tests
./gradlew test

# UI-Tests (erfordert Emulator)
./gradlew connectedAndroidTest

# Code-Coverage
./gradlew jacocoTestReport
```

---

## Code-Standards

### Kotlin-Stil

- **Offizieller Kotlin-Codestyle** - https://kotlinlang.org/docs/coding-conventions.html
- **KDoc-Kommentare** - Alle öffentlichen Funktionen dokumentieren
- **Naming Conventions**:
  - Klassen: `PascalCase`
  - Funktionen: `camelCase`
  - Konstanten: `UPPER_SNAKE_CASE`
  - Variablen: `camelCase`

### Jetpack Compose

- **Material3 Guidelines** befolgen
- **State Hoisting** - State nach oben heben
- **Composable Functions** - Klein und wiederverwendbar
- **Preview Functions** - Für UI-Komponenten erstellen

### Architektur

- **MVVM Pattern** befolgen
- **Repository Pattern** für Datenschicht
- **Dependency Injection** mit Hilt
- **Single Responsibility Principle**

### Beispiel-Code

```kotlin
/**
 * ViewModel für die Home-Bildschirm-Ansicht.
 * Verwaltet den Zustand der zuletzt angesehenen Inhalte.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {

    private val _recentWatch = MutableStateFlow<List<WatchHistoryEntity>>(emptyList())
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = _recentWatch.asStateFlow()

    init {
        loadRecentWatch()
    }

    /**
     * Lädt die zuletzt angesehenen Inhalte aus der Datenbank.
     */
    private fun loadRecentWatch() {
        viewModelScope.launch {
            watchHistoryRepository.getRecentWatch()
                .collect { entries ->
                    _recentWatch.value = entries
                }
        }
    }
}
```

---

## Pull-Request-Prozess

### 1. Branch erstellen

```bash
# Aktuellen Branch aktualisieren
git checkout main
git pull upstream main

# Feature-Branch erstellen
git checkout -b feature/my-feature

# Oder für Bugfixes
git checkout -b fix/my-bugfix
```

### 2. Änderungen machen

```bash
# Dateien hinzufügen
git add .

# Commit erstellen
git commit -m "feat: Add my feature"

# Oder für Bugfixes
git commit -m "fix: Fix my bug"
```

### 3. Commit-Nachrichten

Verwenden Sie [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**
- `feat`: Neues Feature
- `fix`: Bugfix
- `docs`: Dokumentation
- `style`: Code-Formatierung
- `refactor`: Code-Refactoring
- `test`: Tests hinzufügen
- `chore`: Build/CI-Änderungen

**Beispiele:**
```
feat(player): Add subtitle support
fix(api): Handle network timeout correctly
docs(readme): Update installation instructions
test(repository): Add unit tests for IptvRepository
```

### 4. Push und PR erstellen

```bash
# Branch pushen
git push origin feature/my-feature

# Pull Request erstellen
# Gehen Sie zu GitHub und klicken Sie auf "New Pull Request"
```

### 5. PR-Beschreibung

Verwenden Sie diese Vorlage:

```markdown
## Beschreibung
Kurze Beschreibung der Änderungen.

## Änderungen
- [ ] Feature X hinzugefügt
- [ ] Bug Y behoben
- [ ] Dokumentation aktualisiert

## Tests
- [ ] Unit-Tests hinzugefügt
- [ ] UI-Tests hinzugefügt
- [ ] Alle Tests bestanden

## Screenshots (falls zutreffend)
[Screenshot einfügen]

## Checklist
- [ ] Code folgt den Projektrichtlinien
- [ ] Selbst-Review durchgeführt
- [ ] Dokumentation aktualisiert
- [ ] Keine Breaking Changes (oder dokumentiert)
```

---

## Bug-Reports

### Bug-Report-Vorlage

```markdown
## Bug-Beschreibung
Klare, präzise Beschreibung des Bugs.

## Schritte zur Reproduktion
1. Gehe zu '...'
2. Klicke auf '...'
3. Scrolle nach unten
4. Fehler erscheint

## Erwartetes Verhalten
Was sollte passieren.

## Tatsächliches Verhalten
Was tatsächlich passiert.

## Screenshots
[Falls zutreffend]

## Umgebung
- Gerät: [z.B. Samsung Galaxy S21]
- Android-Version: [z.B. Android 14]
- App-Version: [z.B. 1.0.0]

## Zusätzlicher Kontext
Weitere Informationen, die hilfreich sein könnten.
```

---

## Feature-Requests

### Feature-Request-Vorlage

```markdown
## Problem
Welches Problem löst dieses Feature?

## Vorgeschlagene Lösung
Wie sollte das Feature funktionieren?

## Alternativen
Welche Alternativen haben Sie in Betracht gezogen?

## Zusätzlicher Kontext
Weitere Informationen, Mockups, etc.
```

---

## Kommunikation

### Kanäle

- **GitHub Issues** - Für Bug-Reports und Feature-Requests
- **GitHub Discussions** - Für Fragen und Diskussionen
- **Pull Requests** - Für Code-Reviews

### Etikett

- **Seien Sie respektvoll** - Freundlicher Umgang miteinander
- **Seien Sie präzise** - Klare Beschreibungen
- **Seien Sie geduldig** - Reviews brauchen Zeit

### Code-Reviews

- **Konstruktives Feedback** - Verbesserungsvorschläge machen
- **Erklären** - Warum eine Änderung nötig ist
- **Loben** - Gute Arbeit anerkennen

---

## Lizenz

Durch Ihren Beitrag stimmen Sie zu, dass Ihr Code unter der [MIT License](LICENSE) lizenziert wird.

---

## Fragen?

Bei Fragen erstellen Sie ein [GitHub Issue](https://github.com/yourusername/nativeplayer/issues) oder starten Sie eine [Diskussion](https://github.com/yourusername/nativeplayer/discussions).

---

**Vielen Dank für Ihren Beitrag!**
