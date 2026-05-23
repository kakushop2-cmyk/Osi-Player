package com.nativeplayer.ui.login;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010A\u001a\u00020BH\u0002J\u0006\u0010C\u001a\u00020BJ\b\u0010D\u001a\u00020BH\u0002J\b\u0010E\u001a\u00020BH\u0002J\u000e\u0010F\u001a\u00020B2\u0006\u0010G\u001a\u00020\u000bJ\u000e\u0010H\u001a\u00020B2\u0006\u0010G\u001a\u00020\u000bJ\u000e\u0010I\u001a\u00020B2\u0006\u0010G\u001a\u00020\u000bJ\u0006\u0010J\u001a\u00020BJ\u000e\u0010K\u001a\u00020B2\u0006\u0010G\u001a\u00020\u000bJ\u000e\u0010L\u001a\u00020B2\u0006\u0010M\u001a\u00020\u001fR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R/\u0010\f\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R+\u0010\u0013\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u0016\u0010\u0012\u001a\u0004\b\u0014\u0010\u000e\"\u0004\b\u0015\u0010\u0010R+\u0010\u0017\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u001a\u0010\u0012\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R/\u0010\u001b\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b\u001e\u0010\u0012\u001a\u0004\b\u001c\u0010\u000e\"\u0004\b\u001d\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R+\u0010 \u001a\u00020\u001f2\u0006\u0010\n\u001a\u00020\u001f8F@BX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b%\u0010\u0012\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R/\u0010&\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b)\u0010\u0012\u001a\u0004\b\'\u0010\u000e\"\u0004\b(\u0010\u0010R+\u0010*\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b-\u0010\u0012\u001a\u0004\b+\u0010\u000e\"\u0004\b,\u0010\u0010R+\u0010/\u001a\u00020.2\u0006\u0010\n\u001a\u00020.8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b4\u0010\u0012\u001a\u0004\b0\u00101\"\u0004\b2\u00103R\u0017\u00105\u001a\b\u0012\u0004\u0012\u00020\t06\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00108R+\u00109\u001a\u00020\u000b2\u0006\u0010\n\u001a\u00020\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b<\u0010\u0012\u001a\u0004\b:\u0010\u000e\"\u0004\b;\u0010\u0010R/\u0010=\u001a\u0004\u0018\u00010\u000b2\b\u0010\n\u001a\u0004\u0018\u00010\u000b8F@FX\u0086\u008e\u0002\u00a2\u0006\u0012\n\u0004\b@\u0010\u0012\u001a\u0004\b>\u0010\u000e\"\u0004\b?\u0010\u0010\u00a8\u0006N"}, d2 = {"Lcom/nativeplayer/ui/login/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/PreferencesManager;)V", "_state", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/nativeplayer/ui/login/LoginState;", "<set-?>", "", "m3uError", "getM3uError", "()Ljava/lang/String;", "setM3uError", "(Ljava/lang/String;)V", "m3uError$delegate", "Landroidx/compose/runtime/MutableState;", "m3uUrl", "getM3uUrl", "setM3uUrl", "m3uUrl$delegate", "password", "getPassword", "setPassword", "password$delegate", "passwordError", "getPasswordError", "setPasswordError", "passwordError$delegate", "Lcom/nativeplayer/ui/login/LoginTab;", "selectedTab", "getSelectedTab", "()Lcom/nativeplayer/ui/login/LoginTab;", "setSelectedTab", "(Lcom/nativeplayer/ui/login/LoginTab;)V", "selectedTab$delegate", "serverError", "getServerError", "setServerError", "serverError$delegate", "serverUrl", "getServerUrl", "setServerUrl", "serverUrl$delegate", "", "showPassword", "getShowPassword", "()Z", "setShowPassword", "(Z)V", "showPassword$delegate", "state", "Lkotlinx/coroutines/flow/StateFlow;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "username", "getUsername", "setUsername", "username$delegate", "usernameError", "getUsernameError", "setUsernameError", "usernameError$delegate", "clearErrors", "", "connect", "connectM3u", "connectXtream", "onM3uUrlChange", "value", "onPasswordChange", "onServerUrlChange", "onShowPasswordToggle", "onUsernameChange", "selectTab", "tab", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState selectedTab$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState serverUrl$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState username$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState password$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState showPassword$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState m3uUrl$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.ui.login.LoginState> _state = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.ui.login.LoginState> state = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState serverError$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState usernameError$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState passwordError$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.compose.runtime.MutableState m3uError$delegate = null;
    
    @javax.inject.Inject()
    public LoginViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository repository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.ui.login.LoginTab getSelectedTab() {
        return null;
    }
    
    private final void setSelectedTab(com.nativeplayer.ui.login.LoginTab p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getServerUrl() {
        return null;
    }
    
    public final void setServerUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getUsername() {
        return null;
    }
    
    public final void setUsername(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPassword() {
        return null;
    }
    
    public final void setPassword(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    public final boolean getShowPassword() {
        return false;
    }
    
    public final void setShowPassword(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getM3uUrl() {
        return null;
    }
    
    public final void setM3uUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.ui.login.LoginState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getServerError() {
        return null;
    }
    
    public final void setServerError(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUsernameError() {
        return null;
    }
    
    public final void setUsernameError(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getPasswordError() {
        return null;
    }
    
    public final void setPasswordError(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getM3uError() {
        return null;
    }
    
    public final void setM3uError(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    public final void selectTab(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.login.LoginTab tab) {
    }
    
    public final void onServerUrlChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onUsernameChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onPasswordChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onM3uUrlChange(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final void onShowPasswordToggle() {
    }
    
    public final void connect() {
    }
    
    private final void connectXtream() {
    }
    
    private final void connectM3u() {
    }
    
    private final void clearErrors() {
    }
}