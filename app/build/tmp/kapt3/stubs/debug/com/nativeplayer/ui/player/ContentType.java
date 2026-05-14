package com.nativeplayer.ui.player;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.player.PlayerState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

/**
 * Gibt den Typ des abgespielten Inhalts an.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u0000 \u00062\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0006B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0007"}, d2 = {"Lcom/nativeplayer/ui/player/ContentType;", "", "(Ljava/lang/String;I)V", "MOVIE", "SERIES", "LIVE", "Companion", "app_debug"})
public enum ContentType {
    /*public static final*/ MOVIE /* = new MOVIE() */,
    /*public static final*/ SERIES /* = new SERIES() */,
    /*public static final*/ LIVE /* = new LIVE() */;
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.ui.player.ContentType.Companion Companion = null;
    
    ContentType() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.nativeplayer.ui.player.ContentType> getEntries() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/nativeplayer/ui/player/ContentType$Companion;", "", "()V", "fromString", "Lcom/nativeplayer/ui/player/ContentType;", "value", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nativeplayer.ui.player.ContentType fromString(@org.jetbrains.annotations.NotNull()
        java.lang.String value) {
            return null;
        }
    }
}