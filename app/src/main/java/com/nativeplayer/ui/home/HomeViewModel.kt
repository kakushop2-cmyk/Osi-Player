@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.nativeplayer.ui.home

import androidx.media3.common.util.UnstableApi
import android.app.Application
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.Episode
import com.nativeplayer.data.model.Movie
import com.nativeplayer.data.model.Season
import com.nativeplayer.data.model.Series
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"
private const val CLIP_DURATION_MS = 60_000L

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository,
    private val iptvRepository: IptvRepository,
    private val application: Application
) : ViewModel() {

    // ── Cached ExoPlayers for Hero Video (pro URL) ──────────────
    // Ermöglicht schnellen Wechsel zwischen Kategorien ohne Neuerstellung
    private val heroPlayers = mutableMapOf<String, ExoPlayer>()
    private val maxCachedPlayers = 3
    private val heroPlayerLock = Any() // Thread-Safety für Player-Operationen

    /** Gibt einen gecachten ExoPlayer zurück oder erstellt einen neuen */
    fun getOrCreateHeroPlayer(streamUrl: String): ExoPlayer {
        synchronized(heroPlayerLock) {
            heroPlayers[streamUrl]?.let { return it }

            // Alte Players releasen wenn zu viele
            if (heroPlayers.size >= maxCachedPlayers) {
                val oldest = heroPlayers.keys.first()
                heroPlayers.remove(oldest)?.release()
                Log.d(TAG, "Released oldest cached hero player")
            }

            Log.d(TAG, "Creating new ExoPlayer")
            val player = ExoPlayer.Builder(application)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(
                        DefaultHttpDataSource.Factory()
                            .setConnectTimeoutMs(15_000)
                            .setReadTimeoutMs(15_000)
                    )
                )
                .build()
                .apply {
                    playWhenReady = false
                    repeatMode = Player.REPEAT_MODE_OFF
                    volume = 0.7f
                    setHandleAudioBecomingNoisy(true)
                }

            heroPlayers[streamUrl] = player
            return player
        }
    }

    /** Bereitet den Player vor und startet ihn an einer zufälligen Position */
    fun prepareAndPlayHero(streamUrl: String) {
        synchronized(heroPlayerLock) {
            val player = heroPlayers[streamUrl] ?: return

            Log.d(TAG, "prepareAndPlayHero")
            val mediaItem = MediaItem.fromUri(Uri.parse(streamUrl))

            // Player zurücksetzen und neu starten
            player.stop()
            player.clearMediaItems()
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
    }

    /** Stoppt einen bestimmten Player komplett und setzt ihn zurück */
    fun pauseHero(streamUrl: String) {
        synchronized(heroPlayerLock) {
            heroPlayers[streamUrl]?.let {
                it.playWhenReady = false
                it.stop()
                it.clearMediaItems()
                Log.d(TAG, "Stopped hero player")
            }
        }
    }

    /** Stoppt ALLE gecachten Hero-Player sofort (z.B. beim Verlassen des HomeScreens) */
    fun stopAllHeroPlayers() {
        synchronized(heroPlayerLock) {
            heroPlayers.forEach { (url, player) ->
                player.playWhenReady = false
                player.stop()
                player.clearMediaItems()
            }
            Log.d(TAG, "Stopped ALL hero players (${heroPlayers.size}) — screen left")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Releasing all cached ExoPlayers (hero: ${heroPlayers.size})")
        heroPlayers.values.forEach { it.release() }
        heroPlayers.clear()
    }

    // ── Weitersehen (existierend) ──────────────────────────────
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = watchHistoryRepository.getRecent(10)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // ── Hero: Zuletzt hinzugefügte Filme (Fallback) ──────────
    val latestContent: StateFlow<List<Movie>> = pollingFlow(5000L) {
        val allMovies = iptvRepository.getMovies()
        val validMovies = allMovies
            .filter { it.streamUrl.isNotEmpty() }
            .takeLast(5)
            .reversed()
        Log.d(TAG, "latestContent: ${validMovies.size} valid movies (from ${allMovies.size} total)")
        if (validMovies.isNotEmpty()) {
            Log.d(TAG, "Hero movie: ${validMovies.first().title}")
        }
        validMovies
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ── Hero: 10 verschiedene Kategorien, neuester Film pro Kategorie ──
    data class HeroCategoryMovie(
        val category: String,
        val movie: Movie
    )

    val heroMoviesByCategory: StateFlow<List<HeroCategoryMovie>> = pollingFlow(300_000L) {
        val allMovies = iptvRepository.getMovies()
            .filter { it.streamUrl.isNotEmpty() && it.category.isNotEmpty() }

        // Gruppiere nach Kategorie, zufälliger Film pro Kategorie, zufällige Reihenfolge
        val categoryMovies = allMovies
            .groupBy { it.category }
            .map { (category, movies) ->
                HeroCategoryMovie(
                    category = category,
                    movie = movies.random()
                )
            }
            .shuffled()
            .take(10) // Maximal 10 Kategorien

        Log.d(TAG, "heroMoviesByCategory (random): ${categoryMovies.size} categories")
        categoryMovies
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ── Hero: Aktueller Index im Kategorien-Karussell ─────────
    private val _selectedHeroIndex = MutableStateFlow(0)
    val selectedHeroIndex: StateFlow<Int> = _selectedHeroIndex.asStateFlow()

    fun updateHeroIndex(index: Int) {
        _selectedHeroIndex.value = index
    }

    // ── Für dich: 2 random Filme (2025/2026 im Titel, ≥3/5 Sterne), Rotation alle 2 Min ──
    private val _fuerDichMovies = MutableStateFlow<List<Movie>>(emptyList())
    val fuerDichMovies: StateFlow<List<Movie>> = _fuerDichMovies.asStateFlow()

    private var fuerDichPool: List<Movie> = emptyList()
    private var fuerDichIndex = 0

    init {
        // FuerDich: Pool laden & alle 2 Min rotieren
        viewModelScope.launch {
            while (!iptvRepository.isLoaded()) {
                delay(200L)
            }
            fuerDichPool = loadFuerDichPool()
            emitFuerDichPair()

            while (true) {
                delay(120_000L)
                if (fuerDichPool.isNotEmpty()) {
                    fuerDichIndex = (fuerDichIndex + 2) % fuerDichPool.size
                    emitFuerDichPair()
                }
            }
        }

        // Auto-Rotation für Top Rated alle 2 Minuten (120s)
        viewModelScope.launch {
            while (true) {
                delay(120_000L)
                val movies = topRatedMovies.value
                if (movies.isNotEmpty()) {
                    _selectedTopRatedIndex.value =
                        (_selectedTopRatedIndex.value + 1) % movies.size
                }
            }
        }

        // Auto-Rotation für Hero-Kategorien alle 5 Minuten (300s)
        viewModelScope.launch {
            while (true) {
                delay(300_000L)
                val categories = heroMoviesByCategory.value
                if (categories.isNotEmpty()) {
                    _selectedHeroIndex.value =
                        (_selectedHeroIndex.value + 1) % categories.size
                }
            }
        }

    }

    private fun loadFuerDichPool(): List<Movie> {
        return iptvRepository.getMovies()
            .filter { movie ->
                movie.streamUrl.isNotEmpty()
                    && movie.rating >= 3.0f  // 3/5 Sterne
                    && (movie.title.contains("2025", ignoreCase = true)
                        || movie.title.contains("2026", ignoreCase = true))
            }
            .shuffled()
            .also { Log.d(TAG, "FuerDich pool: ${it.size} movies (2025/2026, ≥3★)") }
    }

    private fun emitFuerDichPair() {
        val pool = fuerDichPool
        if (pool.isEmpty()) {
            _fuerDichMovies.value = emptyList()
            return
        }
        // Nimm 2 Filme ab currentIndex (zyklisch)
        val pair = listOf(
            pool[fuerDichIndex % pool.size],
            pool[(fuerDichIndex + 1) % pool.size]
        )
        _fuerDichMovies.value = pair
        Log.d(TAG, "FuerDich rotated: ${pair.map { it.title }}")
    }

    // ── Sport: Erste 5 Live-TV Sender pro Sport-Kategorie ─────
    val sportChannels: StateFlow<List<Channel>> = pollingFlow(30_000L) {
        iptvRepository.getCachedChannels()
            .filter { channel ->
                channel.group.contains("Spor", ignoreCase = true)
            }
            .groupBy { it.group }
            .flatMap { (_, channels) -> channels.take(5) }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // ── Top Bewertete Filme (≥80% / rating ≥ 0.8) ───────────
    val topRatedMovies: StateFlow<List<Movie>> = pollingFlow(30_000L) {
        iptvRepository.getMovies()
            .filter { it.streamUrl.isNotEmpty() && it.rating >= 4.0f }
            .shuffled()
            .take(10)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // Aktueller Index im Karussell (für Swipe + Auto-Rotation)
    private val _selectedTopRatedIndex = MutableStateFlow(0)
    val selectedTopRatedIndex: StateFlow<Int> = _selectedTopRatedIndex.asStateFlow()

    /** Aktualisieren des Karussell-Index (wird vom Pager aufgerufen) */
    fun updateTopRatedIndex(index: Int) {
        _selectedTopRatedIndex.value = index
    }

    // ── Serien nach Kategorien (für Kachel-Sektion) ──────────────
    data class SeriesCategoryGroup(
        val category: String,
        val series: List<Series>
    )

    val seriesByCategory: StateFlow<List<SeriesCategoryGroup>> = pollingFlow(30_000L) {
        val allSeries = iptvRepository.getSeries()
            .filter { it.category.isNotEmpty() && !it.posterUrl.isNullOrEmpty() }

        val groups = allSeries
            .groupBy { it.category }
            .entries
            .sortedBy { it.key }
            .take(15)
            .map { (category, seriesList) ->
                SeriesCategoryGroup(
                    category = category,
                    series = seriesList.shuffled().take(20)
                )
            }
            .filter { it.series.isNotEmpty() }

        Log.d(TAG, "seriesByCategory: ${groups.size} categories, ${groups.sumOf { it.series.size }} series total")
        groups
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * Polling flow that waits for data to be loaded, then emits periodically.
     * - First check: wait until isDataLoaded is true (checks every 200ms)
     * - After data is loaded: emit every intervalMs
     * - If data becomes empty after being loaded (shouldn't happen), keep last value
     */
    private fun <T> pollingFlow(intervalMs: Long, source: suspend () -> T): Flow<T> = flow {
        // Wait for IptvRepository to finish loading (max 30 seconds)
        var waitCount = 0
        while (!iptvRepository.isLoaded() && waitCount < 150) {
            delay(200L)
            waitCount++
        }
        Log.d(TAG, "Data loaded, starting polling (interval: ${intervalMs}ms)")

        // Now poll periodically with error recovery
        while (true) {
            try {
                val data = source()
                emit(data)
            } catch (e: Exception) {
                Log.e(TAG, "pollingFlow error (retrying in ${intervalMs}ms)", e)
            }
            delay(intervalMs)
        }
    }
}
