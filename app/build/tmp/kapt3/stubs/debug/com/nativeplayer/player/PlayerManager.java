package com.nativeplayer.player;

import android.content.Context;
import android.view.SurfaceView;
import androidx.media3.exoplayer.DefaultLoadControl;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.SubtitleView;
import com.nativeplayer.data.model.AspectRatio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011J\b\u0010\u0013\u001a\u0004\u0018\u00010\u0012J\u0006\u0010\u0014\u001a\u00020\u0015J\b\u0010\u0016\u001a\u0004\u0018\u00010\u0017J\u0006\u0010\u0018\u001a\u00020\u0015J\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aJ\u0006\u0010\u001b\u001a\u00020\u001cJ\u0006\u0010\u001d\u001a\u00020\u001eJ\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u00170\u0011J\u0016\u0010 \u001a\u00020\u000f2\u0006\u0010!\u001a\u00020\u001a2\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020%J\u0006\u0010&\u001a\u00020\'J\u0006\u0010(\u001a\u00020\'J\u0006\u0010)\u001a\u00020\'J\u000e\u0010*\u001a\u00020\'2\u0006\u0010+\u001a\u00020\u0015J\u000e\u0010,\u001a\u00020\'2\u0006\u0010-\u001a\u00020\u001aJ\u0010\u0010.\u001a\u00020\'2\b\u0010-\u001a\u0004\u0018\u00010\u001aJ\u000e\u0010/\u001a\u00020\'2\u0006\u00100\u001a\u000201J\u001a\u00102\u001a\u00020\'2\u0012\u00103\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\'04J \u00105\u001a\u00020\'2\u0018\u00103\u001a\u0014\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\'06J\u000e\u00107\u001a\u00020\'2\u0006\u00108\u001a\u00020\u001cJ\u000e\u00109\u001a\u00020\'2\u0006\u0010:\u001a\u00020;J\u0006\u0010<\u001a\u00020\'R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\t\u001a\u0004\u0018\u00010\b8F\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006="}, d2 = {"Lcom/nativeplayer/player/PlayerManager;", "", "context", "Landroid/content/Context;", "bufferSizeSeconds", "", "(Landroid/content/Context;I)V", "currentEngine", "Lcom/nativeplayer/player/PlayerEngine;", "currentPlayer", "getCurrentPlayer", "()Lcom/nativeplayer/player/PlayerEngine;", "exoPlayer", "Landroidx/media3/exoplayer/ExoPlayer;", "exoPlayerEngine", "Lcom/nativeplayer/player/ExoPlayerEngine;", "getAudioTracks", "", "Lcom/nativeplayer/player/AudioTrack;", "getCurrentAudioTrack", "getCurrentPosition", "", "getCurrentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "getDuration", "getLastError", "", "getPlaybackSpeed", "", "getState", "Lcom/nativeplayer/player/PlayerState;", "getSubtitleTracks", "initialize", "url", "surface", "Landroid/view/SurfaceView;", "isPlaying", "", "pause", "", "play", "release", "seekTo", "positionMs", "selectAudioTrack", "trackId", "selectSubtitleTrack", "setAspectRatio", "aspectRatio", "Lcom/nativeplayer/data/model/AspectRatio;", "setOnStateChangeListener", "listener", "Lkotlin/Function1;", "setOnTimeUpdateListener", "Lkotlin/Function2;", "setPlaybackSpeed", "speed", "setSubtitleView", "subtitleView", "Landroidx/media3/ui/SubtitleView;", "stop", "app_debug"})
public final class PlayerManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    private final int bufferSizeSeconds = 0;
    @org.jetbrains.annotations.Nullable()
    private androidx.media3.exoplayer.ExoPlayer exoPlayer;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.ExoPlayerEngine exoPlayerEngine;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.PlayerEngine currentEngine;
    
    public PlayerManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, int bufferSizeSeconds) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.player.PlayerEngine getCurrentPlayer() {
        return null;
    }
    
    /**
     * Initialisiert den Player mit einer URL und bindet ihn an eine SurfaceView.
     * Erzeugt IMMER einen neuen ExoPlayer, um zu verhindern dass ein alter,
     * noch nicht freigegebener Player weiterlaeuft (doppelte Streams).
     */
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.player.ExoPlayerEngine initialize(@org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    android.view.SurfaceView surface) {
        return null;
    }
    
    /**
     * Spielt die Wiedergabe ab (startet/fortsetzen).
     */
    public final void play() {
    }
    
    /**
     * Pausiert die Wiedergabe.
     */
    public final void pause() {
    }
    
    /**
     * Stoppt die Wiedergabe (bleibt initialisierbar).
     */
    public final void stop() {
    }
    
    /**
     * Spult zu einer Position (in Millisekunden).
     */
    public final void seekTo(long positionMs) {
    }
    
    /**
     * Gibt die aktuelle Position zurück (in Millisekunden).
     */
    public final long getCurrentPosition() {
        return 0L;
    }
    
    /**
     * Gibt die Gesamtdauer zurück (in Millisekunden).
     */
    public final long getDuration() {
        return 0L;
    }
    
    /**
     * Gibt zurück, ob gerade abgespielt wird.
     */
    public final boolean isPlaying() {
        return false;
    }
    
    /**
     * Gibt den aktuellen Player-Zustand zurück.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.player.PlayerState getState() {
        return null;
    }
    
    /**
     * Setzt die Wiedergabegeschwindigkeit (0.5f - 3.0f).
     */
    public final void setPlaybackSpeed(float speed) {
    }
    
    /**
     * Gibt die aktuelle Wiedergabegeschwindigkeit zurück.
     */
    public final float getPlaybackSpeed() {
        return 0.0F;
    }
    
    /**
     * Bindet eine SubtitleView für die Untertitel-Darstellung. Muss nach initialize() aufgerufen werden.
     */
    public final void setSubtitleView(@org.jetbrains.annotations.NotNull()
    androidx.media3.ui.SubtitleView subtitleView) {
    }
    
    /**
     * Registriert einen Listener für Zustandsänderungen.
     */
    public final void setOnStateChangeListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> listener) {
    }
    
    /**
     * Registriert einen Listener für Zeitaktualisierungen.
     */
    public final void setOnTimeUpdateListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> listener) {
    }
    
    /**
     * Gibt die letzte Fehlermeldung zurück (falls vorhanden).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastError() {
        return null;
    }
    
    /**
     * Gibt alle verfügbaren Untertitel-Tracks zurück.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.player.SubtitleTrack> getSubtitleTracks() {
        return null;
    }
    
    /**
     * Gibt alle verfügbaren Audio-Tracks zurück.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.player.AudioTrack> getAudioTracks() {
        return null;
    }
    
    /**
     * Wählt einen Untertitel-Track aus (null oder "none" deaktiviert Untertitel).
     */
    public final void selectSubtitleTrack(@org.jetbrains.annotations.Nullable()
    java.lang.String trackId) {
    }
    
    /**
     * Wählt einen Audio-Track aus.
     */
    public final void selectAudioTrack(@org.jetbrains.annotations.NotNull()
    java.lang.String trackId) {
    }
    
    /**
     * Gibt den aktuell ausgewählten Untertitel-Track zurück.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.player.SubtitleTrack getCurrentSubtitleTrack() {
        return null;
    }
    
    /**
     * Gibt den aktuell ausgewählten Audio-Track zurück.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.player.AudioTrack getCurrentAudioTrack() {
        return null;
    }
    
    /**
     * Setzt das Seitenverhältnis des Videos.
     */
    public final void setAspectRatio(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.AspectRatio aspectRatio) {
    }
    
    /**
     * Gibt alle Ressourcen frei. Der Manager kann nach release() nicht mehr verwendet werden.
     */
    public final void release() {
    }
}