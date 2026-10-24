@file:androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)

package com.nativeplayer.player

import androidx.media3.common.util.UnstableApi
import android.content.Context
import android.view.SurfaceView
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.SubtitleView
import com.nativeplayer.data.model.AspectRatio

class PlayerManager(
    private val context: Context,
    private val bufferSizeSeconds: Int = 30
) {

    private var exoPlayer: ExoPlayer? = null
    private var exoPlayerEngine: ExoPlayerEngine? = null
    private var currentEngine: PlayerEngine? = null

    val currentPlayer: PlayerEngine?
        get() = currentEngine

    /** Initialisiert den Player mit einer URL und bindet ihn an eine SurfaceView.
     *  Erzeugt IMMER einen neuen ExoPlayer, um zu verhindern dass ein alter,
     *  noch nicht freigegebener Player weiterlaeuft (doppelte Streams).
     */
    fun initialize(url: String, surface: SurfaceView): ExoPlayerEngine {
        // Vorhandenen Player explizit freigeben, falls release() vergessen wurde
        exoPlayer?.run {
            stop()
            release()
        }
        // DefaultLoadControl mit konfigurierter Puffergröße (in Sekunden → ms umrechnen)
        // maxBufferMs muss >= minBufferMs sein (DEFAULT_MIN_BUFFER_MS = 50_000 ms)
        val bufferMs = maxOf(
            DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
            bufferSizeSeconds * 1_000
        )
        val loadControl = DefaultLoadControl.Builder()
            .setBufferDurationsMs(
                /* minBufferMs = */ DefaultLoadControl.DEFAULT_MIN_BUFFER_MS,
                /* maxBufferMs = */ bufferMs,
                /* bufferForPlaybackMs = */ DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
                /* bufferForPlaybackAfterRebufferMs = */ DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
            )
            .build()
        val player = ExoPlayer.Builder(context)
            .setLoadControl(loadControl)
            .build()
            .also { exoPlayer = it }
        val engine = ExoPlayerEngine(player).also {
            it.setSurface(surface)
            it.initialize(url)
            exoPlayerEngine = it
            currentEngine = it
        }
        return engine
    }

    /** Spielt die Wiedergabe ab (startet/fortsetzen). */
    fun play() {
        currentEngine?.play()
    }

    /** Pausiert die Wiedergabe. */
    fun pause() {
        currentEngine?.pause()
    }

    /** Stoppt die Wiedergabe (bleibt initialisierbar). */
    fun stop() {
        currentEngine?.stop()
    }

    /** Spult zu einer Position (in Millisekunden). */
    fun seekTo(positionMs: Long) {
        currentEngine?.seekTo(positionMs)
    }

    /** Gibt die aktuelle Position zurück (in Millisekunden). */
    fun getCurrentPosition(): Long = currentEngine?.currentPosition ?: 0

    /** Gibt die Gesamtdauer zurück (in Millisekunden). */
    fun getDuration(): Long = currentEngine?.duration ?: 0

    /** Gibt zurück, ob gerade abgespielt wird. */
    fun isPlaying(): Boolean = currentEngine?.isPlaying ?: false

    /** Gibt den aktuellen Player-Zustand zurück. */
    fun getState(): PlayerState = currentEngine?.state ?: PlayerState.IDLE

    /** Setzt die Wiedergabegeschwindigkeit (0.5f - 3.0f). */
    fun setPlaybackSpeed(speed: Float) {
        currentEngine?.setPlaybackSpeed(speed)
    }

    /** Gibt die aktuelle Wiedergabegeschwindigkeit zurück. */
    fun getPlaybackSpeed(): Float = currentEngine?.getPlaybackSpeed() ?: 1.0f

    /** Bindet eine SubtitleView für die Untertitel-Darstellung. Muss nach initialize() aufgerufen werden. */
    fun setSubtitleView(subtitleView: SubtitleView) {
        exoPlayerEngine?.setSubtitleView(subtitleView)
    }

    /** Registriert einen Listener für Zustandsänderungen. */
    fun setOnStateChangeListener(listener: (PlayerState) -> Unit) {
        currentEngine?.setOnStateChangeListener(listener)
    }

    /** Registriert einen Listener für Zeitaktualisierungen. */
    fun setOnTimeUpdateListener(listener: (Long, Long) -> Unit) {
        currentEngine?.setOnTimeUpdateListener(listener)
    }

    /** Gibt die letzte Fehlermeldung zurück (falls vorhanden). */
    fun getLastError(): String? = exoPlayerEngine?.getLastError()
    
    // Track selection methods
    /** Gibt alle verfügbaren Untertitel-Tracks zurück. */
    fun getSubtitleTracks(): List<SubtitleTrack> = 
        exoPlayerEngine?.getSubtitleTracks() ?: emptyList()
    
    /** Gibt alle verfügbaren Audio-Tracks zurück. */
    fun getAudioTracks(): List<AudioTrack> = 
        exoPlayerEngine?.getAudioTracks() ?: emptyList()
    
    /** Wählt einen Untertitel-Track aus (null oder "none" deaktiviert Untertitel). */
    fun selectSubtitleTrack(trackId: String?) {
        exoPlayerEngine?.selectSubtitleTrack(trackId)
    }
    
    /** Wählt einen Audio-Track aus. */
    fun selectAudioTrack(trackId: String) {
        exoPlayerEngine?.selectAudioTrack(trackId)
    }
    
    /** Gibt den aktuell ausgewählten Untertitel-Track zurück. */
    fun getCurrentSubtitleTrack(): SubtitleTrack? = 
        exoPlayerEngine?.getCurrentSubtitleTrack()
    
    /** Gibt den aktuell ausgewählten Audio-Track zurück. */
    fun getCurrentAudioTrack(): AudioTrack? = 
        exoPlayerEngine?.getCurrentAudioTrack()

    /** Setzt das Seitenverhältnis des Videos. */
    fun setAspectRatio(aspectRatio: AspectRatio) {
        exoPlayerEngine?.setAspectRatio(aspectRatio)
    }

    /** Gibt alle Ressourcen frei. Der Manager kann nach release() nicht mehr verwendet werden. */
    fun release() {
        exoPlayerEngine?.release()
        exoPlayer = null
        exoPlayerEngine = null
        currentEngine = null
    }
}
