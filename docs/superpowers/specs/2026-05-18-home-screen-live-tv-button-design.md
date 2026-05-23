# Design: Home Screen Live TV Button

## Zusammenfassung

Erweiterung der CategoryBar im HomeScreen um eine neue Kategorie "Live TV", die direkten Zugang zur Live TV Screen ermöglicht. Die bestehende Kategorie "Alle" bleibt unverändert.

## Aktueller Zustand

- **CategoryBar** zeigt 5 Kategorien: `Alle | Sports | Neu | Beliebt | Kategorien`
- **Alle** zeigt den gesamten Homescreen-Inhalt (Quick Access, Sports, Weiterschauen, Empfehlungen)
- **Screen.Live** existiert bereits für die Live TV Screen

## Geplante Änderungen

### 1. CategoryBar.kt

**Änderung:** Neues Enum-Element `LIVE_TV("Live TV")` hinzufügen

```kotlin
enum class HomeCategory(val label: String) {
    ALLE("Alle"),
    LIVE_TV("Live TV"),      // NEU
    SPORTS("Sports"),
    NEU("Neu"),
    BELIEBT("Beliebt"),
    KATEGORIEN("Kategorien")
}
```

**Reihenfolge:** `Alle | Live TV | Sports | Neu | Beliebt | Kategorien`

### 2. HomeScreen.kt

**Änderung:** Neue Kategorie `LIVE_TV` im `when`-Block behandeln

```kotlin
when (selectedCategory) {
    HomeCategory.ALLE -> {
        // Bestehender Inhalt unverändert
    }
    HomeCategory.LIVE_TV -> {
        // Navigation zur Live TV Screen
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Live.route)
        }
    }
    HomeCategory.SPORTS -> { /* ... */ }
    HomeCategory.NEU -> { /* ... */ }
    HomeCategory.BELIEBT -> { /* ... */ }
    HomeCategory.KATEGORIEN -> { /* ... */ }
}
```

## Verhaltensspezifikation

1. **Button-Text:** "Live TV"
2. **Navigation:** Bei Klick → Navigiere zu `Screen.Live.route`
3. **Visuelles Feedback:** Gleicher Stil wie andere Kategorien (Gold bei Auswahl)
4. **Standard-Zustand:** "Alle" bleibt als Standard-Auswahl

## Betroffene Dateien

| Datei | Änderung |
|-------|----------|
| `app/src/main/java/com/nativeplayer/ui/home/CategoryBar.kt` | Neues Enum-Element |
| `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt` | Neue Kategorie im when-Block |

## Testfälle

1. ✅ "Live TV"-Button erscheint in der CategoryBar
2. ✅ Klick auf "Live TV" navigiert zur Live TV Screen
3. ✅ "Alle"-Button funktioniert unverändert
4. ✅ Visuelles Feedback bei Auswahl von "Live TV"

## Risiken

- **Gering:** Nur minimale Änderungen an bestehender Struktur
- **Keine Breaking Changes:** Bestehende Funktionalität bleibt erhalten

## Zeitplan

- **Implementierung:** 15 Minuten
- **Testing:** 5 Minuten
- **Gesamt:** 20 Minuten
