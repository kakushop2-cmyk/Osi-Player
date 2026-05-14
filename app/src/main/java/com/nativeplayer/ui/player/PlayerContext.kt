package com.nativeplayer.ui.player

import com.nativeplayer.data.model.Channel

/**
 * Einfacher In-Memory-Holder für den aktuellen Live-TV-Kontext.
 * Wird vor Navigation zum FullScreenPlayer befüllt und dort ausgelesen.
 * So können Sender im Vollbildmodus mit Hoch/Runter gewechselt werden,
 * ohne die gesamte Liste über Navigation-Argumente zu schleifen.
 */
object PlayerContext {
    private var _channels: List<Channel> = emptyList()
    private var _currentIndex: Int = -1

    /**
     * Setzt die aktuelle Senderliste und den Index.
     * Sollte direkt vor navController.navigate() aufgerufen werden.
     */
    fun setLiveContext(channels: List<Channel>, currentIndex: Int) {
        _channels = channels
        _currentIndex = currentIndex.coerceIn(0, channels.size.coerceAtLeast(1) - 1)
    }

    /** Löscht den Kontext (z.B. wenn kein Live-TV). */
    fun clear() {
        _channels = emptyList()
        _currentIndex = -1
    }

    /** Aktuelle Senderliste – nur Live-TV. */
    val channels: List<Channel> get() = _channels

    /** Aktueller Index in der Senderliste. */
    val currentIndex: Int get() = _currentIndex

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
