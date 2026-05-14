package com.nativeplayer.ui.player;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.player.PlayerState;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\nJ\b\u0010\f\u001a\u00020\bH\u0014JF\u0010\r\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\b\b\u0002\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u0013\u001a\u00020\n2\b\b\u0002\u0010\u0014\u001a\u00020\nJ \u0010\u0015\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\nJ\u0006\u0010\u0016\u001a\u00020\bR\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/nativeplayer/ui/player/PlayerViewModel;", "Landroidx/lifecycle/ViewModel;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "(Lcom/nativeplayer/data/repository/WatchHistoryRepository;)V", "saveJob", "Lkotlinx/coroutines/Job;", "clearPosition", "", "contentId", "", "url", "onCleared", "savePosition", "position", "", "duration", "type", "Lcom/nativeplayer/ui/player/ContentType;", "title", "imageUrl", "startPeriodicSave", "stopPeriodicSave", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PlayerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job saveJob;
    
    @javax.inject.Inject()
    public PlayerViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository) {
        super();
    }
    
    /**
     * Speichert die aktuelle Position in der Watch-History.
     */
    public final void savePosition(@org.jetbrains.annotations.NotNull()
    java.lang.String url, long position, long duration, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.player.ContentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String contentId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUrl) {
    }
    
    /**
     * Startet periodisches Speichern der Position (alle 15 Sekunden).
     * Verhindert Datenverlust bei App-Abstürzen.
     */
    public final void startPeriodicSave(@org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.player.ContentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    /**
     * Löscht den gespeicherten Fortschritt für einen Film (z.B. wenn er zuende gesehen wurde).
     */
    public final void clearPosition(@org.jetbrains.annotations.NotNull()
    java.lang.String contentId, @org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    /**
     * Stoppt das periodische Speichern.
     */
    public final void stopPeriodicSave() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
}