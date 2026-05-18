# API-Dokumentation - NativePlayer

Vollständige Referenz für die Xtream-Codes-API-Integration in NativePlayer.

---

## Inhaltsverzeichnis

1. [Übersicht](#übersicht)
2. [Authentifizierung](#authentifizierung)
3. [Live-TV-Endpunkte](#live-tv-endpunkte)
4. [VOD-Endpunkte (Filme)](#vod-endpunkte-filme)
5. [Serien-Endpunkte](#serien-endpunkte)
6. [EPG-Endpunkte](#epg-endpunkte)
7. [Fehlerbehandlung](#fehlerbehandlung)
8. [Rate-Limiting](#rate-limiting)
9. [Code-Beispiele](#code-beispiele)

---

## Übersicht

### Basis-URL

Alle API-Aufrufe verwenden die Basis-URL Ihres IPTV-Anbieters:

```
https://your-provider.com/player_api.php
```

### Protokoll

- **HTTP/HTTPS** - Beide unterstützt
- **Format** - JSON (Standard)
- **Encoding** - UTF-8

### Allgemeiner Request-Aufbau

```
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=ACTION_NAME
```

| Parameter | Typ | Beschreibung |
|-----------|-----|--------------|
| `username` | string | Benutzername des IPTV-Kontos |
| `password` | string | Passwort des IPTV-Kontos |
| `action` | string | API-Aktion (siehe Endpunkte) |

---

## Authentifizierung

### Login / Konto-Status prüfen

Authentifiziert den Benutzer und gibt Kontoinformationen zurück.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS
```

**Response:**
```json
{
  "user_info": {
    "username": "your_username",
    "password": "your_password",
    "auth": 1,
    "status": "Active",
    "exp_date": "2025-12-31 23:59:59",
    "is_trial": "0",
    "active_cons": "1",
    "created_at": "2024-01-01 00:00:00",
    "max_connections": "2",
    "allowed_output_formats": ["ts", "m3u8"]
  },
  "server_info": {
    "url": "your-provider.com",
    "port": "80",
    "https_port": "443",
    "server_protocol": "http",
    "rtmp_port": "1935",
    "timezone": "Europe/Berlin",
    "timestamp_now": 1704067200,
    "time_now": "2024-01-01 00:00:00"
  }
}
```

**Felder:**

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `auth` | int | `1` = Erfolgreich, `0` = Fehlgeschlagen |
| `status` | string | Account-Status: `Active`, `Disabled`, `Banned`, `Expired` |
| `exp_date` | string | Ablaufdatum des Kontos |
| `is_trial` | string | `1` = Testkonto, `0` = Vollversion |
| `active_cons` | string | Anzahl aktiver Verbindungen |
| `max_connections` | string | Maximale gleichzeitige Verbindungen |
| `allowed_output_formats` | array | Erlaubte Stream-Formate (z.B. `ts`, `m3u8`) |

---

## Live-TV-Endpunkte

### Live-Kategorien abrufen

Listet alle verfügbaren Live-TV-Kategorien auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_live_categories
```

**Response:**
```json
[
  {
    "category_id": "1",
    "category_name": "Deutschland",
    "parent_id": 0
  },
  {
    "category_id": "2",
    "category_name": "Sport",
    "parent_id": 0
  }
]
```

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `category_id` | string | Eindeutige Kategorie-ID |
| `category_name` | string | Name der Kategorie |
| `parent_id` | int | Übergeordnete Kategorie (0 = Root) |

---

### Live-Sender abrufen

Listet alle verfügbaren Live-TV-Sender auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_live_streams
```

**Optional: Nach Kategorie filtern:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_live_streams&category_id=1
```

**Response:**
```json
[
  {
    "num": 1,
    "name": "ARD HD",
    "stream_type": "live",
    "stream_id": 12345,
    "stream_icon": "https://example.com/logos/ard.png",
    "epg_channel_id": "ard.de",
    "added": "2024-01-01 00:00:00",
    "category_id": "1",
    "category_name": "Deutschland",
    "custom_sid": "",
    "tv_archive": 1,
    "direct_source": "",
    "tv_archive_duration": 7
  }
]
```

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `stream_id` | int | Eindeutige Stream-ID |
| `name` | string | Sendername |
| `stream_icon` | string | URL zum Senderlogo |
| `epg_channel_id` | string | EPG-Kanal-ID für TV-Programm |
| `category_id` | string | Kategorie-ID |
| `tv_archive` | int | `1` = Timeshift verfügbar, `0` = nicht verfügbar |
| `tv_archive_duration` | int | Archivdauer in Tagen |

**Stream-URL generieren:**
```
{baseUrl}/live/{username}/{password}/{streamId}.ts
```

Beispiel:
```
https://your-provider.com/live/your_user/your_pass/12345.ts
```

---

## VOD-Endpunkte (Filme)

### VOD-Kategorien abrufen

Listet alle verfügbaren Filmkategorien auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_vod_categories
```

**Response:**
```json
[
  {
    "category_id": "3",
    "category_name": "Action",
    "parent_id": 0
  },
  {
    "category_id": "4",
    "category_name": "Komödie",
    "parent_id": 0
  }
]
```

---

### VOD-Streams abrufen

Listet alle verfügbaren Filme auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_vod_streams
```

**Optional: Nach Kategorie filtern:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_vod_streams&category_id=3
```

**Response:**
```json
[
  {
    "num": 1,
    "name": "Inception",
    "stream_type": "movie",
    "stream_id": 67890,
    "stream_icon": "https://example.com/posters/inception.jpg",
    "rating": "8.8",
    "rating_5based": 4.4,
    "added": "2024-01-01 00:00:00",
    "plot": "Ein Dieb, der Unternehmensgeheimnisse stiehlt...",
    "cast": "Leonardo DiCaprio, Joseph Gordon-Levitt",
    "director": "Christopher Nolan",
    "genre": "Action, Sci-Fi",
    "releaseDate": "2010-07-16",
    "youtube_trailer": "https://youtube.com/watch?v=...",
    "episode_run_time": "148",
    "category_id": "3",
    "category_name": "Action",
    "container_extension": "mp4"
  }
]
```

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `stream_id` | int | Eindeutige Film-ID |
| `name` | string | Filmtitel |
| `stream_icon` | string | URL zum Poster |
| `rating` | string | Bewertung (0-10) |
| `rating_5based` | double | Bewertung (0-5) |
| `plot` | string | Handlungsbeschreibung |
| `cast` | string | Darsteller |
| `director` | string | Regisseur |
| `genre` | string | Genre(s) |
| `releaseDate` | string | Erscheinungsdatum |
| `container_extension` | string | Dateiformat (mp4, mkv, etc.) |

**Stream-URL generieren:**
```
{baseUrl}/movie/{username}/{password}/{streamId}.{container_extension}
```

Beispiel:
```
https://your-provider.com/movie/your_user/your_pass/67890.mp4
```

---

### VOD-Details abrufen

Ruft detaillierte Informationen zu einem einzelnen Film ab.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_vod_info&vod_id=67890
```

**Response:**
```json
{
  "info": {
    "name": "Inception",
    "title": "Inception",
    "year": "2010",
    "cover": "https://example.com/posters/inception.jpg",
    "plot": "Ein Dieb, der Unternehmensgeheimnisse stiehlt...",
    "cast": "Leonardo DiCaprio, Joseph Gordon-Levitt",
    "director": "Christopher Nolan",
    "genre": "Action, Sci-Fi",
    "rating": "8.8",
    "releaseDate": "2010-07-16",
    "episode_run_time": "148",
    "duration": "2h 28min",
    "bitrate": 5000,
    "youtube_trailer": "https://youtube.com/watch?v=..."
  },
  "movie_data": {
    "stream_id": 67890,
    "name": "Inception",
    "title": "Inception",
    "year": "2010",
    "cover": "https://example.com/posters/inception.jpg",
    "plot": "Ein Dieb, der Unternehmensgeheimnisse stiehlt...",
    "cast": "Leonardo DiCaprio, Joseph Gordon-Levitt",
    "director": "Christopher Nolan",
    "genre": "Action, Sci-Fi",
    "rating": "8.8",
    "releaseDate": "2010-07-16",
    "container_extension": "mp4"
  }
}
```

---

## Serien-Endpunkte

### Serien-Kategorien abrufen

Listet alle verfügbaren Serienkategorien auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_series_categories
```

**Response:**
```json
[
  {
    "category_id": "5",
    "category_name": "Drama",
    "parent_id": 0
  },
  {
    "category_id": "6",
    "category_name": "Sci-Fi",
    "parent_id": 0
  }
]
```

---

### Serien abrufen

Listet alle verfügbaren Serien auf.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_series
```

**Optional: Nach Kategorie filtern:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_series&category_id=5
```

**Response:**
```json
[
  {
    "num": 1,
    "name": "Breaking Bad",
    "series_id": 11111,
    "cover": "https://example.com/covers/breaking-bad.jpg",
    "plot": "Ein Chemielehrer wird zum Drogenproduzenten...",
    "cast": "Bryan Cranston, Aaron Paul",
    "director": "Vince Gilligan",
    "genre": "Drama, Thriller",
    "releaseDate": "2008-01-20",
    "lastModified": "2024-01-01 00:00:00",
    "rating": "9.5",
    "rating_5based": 4.75,
    "backdropPath": ["https://example.com/backdrops/bb1.jpg"],
    "youtubeTrailer": "https://youtube.com/watch?v=...",
    "episodeRunTime": "45",
    "category_id": "5",
    "category_name": "Drama"
  }
]
```

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `series_id` | int | Eindeutige Serien-ID |
| `name` | string | Serientitel |
| `cover` | string | URL zum Cover-Bild |
| `plot` | string | Handlungsbeschreibung |
| `cast` | string | Darsteller |
| `genre` | string | Genre(s) |
| `releaseDate` | string | Erstausstrahlung |
| `rating` | string | Bewertung (0-10) |
| `rating_5based` | double | Bewertung (0-5) |

---

### Serien-Details abrufen

Ruft detaillierte Informationen zu einer Serie ab, einschließlich Staffeln und Episoden.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_series_info&series_id=11111
```

**Response:**
```json
{
  "seasons": [
    {
      "air_date": "2008-01-20",
      "episode_count": 7,
      "id": 3577,
      "name": "Staffel 1",
      "season_number": 1,
      "cover": "https://example.com/covers/bb-s1.jpg",
      "cover_big": "https://example.com/covers/bb-s1-big.jpg",
      "cover_tmdb": "https://image.tmdb.org/..."
    }
  ],
  "info": {
    "name": "Breaking Bad",
    "title": "Breaking Bad",
    "year": "2008",
    "cover": "https://example.com/covers/breaking-bad.jpg",
    "plot": "Ein Chemielehrer wird zum Drogenproduzenten...",
    "cast": "Bryan Cranston, Aaron Paul",
    "director": "Vince Gilligan",
    "genre": "Drama, Thriller",
    "rating": "9.5",
    "releaseDate": "2008-01-20",
    "lastModified": "2024-01-01 00:00:00",
    "episodeRunTime": "45"
  },
  "episodes": {
    "1": [
      {
        "id": "22394",
        "episode_num": 1,
        "title": "Pilot",
        "container_extension": "mp4",
        "info": {
          "tmdb_id": 62085,
          "release_date": "2008-01-20",
          "plot": "Walter White wird mit Lungenkrebs diagnostiziert...",
          "duration_secs": 3600,
          "duration": "1h 0min",
          "movieImage": "https://example.com/episodes/bb-s1e1.jpg",
          "bitrate": 3000,
          "rating": 8.9,
          "season": 1
        },
        "custom_sid": "",
        "added": "2024-01-01 00:00:00",
        "season": 1,
        "direct_source": ""
      }
    ],
    "2": [...]
  }
}
```

**Episoden-URL generieren:**
```
{baseUrl}/series/{username}/{password}/{episodeId}.{container_extension}
```

Beispiel:
```
https://your-provider.com/series/your_user/your_pass/22394.mp4
```

---

## EPG-Endpunkte

### Kurzzeitiges EPG abrufen

Ruft das TV-Programm für einen bestimmten Sender ab.

**Request:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_short_epg&stream_id=12345
```

**Optional: Anzahl der Einträge begrenzen:**
```http
GET /player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_short_epg&stream_id=12345&limit=10
```

**Response:**
```json
{
  "epg_listings": [
    {
      "id": "123456",
      "epg_id": "ard.de",
      "title": "Tagesschau",
      "name": "Tagesschau",
      "lang": "de",
      "start": "2024-01-01 20:00:00",
      "start_timestamp": "1704136800",
      "end": "2024-01-01 20:15:00",
      "stop_timestamp": "1704137700",
      "description": "Nachrichten",
      "desc": "Nachrichten",
      "channel_id": "12345",
      "has_archive": 1
    }
  ]
}
```

| Feld | Typ | Beschreibung |
|------|-----|--------------|
| `id` | string | Eindeutige EPG-ID |
| `epg_id` | string | Kanal-ID |
| `title` | string | Sendungstitel |
| `start` | string | Startzeit (Format: YYYY-MM-DD HH:MM:SS) |
| `start_timestamp` | string | Startzeit (Unix-Timestamp) |
| `end` | string | Endzeit |
| `stop_timestamp` | string | Endzeit (Unix-Timestamp) |
| `description` | string | Sendungsbeschreibung |
| `has_archive` | int | `1` = Archiv verfügbar |

**Hinweis:** Die `title`- und `description`-Felder können Base64-kodiert sein.

---

## Fehlerbehandlung

### HTTP-Statuscodes

| Code | Beschreibung |
|------|--------------|
| `200` | Erfolgreich |
| `401` | Authentifizierung fehlgeschlagen |
| `403` | Zugriff verweigert |
| `404` | Endpunkt nicht gefunden |
| `429` | Rate-Limit erreicht |
| `500` | Serverfehler |

### API-Fehler-Responses

**Authentifizierungsfehler:**
```json
{
  "user_info": {
    "auth": 0,
    "status": "Disabled",
    "message": "Account disabled"
  }
}
```

**Ungültige Parameter:**
```json
{
  "error": "Invalid parameters",
  "message": "username and password are required"
}
```

### Fehlerbehandlung in der App

Die App implementiert folgende Fehlerbehandlung:

1. **Netzwerkfehler** - Retry mit exponential backoff
2. **Authentifizierungsfehler** - Login-Bildschirm anzeigen
3. **Timeout** - Nach 30 Sekunden Timeout
4. **Cache** - Bei API-Fehlern gecachte Daten verwenden

---

## Rate-Limiting

### Beschränkungen

Die meisten IPTV-Anbieter haben folgende Limits:

| Limit | Typischer Wert |
|-------|----------------|
| Requests pro Minute | 60-120 |
| Gleichzeitige Streams | 1-3 (je nach Paket) |
| EPG-Abfragen | 10 pro Minute |

### Best Practices

1. **Caching** - Daten lokal cachen
2. **Lazy Loading** - Nur bei Bedarf laden
3. **Batching** - Mehrere Abfragen kombinieren
4. **Retry** - Bei 429 warten und erneut versuchen

---

## Code-Beispiele

### Kotlin - Authentifizierung mit Retrofit

```kotlin
interface XtreamApi {
    @GET("player_api.php")
    suspend fun authenticate(
        @Query("username") username: String,
        @Query("password") password: String
    ): XtreamLoginResponse
}

// Verwendung
val api = Retrofit.Builder()
    .baseUrl("https://your-provider.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(XtreamApi::class.java)

val response = api.authenticate("your_user", "your_pass")
if (response.userInfo.auth == 1) {
    // Erfolgreich authentifiziert
    println("Status: ${response.userInfo.status}")
    println("Ablauf: ${response.userInfo.expDate}")
}
```

### Kotlin - Live-Sender laden

```kotlin
@GET("player_api.php")
suspend fun getLiveStreams(
    @Query("username") username: String,
    @Query("password") password: String,
    @Query("action") action: String = "get_live_streams"
): List<XtreamLiveStream>

// Verwendung
val streams = api.getLiveStreams(username, password)
streams.forEach { stream ->
    println("${stream.name} (ID: ${stream.streamId})")
    
    // Stream-URL generieren
    val streamUrl = "$baseUrl/live/$username/$password/${stream.streamId}.ts"
}
```

### Kotlin - EPG laden

```kotlin
@GET("player_api.php")
suspend fun getShortEpg(
    @Query("username") username: String,
    @Query("password") password: String,
    @Query("action") action: String = "get_short_epg",
    @Query("stream_id") streamId: Int
): XtreamEpgResponse

// Verwendung
val epg = api.getShortEpg(username, password, streamId = 12345)
epg.epgListings.forEach { listing ->
    val title = decodeBase64(listing.title ?: "")
    println("$title: ${listing.start} - ${listing.end}")
}

// Base64-Dekodierung
fun decodeBase64(text: String): String {
    return try {
        val decoded = Base64.decode(text, Base64.DEFAULT)
        String(decoded, Charsets.UTF_8)
    } catch (e: Exception) {
        text
    }
}
```

### Kotlin - EPG-Timestamp parsen

```kotlin
fun parseEpgTimestamp(timestampStr: String?, dateStr: String?): Long? {
    // Unix-Timestamp (Sekunden oder Millisekunden)
    timestampStr?.toLongOrNull()?.let { ts ->
        return if (ts < 1_000_000_000_000L) ts * 1000L else ts
    }
    
    // Fallback: Datum-String parsen
    dateStr?.let { date ->
        try {
            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            format.timeZone = TimeZone.getTimeZone("UTC")
            return format.parse(date)?.time
        } catch (e: Exception) { }
    }
    
    return null
}
```

### cURL-Beispiele

```bash
# Authentifizierung
curl "https://your-provider.com/player_api.php?username=YOUR_USER&password=YOUR_PASS"

# Live-Sender
curl "https://your-provider.com/player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_live_streams"

# Filme
curl "https://your-provider.com/player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_vod_streams"

# Serien
curl "https://your-provider.com/player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_series"

# EPG
curl "https://your-provider.com/player_api.php?username=YOUR_USER&password=YOUR_PASS&action=get_short_epg&stream_id=12345"

# Stream abspielen (VLC)
vlc "https://your-provider.com/live/YOUR_USER/YOUR_PASS/12345.ts"
```

---

## Anhang

### Unterstützte Stream-Formate

| Format | Beschreibung |
|--------|--------------|
| `.ts` | MPEG Transport Stream (Standard) |
| `.m3u8` | HLS (HTTP Live Streaming) |
| `.mp4` | MPEG-4 Video |
| `.mkv` | Matroska Video |

### Unterstützte Container-Extensions

- `mp4`, `mkv`, `avi`, `mov`, `flv`, `wmv`, `ts`, `m3u8`

### EPG-Dekodierung

EPG-Daten können Base64-kodiert sein. Beispiel-Dekodierung:

```kotlin
fun decodeEpgText(text: String): String {
    if (text.isBlank()) return ""
    
    // Base64-Dekodierung versuchen
    val base64Decoded = try {
        val decoded = android.util.Base64.decode(text, android.util.Base64.DEFAULT)
        String(decoded, Charsets.UTF_8)
    } catch (_: Exception) { null }
    
    val result = base64Decoded ?: text
    
    // HTML-Tags entfernen
    return result.replace(Regex("<[^>]*>"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()
}
```

---

## Support

Bei Fragen zur API:
- **GitHub Issues** - [Issues erstellen](https://github.com/yourusername/nativeplayer/issues)
- **Dokumentation** - [Offizielle Xtream-Codes Docs](https://github.com/xstream-codes/api)

---

*Letzte Aktualisierung: Januar 2024*
