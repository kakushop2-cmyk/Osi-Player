package com.nativeplayer.ui.settings;

import androidx.lifecycle.ViewModel;
import android.util.Log;
import com.nativeplayer.data.model.PlayerSettings;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.flow.SharedFlow;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0013\u001a\u00020\u0014J\b\u0010\u0015\u001a\u00020\u0007H\u0002J\u000e\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u000f\u001a\u00020\nR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0018"}, d2 = {"Lcom/nativeplayer/ui/settings/PlayerSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lcom/nativeplayer/data/repository/PreferencesManager;)V", "_saveResult", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "_settings", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/nativeplayer/data/model/PlayerSettings;", "saveResult", "Lkotlinx/coroutines/flow/SharedFlow;", "getSaveResult", "()Lkotlinx/coroutines/flow/SharedFlow;", "settings", "Lkotlinx/coroutines/flow/StateFlow;", "getSettings", "()Lkotlinx/coroutines/flow/StateFlow;", "save", "", "saveInternal", "update", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class PlayerSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "PlayerSettingsViewModel";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.PlayerSettings> _settings = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.PlayerSettings> settings = null;
    
    /**
     * Emits true on success, false on failure
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Boolean> _saveResult = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.Boolean> saveResult = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.ui.settings.PlayerSettingsViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public PlayerSettingsViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.PlayerSettings> getSettings() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.Boolean> getSaveResult() {
        return null;
    }
    
    public final void update(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.PlayerSettings settings) {
    }
    
    public final void save() {
    }
    
    private final boolean saveInternal() {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/nativeplayer/ui/settings/PlayerSettingsViewModel$Companion;", "", "()V", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}