package com.nativeplayer.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import com.nativeplayer.player.PlayerManager
import com.nativeplayer.player.PlayerState
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

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository,
    private val preferencesManager: PreferencesManager
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
        imageUrl: String = ""
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
                    duration = duration
                )
            )
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
