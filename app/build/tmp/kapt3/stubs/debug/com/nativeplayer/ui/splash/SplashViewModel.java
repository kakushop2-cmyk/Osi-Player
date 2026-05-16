package com.nativeplayer.ui.splash;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0014\u001a\u00020\u0015J\u000e\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018J\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000b0\u001aJ\b\u0010\u001b\u001a\u0004\u0018\u00010\u000bJ\u0012\u0010\u001c\u001a\u00020\u00152\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000bJ\u000e\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u000e\u001a\u00020\u000bJ\u000e\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018R\u0016\u0010\t\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0011R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/nativeplayer/ui/splash/SplashViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "watchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/PreferencesManager;Lcom/nativeplayer/data/repository/WatchHistoryRepository;)V", "_account", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/nativeplayer/data/model/Account;", "_state", "Lcom/nativeplayer/ui/splash/SplashState;", "account", "Lkotlinx/coroutines/flow/StateFlow;", "getAccount", "()Lkotlinx/coroutines/flow/StateFlow;", "state", "getState", "clearWatchHistory", "", "deleteAccount", "accountId", "", "getAccounts", "", "getActiveAccount", "loadData", "saveAccount", "setActiveAccount", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SplashViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.ui.splash.SplashState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.ui.splash.SplashState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.Account> _account = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Account> account = null;
    
    @javax.inject.Inject()
    public SplashViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository repository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryRepository watchHistoryRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.ui.splash.SplashState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Account> getAccount() {
        return null;
    }
    
    public final void loadData(@org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.Account account) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.Account getActiveAccount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Account> getAccounts() {
        return null;
    }
    
    public final void saveAccount(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Account account) {
    }
    
    public final void setActiveAccount(@org.jetbrains.annotations.NotNull()
    java.lang.String accountId) {
    }
    
    public final void deleteAccount(@org.jetbrains.annotations.NotNull()
    java.lang.String accountId) {
    }
    
    public final void clearWatchHistory() {
    }
}