# Favorites, Search & Recently Watched Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Add favorites (hearts), cross-entity search, recently watched, and resume-watching to the IPTV player.

**Architecture:** Room (SQLite) entities for persistent storage, DAO layer with Flow-based reactive queries, Repository wrappers injected via Hilt, Compose UI observation. Resume position saved from PlayerScreen on pause/stop/back.

**Tech Stack:** Room, Kotlin Coroutines Flow, Jetpack Compose, Hilt DI, Navigation Compose

---

### Task 0: Add Room Dependencies

**Files:**
- Modify: `app/build.gradle.kts`

- [ ] **Step 1: Add Room dependencies**

After the `// Core` line, add:
```
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
```

---

### Task 1: Create Room Entities

**Files:**
- Create: `app/src/main/java/com/nativeplayer/data/model/FavoriteEntity.kt`
- Create: `app/src/main/java/com/nativeplayer/data/model/WatchHistoryEntity.kt`

- [ ] **Step 1: Create FavoriteEntity.kt**

```kotlin
package com.nativeplayer.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val type: String,
    val itemId: String,
    val title: String,
    val subtitle: String = "",
    val imageUrl: String = "",
    val addedAt: Long = System.currentTimeMillis()
)
```

- [ ] **Step 2: Create WatchHistoryEntity.kt**

```kotlin
package com.nativeplayer.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val id: String,
    val type: String,
    val itemId: String,
    val title: String,
    val imageUrl: String = "",
    val streamUrl: String = "",
    val position: Long = 0,
    val duration: Long = 0,
    val seasonNumber: Int = -1,
    val episodeNumber: Int = -1,
    val episodeTitle: String = "",
    val lastWatchedAt: Long = System.currentTimeMillis()
)
```

---

### Task 2: Create DAOs + Database

**Files:**
- Create: `app/src/main/java/com/nativeplayer/data/repository/FavoriteDao.kt`
- Create: `app/src/main/java/com/nativeplayer/data/repository/WatchHistoryDao.kt`
- Create: `app/src/main/java/com/nativeplayer/data/repository/AppDatabase.kt`

- [ ] **Step 1: Create FavoriteDao.kt**

```kotlin
package com.nativeplayer.data.repository
import androidx.room.*
import com.nativeplayer.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY addedAt DESC")
    fun getByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    fun isFavorite(itemId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    suspend fun isFavoriteSync(itemId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: String)
}
```

- [ ] **Step 2: Create WatchHistoryDao.kt**

```kotlin
package com.nativeplayer.data.repository
import androidx.room.*
import com.nativeplayer.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY lastWatchedAt DESC")
    fun getAll(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE type != 'live' ORDER BY lastWatchedAt DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE itemId = :itemId LIMIT 1")
    suspend fun getByItemId(itemId: String): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: String)
}
```

- [ ] **Step 3: Create AppDatabase.kt**

```kotlin
package com.nativeplayer.data.repository
import androidx.room.Database
import androidx.room.RoomDatabase
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.WatchHistoryEntity

@Database(entities = [FavoriteEntity::class, WatchHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun watchHistoryDao(): WatchHistoryDao
}
```

---

### Task 3: Create Repositories + Update DI

**Files:**
- Create: `app/src/main/java/com/nativeplayer/data/repository/FavoriteRepository.kt`
- Create: `app/src/main/java/com/nativeplayer/data/repository/WatchHistoryRepository.kt`
- Modify: `app/src/main/java/com/nativeplayer/di/AppModule.kt`

- [ ] **Step 1: Create FavoriteRepository.kt**

```kotlin
package com.nativeplayer.data.repository
import com.nativeplayer.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    fun getAll(): Flow<List<FavoriteEntity>> = favoriteDao.getAll()
    fun getByType(type: String): Flow<List<FavoriteEntity>> = favoriteDao.getByType(type)
    fun isFavorite(itemId: String): Flow<Boolean> = favoriteDao.isFavorite(itemId)

    suspend fun toggle(item: FavoriteEntity) {
        if (favoriteDao.isFavoriteSync(item.itemId)) {
            favoriteDao.deleteByItemId(item.itemId)
        } else {
            favoriteDao.insert(item)
        }
    }
    suspend fun add(item: FavoriteEntity) = favoriteDao.insert(item)
    suspend fun remove(itemId: String) = favoriteDao.deleteByItemId(itemId)
    suspend fun isFavoriteSync(itemId: String): Boolean = favoriteDao.isFavoriteSync(itemId)
}
```

- [ ] **Step 2: Create WatchHistoryRepository.kt**

```kotlin
package com.nativeplayer.data.repository
import com.nativeplayer.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchHistoryRepository @Inject constructor(
    private val watchHistoryDao: WatchHistoryDao
) {
    fun getAll(): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getAll()
    fun getRecent(limit: Int = 10): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getRecent(limit)
    suspend fun savePosition(entry: WatchHistoryEntity) = watchHistoryDao.upsert(entry)
    suspend fun getByItemId(itemId: String): WatchHistoryEntity? = watchHistoryDao.getByItemId(itemId)
    suspend fun remove(itemId: String) = watchHistoryDao.deleteByItemId(itemId)
}
```

- [ ] **Step 3: Update AppModule.kt**

Add Room-related providers after existing providers:

```kotlin
    @Provides @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "nativeplayer_db").build()

    @Provides @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides @Singleton
    fun provideWatchHistoryDao(db: AppDatabase): WatchHistoryDao = db.watchHistoryDao()

    @Provides @Singleton
    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository = FavoriteRepository(dao)

    @Provides @Singleton
    fun provideWatchHistoryRepository(dao: WatchHistoryDao): WatchHistoryRepository = WatchHistoryRepository(dao)
```

---

### Task 4: Add Navigation Routes

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Add Search route to Screen.kt**

```kotlin
    data object Search : Screen("search")
```

- [ ] **Step 2: Add Search composable to NavGraph.kt**

```kotlin
import com.nativeplayer.ui.search.SearchScreen
// ...
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
```

---

### Task 5: Create Favorites UI

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/favorites/FavoritesViewModel.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/favorites/FavoritesScreen.kt`

- [ ] **Step 1: Create FavoritesViewModel.kt**

```kotlin
package com.nativeplayer.ui.favorites
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    val liveFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("live")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val movieFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("movie")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val seriesFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("series")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFavorite(itemId: String) {
        viewModelScope.launch { favoriteRepository.remove(itemId) }
    }
}
```

- [ ] **Step 2: Create FavoritesScreen.kt**

```kotlin
package com.nativeplayer.ui.favorites
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val liveFavs by viewModel.liveFavorites.collectAsState()
    val movieFavs by viewModel.movieFavorites.collectAsState()
    val seriesFavs by viewModel.seriesFavorites.collectAsState()
    val allEmpty = liveFavs.isEmpty() && movieFavs.isEmpty() && seriesFavs.isEmpty()

    if (allEmpty) {
        Box(Modifier.fillMaxSize().background(DarkBackground), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.FavoriteBorder, null, Modifier.size(64.dp), tint = TextSecondary)
                Spacer(Modifier.height(16.dp))
                Text("Keine Favoriten", color = TextSecondary, fontSize = 16.sp)
                Text("Tippe auf das Herz-Icon", color = TextSecondary, fontSize = 13.sp)
            }
        }
    } else {
        LazyColumn(Modifier.fillMaxSize().background(DarkBackground).padding(horizontal = 12.dp)) {
            if (liveFavs.isNotEmpty()) {
                item { SectionHeader("Live TV", Icons.Filled.LiveTv) }
                items(liveFavs) { fav ->
                    FavItem(fav, AccentBlue,
                        onClick = { navController.navigate(Screen.FullScreenPlayer.createRoute(fav.itemId)) },
                        onRemove = { viewModel.removeFavorite(fav.itemId) })
                }
            }
            if (movieFavs.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Filme", Icons.Filled.Movie) }
                items(movieFavs) { fav ->
                    FavItem(fav, AccentGreen,
                        onClick = { navController.navigate(Screen.MovieDetail.createRoute(fav.itemId)) },
                        onRemove = { viewModel.removeFavorite(fav.itemId) })
                }
            }
            if (seriesFavs.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Serien", Icons.Filled.Theaters) }
                items(seriesFavs) { fav ->
                    FavItem(fav, AccentGreen,
                        onClick = { navController.navigate(Screen.SeriesDetail.createRoute(fav.itemId)) },
                        onRemove = { viewModel.removeFavorite(fav.itemId) })
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, null, Modifier.size(20.dp), tint = AccentBlue)
        Spacer(Modifier.width(8.dp))
        Text(title, color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
private fun FavItem(fav: FavoriteEntity, accentColor: Color, onClick: () -> Unit, onRemove: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp)).background(SurfaceDark)
            .clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(48.dp).background(SurfaceLight, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
            Icon(Icons.Filled.Tv, null, Modifier.size(24.dp), tint = accentColor)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(fav.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (fav.subtitle.isNotEmpty()) Text(fav.subtitle, color = TextSecondary, fontSize = 12.sp)
        }
        IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, "Entfernen", tint = TextSecondary, modifier = Modifier.size(20.dp)) }
    }
}
```

---

### Task 6: Add Favorites to LiveScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt`

- [ ] **Step 1: Update LiveViewModel — inject FavoriteRepository, add favoriteIds + toggle**

```kotlin
class LiveViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    // ... existing fields ...

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    init {
        loadData()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("live").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }

    fun toggleFavorite(channel: Channel) {
        viewModelScope.launch {
            val fav = FavoriteEntity(
                id = "live_${channel.id}", type = "live", itemId = channel.id,
                title = channel.name, subtitle = channel.group, imageUrl = channel.logo ?: ""
            )
            favoriteRepository.toggle(fav)
        }
    }
}
```

- [ ] **Step 2: Update ChannelItem in LiveScreen — add heart icon**

Change the `ChannelItem` function signature to accept `isFavorite: Boolean` and `onFavoriteClick: () -> Unit`. Add a heart IconButton at the right side:

```kotlin
@Composable
private fun ChannelItem(channel: Channel, isSelected: Boolean, isFavorite: Boolean, onClick: () -> Unit, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) AccentBlue.copy(alpha = 0.2f) else Color.Transparent)
            .clickable(onClick = onClick).padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Filled.PlayArrow, null, Modifier.size(16.dp), tint = if (isSelected) AccentBlue else TextSecondary)
        Spacer(Modifier.width(6.dp))
        Text(channel.name, color = if (isSelected) AccentBlue else TextPrimary, fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
        IconButton(onClick = onFavoriteClick, modifier = Modifier.size(28.dp)) {
            Icon(
                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                "Favorit", tint = if (isFavorite) ErrorRed else TextSecondary, modifier = Modifier.size(18.dp))
        }
    }
}
```

In `LiveScreen`, update the ChannelItem call site to pass:
```kotlin
val favoriteIds by viewModel.favoriteIds.collectAsState()
// ...
ChannelItem(
    channel = channel, isSelected = isSelected,
    isFavorite = channel.id in favoriteIds,
    onClick = { ... }, onFavoriteClick = { viewModel.toggleFavorite(channel) }
)
```

---

### Task 7: Add Favorites to MoviesScreen + MovieDetailScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MoviesViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MovieDetailScreen.kt`

- [ ] **Step 1: Update MoviesViewModel — inject FavoriteRepository**

Add fields and methods:
```kotlin
class MoviesViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    // ... existing ...
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()
    init { loadMovies(); observeFavorites() }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("movie").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }
    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            favoriteRepository.toggle(FavoriteEntity(
                id = "movie_${movie.id}", type = "movie", itemId = movie.id,
                title = movie.title, subtitle = movie.category, imageUrl = movie.posterUrl ?: ""))
        }
    }
}
```

- [ ] **Step 2: Update MoviePosterItem — add heart icon in top-right corner**

```kotlin
@Composable
private fun MoviePosterItem(movie: Movie, isFavorite: Boolean, onFavoriteClick: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Box {
            Column {
                Box(Modifier.fillMaxWidth().height(180.dp).background(SurfaceLight), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Movie, null, Modifier.size(48.dp), tint = TextSecondary)
                }
                Column(Modifier.padding(8.dp)) {
                    Text(movie.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, Modifier.size(12.dp), tint = StarYellow)
                        Text(" ${movie.rating}", color = TextSecondary, fontSize = 11.sp)
                        Spacer(Modifier.weight(1f))
                        Text(movie.year, color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
            IconButton(onClick = onFavoriteClick, modifier = Modifier.align(Alignment.TopEnd).size(32.dp)) {
                Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, "Fav",
                    tint = if (isFavorite) ErrorRed else TextSecondary, modifier = Modifier.size(18.dp))
            }
        }
    }
}
```

Update call site in MoviesScreen:
```kotlin
val favoriteIds by viewModel.favoriteIds.collectAsState()
// ...
items(movies) { movie ->
    MoviePosterItem(movie, isFavorite = movie.id in favoriteIds,
        onFavoriteClick = { viewModel.toggleFavorite(movie) },
        onClick = { navController.navigate(Screen.MovieDetail.createRoute(movie.id)) })
}
```

- [ ] **Step 3: Update MovieDetailScreen — add heart + resume info**

Inject `FavoriteRepository` and `WatchHistoryRepository` into a new detail ViewModel or use `hiltViewModel()` with the existing MoviesViewModel extended. Simplest: pass them as params from NavGraph.

Better: Create a dedicated MovieDetailViewModel. Or just use MoviesViewModel since it already exists. Let me extend MoviesViewModel:

Add to MoviesViewModel:
```kotlin
    fun isFavoriteSync(itemId: String): Boolean = _favoriteIds.value.contains(itemId)
```

In MovieDetailScreen, add heart icon in TopAppBar actions and a resume banner if watch history exists.

---

### Task 8: Add Favorites to Series Screen + SeriesDetailScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesDetailScreen.kt`

- [ ] **Step 1: Update SeriesViewModel — inject FavoriteRepository (same pattern as MoviesViewModel)**

```kotlin
class SeriesViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    // ... existing fields ...
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()
    init { loadSeries(); observeFavorites() }
    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("series").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }
    fun toggleFavorite(series: Series) {
        viewModelScope.launch {
            favoriteRepository.toggle(FavoriteEntity(
                id = "series_${series.id}", type = "series", itemId = series.id,
                title = series.title, subtitle = series.category, imageUrl = series.posterUrl ?: ""))
        }
    }
}
```

- [ ] **Step 2: Update SeriesPosterItem — add heart icon (same as MoviePosterItem pattern)**

```kotlin
@Composable
private fun SeriesPosterItem(series: Series, isFavorite: Boolean, onFavoriteClick: () -> Unit, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)) {
        Box {
            Column {
                Box(Modifier.fillMaxWidth().height(180.dp).background(SurfaceLight), contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Theaters, null, Modifier.size(48.dp), tint = TextSecondary)
                }
                Column(Modifier.padding(8.dp)) {
                    Text(series.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                        maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Row {
                        Icon(Icons.Filled.Star, null, Modifier.size(12.dp), tint = StarYellow)
                        Text(" ${series.rating}", color = TextSecondary, fontSize = 11.sp)
                        Spacer(Modifier.weight(1f)); Text(series.year, color = TextSecondary, fontSize = 10.sp)
                    }
                }
            }
            IconButton(onClick = onFavoriteClick, modifier = Modifier.align(Alignment.TopEnd).size(32.dp)) {
                Icon(if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, "Fav",
                    tint = if (isFavorite) ErrorRed else TextSecondary, modifier = Modifier.size(18.dp))
            }
        }
    }
}
```

Update call site in SeriesScreen with favoriteIds from viewModel.

---

### Task 9: Add HomeViewModel + Update HomeScreen

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/home/HomeScreen.kt`

- [ ] **Step 1: Create HomeViewModel.kt**

```kotlin
package com.nativeplayer.ui.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = watchHistoryRepository.getRecent(10)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
```

- [ ] **Step 2: Update HomeScreen — add ViewModel, Search icon, Weiterschauen row, Favorites tab**

Replace the content with:
```kotlin
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = hiltViewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val recentWatch by homeViewModel.recentWatch.collectAsState()

    Scaffold(containerColor = DarkBackground) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TopAppBar(
                title = { Text("NativePlayer", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                        Icon(Icons.Filled.Search, "Suche", tint = TextPrimary)
                    }
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(Icons.Filled.Settings, "Einstellungen", tint = TextSecondary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark, titleContentColor = TextPrimary)
            )

            // Weiterschauen row
            if (recentWatch.isNotEmpty()) {
                Column(Modifier.padding(start = 12.dp, top = 4.dp, bottom = 4.dp)) {
                    Text("Weiterschauen", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(recentWatch) { entry -> ResumeCard(entry, navController) }
                    }
                }
            }

            // Tabs
            TabRow(selectedTabIndex = selectedTab, containerColor = SurfaceDark, contentColor = AccentBlue) {
                TabItem("Live", Icons.Filled.LiveTv, 0, selectedTab) { selectedTab = it }
                TabItem("Filme", Icons.Filled.Movie, 1, selectedTab) { selectedTab = it }
                TabItem("Serien", Icons.Filled.Theaters, 2, selectedTab) { selectedTab = it }
                TabItem("Favoriten", Icons.Filled.Favorite, 3, selectedTab) { selectedTab = it }
            }

            when (selectedTab) {
                0 -> LiveTabContent(navController)
                1 -> MoviesTabContent(navController)
                2 -> SeriesTabContent(navController)
                3 -> FavoritesTabContent(navController)
            }
        }
    }
}

@Composable
private fun FavoritesTabContent(navController: NavController) {
    FavoritesScreen(navController = navController)
}
```

- [ ] **Step 3: Create ResumeCard composable (add at end of HomeScreen.kt)**

```kotlin
@Composable
private fun ResumeCard(entry: WatchHistoryEntity, navController: NavController) {
    Card(
        modifier = Modifier.width(140.dp).clickable {
            if (entry.streamUrl.isNotEmpty()) {
                navController.navigate(Screen.FullScreenPlayer.createRoute(entry.streamUrl))
            } else if (entry.type == "movie") {
                navController.navigate(Screen.MovieDetail.createRoute(entry.itemId))
            } else if (entry.type == "series") {
                navController.navigate(Screen.SeriesDetail.createRoute(entry.itemId))
            }
        },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(90.dp).background(SurfaceLight), contentAlignment = Alignment.Center) {
                Icon(if (entry.type == "movie") Icons.Filled.Movie else Icons.Filled.Theaters,
                    null, Modifier.size(36.dp), tint = TextSecondary)
            }
            Column(Modifier.padding(8.dp)) {
                Text(entry.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (entry.type == "series" && entry.episodeTitle.isNotEmpty()) {
                    Text("S${entry.seasonNumber} E${entry.episodeNumber}", color = AccentBlue, fontSize = 10.sp)
                    Text(entry.episodeTitle, color = TextSecondary, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(Modifier.height(4.dp))
                val progress = if (entry.duration > 0) (entry.position.toFloat() / entry.duration).coerceIn(0f, 1f) else 0f
                LinearProgressIndicator(progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = AccentBlue, trackColor = SurfaceLight)
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PlayArrow, null, Modifier.size(12.dp), tint = AccentBlue)
                    Text("Weiter", color = AccentBlue, fontSize = 10.sp)
                }
            }
        }
    }
}
```

Add imports to HomeScreen.kt:
```kotlin
import com.nativeplayer.ui.favorites.FavoritesScreen
import com.nativeplayer.data.model.WatchHistoryEntity
```

---

### Task 10: Create Search UI

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/search/SearchViewModel.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/search/SearchScreen.kt`

- [ ] **Step 1: Create SearchViewModel.kt**

```kotlin
package com.nativeplayer.ui.search
import androidx.lifecycle.ViewModel
import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.Movie
import com.nativeplayer.data.model.Series
import com.nativeplayer.data.repository.IptvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: IptvRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()
    private val _results = MutableStateFlow(SearchResults())
    val results: StateFlow<SearchResults> = _results.asStateFlow()

    data class SearchResults(
        val channels: List<Channel> = emptyList(),
        val movies: List<Movie> = emptyList(),
        val series: List<Series> = emptyList()
    )

    fun onQueryChanged(q: String) {
        _query.value = q
        if (q.length < 2) { _results.value = SearchResults(); return }
        val lower = q.lowercase()
        _results.value = SearchResults(
            channels = repository.getCachedChannels().filter {
                it.name.lowercase().contains(lower) || it.group.lowercase().contains(lower)
            },
            movies = repository.getMovies().filter {
                it.title.lowercase().contains(lower) || it.category.lowercase().contains(lower)
            },
            series = repository.getSeries().filter {
                it.title.lowercase().contains(lower) || it.category.lowercase().contains(lower)
            }
        )
    }
}
```

- [ ] **Step 2: Create SearchScreen.kt**

```kotlin
package com.nativeplayer.ui.search
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val hasQuery = query.length >= 2
    val hasResults = results.channels.isNotEmpty() || results.movies.isNotEmpty() || results.series.isNotEmpty()

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query, onValueChange = { viewModel.onQueryChanged(it) },
                        placeholder = { Text("Suche nach Sendern, Filmen, Serien...", color = TextSecondary, fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AccentBlue, unfocusedBorderColor = SurfaceLight,
                            cursorColor = AccentBlue
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceDark)
            )
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(DarkBackground)) {
            if (!hasQuery) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Search, null, Modifier.size(64.dp), tint = TextSecondary)
                            Spacer(Modifier.height(12.dp))
                            Text("Gib mindestens 2 Zeichen ein", color = TextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            } else if (!hasResults) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.SearchOff, null, Modifier.size(48.dp), tint = TextSecondary)
                            Spacer(Modifier.height(8.dp))
                            Text("Keine Ergebnisse", color = TextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            } else {
                if (results.channels.isNotEmpty()) {
                    item {
                        Row(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LiveTv, null, Modifier.size(16.dp), tint = AccentBlue)
                            Spacer(Modifier.width(6.dp))
                            Text("Live TV (${results.channels.size})", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    items(results.channels) { ch ->
                        SearchResultItem(
                            icon = Icons.Filled.LiveTv, primary = ch.name, secondary = ch.group,
                            onClick = { navController.navigate(Screen.FullScreenPlayer.createRoute(ch.url)) }
                        )
                    }
                }
                if (results.movies.isNotEmpty()) {
                    item {
                        Row(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Movie, null, Modifier.size(16.dp), tint = AccentBlue)
                            Spacer(Modifier.width(6.dp))
                            Text("Filme (${results.movies.size})", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    items(results.movies) { movie ->
                        SearchResultItem(
                            icon = Icons.Filled.Movie, primary = movie.title, secondary = "${movie.year} • ${movie.category}",
                            onClick = { navController.navigate(Screen.MovieDetail.createRoute(movie.id)) }
                        )
                    }
                }
                if (results.series.isNotEmpty()) {
                    item {
                        Row(Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Theaters, null, Modifier.size(16.dp), tint = AccentBlue)
                            Spacer(Modifier.width(6.dp))
                            Text("Serien (${results.series.size})", color = AccentBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    items(results.series) { s ->
                        SearchResultItem(
                            icon = Icons.Filled.Theaters, primary = s.title, secondary = "${s.year} • ${s.category}",
                            onClick = { navController.navigate(Screen.SeriesDetail.createRoute(s.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(icon: androidx.compose.ui.graphics.vector.ImageVector, primary: String, secondary: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp)).background(SurfaceDark)
            .clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(20.dp), tint = AccentBlue)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(primary, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (secondary.isNotEmpty()) Text(secondary, color = TextSecondary, fontSize = 12.sp)
        }
        Icon(Icons.Filled.ChevronRight, null, Modifier.size(20.dp), tint = TextSecondary)
    }
}
```

---

### Task 11: Save Position on PlayerScreen (Resume Watching)

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/player/PlayerViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Create PlayerViewModel.kt**

```kotlin
package com.nativeplayer.ui.player
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    fun savePosition(entry: WatchHistoryEntity) {
        viewModelScope.launch { watchHistoryRepository.savePosition(entry) }
    }
}
```

- [ ] **Step 2: Update PlayerScreen — accept viewModel, save position on pause/stop/back**

Add `viewModel: PlayerViewModel = hiltViewModel()` parameter. Add save logic:
```kotlin
// Add after the isPlaying toggle and in DisposableEffect:
fun saveCurrentPosition() {
    val pm = playerManager ?: return
    val pos = pm.getCurrentPosition()
    val dur = pm.getDuration()
    if (dur > 30_000) { // only save if content > 30s
        viewModel.savePosition(WatchHistoryEntity(
            id = "watch_$url",
            type = if (url.contains("movie")) "movie" else if (url.contains("series")) "series" else "live",
            itemId = url,
            title = "Wiedergabe",
            streamUrl = url,
            position = pos,
            duration = dur
        ))
    }
}

// In pause button, add saveCurrentPosition() call before pausing
// In DisposableEffect, add saveCurrentPosition() in onDispose block
```

- [ ] **Step 3: Update NavGraph — pass PlayerScreen with viewModel**

No change needed — `hiltViewModel()` works automatically via NavGraph entry's default ViewModelStoreOwner.

---

### Task 12: Add MovieDetailViewModel for favorites + resume

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/movies/MovieDetailViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MovieDetailScreen.kt`

- [ ] **Step 1: Create MovieDetailViewModel**

```kotlin
@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    fun isFavorite(itemId: String): Flow<Boolean> = favoriteRepository.isFavorite(itemId)
    suspend fun isFavoriteSync(itemId: String): Boolean = favoriteRepository.isFavoriteSync(itemId)
    fun toggle(movie: Movie) { viewModelScope.launch {
        favoriteRepository.toggle(FavoriteEntity(
            id="movie_${movie.id}", type="movie", itemId=movie.id,
            title=movie.title, subtitle=movie.category, imageUrl=movie.posterUrl?:""))
    }}
    fun getWatchHistory(itemId: String): Flow<WatchHistoryEntity?> {
        // We need a Flow version. Convert from suspend.
        // Simpler: just suspend and load once
        val result = MutableStateFlow<WatchHistoryEntity?>(null)
        viewModelScope.launch { result.value = watchHistoryRepository.getByItemId(itemId) }
        return result.asStateFlow()
    }
}
```

- [ ] **Step 2: Update MovieDetailScreen — add heart in TopAppBar + resume banner**

Add heart icon action in TopAppBar. Add resume banner with progress if history exists. Use `hiltViewModel<MovieDetailViewModel>()`.

---

### Task 13: Add SeriesDetailViewModel for favorites + resume

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/series/SeriesDetailViewModel.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesDetailScreen.kt`

- [ ] **Step 1: Create SeriesDetailViewModel (mirrors MovieDetailViewModel for series)

- [ ] **Step 2: Update SeriesDetailScreen — add heart in TopAppBar + resume banner

---

### Task 14: Build & Verify

- [ ] **Step 1: Build project**

Run: `cd /home/osi61/nativeplayer && ./gradlew assembleDebug`
Expected: BUILD SUCCESSFUL
