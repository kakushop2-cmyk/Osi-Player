@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.nativeplayer.player

import androidx.media3.common.util.UnstableApi
import android.view.SurfaceView
import androidx.media3.ui.SubtitleView

enum class PlayerState { IDLE, BUFFERING, READY, PLAYING, PAUSED, ENDED, ERROR }

/**
 * Represents a subtitle track with language information
 */
data class SubtitleTrack(
    val id: String,
    val language: String,
    val label: String
)

/**
 * Represents an audio track with language information
 */
data class AudioTrack(
    val id: String,
    val language: String,
    val label: String
)

interface PlayerEngine {
    val state: PlayerState
    val currentPosition: Long
    val duration: Long
    val isPlaying: Boolean

    fun initialize(url: String)
    fun setSurface(surface: SurfaceView)
    fun setSubtitleView(subtitleView: SubtitleView)
    fun play()
    fun pause()
    fun stop()
    fun seekTo(positionMs: Long)
    fun setPlaybackSpeed(speed: Float)
    fun getPlaybackSpeed(): Float
    fun release()
    fun setOnStateChangeListener(listener: (PlayerState) -> Unit)
    fun setOnTimeUpdateListener(listener: (Long, Long) -> Unit)
    
    // Track selection methods
    fun getSubtitleTracks(): List<SubtitleTrack>
    fun getAudioTracks(): List<AudioTrack>
    fun selectSubtitleTrack(trackId: String?)
    fun selectAudioTrack(trackId: String)
    fun getCurrentSubtitleTrack(): SubtitleTrack?
    fun getCurrentAudioTrack(): AudioTrack?
}
