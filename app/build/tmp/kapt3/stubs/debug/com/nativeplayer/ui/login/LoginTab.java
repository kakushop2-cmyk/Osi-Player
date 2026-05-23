package com.nativeplayer.ui.login;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0004\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/nativeplayer/ui/login/LoginTab;", "", "(Ljava/lang/String;I)V", "XTREAM", "M3U", "app_debug"})
public enum LoginTab {
    /*public static final*/ XTREAM /* = new XTREAM() */,
    /*public static final*/ M3U /* = new M3U() */;
    
    LoginTab() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.nativeplayer.ui.login.LoginTab> getEntries() {
        return null;
    }
}