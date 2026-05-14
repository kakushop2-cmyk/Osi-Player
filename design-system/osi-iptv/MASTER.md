# Design System Master File — OSI IPTV

> **LOGIC:** When building a specific page, first check `design-system/pages/[page-name].md`.
> If that file exists, its rules **override** this Master file.
> If not, strictly follow the rules below.

---

**Project:** OSI IPTV
**Generated:** 2026-05-12 03:20:11
**Updated:** 2026-05-12 — User selected: Warm Minimal + Gold Accent
**Category:** Video Streaming/OTT — Native Android IPTV Player

---

## Global Rules

### Color Palette (Warm Dark)

| Role | Hex | Compose Name | Usage |
|------|-----|-------------|-------|
| Background | `#0D0D0B` | `WarmBackground` | App-Hintergrund (statt reinem #000 für OLED-friendly) |
| Surface | `#1A1A17` | `WarmSurface` | Cards, Sheets, Surface-Container |
| Surface Elevated | `#252522` | `WarmSurfaceElevated` | Hover/Elevated Cards, Dialogs |
| Accent Gold | `#CCA770` | `WarmGoldAccent` | Primärer CTA, Tabs, Fortschritt, Favoriten-Herz aktiv |
| Accent Gold Dim | `rgba(204,167,112,0.12)` | `WarmGoldAccentDim` | Dezente Gold-Hintergründe |
| Text Primary | `#E8E3D7` | `WarmTextPrimary` | Haupttext auf dunklen Flächen |
| Text Secondary | `#8A857C` | `WarmTextSecondary` | Sekundär-/Muted-Text |
| Text On Accent | `#0D0D0B` | `WarmTextOnGold` | Text auf Gold-Buttons |
| Error | `#E05555` | `WarmError` | Fehlerzustände (warmes Rot) |
| Star | `#CCA770` | `WarmGoldStar` | Bewertungsstern-Farbe (wie Accent) |
| Border | `rgba(232,227,215,0.08)` | `WarmBorder` | Subtle Card Borders |
| Progress Track | `#252522` | `WarmProgressTrack` | Progress-Bar Hintergrund |

**Color Notes:** Kein reines Weiß — alle Textfarben sind warm abgetönt. Gold-Akzent statt kaltem Blau/Rot für Premium-Video-Feeling.

### Typography — Inter

- **Font:** Inter (Google Fonts, Android Downloadable Fonts)
- **Fallback:** System Sans-Serif
- **Single-Family Precision:** Inter 700 für Display/Headlines, Inter 600 für Sub-Headlines, Inter 400/500 für Body

| Role | Weight | Size | Letter Spacing | Use |
|------|--------|------|----------------|-----|
| Display Large | 700 | 28sp | -0.5px | Film-/Serientitel in Detail-Views |
| Headline | 600 | 20sp | -0.3px | Screen-Titel, Kategorie-Überschriften |
| Title | 500 | 16sp | 0 | Card-Titel, List-Item Primary |
| Body | 400 | 14sp | 0 | Beschreibungstexte, Plot |
| Label | 500 | 11sp | +0.5px | Uppercase Labels (Kategorien, Badges) |
| Caption | 400 | 10sp | 0 | Metadaten (Jahr, Dauer, Sender) |

### Spacing (8dp Grid)

| Token | Value | Usage |
|-------|-------|-------|
| xs | 4dp | Icon-Padding, Micro-Gaps |
| sm | 8dp | Innenabstand Cards, Tab-Padding |
| md | 16dp | Standard Screen-Padding, Card-Gaps |
| lg | 24dp | Section-Abstände, Header-Padding |
| xl | 32dp | Große Section-Abstände |
| 2xl | 48dp | Hero/Splash-Padding |

### Corner Radius

| Token | Value | Usage |
|-------|-------|-------|
| `sm` | 6dp | Chips, Badges, kleine Buttons |
| `md` | 10dp | Cards, Input-Felder |
| `lg` | 14dp | Dialogs, Bottom Sheets, Player-Overlay |

### Elevation / Shadows

| Level | Compose Elevation | Usage |
|-------|-------------------|-------|
| Resting | 0dp | Background, Text |
| Raised | 2dp | Cards |
| Overlay | 8dp | Dialogs, Player-Controls |

### Animation

| Property | Value |
|----------|-------|
| Micro-interaction | 150–300ms |
| Easing | FastOutSlowInEasing (Material default) |
| Content transition | Crossfade (AnimatedContent) |
| Loading | Skeleton-Shimmer statt CircularProgressIndicator |
| Tab indicator | animateColorAsState + animateDpAsState |

---

## Component Specs

### Buttons

**Primary (Gold):**
- Background: `WarmGoldAccent`, Text: `WarmTextOnGold`
- Padding: 12dp vertical, 24dp horizontal
- Corner: `md` (10dp)
- Shadow: 0 2dp 16dp `rgba(204,167,112,0.2)`

**Secondary (Outlined):**
- Border: `rgba(232,227,215,0.15)`, Text: `WarmGoldAccent`
- Same padding/corner as primary
- No shadow

### Cards

- Background: `WarmSurface`
- Corner: `md` (10dp)
- Border: `1dp` `WarmBorder` (hairline)
- Padding: 12dp
- Inner content elevation: 0dp

### Input Fields

- Background: `WarmSurfaceElevated`
- Corner: `md` (10dp)
- Border (idle): transparent
- Border (focus): `WarmGoldAccent` 1.5dp
- Padding: 12dp horizontal, 14dp vertical

### Progress Bars

- Track: `WarmProgressTrack` (#252522)
- Indicator: `WarmGoldAccent` (#CCA770)
- Height: 4dp, Corner: 2dp

### Player Overlay

- Background: `rgba(13,13,11,0.85)` + `blur(20dp)` (via RenderScript/BlendMode)
- Corner: `lg` (14dp)
- Auto-hide: 5 Sekunden nach letzter Interaktion
- Seekbar: Wie Progress-Bar + 12dp Thumb mit Glow

---

## Style Guidelines

**Style:** Warm Minimal Dark (OLED-optimiert)

**Keywords:** Dark theme, warm tones, gold accent, content-first, premium, eye-friendly, OLED, entertainment

**Key Effects:**
- Minimal gold glow auf primären Elementen
- Skeleton-Loading für Content-Platzhalter
- Crossfade für Content-Wechsel
- Subtitle goldene Tab-Indikatoren
- Weiche Card-Shadows

---

## Anti-Patterns (Do NOT Use)

- ❌ Reines #000000 (verwende #0D0D0B)
- ❌ Reines #FFFFFF Text (verwende #E8E3D7)
- ❌ Blauer Akzent (altes #3D8BFD)
- ❌ Harter weißer Spinner auf dunklem Grund
- ❌ Keine Semantik-Tokens (rohe Hex in Komponenten)
- ❌ Fehlende Loading-Zustände
- ❌ Inkonsistente Corner-Radii
