package com.nativeplayer.ui.home;

import android.app.Application;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import com.nativeplayer.data.model.Channel;
import com.nativeplayer.data.model.Episode;
import com.nativeplayer.data.model.Movie;
import com.nativeplayer.data.model.Season;
import com.nativeplayer.data.model.Series;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import android.util.Log;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0094\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001:\u0002FGB\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u00101\u001a\u000202H\u0002J\u000e\u00103\u001a\u00020\u001e2\u0006\u00104\u001a\u00020\u001dJ\u000e\u00105\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\b\u00106\u001a\u000202H\u0014J\u000e\u00107\u001a\u0002022\u0006\u00104\u001a\u00020\u001dJ?\u00108\u001a\b\u0012\u0004\u0012\u0002H:09\"\u0004\b\u0000\u0010:2\u0006\u0010;\u001a\u00020<2\u001c\u0010=\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H:0?\u0012\u0006\u0012\u0004\u0018\u00010\u001a0>H\u0002\u00a2\u0006\u0002\u0010@J\u000e\u0010A\u001a\u0002022\u0006\u00104\u001a\u00020\u001dJ\u0006\u0010B\u001a\u000202J\u000e\u0010C\u001a\u0002022\u0006\u0010D\u001a\u00020\u000eJ\u000e\u0010E\u001a\u0002022\u0006\u0010D\u001a\u00020\u000eR\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u000e\u0010\u0019\u001a\u00020\u001aX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u001b\u001a\u000e\u0012\u0004\u0012\u00020\u001d\u0012\u0004\u0012\u00020\u001e0\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0014R\u000e\u0010!\u001a\u00020\u000eX\u0082D\u00a2\u0006\u0002\n\u0000R\u001d\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020#0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0014R\u0017\u0010%\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0014R\u0017\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0014R\u001d\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020*0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0014R\u001d\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020-0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0014R\u001d\u0010/\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b0\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006H"}, d2 = {"Lcom/nativeplayer/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "iptvRepository", "Lcom/nativeplayer/data/repository/IptvRepository;", "application", "Landroid/app/Application;", "(Lcom/nativeplayer/data/repository/WatchHistoryRepository;Lcom/nativeplayer/data/repository/IptvRepository;Landroid/app/Application;)V", "_fuerDichMovies", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/nativeplayer/data/model/Movie;", "_selectedHeroIndex", "", "_selectedTopRatedIndex", "fuerDichIndex", "fuerDichMovies", "Lkotlinx/coroutines/flow/StateFlow;", "getFuerDichMovies", "()Lkotlinx/coroutines/flow/StateFlow;", "fuerDichPool", "heroMoviesByCategory", "Lcom/nativeplayer/ui/home/HomeViewModel$HeroCategoryMovie;", "getHeroMoviesByCategory", "heroPlayerLock", "", "heroPlayers", "", "", "Landroidx/media3/exoplayer/ExoPlayer;", "latestContent", "getLatestContent", "maxCachedPlayers", "recentWatch", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "getRecentWatch", "selectedHeroIndex", "getSelectedHeroIndex", "selectedTopRatedIndex", "getSelectedTopRatedIndex", "seriesByCategory", "Lcom/nativeplayer/ui/home/HomeViewModel$SeriesCategoryGroup;", "getSeriesByCategory", "sportChannels", "Lcom/nativeplayer/data/model/Channel;", "getSportChannels", "topRatedMovies", "getTopRatedMovies", "emitFuerDichPair", "", "getOrCreateHeroPlayer", "streamUrl", "loadFuerDichPool", "onCleared", "pauseHero", "pollingFlow", "Lkotlinx/coroutines/flow/Flow;", "T", "intervalMs", "", "source", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "(JLkotlin/jvm/functions/Function1;)Lkotlinx/coroutines/flow/Flow;", "prepareAndPlayHero", "stopAllHeroPlayers", "updateHeroIndex", "index", "updateTopRatedIndex", "HeroCategoryMovie", "SeriesCategoryGroup", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository iptvRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.Application application = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, androidx.media3.exoplayer.ExoPlayer> heroPlayers = null;
    private final int maxCachedPlayers = 3;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.Object heroPlayerLock = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> recentWatch = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> latestContent = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.ui.home.HomeViewModel.HeroCategoryMovie>> heroMoviesByCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _selectedHeroIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> selectedHeroIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.nativeplayer.data.model.Movie>> _fuerDichMovies = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> fuerDichMovies = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.nativeplayer.data.model.Movie> fuerDichPool;
    private int fuerDichIndex = 0;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Channel>> sportChannels = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> topRatedMovies = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _selectedTopRatedIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> selectedTopRatedIndex = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.ui.home.HomeViewModel.SeriesCategoryGroup>> seriesByCategory = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository iptvRepository, @org.jetbrains.annotations.NotNull()
    android.app.Application application) {
        super();
    }
    
    /**
     * Gibt einen gecachten ExoPlayer zurück oder erstellt einen neuen
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.media3.exoplayer.ExoPlayer getOrCreateHeroPlayer(@org.jetbrains.annotations.NotNull()
    java.lang.String streamUrl) {
        return null;
    }
    
    /**
     * Bereitet den Player vor und startet ihn an einer zufälligen Position
     */
    public final void prepareAndPlayHero(@org.jetbrains.annotations.NotNull()
    java.lang.String streamUrl) {
    }
    
    /**
     * Stoppt einen bestimmten Player komplett und setzt ihn zurück
     */
    public final void pauseHero(@org.jetbrains.annotations.NotNull()
    java.lang.String streamUrl) {
    }
    
    /**
     * Stoppt ALLE gecachten Hero-Player sofort (z.B. beim Verlassen des HomeScreens)
     */
    public final void stopAllHeroPlayers() {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getRecentWatch() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> getLatestContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.ui.home.HomeViewModel.HeroCategoryMovie>> getHeroMoviesByCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getSelectedHeroIndex() {
        return null;
    }
    
    public final void updateHeroIndex(int index) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> getFuerDichMovies() {
        return null;
    }
    
    private final java.util.List<com.nativeplayer.data.model.Movie> loadFuerDichPool() {
        return null;
    }
    
    private final void emitFuerDichPair() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Channel>> getSportChannels() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Movie>> getTopRatedMovies() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getSelectedTopRatedIndex() {
        return null;
    }
    
    /**
     * Aktualisieren des Karussell-Index (wird vom Pager aufgerufen)
     */
    public final void updateTopRatedIndex(int index) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.ui.home.HomeViewModel.SeriesCategoryGroup>> getSeriesByCategory() {
        return null;
    }
    
    /**
     * Polling flow that waits for data to be loaded, then emits periodically.
     * - First check: wait until isDataLoaded is true (checks every 200ms)
     * - After data is loaded: emit every intervalMs
     * - If data becomes empty after being loaded (shouldn't happen), keep last value
     */
    private final <T extends java.lang.Object>kotlinx.coroutines.flow.Flow<T> pollingFlow(long intervalMs, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> source) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0014"}, d2 = {"Lcom/nativeplayer/ui/home/HomeViewModel$HeroCategoryMovie;", "", "category", "", "movie", "Lcom/nativeplayer/data/model/Movie;", "(Ljava/lang/String;Lcom/nativeplayer/data/model/Movie;)V", "getCategory", "()Ljava/lang/String;", "getMovie", "()Lcom/nativeplayer/data/model/Movie;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class HeroCategoryMovie {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String category = null;
        @org.jetbrains.annotations.NotNull()
        private final com.nativeplayer.data.model.Movie movie = null;
        
        public HeroCategoryMovie(@org.jetbrains.annotations.NotNull()
        java.lang.String category, @org.jetbrains.annotations.NotNull()
        com.nativeplayer.data.model.Movie movie) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCategory() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nativeplayer.data.model.Movie getMovie() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nativeplayer.data.model.Movie component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nativeplayer.ui.home.HomeViewModel.HeroCategoryMovie copy(@org.jetbrains.annotations.NotNull()
        java.lang.String category, @org.jetbrains.annotations.NotNull()
        com.nativeplayer.data.model.Movie movie) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J#\u0010\u000e\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0012\u001a\u00020\u0013H\u00d6\u0001J\t\u0010\u0014\u001a\u00020\u0003H\u00d6\u0001R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2 = {"Lcom/nativeplayer/ui/home/HomeViewModel$SeriesCategoryGroup;", "", "category", "", "series", "", "Lcom/nativeplayer/data/model/Series;", "(Ljava/lang/String;Ljava/util/List;)V", "getCategory", "()Ljava/lang/String;", "getSeries", "()Ljava/util/List;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
    public static final class SeriesCategoryGroup {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String category = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.nativeplayer.data.model.Series> series = null;
        
        public SeriesCategoryGroup(@org.jetbrains.annotations.NotNull()
        java.lang.String category, @org.jetbrains.annotations.NotNull()
        java.util.List<com.nativeplayer.data.model.Series> series) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getCategory() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.nativeplayer.data.model.Series> getSeries() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.nativeplayer.data.model.Series> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.nativeplayer.ui.home.HomeViewModel.SeriesCategoryGroup copy(@org.jetbrains.annotations.NotNull()
        java.lang.String category, @org.jetbrains.annotations.NotNull()
        java.util.List<com.nativeplayer.data.model.Series> series) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
}