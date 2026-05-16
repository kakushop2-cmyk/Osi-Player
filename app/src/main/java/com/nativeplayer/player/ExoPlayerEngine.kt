package com.nativeplayer.player

import android.os.Handler
import android.os.Looper
import android.view.SurfaceView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.text.CueGroup
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.common.Tracks
import androidx.media3.ui.SubtitleView
import com.nativeplayer.data.model.AspectRatio

class ExoPlayerEngine(private val exoPlayer: ExoPlayer) : PlayerEngine {

    override var state: PlayerState = PlayerState.IDLE
        private set
    override val currentPosition: Long
        get() = exoPlayer.currentPosition.coerceAtLeast(0)
    override val duration: Long
        get() = exoPlayer.duration.coerceAtLeast(0)
    override var isPlaying: Boolean = false
        private set

    private var stateListener: ((PlayerState) -> Unit)? = null
    private var timeListener: ((Long, Long) -> Unit)? = null

    private var lastError: String? = null
    private var currentSpeed: Float = 1.0f
    private var isReleased = false
    
    // Track selection state
    private var currentSubtitleTrack: SubtitleTrack? = null
    private var currentAudioTrack: AudioTrack? = null
    private var subtitleView: SubtitleView? = null

    // Handler für regelmäßige Zeit-Updates (alle 500ms)
    private val timeUpdateHandler = Handler(Looper.getMainLooper())
    private val timeUpdateRunnable = object : Runnable {
        override fun run() {
            if (!isReleased && isPlaying) {
                timeListener?.invoke(currentPosition, duration)
                timeUpdateHandler.postDelayed(this, 500)
            }
        }
    }

    /** Fehlermeldung des letzten Playback-Fehlers (für UI-Anzeige) */
    fun getLastError(): String? = lastError

    init {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                syncPlayerState()
                if (playbackState == Player.STATE_READY) {
                    // Sofortiges Update beim Ready-State, dann regelmäßig via Handler
                    timeListener?.invoke(currentPosition, duration)
                    if (isPlaying) startTimeUpdates()
                } else if (playbackState == Player.STATE_ENDED) {
                    stopTimeUpdates()
                    timeListener?.invoke(currentPosition, duration)
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                syncPlayerState()
                if (playing) {
                    startTimeUpdates()
                } else {
                    stopTimeUpdates()
                }
            }

            override fun onPlayerError(error: PlaybackException) {
                state = PlayerState.ERROR
                lastError = error.localizedMessage ?: error.errorCodeName
                stateListener?.invoke(PlayerState.ERROR)
            }
            
            override fun onTracksChanged(tracks: Tracks) {
                updateCurrentTracks(tracks)
            }
        })
    }
    
    /**
     * Aktualisiert die aktuell ausgewählten Tracks basierend auf den verfügbaren Tracks.
     */
    private fun updateCurrentTracks(tracks: Tracks) {
        // Finde den aktuell ausgewählten Untertitel-Track
        tracks.groups.forEach { group ->
            for (i in 0 until group.length) {
                if (group.isTrackSelected(i)) {
                    val format = group.getTrackFormat(i)
                    if (format.sampleMimeType?.startsWith("text/") == true || 
                        format.sampleMimeType?.contains("cea-608") == true ||
                        format.sampleMimeType?.contains("cea-708") == true) {
                        currentSubtitleTrack = SubtitleTrack(
                            id = "${group.mediaTrackGroup.id}_$i",
                            language = format.language ?: "",
                            label = format.label ?: format.language ?: "Unbekannt"
                        )
                    } else if (format.sampleMimeType?.startsWith("audio/") == true) {
                        currentAudioTrack = AudioTrack(
                            id = "${group.mediaTrackGroup.id}_$i",
                            language = format.language ?: "",
                            label = format.label ?: format.language ?: "Unbekannt"
                        )
                    }
                }
            }
        }
    }

    /**
     * Konsolidiert State- und isPlaying-Updates aus beiden Quellen
     * ([onPlaybackStateChanged] + [onIsPlayingChanged]) in einer Methode,
     * um Race-Conditions durch undefinierte Callback-Reihenfolge zu vermeiden.
     */
    private fun syncPlayerState() {
        val playbackState = exoPlayer.playbackState

        isPlaying = when (playbackState) {
            Player.STATE_IDLE, Player.STATE_ENDED -> false
            Player.STATE_READY -> exoPlayer.playWhenReady
            Player.STATE_BUFFERING -> isPlaying // Zwischenzustand beibehalten
            else -> isPlaying
        }

        val newState = when (playbackState) {
            Player.STATE_IDLE -> PlayerState.IDLE
            Player.STATE_BUFFERING -> PlayerState.BUFFERING
            Player.STATE_READY -> if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
            Player.STATE_ENDED -> PlayerState.ENDED
            else -> PlayerState.IDLE
        }

        if (state != newState) {
            state = newState
            stateListener?.invoke(state)
        }
    }

    override fun initialize(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    override fun setSurface(surface: SurfaceView) {
        exoPlayer.setVideoSurfaceView(surface)
    }

    override fun setSubtitleView(subtitleView: SubtitleView) {
        this.subtitleView = subtitleView
        // ExoPlayer-Cue-Listener registrieren, damit Untertitel in die SubtitleView gerendert werden
        subtitleView.setCues(emptyList())
        exoPlayer.addListener(object : Player.Listener {
            override fun onCues(cues: CueGroup) {
                subtitleView.setCues(cues.cues)
            }
        })
    }

    override fun play() {
        if (exoPlayer.playbackState == Player.STATE_IDLE) {
            exoPlayer.prepare()
        }
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        stopTimeUpdates()
        exoPlayer.stop()
    }

    override fun seekTo(positionMs: Long) {
        exoPlayer.seekTo(positionMs)
    }

    override fun setPlaybackSpeed(speed: Float) {
        currentSpeed = speed
        exoPlayer.setPlaybackSpeed(speed)
    }

    override fun getPlaybackSpeed(): Float = currentSpeed
    
    override fun getSubtitleTracks(): List<SubtitleTrack> {
        val tracks = mutableListOf<SubtitleTrack>()
        
        // Füge "Keine" Option hinzu
        tracks.add(SubtitleTrack("none", "", "Keine"))
        
        exoPlayer.currentTracks?.groups?.forEach { group ->
            if (group.type == C.TRACK_TYPE_TEXT) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    val id = "${group.mediaTrackGroup.id}_$i"
                    val language = format.language ?: ""
                    val label = format.label ?: language.ifEmpty { "Unbekannt" }
                    tracks.add(SubtitleTrack(id, language, label))
                }
            }
        }
        
        return tracks
    }
    
    override fun getAudioTracks(): List<AudioTrack> {
        val tracks = mutableListOf<AudioTrack>()
        
        exoPlayer.currentTracks?.groups?.forEach { group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                for (i in 0 until group.length) {
                    val format = group.getTrackFormat(i)
                    val id = "${group.mediaTrackGroup.id}_$i"
                    val language = format.language ?: ""
                    val label = format.label ?: language.ifEmpty { "Unbekannt" }
                    tracks.add(AudioTrack(id, language, label))
                }
            }
        }
        
        return tracks
    }
    
    override fun selectSubtitleTrack(trackId: String?) {
        if (trackId == null || trackId == "none") {
            // Untertitel deaktivieren
            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                .buildUpon()
                .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, true)
                .build()
            currentSubtitleTrack = null
        } else {
            // Untertitel aktivieren
            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                .buildUpon()
                .setTrackTypeDisabled(C.TRACK_TYPE_TEXT, false)
                .build()
            
            // Finde und selektiere den spezifischen Track
            exoPlayer.currentTracks?.groups?.forEach { group ->
                if (group.type == C.TRACK_TYPE_TEXT) {
                    for (i in 0 until group.length) {
                        val id = "${group.mediaTrackGroup.id}_$i"
                        if (id == trackId) {
                            val format = group.getTrackFormat(i)
                            currentSubtitleTrack = SubtitleTrack(
                                id = id,
                                language = format.language ?: "",
                                label = format.label ?: format.language ?: "Unbekannt"
                            )
                            // Track selektieren über den TrackSelector
                            exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                                .buildUpon()
                                .setPreferredTextLanguage(format.language ?: "")
                                .build()
                            return
                        }
                    }
                }
            }
        }
    }
    
    override fun selectAudioTrack(trackId: String) {
        exoPlayer.currentTracks?.groups?.forEach { group ->
            if (group.type == C.TRACK_TYPE_AUDIO) {
                for (i in 0 until group.length) {
                    val id = "${group.mediaTrackGroup.id}_$i"
                    if (id == trackId) {
                        val format = group.getTrackFormat(i)
                        currentAudioTrack = AudioTrack(
                            id = id,
                            language = format.language ?: "",
                            label = format.label ?: format.language ?: "Unbekannt"
                        )
                        // Track selektieren über den TrackSelector
                        exoPlayer.trackSelectionParameters = exoPlayer.trackSelectionParameters
                            .buildUpon()
                            .setPreferredAudioLanguage(format.language ?: "")
                            .build()
                        return
                    }
                }
            }
        }
    }
    
    override fun getCurrentSubtitleTrack(): SubtitleTrack? = currentSubtitleTrack
    
    override fun getCurrentAudioTrack(): AudioTrack? = currentAudioTrack

    override fun release() {
        if (!isReleased) {
            isReleased = true
            stopTimeUpdates()
            // Reihenfolge ist wichtig: zuerst stop() um die Wiedergabe sofort zu beenden,
            // dann release() um alle Ressourcen freizugeben.
            // Ohne vorheriges stop() könnte der ExoPlayer noch gepufferte Audio-Daten
            // ausgeben während die neue Player-Instanz bereits initialisiert wird.
            exoPlayer.stop()
            exoPlayer.release()
        }
    }

    private fun startTimeUpdates() {
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable)
        timeUpdateHandler.post(timeUpdateRunnable)
    }

    private fun stopTimeUpdates() {
        timeUpdateHandler.removeCallbacks(timeUpdateRunnable)
    }

    override fun setOnStateChangeListener(listener: (PlayerState) -> Unit) {
        stateListener = listener
    }

    override fun setOnTimeUpdateListener(listener: (Long, Long) -> Unit) {
        timeListener = listener
    }

    fun setAspectRatio(aspectRatio: AspectRatio) {
        val ratio = when (aspectRatio) {
            AspectRatio.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            AspectRatio.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            else -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT // Fallback für alte enum values
        }
        try {
            exoPlayer.setVideoScalingMode(ratio)
        } catch (e: Exception) {
            lastError = "Fehler beim Setzen des Seitenverhältnisses: ${e.message}"
        }
    }
}
