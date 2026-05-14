package com.nativeplayer.player;

import android.view.SurfaceView;
import androidx.media3.ui.SubtitleView;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\t\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007j\u0002\b\bj\u0002\b\t\u00a8\u0006\n"}, d2 = {"Lcom/nativeplayer/player/PlayerState;", "", "(Ljava/lang/String;I)V", "IDLE", "BUFFERING", "READY", "PLAYING", "PAUSED", "ENDED", "ERROR", "app_debug"})
public enum PlayerState {
    /*public static final*/ IDLE /* = new IDLE() */,
    /*public static final*/ BUFFERING /* = new BUFFERING() */,
    /*public static final*/ READY /* = new READY() */,
    /*public static final*/ PLAYING /* = new PLAYING() */,
    /*public static final*/ PAUSED /* = new PAUSED() */,
    /*public static final*/ ENDED /* = new ENDED() */,
    /*public static final*/ ERROR /* = new ERROR() */;
    
    PlayerState() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.nativeplayer.player.PlayerState> getEntries() {
        return null;
    }
}