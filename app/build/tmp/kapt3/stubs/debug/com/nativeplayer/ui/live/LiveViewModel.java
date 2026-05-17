package com.nativeplayer.ui.live;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.Category;
import com.nativeplayer.data.model.Channel;
import com.nativeplayer.data.model.EpgItem;
import com.nativeplayer.data.model.FavoriteEntity;
import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.PreferencesManager;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;
import java.util.Calendar;
import java.util.TimeZone;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u001a\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u001e\u0010@\u001a\u00020\u00152\u0006\u0010A\u001a\u00020\u00122\u0006\u0010B\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u0012J\u0016\u0010D\u001a\u00020\u001e2\u0006\u0010B\u001a\u00020\u00122\u0006\u0010C\u001a\u00020\u0012J\u0016\u0010E\u001a\u00020F2\u0006\u0010G\u001a\u00020\u00122\u0006\u0010H\u001a\u00020\u000eJ\u0016\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020\u000eH\u0086@\u00a2\u0006\u0002\u0010LJ\u000e\u0010M\u001a\u00020\u000e2\u0006\u0010N\u001a\u00020FJ\u0006\u0010O\u001a\u00020\u001eJ\u0010\u0010\'\u001a\u00020\u000e2\u0006\u0010K\u001a\u00020\u000eH\u0002J\u0006\u0010P\u001a\u00020JJ\u0016\u0010Q\u001a\u00020J2\u0006\u0010R\u001a\u00020!H\u0086@\u00a2\u0006\u0002\u0010SJ\u000e\u0010T\u001a\u00020J2\u0006\u0010U\u001a\u00020\u001eJ\u000e\u0010V\u001a\u00020J2\u0006\u0010U\u001a\u00020\u001eJ\u000e\u0010W\u001a\u00020J2\u0006\u0010U\u001a\u00020\u001eJ\u000e\u0010X\u001a\u00020J2\u0006\u0010U\u001a\u00020\u001eJ\b\u0010Y\u001a\u00020JH\u0002J\b\u0010Z\u001a\u00020JH\u0002J\u000e\u0010[\u001a\u00020J2\u0006\u0010\\\u001a\u00020\fJ\u000e\u0010]\u001a\u00020J2\u0006\u0010R\u001a\u00020!J\u0006\u0010^\u001a\u00020JJ\u0006\u0010_\u001a\u00020JJ\u0006\u0010`\u001a\u00020JJ\u0006\u0010a\u001a\u00020JJ\u000e\u0010b\u001a\u00020J2\u0006\u0010R\u001a\u00020!J\u0016\u0010c\u001a\u00020J2\u0006\u0010R\u001a\u00020!H\u0082@\u00a2\u0006\u0002\u0010SR\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00170\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00190\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001c\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u001f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010 \u001a\n\u0012\u0006\u0012\u0004\u0018\u00010!0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\"\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0#\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0017\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000e0#\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010%R\u0019\u0010(\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100#\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010%R\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00120#\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010%R\u001d\u0010,\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0#\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010%R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00150#\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010%R\u001d\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\u00170#\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010%R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u00102\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0#\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010%R\u0017\u00104\u001a\b\u0012\u0004\u0012\u00020\u00190#\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010%R\u0017\u00105\u001a\b\u0012\u0004\u0012\u00020\u00190#\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010%R\u0019\u00106\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100#\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u0010%R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u00108\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100#\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010%R\u0017\u0010:\u001a\b\u0012\u0004\u0012\u00020\u001e0#\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u0010%R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010<\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0#\u00a2\u0006\b\n\u0000\u001a\u0004\b=\u0010%R\u0019\u0010>\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010!0#\u00a2\u0006\b\n\u0000\u001a\u0004\b?\u0010%\u00a8\u0006d"}, d2 = {"Lcom/nativeplayer/ui/live/LiveViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "favoriteRepository", "Lcom/nativeplayer/data/repository/FavoriteRepository;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/FavoriteRepository;Lcom/nativeplayer/data/repository/PreferencesManager;)V", "_categories", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/nativeplayer/data/model/Category;", "_categoryTimezone", "", "_currentEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "_currentTimeInZone", "", "_epgItems", "_epgProgress", "", "_favoriteIds", "", "_isCategorySortMode", "", "_isChannelSortMode", "_nextEpgItem", "_previousEpgItem", "_remainingMinutes", "", "_selectedCategory", "_selectedChannel", "Lcom/nativeplayer/data/model/Channel;", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "categoryTimezone", "getCategoryTimezone", "currentEpgItem", "getCurrentEpgItem", "currentTimeInZone", "getCurrentTimeInZone", "epgItems", "getEpgItems", "epgProgress", "getEpgProgress", "favoriteIds", "getFavoriteIds", "filteredCategories", "getFilteredCategories", "isCategorySortMode", "isChannelSortMode", "nextEpgItem", "getNextEpgItem", "previousEpgItem", "getPreviousEpgItem", "remainingMinutes", "getRemainingMinutes", "selectedCategory", "getSelectedCategory", "selectedChannel", "getSelectedChannel", "calculateEpgProgress", "programStartMs", "programEndMs", "currentTimeMs", "calculateRemainingMinutes", "convertTimeToZone", "Ljava/util/Calendar;", "utcTimeMs", "timezoneId", "fetchAndSetCategoryTimezone", "", "categoryId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "formatTimeForDisplay", "calendar", "getBufferSizeSeconds", "loadData", "loadEpgSync", "channel", "(Lcom/nativeplayer/data/model/Channel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "moveCategoryDown", "index", "moveCategoryUp", "moveChannelDown", "moveChannelUp", "observeChannelChanges", "observeFavorites", "selectCategory", "category", "selectChannel", "startEpgProgressUpdate", "stopEpgProgressUpdate", "toggleCategorySortMode", "toggleChannelSortMode", "toggleFavorite", "updateEpgItems", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LiveViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.FavoriteRepository favoriteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.nativeplayer.data.model.Category>> _categories = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Category>> categories = null;
    
    /**
     * Gefilterte Kategorien (versteckte Kategorien werden ausgeblendet).
     * Diese StateFlow kombiniert _categories mit den Visibility-Einstellungen.
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Category>> filteredCategories = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.Category> _selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Category> selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.Channel> _selectedChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Channel> selectedChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.nativeplayer.data.model.EpgItem>> _epgItems = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.EpgItem>> epgItems = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Set<java.lang.String>> _favoriteIds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> favoriteIds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isCategorySortMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCategorySortMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isChannelSortMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isChannelSortMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.EpgItem> _previousEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> previousEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.EpgItem> _currentEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> currentEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.EpgItem> _nextEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> nextEpgItem = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Float> _epgProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Float> epgProgress = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _categoryTimezone = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> categoryTimezone = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _currentTimeInZone = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Long> currentTimeInZone = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _remainingMinutes = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> remainingMinutes = null;
    
    @javax.inject.Inject()
    public LiveViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository repository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteRepository favoriteRepository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Category>> getCategories() {
        return null;
    }
    
    /**
     * Gefilterte Kategorien (versteckte Kategorien werden ausgeblendet).
     * Diese StateFlow kombiniert _categories mit den Visibility-Einstellungen.
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.Category>> getFilteredCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Category> getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.Channel> getSelectedChannel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.nativeplayer.data.model.EpgItem>> getEpgItems() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> getFavoriteIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isCategorySortMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isChannelSortMode() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> getPreviousEpgItem() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> getCurrentEpgItem() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.EpgItem> getNextEpgItem() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Float> getEpgProgress() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getCategoryTimezone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Long> getCurrentTimeInZone() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getRemainingMinutes() {
        return null;
    }
    
    private final void observeChannelChanges() {
    }
    
    private final void observeFavorites() {
    }
    
    public final void loadData() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Category category) {
    }
    
    public final void selectChannel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Channel channel) {
    }
    
    /**
     * Synchronous version of selectChannel that suspends until EPG data is loaded.
     * Used before navigation to FullScreenPlayer so EPG data is available immediately.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadEpgSync(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Channel channel, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Channel channel) {
    }
    
    /**
     * Toggles category sort mode. Disables channel sort mode if activated.
     */
    public final void toggleCategorySortMode() {
    }
    
    /**
     * Toggles channel sort mode. Disables category sort mode if activated.
     */
    public final void toggleChannelSortMode() {
    }
    
    public final void moveCategoryUp(int index) {
    }
    
    public final void moveCategoryDown(int index) {
    }
    
    public final void moveChannelUp(int index) {
    }
    
    public final void moveChannelDown(int index) {
    }
    
    /**
     * Gibt die konfigurierte Puffergröße aus den Player-Einstellungen zurück.
     */
    public final int getBufferSizeSeconds() {
        return 0;
    }
    
    /**
     * Converts UTC timestamp to the given timezone
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Calendar convertTimeToZone(long utcTimeMs, @org.jetbrains.annotations.NotNull()
    java.lang.String timezoneId) {
        return null;
    }
    
    /**
     * Calculates EPG progress as a percentage (0-100)
     */
    public final float calculateEpgProgress(long programStartMs, long programEndMs, long currentTimeMs) {
        return 0.0F;
    }
    
    /**
     * Calculates remaining minutes until program ends
     */
    public final int calculateRemainingMinutes(long programEndMs, long currentTimeMs) {
        return 0;
    }
    
    /**
     * Formats time for display (HH:MM)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String formatTimeForDisplay(@org.jetbrains.annotations.NotNull()
    java.util.Calendar calendar) {
        return null;
    }
    
    /**
     * Fetches timezone for category from backend and updates state
     * (Backend will provide this with category data)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchAndSetCategoryTimezone(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Helper to map category names to timezones (temporary until backend provides this)
     */
    private final java.lang.String getCategoryTimezone(java.lang.String categoryId) {
        return null;
    }
    
    /**
     * Updates previous, current, and next EPG items for the selected channel
     */
    private final java.lang.Object updateEpgItems(com.nativeplayer.data.model.Channel channel, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Starts a coroutine to update EPG progress and time every second
     */
    public final void startEpgProgressUpdate() {
    }
    
    /**
     * Stops the EPG progress update coroutine
     */
    public final void stopEpgProgressUpdate() {
    }
}