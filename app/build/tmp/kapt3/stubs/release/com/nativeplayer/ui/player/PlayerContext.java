package com.nativeplayer.ui.player;

import com.nativeplayer.data.model.Channel;

/**
 * Einfacher In-Memory-Holder für den aktuellen Live-TV-Kontext.
 * Wird vor Navigation zum FullScreenPlayer befüllt und dort ausgelesen.
 * So können Sender im Vollbildmodus mit Hoch/Runter gewechselt werden,
 * ohne die gesamte Liste über Navigation-Argumente zu schleifen.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000e\u001a\u00020\u000fJ\b\u0010\u0010\u001a\u0004\u0018\u00010\u0005J\u0006\u0010\u0011\u001a\u00020\u0012J\u0006\u0010\u0013\u001a\u00020\u0012J\b\u0010\u0014\u001a\u0004\u0018\u00010\u0005J\b\u0010\u0015\u001a\u0004\u0018\u00010\u0005J\u001c\u0010\u0016\u001a\u00020\u000f2\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u000b\u001a\u00020\u0007R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u00048F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\u00078F\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0017"}, d2 = {"Lcom/nativeplayer/ui/player/PlayerContext;", "", "()V", "_channels", "", "Lcom/nativeplayer/data/model/Channel;", "_currentIndex", "", "channels", "getChannels", "()Ljava/util/List;", "currentIndex", "getCurrentIndex", "()I", "clear", "", "current", "hasNext", "", "hasPrevious", "next", "previous", "setLiveContext", "app_release"})
public final class PlayerContext {
    @org.jetbrains.annotations.NotNull()
    private static java.util.List<com.nativeplayer.data.model.Channel> _channels;
    private static int _currentIndex = -1;
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.ui.player.PlayerContext INSTANCE = null;
    
    private PlayerContext() {
        super();
    }
    
    /**
     * Setzt die aktuelle Senderliste und den Index.
     * Sollte direkt vor navController.navigate() aufgerufen werden.
     */
    public final void setLiveContext(@org.jetbrains.annotations.NotNull()
    java.util.List<com.nativeplayer.data.model.Channel> channels, int currentIndex) {
    }
    
    /**
     * Löscht den Kontext (z.B. wenn kein Live-TV).
     */
    public final void clear() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Channel> getChannels() {
        return null;
    }
    
    public final int getCurrentIndex() {
        return 0;
    }
    
    /**
     * Gibt es einen vorherigen Sender?
     */
    public final boolean hasPrevious() {
        return false;
    }
    
    /**
     * Gibt es einen nächsten Sender?
     */
    public final boolean hasNext() {
        return false;
    }
    
    /**
     * Wechselt zum vorherigen Sender und gibt ihn zurück.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.Channel previous() {
        return null;
    }
    
    /**
     * Wechselt zum nächsten Sender und gibt ihn zurück.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.Channel next() {
        return null;
    }
    
    /**
     * Aktueller Sender oder null.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.Channel current() {
        return null;
    }
}