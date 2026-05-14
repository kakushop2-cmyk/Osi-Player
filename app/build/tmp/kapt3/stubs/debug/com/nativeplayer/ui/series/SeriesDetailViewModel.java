package com.nativeplayer.ui.series;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.FavoriteEntity;
import com.nativeplayer.data.model.Series;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001b\u001a\u00020\u00182\u0006\u0010\u0013\u001a\u00020\rR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0012R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0019\u0010\u0015\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0012R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/nativeplayer/ui/series/SeriesDetailViewModel;", "Landroidx/lifecycle/ViewModel;", "iptvRepository", "Lcom/nativeplayer/data/repository/IptvRepository;", "favoriteRepository", "Lcom/nativeplayer/data/repository/FavoriteRepository;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/FavoriteRepository;Lcom/nativeplayer/data/repository/WatchHistoryRepository;)V", "_isFavorite", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_series", "Lcom/nativeplayer/data/model/Series;", "_watchHistory", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "isFavorite", "Lkotlinx/coroutines/flow/StateFlow;", "()Lkotlinx/coroutines/flow/StateFlow;", "series", "getSeries", "watchHistory", "getWatchHistory", "load", "", "seriesId", "", "toggleFavorite", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SeriesDetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository iptvRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.FavoriteRepository favoriteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isFavorite = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFavorite = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.WatchHistoryEntity> _watchHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.WatchHistoryEntity> watchHistory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.Series> _series = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Series> series = null;
    
    @javax.inject.Inject()
    public SeriesDetailViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository iptvRepository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteRepository favoriteRepository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isFavorite() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.WatchHistoryEntity> getWatchHistory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Series> getSeries() {
        return null;
    }
    
    public final void load(@org.jetbrains.annotations.NotNull()
    java.lang.String seriesId) {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Series series) {
    }
}