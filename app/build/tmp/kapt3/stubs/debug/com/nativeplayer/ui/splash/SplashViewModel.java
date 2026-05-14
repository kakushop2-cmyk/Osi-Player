package com.nativeplayer.ui.splash;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015J\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\t0\u0017J\b\u0010\u0018\u001a\u0004\u0018\u00010\tJ\u0012\u0010\u0019\u001a\u00020\u00132\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\tJ\u000e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\f\u001a\u00020\tJ\u000e\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f\u00a8\u0006\u001c"}, d2 = {"Lcom/nativeplayer/ui/splash/SplashViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/PreferencesManager;)V", "_account", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/nativeplayer/data/model/Account;", "_state", "Lcom/nativeplayer/ui/splash/SplashState;", "account", "Lkotlinx/coroutines/flow/StateFlow;", "getAccount", "()Lkotlinx/coroutines/flow/StateFlow;", "state", "getState", "deleteAccount", "", "accountId", "", "getAccounts", "", "getActiveAccount", "loadData", "saveAccount", "setActiveAccount", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SplashViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
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
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
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
}