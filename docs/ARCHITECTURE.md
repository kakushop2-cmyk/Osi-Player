# Architecture Decision Records (ADR)

Diese Dokumentation enthält die wichtigsten Architektur-Entscheidungen des NativePlayer-Projekts.

---

## ADR-001: Jetpack Compose als UI-Framework

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für die Benutzeroberfläche musste ein UI-Framework gewählt werden. Zur Auswahl standen:
- XML-basierte Android Views
- Jetpack Compose
- Cross-Platform-Lösungen (Flutter, React Native)

### Entscheidung

**Jetpack Compose** wurde gewählt.

### Begründung

| Kriterium | XML Views | Jetpack Compose | Flutter |
|-----------|-----------|-----------------|---------|
| **Moderne API** | ❌ Veraltet | ✅ Deklarativ | ✅ Deklarativ |
| **Android-native** | ✅ Ja | ✅ Ja | ❌ Nein |
| **Performance** | ✅ Gut | ✅ Gut | ⚠️ Overhead |
| **Lernkurve** | ✅ Bekannt | ⚠️ Neu | ❌ Neues Framework |
| **Material3** | ⚠️ Manuelles Update | ✅ Integriert | ❌ Eigenes System |
| **TV-Support** | ✅ Leanback | ✅ Compose TV | ❌ Begrenzt |

### Konsequenzen

**Positiv:**
- Moderne, deklarative UI-Entwicklung
- Bessere Performance durch Compose-Runtime
- Einheitliches Designsystem mit Material3
- Einfacheres State-Management

**Negativ:**
- Lernkurve für das Team
- Noch nicht alle Bibliotheken Compose-kompatibel
- Größere APK-Größe

---

## ADR-002: Hilt für Dependency Injection

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für Dependency Injection standen zur Verfügung:
- Manuelles DI
- Koin
- Dagger 2
- Hilt

### Entscheidung

**Hilt** wurde gewählt.

### Begründung

| Kriterium | Manuelles DI | Koin | Dagger 2 | Hilt |
|-----------|--------------|------|----------|------|
| **Compile-Time** | ❌ Nein | ❌ Nein | ✅ Ja | ✅ Ja |
| **Android-Integration** | ❌ Manuelles Setup | ⚠️ Gut | ⚠️ Manuelles Setup | ✅ Exzellent |
| **Compose-Support** | ❌ Manuelles Setup | ✅ Ja | ⚠️ Manuelles Setup | ✅ Integriert |
| **Testbarkeit** | ✅ Gut | ✅ Gut | ✅ Gut | ✅ Exzellent |
| **Wartbarkeit** | ❌ Skaliert schlecht | ✅ Gut | ⚠️ Boilerplate | ✅ Gut |

### Konsequenzen

**Positiv:**
- Compile-Time-Sicherheit
- Nahtlose Android-Integration
- Einfaches Testing mit `@HiltAndroidTest`
- Reduzierter Boilerplate-Code

**Negativ:**
- Kapt-Compiler (langsamer als KSP)
- Lernkurve für Dagger-Konzepte

---

## ADR-003: Retrofit + OkHttp für Netzwerkzugriff

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für API-Kommunikation standen zur Verfügung:
- HttpURLConnection
- OkHttp
- Retrofit
- Ktor Client

### Entscheidung

**Retrofit + OkHttp** wurde gewählt.

### Begründung

| Kriterium | HttpURLConnection | OkHttp | Retrofit | Ktor |
|-----------|-------------------|--------|----------|------|
| **Type-Safety** | ❌ Nein | ❌ Nein | ✅ Ja | ✅ Ja |
| **Code-Generation** | ❌ Manuelles Parsen | ❌ Manuelles Parsen | ✅ Automatisch | ⚠️ Manuelles Setup |
| **Caching** | ❌ Manuelles Setup | ✅ Integriert | ✅ Über OkHttp | ❌ Manuelles Setup |
| **Interceptors** | ❌ Nein | ✅ Ja | ✅ Ja | ✅ Ja |
| **Android-Standard** | ⚠️ Veraltet | ✅ Ja | ✅ Ja | ⚠️ Neuer |

### Konsequenzen

**Positiv:**
- Type-safe API-Definition mit Annotations
- Automatisches JSON-Parsen mit Gson
- Mächtige Interceptor-API für Logging, Auth
- Breite Community-Unterstützung

**Negativ:**
- Zwei Bibliotheken (Retrofit + OkHttp)
- Gson statt modernerem Moshi/Kotlinx.serialization

---

## ADR-004: Room für lokale Datenspeicherung

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für lokale Datenbank standen zur Verfügung:
- SQLite direkt
- Room
- Realm
- SQLDelight

### Entscheidung

**Room** wurde gewählt.

### Begründung

| Kriterium | SQLite | Room | Realm | SQLDelight |
|-----------|--------|------|-------|------------|
| **Compile-Time-Checks** | ❌ Nein | ✅ Ja | ✅ Ja | ✅ Ja |
| **Android-Integration** | ⚠️ Manuelles Setup | ✅ Exzellent | ⚠️ Eigenes SDK | ⚠️ Manuelles Setup |
| **LiveData/Flow-Support** | ❌ Nein | ✅ Ja | ✅ Ja | ✅ Ja |
| **Migration** | ❌ Manuelles SQL | ✅ Automatisch | ✅ Automatisch | ✅ Automatisch |
| **Größe** | ✅ Minimal | ✅ Klein | ❌ Groß | ✅ Klein |

### Konsequenzen

**Positiv:**
- Compile-Time-Validierung von SQL-Queries
- Nahtlose Integration mit Coroutines/Flow
- Automatische Migrationen
- Offizielle Android-Bibliothek

**Negativ:**
- Eingeschränkte Flexibilität bei komplexen Queries
- Kapt-Compiler benötigt

---

## ADR-005: ExoPlayer (Media3) für Video-Wiedergabe

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für Video-Wiedergabe standen zur Verfügung:
- MediaPlayer (Android Standard)
- ExoPlayer / Media3
- VLC Library
- IJKPlayer

### Entscheidung

**Media3 ExoPlayer** wurde gewählt.

### Begründung

| Kriterium | MediaPlayer | Media3 | VLC | IJKPlayer |
|-----------|-------------|--------|-----|-----------|
| **HLS-Support** | ⚠️ Begrenzt | ✅ Exzellent | ✅ Exzellent | ✅ Gut |
| **Format-Support** | ⚠️ Begrenzt | ✅ Erweiterbar | ✅ Exzellent | ✅ Gut |
| **Android-Integration** | ✅ Standard | ✅ Exzellent | ⚠️ Externes SDK | ⚠️ Externes SDK |
| **Wartung** | ❌ Veraltet | ✅ Aktiv | ✅ Aktiv | ⚠️ Community |
| **Größe** | ✅ Minimal | ✅ Klein | ❌ Groß | ⚠️ Mittel |

### Konsequenzen

**Positiv:**
- Exzellenter HLS-Support für IPTV
- Erweiterbar durch Plugins
- Offizielle Android-Bibliothek
- Gute Performance

**Negativ:**
- Eingeschränkte DRM-Unterstützung in der Open-Source-Version

---

## ADR-006: Repository-Pattern für Datenschicht

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Die Datenschicht sollte:
- Testbar sein
- Klare Trennung von UI und Daten
- Mehrere Datenquellen unterstützen (API, DB, Cache)

### Entscheidung

**Repository-Pattern** wurde gewählt.

### Implementierung

```
UI (Compose) → ViewModel → Repository → API (Retrofit)
                                   → Database (Room)
                                   → Cache (In-Memory)
```

### Konsequenzen

**Positiv:**
- Single Source of Truth
- Einfaches Umschalten zwischen Datenquellen
- Testbar durch Dependency Injection
- Klare Verantwortlichkeiten

**Negativ:**
- Mehr Code für einfache Operationen
- Boilerplate für Repository-Interfaces

---

## ADR-007: MVVM als Architektur-Pattern

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für die App-Architektur standen zur Verfügung:
- MVC
- MVP
- MVVM
- MVI

### Entscheidung

**MVVM (Model-View-ViewModel)** wurde gewählt.

### Begründung

| Kriterium | MVC | MVP | MVVM | MVI |
|-----------|-----|-----|------|-----|
| **Testbarkeit** | ❌ Schlecht | ✅ Gut | ✅ Gut | ✅ Exzellent |
| **State-Management** | ❌ Unstrukturiert | ⚠️ Manuelles Setup | ✅ StateFlow | ✅ Unidirectional |
| **Compose-Support** | ❌ Nicht passend | ⚠️ Manuelles Setup | ✅ Perfekt | ✅ Gut |
| **Komplexität** | ✅ Einfach | ⚠️ Mittel | ⚠️ Mittel | ❌ Hoch |

### Konsequenzen

**Positiv:**
- Natürliche Integration mit Compose
- StateFlow für reaktive UI
- Einfaches Testing
- Gute Trennung der Concerns

**Negativ:**
- ViewModel kann groß werden bei komplexen Screens
- State-Management erfordert Disziplin

---

## ADR-008: Coroutines für asynchrone Operationen

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für asynchrone Operationen standen zur Verfügung:
- AsyncTask (veraltet)
- RxJava
- Coroutines

### Entscheidung

**Kotlin Coroutines** wurde gewählt.

### Begründung

| Kriterium | AsyncTask | RxJava | Coroutines |
|-----------|-----------|--------|------------|
| **Modern** | ❌ Veraltet | ⚠️ Legacy | ✅ Modern |
| **Lernkurve** | ✅ Einfach | ❌ Steil | ✅ Moderat |
| **Performance** | ⚠️ Mittel | ✅ Gut | ✅ Exzellent |
| **Compose-Integration** | ❌ Nein | ⚠️ Manuelles Setup | ✅ Native |

### Konsequenzen

**Positiv:**
- Native Kotlin-Support
- Einfache Fehlerbehandlung mit try/catch
- Strukturierte Concurrency
- Perfekte Compose-Integration

**Negativ:**
- Debugging kann komplex sein
- Coroutine-Scope-Management erfordert Sorgfalt

---

## ADR-009: Coil für Bildladung

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für Bildladung standen zur Verfügung:
- BitmapFactory (manuell)
- Glide
- Picasso
- Coil

### Entscheidung

**Coil** wurde gewählt.

### Begründung

| Kriterium | BitmapFactory | Glide | Picasso | Coil |
|-----------|---------------|-------|---------|------|
| **Compose-Support** | ❌ Manuelles Setup | ⚠️ Extension | ⚠️ Extension | ✅ Native |
| **Kotlin-First** | ❌ Nein | ⚠️ Java-First | ⚠️ Java-First | ✅ Ja |
| **Größe** | ✅ Minimal | ❌ Groß | ⚠️ Mittel | ✅ Klein |
| **Performance** | ⚠️ Manuelles Caching | ✅ Gut | ✅ Gut | ✅ Gut |

### Konsequenzen

**Positiv:**
- Native Compose-Integration
- Kotlin-First-Design
- Kleinste Bibliothek in seiner Klasse
- Automatisches Caching

**Negativ:**
- Kleinere Community als Glide/Picasso

---

## ADR-010: M3U-Parser für Playlist-Support

**Status:** Akzeptiert  
**Datum:** 2024-01-01  
**Entscheidender:** Entwicklungsteam

### Kontext

Für M3U-Playlist-Parsing standen zur Verfügung:
- Externe Bibliothek
- Eigenentwicklung

### Entscheidung

**Eigenentwicklung** wurde gewählt.

### Begründung

- M3U-Format ist relativ einfach
- Volle Kontrolle über Parsing-Logik
- Keine externen Abhängigkeiten
- Leichtgewichtig

### Konsequenzen

**Positiv:**
- Keine externen Abhängigkeiten
- Volle Kontrolle
- Kleinste Lösung

**Negativ:**
- Muss selbst gewartet werden
- Edge Cases müssen selbst behandelt werden

---

## Zusammenfassung

| Entscheidung | Technologie | Grund |
|--------------|-------------|-------|
| UI-Framework | Jetpack Compose | Modern, deklarativ, Material3 |
| Dependency Injection | Hilt | Compile-Time-Sicherheit, Android-Integration |
| Netzwerk | Retrofit + OkHttp | Type-Safety, Code-Generation |
| Datenbank | Room | Compile-Time-Checks, Flow-Support |
| Video-Player | Media3 ExoPlayer | HLS-Support, Android-Integration |
| Architektur-Pattern | MVVM | Compose-Integration, Testbarkeit |
| Asynchronität | Coroutines | Native Kotlin-Support |
| Bildladung | Coil | Compose-Integration, Kotlin-First |
| Playlist-Parsing | Eigenentwicklung | Volle Kontrolle, keine Abhängigkeiten |

---

*Letzte Aktualisierung: Januar 2024*
