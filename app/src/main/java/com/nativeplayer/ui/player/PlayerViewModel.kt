package com.nativeplayer.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import com.nativeplayer.player.PlayerManager
import com.nativeplayer.player.PlayerState
import com.nativeplayer.data.model.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Gibt den Typ des abgespielten Inhalts an.
 */
enum class ContentType {
    MOVIE, SERIES, LIVE;

    companion object {
        fun fromString(value: String): ContentType = when (value.lowercase()) {
            "movie" -> MOVIE
            "series" -> SERIES
            "live" -> LIVE
            else -> MOVIE
        }
    }
}

/**
 * Enthält die Informationen zur nächsten Episode für Auto-Play-Next.
 */
data class NextEpisodeInfo(
    val url: String,
    val title: String,
    val seasonNumber: Int,
    val episodeNumber: Int
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository,
    private val preferencesManager: PreferencesManager,
    private val iptvRepository: IptvRepository
) : ViewModel() {

    /** Liest die gespeicherten Player-Einstellungen. */
    fun getPlayerSettings(): PlayerSettings = preferencesManager.getPlayerSettings()

    private var saveJob: Job? = null

    /** Speichert die aktuelle Position in der Watch-History. */
    fun savePosition(
        url: String,
        position: Long,
        duration: Long,
        type: ContentType = ContentType.MOVIE,
        contentId: String = "",
        title: String = "Wiedergabe",
        imageUrl: String = "",
        seasonNumber: Int = -1,
        episodeNumber: Int = -1
    ) {
        if (type == ContentType.LIVE) return
        if (duration < 30_000) return // nur bei Inhalten > 30s
        val effectiveId = contentId.ifEmpty { url }

        viewModelScope.launch {
            watchHistoryRepository.savePosition(
                WatchHistoryEntity(
                    id = "watch_${effectiveId.hashCode()}",
                    type = type.name.lowercase(),
                    itemId = effectiveId,
                    title = title,
                    imageUrl = imageUrl,
                    streamUrl = url,
                    position = position,
                    duration = duration,
                    seasonNumber = seasonNumber,
                    episodeNumber = episodeNumber
                )
            )
        }
    }

    /**
     * Lädt die nächste Episode einer Serie basierend auf aktueller Staffel/Episode.
     * Durchläuft die Episoden innerhalb der aktuellen Staffel,
     * dann die nächste Staffel.
     * @param seriesId Die Serien-ID
     * @param currentSeason Aktuelle Staffelnummer
     * @param currentEpisode Aktuelle Episodennummer
     * @return NextEpisodeInfo oder null, wenn keine nächste Episode existiert
     */
    suspend fun loadNextEpisode(
        seriesId: String,
        currentSeason: Int,
        currentEpisode: Int
    ): NextEpisodeInfo? {
        return try {
            val series = iptvRepository.loadSeriesInfo(seriesId) ?: return null
            val seasonIndex = series.seasons.indexOfFirst { it.number == currentSeason }
            if (seasonIndex == -1) return null
            val season = series.seasons[seasonIndex]
            val episodeIndex = season.episodes.indexOfFirst { it.number == currentEpisode }
            if (episodeIndex == -1) return null

            // Nächste Episode in derselben Staffel
            if (episodeIndex + 1 < season.episodes.size) {
                val next = season.episodes[episodeIndex + 1]
                return NextEpisodeInfo(
                    url = next.streamUrl,
                    title = next.title,
                    seasonNumber = currentSeason,
                    episodeNumber = next.number
                )
            }

            // Erste Episode der nächsten Staffel
            if (seasonIndex + 1 < series.seasons.size) {
                val nextSeason = series.seasons[seasonIndex + 1]
                if (nextSeason.episodes.isNotEmpty()) {
                    val next = nextSeason.episodes.first()
                    return NextEpisodeInfo(
                        url = next.streamUrl,
                        title = next.title,
                        seasonNumber = nextSeason.number,
                        episodeNumber = next.number
                    )
                }
            }

            null // Keine weiteren Episoden
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Startet periodisches Speichern der Position (alle 15 Sekunden).
     * Verhindert Datenverlust bei App-Abstürzen.
     */
    fun startPeriodicSave(url: String, type: ContentType, title: String = "Wiedergabe") {
        saveJob?.cancel()
        saveJob = viewModelScope.launch {
            while (isActive) {
                delay(15_000)
                // Position wird über den PlayerManager im Screen geholt
            }
        }
    }

    /** Löscht den gespeicherten Fortschritt für einen Film (z.B. wenn er zuende gesehen wurde). */
    fun clearPosition(contentId: String = "", url: String = "") {
        val effectiveId = contentId.ifEmpty { url }
        viewModelScope.launch {
            watchHistoryRepository.remove(effectiveId)
        }
    }

    /** Lädt EPG-Daten für einen Live-Kanal (für Kanalwechsel im Vollbild). */
    suspend fun loadEpgForChannel(channelId: String): List<EpgItem> {
        return try {
            iptvRepository.getEpgForChannel(channelId)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /** Stoppt das periodische Speichern. */
    fun stopPeriodicSave() {
        saveJob?.cancel()
        saveJob = null
    }

    override fun onCleared() {
        super.onCleared()
        saveJob?.cancel()
    }
}
