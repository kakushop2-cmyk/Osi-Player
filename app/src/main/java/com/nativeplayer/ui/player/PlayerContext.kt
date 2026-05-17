package com.nativeplayer.ui.player

import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.EpgItem

/**
 * Einfacher In-Memory-Holder für den aktuellen Live-TV-Kontext.
 * Wird vor Navigation zum FullScreenPlayer befüllt und dort ausgelesen.
 * So können Sender im Vollbildmodus mit Hoch/Runter gewechselt werden,
 * ohne die gesamte Liste über Navigation-Argumente zu schleifen.
 * 
 * Auch enthält EPG-Daten für Live-TV Fullscreen UI.
 */
object PlayerContext {
    private var _channels: List<Channel> = emptyList()
    private var _currentIndex: Int = -1

    /** Wird beim Zurücknavigieren aus Fullscreen gesetzt, damit LiveScreen den Fokus setzen kann. */
    var lastSelectedChannelId: String? = null
    
    // EPG Data für Fullscreen UI
    private var _previousEpgItem: EpgItem? = null
    private var _currentEpgItem: EpgItem? = null
    private var _nextEpgItem: EpgItem? = null
    private var _channelLogoUrl: String? = null
    private var _epgProgress: Float = 0f
    private var _currentTimeInZone: Long = 0L
    private var _remainingMinutes: Int = 0
    private var _categoryTimezone: String = "UTC"

    /**
     * Setzt die aktuelle Senderliste und den Index.
     * Sollte direkt vor navController.navigate() aufgerufen werden.
     */
    fun setLiveContext(channels: List<Channel>, currentIndex: Int) {
        _channels = channels
        _currentIndex = currentIndex.coerceIn(0, channels.size.coerceAtLeast(1) - 1)
    }
    
    /**
     * Setzt EPG-Daten für Live-TV Fullscreen UI.
     */
    fun setEpgData(
        previousEpgItem: EpgItem?,
        currentEpgItem: EpgItem?,
        nextEpgItem: EpgItem?,
        channelLogoUrl: String?,
        epgProgress: Float,
        currentTimeInZone: Long,
        remainingMinutes: Int,
        categoryTimezone: String
    ) {
        _previousEpgItem = previousEpgItem
        _currentEpgItem = currentEpgItem
        _nextEpgItem = nextEpgItem
        _channelLogoUrl = channelLogoUrl
        _epgProgress = epgProgress
        _currentTimeInZone = currentTimeInZone
        _remainingMinutes = remainingMinutes
        _categoryTimezone = categoryTimezone
    }

    /** Löscht den Kontext (z.B. wenn kein Live-TV). */
    fun clear() {
        _channels = emptyList()
        _currentIndex = -1
        lastSelectedChannelId = null
        _previousEpgItem = null
        _currentEpgItem = null
        _nextEpgItem = null
        _channelLogoUrl = null
        _epgProgress = 0f
        _currentTimeInZone = 0L
        _remainingMinutes = 0
        _categoryTimezone = "UTC"
    }

    /** Aktuelle Senderliste – nur Live-TV. */
    val channels: List<Channel> get() = _channels

    /** Aktueller Index in der Senderliste. */
    val currentIndex: Int get() = _currentIndex
    
    // EPG Getters
    val previousEpgItem: EpgItem? get() = _previousEpgItem
    val currentEpgItem: EpgItem? get() = _currentEpgItem
    val nextEpgItem: EpgItem? get() = _nextEpgItem
    var channelLogoUrl: String?
        get() = _channelLogoUrl
        set(value) { _channelLogoUrl = value }
    val epgProgress: Float get() = _epgProgress
    val currentTimeInZone: Long get() = _currentTimeInZone
    val remainingMinutes: Int get() = _remainingMinutes
    val categoryTimezone: String get() = _categoryTimezone

    /** Setzt EPG zurück (für Kanalwechsel – behält Channel-Logo). */
    fun resetEpgItems() {
        _previousEpgItem = null
        _currentEpgItem = null
        _nextEpgItem = null
        _epgProgress = 0f
        _currentTimeInZone = System.currentTimeMillis()
        _remainingMinutes = 0
    }

    /** Gibt es einen vorherigen Sender? */
    fun hasPrevious(): Boolean = _currentIndex > 0

    /** Gibt es einen nächsten Sender? */
    fun hasNext(): Boolean = _currentIndex >= 0 && _currentIndex < _channels.size - 1

    /** Wechselt zum vorherigen Sender und gibt ihn zurück. */
    fun previous(): Channel? {
        if (!hasPrevious()) return null
        _currentIndex--
        return _channels[_currentIndex]
    }

    /** Wechselt zum nächsten Sender und gibt ihn zurück. */
    fun next(): Channel? {
        if (!hasNext()) return null
        _currentIndex++
        return _channels[_currentIndex]
    }

    /** Aktueller Sender oder null. */
    fun current(): Channel? = _channels.getOrNull(_currentIndex)
}
