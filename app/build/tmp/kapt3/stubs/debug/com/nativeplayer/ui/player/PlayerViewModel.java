package com.nativeplayer.ui.player;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.EpgItem;
import com.nativeplayer.data.model.PlayerSettings;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.player.PlayerState;
import com.nativeplayer.data.model.Episode;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001a\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u000eJ\u0006\u0010\u0010\u001a\u00020\u0011J\u001c\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u0015\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0016J(\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010\u001dJ\b\u0010\u001e\u001a\u00020\fH\u0014JZ\u0010\u001f\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\b\b\u0002\u0010#\u001a\u00020$2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010%\u001a\u00020\u000e2\b\b\u0002\u0010&\u001a\u00020\u000e2\b\b\u0002\u0010\'\u001a\u00020\u001b2\b\b\u0002\u0010(\u001a\u00020\u001bJ \u0010)\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010#\u001a\u00020$2\b\b\u0002\u0010%\u001a\u00020\u000eJ\u0006\u0010*\u001a\u00020\fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006+"}, d2 = {"Lcom/nativeplayer/ui/player/PlayerViewModel;", "Landroidx/lifecycle/ViewModel;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "iptvRepository", "Lcom/nativeplayer/data/repository/IptvRepository;", "(Lcom/nativeplayer/data/repository/WatchHistoryRepository;Lcom/nativeplayer/data/repository/PreferencesManager;Lcom/nativeplayer/data/repository/IptvRepository;)V", "saveJob", "Lkotlinx/coroutines/Job;", "clearPosition", "", "contentId", "", "url", "getPlayerSettings", "Lcom/nativeplayer/data/model/PlayerSettings;", "loadEpgForChannel", "", "Lcom/nativeplayer/data/model/EpgItem;", "channelId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadNextEpisode", "Lcom/nativeplayer/ui/player/NextEpisodeInfo;", "seriesId", "currentSeason", "", "currentEpisode", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onCleared", "savePosition", "position", "", "duration", "type", "Lcom/nativeplayer/ui/player/ContentType;", "title", "imageUrl", "seasonNumber", "episodeNumber", "startPeriodicSave", "stopPeriodicSave", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PlayerViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository iptvRepository = null;
    @org.jetbrains.annotations.Nullable()
    private kotlinx.coroutines.Job saveJob;
    
    @javax.inject.Inject()
    public PlayerViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository iptvRepository) {
        super();
    }
    
    /**
     * Liest die gespeicherten Player-Einstellungen.
     */
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.model.PlayerSettings getPlayerSettings() {
        return null;
    }
    
    /**
     * Speichert die aktuelle Position in der Watch-History.
     */
    public final void savePosition(@org.jetbrains.annotations.NotNull()
    java.lang.String url, long position, long duration, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.player.ContentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String contentId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUrl, int seasonNumber, int episodeNumber) {
    }
    
    /**
     * Lädt die nächste Episode einer Serie basierend auf aktueller Staffel/Episode.
     * Durchläuft die Episoden innerhalb der aktuellen Staffel,
     * dann die nächste Staffel.
     * @param seriesId Die Serien-ID
     * @param currentSeason Aktuelle Staffelnummer
     * @param currentEpisode Aktuelle Episodennummer
     * @return NextEpisodeInfo oder null, wenn keine nächste Episode existiert
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadNextEpisode(@org.jetbrains.annotations.NotNull()
    java.lang.String seriesId, int currentSeason, int currentEpisode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.ui.player.NextEpisodeInfo> $completion) {
        return null;
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
     * Lädt EPG-Daten für einen Live-Kanal (für Kanalwechsel im Vollbild).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadEpgForChannel(@org.jetbrains.annotations.NotNull()
    java.lang.String channelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.EpgItem>> $completion) {
        return null;
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