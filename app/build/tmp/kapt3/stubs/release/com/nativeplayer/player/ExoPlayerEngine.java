package com.nativeplayer.player;

import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceView;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.common.TrackSelectionParameters;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.common.Tracks;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010%\u001a\b\u0012\u0004\u0012\u00020\u00060&H\u0016J\n\u0010\'\u001a\u0004\u0018\u00010\u0006H\u0016J\n\u0010(\u001a\u0004\u0018\u00010\u000eH\u0016J\b\u0010)\u001a\u0004\u0018\u00010\u0017J\b\u0010*\u001a\u00020\fH\u0016J\u000e\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000e0&H\u0016J\u0010\u0010,\u001a\u00020\u001e2\u0006\u0010-\u001a\u00020\u0017H\u0016J\b\u0010.\u001a\u00020\u001eH\u0016J\b\u0010/\u001a\u00020\u001eH\u0016J\b\u00100\u001a\u00020\u001eH\u0016J\u0010\u00101\u001a\u00020\u001e2\u0006\u00102\u001a\u00020\bH\u0016J\u0010\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u0017H\u0016J\u0012\u00105\u001a\u00020\u001e2\b\u00104\u001a\u0004\u0018\u00010\u0017H\u0016J\u001c\u00106\u001a\u00020\u001e2\u0012\u00107\u001a\u000e\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\"\u00108\u001a\u00020\u001e2\u0018\u00107\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u001e0 H\u0016J\u0010\u00109\u001a\u00020\u001e2\u0006\u0010:\u001a\u00020\fH\u0016J\u0010\u0010;\u001a\u00020\u001e2\u0006\u0010<\u001a\u00020=H\u0016J\b\u0010>\u001a\u00020\u001eH\u0002J\b\u0010?\u001a\u00020\u001eH\u0016J\b\u0010@\u001a\u00020\u001eH\u0002J\b\u0010A\u001a\u00020\u001eH\u0002J\u0010\u0010B\u001a\u00020\u001e2\u0006\u0010C\u001a\u00020DH\u0002R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u0011\u001a\u00020\u0018@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001c\u0010\u001c\u001a\u0010\u0012\u0004\u0012\u00020\u0018\u0012\u0004\u0012\u00020\u001e\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u001f\u001a\u0016\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u001e\u0018\u00010 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006E"}, d2 = {"Lcom/nativeplayer/player/ExoPlayerEngine;", "Lcom/nativeplayer/player/PlayerEngine;", "exoPlayer", "Landroidx/media3/exoplayer/ExoPlayer;", "(Landroidx/media3/exoplayer/ExoPlayer;)V", "currentAudioTrack", "Lcom/nativeplayer/player/AudioTrack;", "currentPosition", "", "getCurrentPosition", "()J", "currentSpeed", "", "currentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "duration", "getDuration", "<set-?>", "", "isPlaying", "()Z", "isReleased", "lastError", "", "Lcom/nativeplayer/player/PlayerState;", "state", "getState", "()Lcom/nativeplayer/player/PlayerState;", "stateListener", "Lkotlin/Function1;", "", "timeListener", "Lkotlin/Function2;", "timeUpdateHandler", "Landroid/os/Handler;", "timeUpdateRunnable", "Ljava/lang/Runnable;", "getAudioTracks", "", "getCurrentAudioTrack", "getCurrentSubtitleTrack", "getLastError", "getPlaybackSpeed", "getSubtitleTracks", "initialize", "url", "pause", "play", "release", "seekTo", "positionMs", "selectAudioTrack", "trackId", "selectSubtitleTrack", "setOnStateChangeListener", "listener", "setOnTimeUpdateListener", "setPlaybackSpeed", "speed", "setSurface", "surface", "Landroid/view/SurfaceView;", "startTimeUpdates", "stop", "stopTimeUpdates", "syncPlayerState", "updateCurrentTracks", "tracks", "Landroidx/media3/common/Tracks;", "app_release"})
public final class ExoPlayerEngine implements com.nativeplayer.player.PlayerEngine {
    @org.jetbrains.annotations.NotNull()
    private final androidx.media3.exoplayer.ExoPlayer exoPlayer = null;
    @org.jetbrains.annotations.NotNull()
    private com.nativeplayer.player.PlayerState state = com.nativeplayer.player.PlayerState.IDLE;
    private boolean isPlaying = false;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> stateListener;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> timeListener;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String lastError;
    private float currentSpeed = 1.0F;
    private boolean isReleased = false;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.SubtitleTrack currentSubtitleTrack;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.player.AudioTrack currentAudioTrack;
    @org.jetbrains.annotations.NotNull()
    private final android.os.Handler timeUpdateHandler = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Runnable timeUpdateRunnable = null;
    
    public ExoPlayerEngine(@org.jetbrains.annotations.NotNull()
    androidx.media3.exoplayer.ExoPlayer exoPlayer) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.nativeplayer.player.PlayerState getState() {
        return null;
    }
    
    @java.lang.Override()
    public long getCurrentPosition() {
        return 0L;
    }
    
    @java.lang.Override()
    public long getDuration() {
        return 0L;
    }
    
    @java.lang.Override()
    public boolean isPlaying() {
        return false;
    }
    
    /**
     * Fehlermeldung des letzten Playback-Fehlers (für UI-Anzeige)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getLastError() {
        return null;
    }
    
    /**
     * Aktualisiert die aktuell ausgewählten Tracks basierend auf den verfügbaren Tracks.
     */
    private final void updateCurrentTracks(androidx.media3.common.Tracks tracks) {
    }
    
    /**
     * Konsolidiert State- und isPlaying-Updates aus beiden Quellen
     * ([onPlaybackStateChanged] + [onIsPlayingChanged]) in einer Methode,
     * um Race-Conditions durch undefinierte Callback-Reihenfolge zu vermeiden.
     */
    private final void syncPlayerState() {
    }
    
    @java.lang.Override()
    public void initialize(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    @java.lang.Override()
    public void setSurface(@org.jetbrains.annotations.NotNull()
    android.view.SurfaceView surface) {
    }
    
    @java.lang.Override()
    public void play() {
    }
    
    @java.lang.Override()
    public void pause() {
    }
    
    @java.lang.Override()
    public void stop() {
    }
    
    @java.lang.Override()
    public void seekTo(long positionMs) {
    }
    
    @java.lang.Override()
    public void setPlaybackSpeed(float speed) {
    }
    
    @java.lang.Override()
    public float getPlaybackSpeed() {
        return 0.0F;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.nativeplayer.player.SubtitleTrack> getSubtitleTracks() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.nativeplayer.player.AudioTrack> getAudioTracks() {
        return null;
    }
    
    @java.lang.Override()
    public void selectSubtitleTrack(@org.jetbrains.annotations.Nullable()
    java.lang.String trackId) {
    }
    
    @java.lang.Override()
    public void selectAudioTrack(@org.jetbrains.annotations.NotNull()
    java.lang.String trackId) {
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.nativeplayer.player.SubtitleTrack getCurrentSubtitleTrack() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public com.nativeplayer.player.AudioTrack getCurrentAudioTrack() {
        return null;
    }
    
    @java.lang.Override()
    public void release() {
    }
    
    private final void startTimeUpdates() {
    }
    
    private final void stopTimeUpdates() {
    }
    
    @java.lang.Override()
    public void setOnStateChangeListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> listener) {
    }
    
    @java.lang.Override()
    public void setOnTimeUpdateListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> listener) {
    }
}