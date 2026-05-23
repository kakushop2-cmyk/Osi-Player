# Hero Trailer Carousel — Design Spec

## Ziel
Die Hero-Trailer-Logik auf dem Homescreen ändern: Statt nur den zuletzt hinzugefügten Film anzuzeigen, wird ein Swipe-Karussell mit zufälligen Filmen aus dem gesamten Katalog implementiert. Nutzer können manuell zwischen Filmen swipen. Optional: Auto-Rotation alle 2 Minuten.

## Status
- **Erstellt:** 2026-05-18
- **Genehmigt:** Ja
- **Ansatz:** B — Manuelle Rotation mit Swipe-Indikator

## Betroffene Dateien

| Datei | Änderung |
|-------|----------|
| `app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt` | `latestContent` → `heroMovies` (zufällig aus ALLEN Filmen), `selectedHeroIndex` State |
| `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt` | HorizontalPager + PaginationDots ersetzen einzelnen HeroVideoPlayer |
| `app/src/main/java/com/nativeplayer/ui/home/HeroVideoPlayer.kt` | Keine großen Änderungen — bleibt eigenständiges Composable |

## Architektur

```
HomeViewModel
  ├── heroMovies: StateFlow<List<Movie>>    (zufällig 10 aus allen Filmen)
  ├── selectedHeroIndex: MutableState<Int>  (aktueller Index, Start 0)
  └── Auto-Rotation Timer (2 Min) → Index++

HomeScreen
  └── LazyColumn item("hero")
      ├── HorizontalPager (Foundation)
      │   ├── [Index]: HeroVideoPlayer (spielt ab)
      │   ├── [Index+1]: HeroVideoPlayer (pausiert)
      │   └── [Index-1]: HeroVideoPlayer (pausiert)
      └── PaginationDots (Row, zentriert)
          ● ○ ○ ○ ○
```

## Datenfluss

1. **HomeViewModel** pollt alle 5s, mischt ALLE Filme zufällig, nimmt Top 10
2. **HomeScreen** liest `heroMovies` und `selectedHeroIndex`
3. **HorizontalPager** rendert 3 pages (current-1, current, current+1), nur aktive spielt Video
4. **PaginationDots** zeigt aktuelle Position

## UI/UX Spezifikation

### Pagination-Dots
- **Position:** Zentriert unter dem Hero-Video
- **Stil:** 8dp Durchmesser
- **Aktiv:** Gold/Primärfarbe (hex #FFD700 oder App-Primärfarbe)
- **Inaktiv:** Grau (#999999) mit 50% Opacity
- **Abstand:** 6dp zwischen Dots
- **Max:** 10 Dots sichtbar (einer pro Film)

### Swipe-Verhalten
- **Horizontal swipe** → Wechselt zum nächsten/vorherigen Film
- **Animation:** Smooth snap, kein rubber-band
- **Timer-Reset:** Bei Swipe wird Auto-Rotation Timer resettet

### Auto-Rotation
- **Intervall:** 2 Minuten (120.000ms)
- **Start:** Nach erstem Laden oder nach Nutzer-Swipe
- **Deaktiviert:** Wenn Nutzer aktiv swiped (Reset auf 2 Min)
- **Wrap-around:** Index 9 → 0

### Video-Playback
- **Nur aktive Page** spielt Video ab
- **Wechsel:** Aktueller Player pausiert, neuer startet nach 300ms Delay
- **60s Clip-Limit** bleibt bestehen
- **Poster-Fallback** bleibt bestehen

## Edge Cases

| Fall | Lösung |
|------|--------|
| Nur 1 Film verfügbar | Kein Swipe möglich, keine Dots |
| 0 Filme verfügbar | Poster-Fallback oder EmptyState |
| ExoPlayer Fehler | Poster-Fallback (bestehende Logik) |
| Nutzer scrollt vertikal | Hero-Video pausiert bei vertikalem Scroll |
| App kommt aus Background | ExoPlayer wird neu initialisiert |

## Nicht im Scope
- ViewPager2 (Compose Foundation HorizontalPager reicht)
- Mehrere gleichzeitige ExoPlayer (nur eine Page spielt)
- Download/Offline-Modus für Hero-Filme
- Nutzer-Präferenzen für Karussell-Inhalt
