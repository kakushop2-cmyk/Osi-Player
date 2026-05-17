package com.nativeplayer.ui.player;

import com.nativeplayer.data.model.Channel;
import com.nativeplayer.data.model.EpgItem;

/**
 * Einfacher In-Memory-Holder für den aktuellen Live-TV-Kontext.
 * Wird vor Navigation zum FullScreenPlayer befüllt und dort ausgelesen.
 * So können Sender im Vollbildmodus mit Hoch/Runter gewechselt werden,
 * ohne die gesamte Liste über Navigation-Argumente zu schleifen.
 *
 * Auch enthält EPG-Daten für Live-TV Fullscreen UI.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u001c\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010+\u001a\u00020,J\b\u0010-\u001a\u0004\u0018\u00010\u0007J\u0006\u0010.\u001a\u00020/J\u0006\u00100\u001a\u00020/J\b\u00101\u001a\u0004\u0018\u00010\u0007J\b\u00102\u001a\u0004\u0018\u00010\u0007JD\u00103\u001a\u00020,2\b\u0010\'\u001a\u0004\u0018\u00010\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\t2\b\u0010%\u001a\u0004\u0018\u00010\t2\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010\u001f\u001a\u00020\r2\u0006\u0010)\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0004J\u001c\u00104\u001a\u00020,2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u001c\u001a\u00020\u000bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0013\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018R\u0013\u0010\u0019\u001a\u0004\u0018\u00010\t8F\u00a2\u0006\u0006\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u001eR\u0011\u0010\u001f\u001a\u00020\r8F\u00a2\u0006\u0006\u001a\u0004\b \u0010!R\u0011\u0010\"\u001a\u00020\u000f8F\u00a2\u0006\u0006\u001a\u0004\b#\u0010$R\u0013\u0010%\u001a\u0004\u0018\u00010\t8F\u00a2\u0006\u0006\u001a\u0004\b&\u0010\u001bR\u0013\u0010\'\u001a\u0004\u0018\u00010\t8F\u00a2\u0006\u0006\u001a\u0004\b(\u0010\u001bR\u0011\u0010)\u001a\u00020\u000b8F\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u001e\u00a8\u00065"}, d2 = {"Lcom/nativeplayer/ui/player/PlayerContext;", "", "()V", "_categoryTimezone", "", "_channels", "", "Lcom/nativeplayer/data/model/Channel;", "_currentEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "_currentIndex", "", "_currentTimeInZone", "", "_epgProgress", "", "_nextEpgItem", "_previousEpgItem", "_remainingMinutes", "categoryTimezone", "getCategoryTimezone", "()Ljava/lang/String;", "channels", "getChannels", "()Ljava/util/List;", "currentEpgItem", "getCurrentEpgItem", "()Lcom/nativeplayer/data/model/EpgItem;", "currentIndex", "getCurrentIndex", "()I", "currentTimeInZone", "getCurrentTimeInZone", "()J", "epgProgress", "getEpgProgress", "()F", "nextEpgItem", "getNextEpgItem", "previousEpgItem", "getPreviousEpgItem", "remainingMinutes", "getRemainingMinutes", "clear", "", "current", "hasNext", "", "hasPrevious", "next", "previous", "setEpgData", "setLiveContext", "app_debug"})
public final class PlayerContext {
    @org.jetbrains.annotations.NotNull()
    private static java.util.List<com.nativeplayer.data.model.Channel> _channels;
    private static int _currentIndex = -1;
    @org.jetbrains.annotations.Nullable()
    private static com.nativeplayer.data.model.EpgItem _previousEpgItem;
    @org.jetbrains.annotations.Nullable()
    private static com.nativeplayer.data.model.EpgItem _currentEpgItem;
    @org.jetbrains.annotations.Nullable()
    private static com.nativeplayer.data.model.EpgItem _nextEpgItem;
    private static float _epgProgress = 0.0F;
    private static long _currentTimeInZone = 0L;
    private static int _remainingMinutes = 0;
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String _categoryTimezone = "UTC";
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
     * Setzt EPG-Daten für Live-TV Fullscreen UI.
     */
    public final void setEpgData(@org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem previousEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem nextEpgItem, float epgProgress, long currentTimeInZone, int remainingMinutes, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryTimezone) {
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
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.EpgItem getPreviousEpgItem() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.EpgItem getCurrentEpgItem() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.EpgItem getNextEpgItem() {
        return null;
    }
    
    public final float getEpgProgress() {
        return 0.0F;
    }
    
    public final long getCurrentTimeInZone() {
        return 0L;
    }
    
    public final int getRemainingMinutes() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCategoryTimezone() {
        return null;
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