package com.nativeplayer.player;

import android.view.SurfaceView;

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
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019H\u0016J\n\u0010\u001b\u001a\u0004\u0018\u00010\u001aH\u0016J\n\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\u000e\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001d0\u0019H\u0016J\u0010\u0010!\u001a\u00020\u00132\u0006\u0010\"\u001a\u00020\u0015H\u0016J\b\u0010#\u001a\u00020\u0013H\u0016J\b\u0010$\u001a\u00020\u0013H\u0016J\b\u0010%\u001a\u00020\u0013H\u0016J\u0010\u0010&\u001a\u00020\u00132\u0006\u0010\'\u001a\u00020\u0004H\u0016J\u0010\u0010(\u001a\u00020\u00132\u0006\u0010)\u001a\u00020\u0015H\u0016J\u0012\u0010*\u001a\u00020\u00132\b\u0010)\u001a\u0004\u0018\u00010\u0015H\u0016J\u001c\u0010+\u001a\u00020\u00132\u0012\u0010,\u001a\u000e\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u00130\u0012H\u0016J\"\u0010-\u001a\u00020\u00132\u0018\u0010,\u001a\u0014\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00130\u0017H\u0016J\u0010\u0010.\u001a\u00020\u00132\u0006\u0010/\u001a\u00020\u001fH\u0016J\u0010\u00100\u001a\u00020\u00132\u0006\u00101\u001a\u000202H\u0016J\b\u00103\u001a\u00020\u0013H\u0016R\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001e\u0010\b\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u001e\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0003\u001a\u00020\n@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001e\u0010\u000e\u001a\u00020\r2\u0006\u0010\u0003\u001a\u00020\r@RX\u0096\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u001c\u0010\u0011\u001a\u0010\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\"\u0010\u0016\u001a\u0016\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0013\u0018\u00010\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/nativeplayer/player/IjkPlayerEngine;", "Lcom/nativeplayer/player/PlayerEngine;", "()V", "<set-?>", "", "currentPosition", "getCurrentPosition", "()J", "duration", "getDuration", "", "isPlaying", "()Z", "Lcom/nativeplayer/player/PlayerState;", "state", "getState", "()Lcom/nativeplayer/player/PlayerState;", "stateListener", "Lkotlin/Function1;", "", "targetUrl", "", "timeListener", "Lkotlin/Function2;", "getAudioTracks", "", "Lcom/nativeplayer/player/AudioTrack;", "getCurrentAudioTrack", "getCurrentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "getPlaybackSpeed", "", "getSubtitleTracks", "initialize", "url", "pause", "play", "release", "seekTo", "positionMs", "selectAudioTrack", "trackId", "selectSubtitleTrack", "setOnStateChangeListener", "listener", "setOnTimeUpdateListener", "setPlaybackSpeed", "speed", "setSurface", "surface", "Landroid/view/SurfaceView;", "stop", "app_release"})
@java.lang.Deprecated()
public final class IjkPlayerEngine implements com.nativeplayer.player.PlayerEngine {
    @org.jetbrains.annotations.NotNull()
    private com.nativeplayer.player.PlayerState state = com.nativeplayer.player.PlayerState.IDLE;
    private long currentPosition = 0L;
    private long duration = 0L;
    private boolean isPlaying = false;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> stateListener;
    @org.jetbrains.annotations.Nullable()
    private kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> timeListener;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String targetUrl = "";
    
    public IjkPlayerEngine() {
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
    public void release() {
    }
    
    @java.lang.Override()
    public void setOnStateChangeListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> listener) {
    }
    
    @java.lang.Override()
    public void setOnTimeUpdateListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> listener) {
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
}