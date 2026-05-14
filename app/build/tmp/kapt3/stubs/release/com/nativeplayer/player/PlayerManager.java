package com.nativeplayer.player;

import android.content.Context;
import android.view.SurfaceView;
import androidx.media3.exoplayer.ExoPlayer;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fJ\b\u0010\u0011\u001a\u0004\u0018\u00010\u0010J\u0006\u0010\u0012\u001a\u00020\u0013J\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015J\u0006\u0010\u0016\u001a\u00020\u0013J\b\u0010\u0017\u001a\u0004\u0018\u00010\u0018J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0006\u0010\u001b\u001a\u00020\u001cJ\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u00150\u000fJ\u0016\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u00182\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020#J\u0006\u0010$\u001a\u00020%J\u0006\u0010&\u001a\u00020%J\u0006\u0010\'\u001a\u00020%J\u000e\u0010(\u001a\u00020%2\u0006\u0010)\u001a\u00020\u0013J\u000e\u0010*\u001a\u00020%2\u0006\u0010+\u001a\u00020\u0018J\u0010\u0010,\u001a\u00020%2\b\u0010+\u001a\u0004\u0018\u00010\u0018J\u001a\u0010-\u001a\u00020%2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\u001c\u0012\u0004\u0012\u00020%0/J \u00100\u001a\u00020%2\u0018\u0010.\u001a\u0014\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020\u0013\u0012\u0004\u0012\u00020%01J\u000e\u00102\u001a\u00020%2\u0006\u00103\u001a\u00020\u001aJ\u0006\u00104\u001a\u00020%R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u00068F\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcom/nativeplayer/player/PlayerManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "currentEngine", "Lcom/nativeplayer/player/PlayerEngine;", "currentPlayer", "getCurrentPlayer", "()Lcom/nativeplayer/player/PlayerEngine;", "exoPlayer", "Landroidx/media3/exoplayer/ExoPlayer;", "exoPlayerEngine", "Lcom/nativeplayer/player/ExoPlayerEngine;", "getAudioTracks", "", "Lcom/nativeplayer/player/AudioTrack;", "getCurrentAudioTrack", "getCurrentPosition", "", "getCurrentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "getDuration", "getLastError", "", "getPlaybackSpeed", "", "getState", "Lcom/nativeplayer/player/PlayerState;", "getSubtitleTracks", "initialize", "url", "surface", "Landroid/view/SurfaceView;", "isPlaying", "", "pause", "", "play", "release", "seekTo", "positionMs", "selectAudioTrack", "trackId", "selectSubtitleTrack", "setOnStateChangeListener", "listener", "Lkotlin/Function1;", "setOnTimeUpdateListener", "Lkotlin/Function2;", "setPlaybackSpeed", "speed", "stop", "app_release"})
public final class PlayerManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.Nullable()
    private androidx.media3.exoplayer.ExoPlayer exoPlayer;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.ExoPlayerEngine exoPlayerEngine;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.PlayerEngine currentEngine;
    
    public PlayerManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
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
     * Gibt alle Ressourcen frei. Der Manager kann nach release() nicht mehr verwendet werden.
     */
    public final void release() {
    }
}