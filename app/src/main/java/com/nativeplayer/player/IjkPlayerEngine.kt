package com.nativeplayer.player

import android.view.SurfaceView
import androidx.media3.ui.SubtitleView

/**
 * IJKPlayer-Engine (derzeit nicht vollständig implementiert).
 *
 * Aktuell wird ExoPlayer als primäre Engine verwendet.
 * Sofern IJKPlayer in Zukunft benötigt wird (z. B. für spezielle Stream-Formate),
 * muss diese Implementierung vervollständigt werden.
 *
 * Build-Hinweis: IJKPlayer muss aus dem Quellcode von
 * https://github.com/bilibili/ijkplayer gebaut werden.
 * JitPack hostet die Binärartefakte nicht mehr.
 */
@Deprecated("Nicht vollständig implementiert — verwende ExoPlayerEngine")
class IjkPlayerEngine : PlayerEngine {

    override var state: PlayerState = PlayerState.IDLE
        private set
    override var currentPosition: Long = 0
        private set
    override var duration: Long = 0
        private set
    override var isPlaying: Boolean = false
        private set

    private var stateListener: ((PlayerState) -> Unit)? = null
    private var timeListener: ((Long, Long) -> Unit)? = null
    private var targetUrl: String = ""

    override fun initialize(url: String) {
        targetUrl = url
        state = PlayerState.BUFFERING
        stateListener?.invoke(state)
    }

    override fun setSurface(surface: SurfaceView) {
        // IJKPlayer surface setup (nicht implementiert)
    }

    override fun setSubtitleView(subtitleView: SubtitleView) {
        // IJKPlayer subtitle view (nicht implementiert)
    }

    override fun play() {
        isPlaying = true
        state = PlayerState.PLAYING
        stateListener?.invoke(state)
    }

    override fun pause() {
        isPlaying = false
        state = PlayerState.PAUSED
        stateListener?.invoke(state)
    }

    override fun stop() {
        isPlaying = false
        state = PlayerState.IDLE
        stateListener?.invoke(state)
    }

    override fun seekTo(positionMs: Long) {
        currentPosition = positionMs
    }

    override fun setPlaybackSpeed(speed: Float) {
        // IJKPlayer speed (nicht implementiert)
    }

    override fun getPlaybackSpeed(): Float = 1.0f

    override fun release() {
        // IJKPlayer-Ressourcen freigeben (nicht implementiert)
    }

    override fun setOnStateChangeListener(listener: (PlayerState) -> Unit) {
        stateListener = listener
    }

    override fun setOnTimeUpdateListener(listener: (Long, Long) -> Unit) {
        timeListener = listener
    }
    
    // Track selection methods (nicht implementiert für IjkPlayer)
    override fun getSubtitleTracks(): List<SubtitleTrack> = emptyList()
    override fun getAudioTracks(): List<AudioTrack> = emptyList()
    override fun selectSubtitleTrack(trackId: String?) { }
    override fun selectAudioTrack(trackId: String) { }
    override fun getCurrentSubtitleTrack(): SubtitleTrack? = null
    override fun getCurrentAudioTrack(): AudioTrack? = null
}
