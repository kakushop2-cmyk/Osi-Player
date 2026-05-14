package com.nativeplayer.ui.home;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0007R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/nativeplayer/ui/home/HomeViewModel;", "Landroidx/lifecycle/ViewModel;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "(Lcom/nativeplayer/data/repository/WatchHistoryRepository;)V", "_lastSelectedTab", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "lastSelectedTab", "Lkotlinx/coroutines/flow/StateFlow;", "getLastSelectedTab", "()Lkotlinx/coroutines/flow/StateFlow;", "recentWatch", "", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "getRecentWatch", "saveSelectedTab", "", "tabIndex", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HomeViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> recentWatch = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _lastSelectedTab = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> lastSelectedTab = null;
    
    @javax.inject.Inject()
    public HomeViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getRecentWatch() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getLastSelectedTab() {
        return null;
    }
    
    public final void saveSelectedTab(int tabIndex) {
    }
}