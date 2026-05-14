package com.nativeplayer.player;

import android.view.SurfaceView;
import androidx.media3.ui.SubtitleView;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00110\u0010H&J\n\u0010\u0012\u001a\u0004\u0018\u00010\u0011H&J\n\u0010\u0013\u001a\u0004\u0018\u00010\u0014H&J\b\u0010\u0015\u001a\u00020\u0016H&J\u000e\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00140\u0010H&J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH&J\b\u0010\u001c\u001a\u00020\u0019H&J\b\u0010\u001d\u001a\u00020\u0019H&J\b\u0010\u001e\u001a\u00020\u0019H&J\u0010\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020\u0003H&J\u0010\u0010!\u001a\u00020\u00192\u0006\u0010\"\u001a\u00020\u001bH&J\u0012\u0010#\u001a\u00020\u00192\b\u0010\"\u001a\u0004\u0018\u00010\u001bH&J\u001c\u0010$\u001a\u00020\u00192\u0012\u0010%\u001a\u000e\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\u00190&H&J\"\u0010\'\u001a\u00020\u00192\u0018\u0010%\u001a\u0014\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00190(H&J\u0010\u0010)\u001a\u00020\u00192\u0006\u0010*\u001a\u00020\u0016H&J\u0010\u0010+\u001a\u00020\u00192\u0006\u0010,\u001a\u00020-H&J\u0010\u0010.\u001a\u00020\u00192\u0006\u0010/\u001a\u000200H&J\b\u00101\u001a\u00020\u0019H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\nR\u0012\u0010\u000b\u001a\u00020\fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u00a8\u00062"}, d2 = {"Lcom/nativeplayer/player/PlayerEngine;", "", "currentPosition", "", "getCurrentPosition", "()J", "duration", "getDuration", "isPlaying", "", "()Z", "state", "Lcom/nativeplayer/player/PlayerState;", "getState", "()Lcom/nativeplayer/player/PlayerState;", "getAudioTracks", "", "Lcom/nativeplayer/player/AudioTrack;", "getCurrentAudioTrack", "getCurrentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "getPlaybackSpeed", "", "getSubtitleTracks", "initialize", "", "url", "", "pause", "play", "release", "seekTo", "positionMs", "selectAudioTrack", "trackId", "selectSubtitleTrack", "setOnStateChangeListener", "listener", "Lkotlin/Function1;", "setOnTimeUpdateListener", "Lkotlin/Function2;", "setPlaybackSpeed", "speed", "setSubtitleView", "subtitleView", "Landroidx/media3/ui/SubtitleView;", "setSurface", "surface", "Landroid/view/SurfaceView;", "stop", "app_debug"})
public abstract interface PlayerEngine {
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.nativeplayer.player.PlayerState getState();
    
    public abstract long getCurrentPosition();
    
    public abstract long getDuration();
    
    public abstract boolean isPlaying();
    
    public abstract void initialize(@org.jetbrains.annotations.NotNull()
    java.lang.String url);
    
    public abstract void setSurface(@org.jetbrains.annotations.NotNull()
    android.view.SurfaceView surface);
    
    public abstract void setSubtitleView(@org.jetbrains.annotations.NotNull()
    androidx.media3.ui.SubtitleView subtitleView);
    
    public abstract void play();
    
    public abstract void pause();
    
    public abstract void stop();
    
    public abstract void seekTo(long positionMs);
    
    public abstract void setPlaybackSpeed(float speed);
    
    public abstract float getPlaybackSpeed();
    
    public abstract void release();
    
    public abstract void setOnStateChangeListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.nativeplayer.player.PlayerState, kotlin.Unit> listener);
    
    public abstract void setOnTimeUpdateListener(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.lang.Long, kotlin.Unit> listener);
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.nativeplayer.player.SubtitleTrack> getSubtitleTracks();
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.util.List<com.nativeplayer.player.AudioTrack> getAudioTracks();
    
    public abstract void selectSubtitleTrack(@org.jetbrains.annotations.Nullable()
    java.lang.String trackId);
    
    public abstract void selectAudioTrack(@org.jetbrains.annotations.NotNull()
    java.lang.String trackId);
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.nativeplayer.player.SubtitleTrack getCurrentSubtitleTrack();
    
    @org.jetbrains.annotations.Nullable()
    public abstract com.nativeplayer.player.AudioTrack getCurrentAudioTrack();
}