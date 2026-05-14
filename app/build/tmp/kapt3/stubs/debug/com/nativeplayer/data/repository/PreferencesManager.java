package com.nativeplayer.data.repository;

import android.content.SharedPreferences;
import com.nativeplayer.data.model.Account;
import com.nativeplayer.data.model.AccountSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0013\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\b\u0010\u000e\u001a\u0004\u0018\u00010\rJ\u0006\u0010\u000f\u001a\u00020\nJ\u0016\u0010\u0010\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\f2\u0006\u0010\u0011\u001a\u00020\nJ\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00020\n\u0018\u00010\f2\u0006\u0010\u0013\u001a\u00020\nJ\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\u0006\u0010\u0015\u001a\u00020\nJ\u000e\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\rJ\u001c\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\n2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\n0\fJ\u001c\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\n2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\n0\fJ\u000e\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\nJ\u000e\u0010\u001e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/nativeplayer/data/repository/PreferencesManager;", "", "prefs", "Landroid/content/SharedPreferences;", "(Landroid/content/SharedPreferences;)V", "gson", "Lcom/google/gson/Gson;", "deleteAccount", "", "accountId", "", "getAccounts", "", "Lcom/nativeplayer/data/model/Account;", "getActiveAccount", "getActiveAccountId", "getCategoryOrder", "type", "getChannelOrder", "categoryName", "getDefaultAccounts", "getM3uUrl", "saveAccount", "account", "saveCategoryOrder", "order", "saveChannelOrder", "channelIds", "saveM3uUrl", "url", "setActiveAccount", "Companion", "app_debug"})
public final class PreferencesManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.SharedPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ACCOUNTS = "accounts";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_ACTIVE_ACCOUNT = "active_account";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String KEY_M3U_URL = "m3u_url";
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.data.repository.PreferencesManager.Companion Companion = null;
    
    @javax.inject.Inject()
    public PreferencesManager(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences prefs) {
        super();
    }
    
    public final void saveAccount(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Account account) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Account> getAccounts() {
        return null;
    }
    
    public final void deleteAccount(@org.jetbrains.annotations.NotNull()
    java.lang.String accountId) {
    }
    
    public final void setActiveAccount(@org.jetbrains.annotations.NotNull()
    java.lang.String accountId) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getActiveAccountId() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.nativeplayer.data.model.Account getActiveAccount() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Account> getDefaultAccounts() {
        return null;
    }
    
    public final void saveM3uUrl(@org.jetbrains.annotations.NotNull()
    java.lang.String url) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getM3uUrl() {
        return null;
    }
    
    /**
     * Saves the user-defined category order for a given type.
     * @param type One of: "live", "movies", "series"
     * @param order The ordered list of category names
     */
    public final void saveCategoryOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> order) {
    }
    
    /**
     * Returns the saved category order for [type], or null if none saved yet.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> getCategoryOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
        return null;
    }
    
    /**
     * Saves the user-defined channel order for a category.
     * Uses a stable hash of the category name as key.
     */
    public final void saveChannelOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryName, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> channelIds) {
    }
    
    /**
     * Returns the saved channel ID order for [categoryName], or null if none saved yet.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<java.lang.String> getChannelOrder(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryName) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/nativeplayer/data/repository/PreferencesManager$Companion;", "", "()V", "KEY_ACCOUNTS", "", "KEY_ACTIVE_ACCOUNT", "KEY_M3U_URL", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}