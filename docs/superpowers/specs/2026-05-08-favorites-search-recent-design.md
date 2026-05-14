# Favorites, Search & Recently Watched — Design Spec

## Summary
Add favorites (hearts), cross-entity search, recently watched, and resume-watching to the NativePlayer IPTV app.

## 1. Data Models (Room Entities)

### FavoriteEntity
```kotlin
@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,        // "live_ch_123", "movie_vod_456", "series_s7"
    val type: String,                   // "live", "movie", "series"
    val itemId: String,                 // original id from Channel/Movie/Series
    val title: String,
    val subtitle: String = "",          // channel group, movie category, series category
    val imageUrl: String = "",
    val addedAt: Long = System.currentTimeMillis()
)
```

### WatchHistoryEntity
```kotlin
@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val id: String,         // same format as FavoriteEntity.id
    val type: String,                   // "live", "movie", "series"
    val itemId: String,
    val title: String,
    val imageUrl: String = "",
    val streamUrl: String = "",
    val position: Long = 0,             // current playback position in ms
    val duration: Long = 0,             // total duration in ms
    val seasonNumber: Int = -1,         // -1 = not a series
    val episodeNumber: Int = -1,
    val episodeTitle: String = "",
    val lastWatchedAt: Long = System.currentTimeMillis()
)
```

### DAOs
- **FavoriteDao**: insert, delete, getAll, getByType, isFavorite(itemId) → Flow<Boolean>
- **WatchHistoryDao**: upsert, delete, getAll, getRecent(limit), getByType(type)

## 2. Database
- **AppDatabase**: Room database with both entities, version 1, auto-migration

## 3. Repositories
- **FavoriteRepository**: wraps DAO, exposes Flow-based methods
- **WatchHistoryRepository**: wraps DAO, exposes Flow-based methods

Both injected as `@Singleton` in Hilt.

## 4. UI Structure

### HomeScreen — restructured
- **"Weiterschauen" row**: horizontal LazyRow at top (before tab row), shows up to 10 watch-history items with progress bar and "▶ Weiter" label. Only shown when data exists.
- **4 tabs**: Live | Filme | Serien | ❤ Favoriten
- **Search icon** in TopAppBar → navigates to SearchScreen

### Favorites Tab (FavoritesScreen.kt)
- Three sections: Live TV, Filme, Serien
- Each section lists saved items as cards
- Swipe to remove or tap to navigate
- Powered by FavoritesViewModel

### Search (SearchScreen.kt)
- Full-screen search overlay
- Text field at top, auto-focus
- Real-time results as user types (debounced 300ms)
- Results grouped: Live TV → Movies → Series
- Tapping a result navigates to the appropriate screen
- Powered by SearchViewModel

### Heart Icons
- Added to ChannelItem, MoviePosterItem, SeriesPosterItem
- Toggle heart → insert/delete from Room
- LivePreview shows filled/empty heart state

## 5. Resume-Watching Integration

### PlayerScreen
- On pause / back-press / stop: save current position + duration to WatchHistory
- For series episodes: save seasonNumber, episodeNumber, episodeTitle
- Do NOT save for live TV (live streams have no meaningful position)

### HomeScreen "Weiterschauen" row
- Shows movies/series with lastWatchedAt position
- Progress bar (position/duration)
- Click → navigate to player with streamUrl and seek to saved position
- Series items show "S{season} E{episode} - {episodeTitle}"

## 6. Files Changed

### New files:
| File | Purpose |
|------|---------|
| `data/model/FavoriteEntity.kt` | Room entity |
| `data/model/WatchHistoryEntity.kt` | Room entity |
| `data/repository/FavoriteDao.kt` | Room DAO |
| `data/repository/WatchHistoryDao.kt` | Room DAO |
| `data/repository/AppDatabase.kt` | Room database |
| `data/repository/FavoriteRepository.kt` | Business logic layer |
| `data/repository/WatchHistoryRepository.kt` | Business logic layer |
| `ui/favorites/FavoritesScreen.kt` | Favorites tab |
| `ui/favorites/FavoritesViewModel.kt` | Favorites state |
| `ui/search/SearchScreen.kt` | Search overlay |
| `ui/search/SearchViewModel.kt` | Search state |

### Modified files:
| File | Changes |
|------|---------|
| `app/build.gradle.kts` | Add Room dependencies |
| `di/AppModule.kt` | Add Room DB & DAO providers |
| `ui/home/HomeScreen.kt` | Add Weiterchauen row + Favoriten tab |
| `ui/live/LiveScreen.kt` | Add heart icon to ChannelItem |
| `ui/movies/MoviesScreen.kt` | Add heart icon to MoviePosterItem |
| `ui/movies/MovieDetailScreen.kt` | Add heart + "Weiterschauen" state |
| `ui/series/SeriesScreen.kt` | Add heart icon to SeriesPosterItem |
| `ui/series/SeriesDetailScreen.kt` | Add heart + resume state |
| `ui/player/PlayerScreen.kt` | Save position on pause/stop/back |
| `ui/navigation/Screen.kt` | Add Search route |
| `ui/navigation/NavGraph.kt` | Add Search composable |

## 7. Search Scope
Searches across:
- **Live TV**: channel names and group names
- **Movies**: titles, categories, year
- **Series**: titles, categories, year

Results are grouped by type in a LazyColumn with section headers.

## 8. Error Handling
- Room operations are non-blocking (Room handles DB on background threads by default)
- Empty states: "Keine Favoriten", "Nichts gefunden", "Noch nichts angesehen"
- Loading: shimmer/skeleton placeholders where appropriate
- Edge case: if an item is deleted from the source data but still in Favorites/History, show it with a "(nicht verfügbar)" label

## 9. Future Considerations (out of scope for v1)
- Cloud sync of favorites
- EPG search
- Favorites folders/playlists
- Export/import favorites
