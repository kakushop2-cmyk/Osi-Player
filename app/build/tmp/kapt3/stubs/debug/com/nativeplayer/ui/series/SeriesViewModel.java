package com.nativeplayer.ui.series;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.FavoriteEntity;
import com.nativeplayer.data.model.Series;
import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u001e\u001a\u00020\u001fJ\u000e\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\"J\u000e\u0010#\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\"J\b\u0010$\u001a\u00020\u001fH\u0002J\u000e\u0010%\u001a\u00020\u001f2\u0006\u0010&\u001a\u00020\nJ\u000e\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0012J\u0006\u0010)\u001a\u00020\u001fR\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\u0010\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\t0\u00110\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u001d\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\f0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016R)\u0010\u001c\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\t0\u00110\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0016\u00a8\u0006*"}, d2 = {"Lcom/nativeplayer/ui/series/SeriesViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "favoriteRepository", "Lcom/nativeplayer/data/repository/FavoriteRepository;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/FavoriteRepository;)V", "_categories", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "_favoriteIds", "", "_isSortMode", "", "_selectedCategory", "_seriesByCategory", "", "Lcom/nativeplayer/data/model/Series;", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "favoriteIds", "getFavoriteIds", "isSortMode", "selectedCategory", "getSelectedCategory", "seriesByCategory", "getSeriesByCategory", "loadSeries", "", "moveCategoryDown", "index", "", "moveCategoryUp", "observeFavorites", "selectCategory", "category", "toggleFavorite", "series", "toggleSortMode", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SeriesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.FavoriteRepository favoriteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Series>>> _seriesByCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Series>>> seriesByCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.lang.String>> _categories = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> categories = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> selectedCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Set<java.lang.String>> _favoriteIds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> favoriteIds = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isSortMode = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSortMode = null;
    
    @javax.inject.Inject()
    public SeriesViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository repository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteRepository favoriteRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Series>>> getSeriesByCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getSelectedCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Set<java.lang.String>> getFavoriteIds() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isSortMode() {
        return null;
    }
    
    private final void observeFavorites() {
    }
    
    public final void loadSeries() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
    }
    
    public final void toggleSortMode() {
    }
    
    /**
     * Moves category at [index] one position up.
     * Index 0 ("Alle") is protected and cannot be moved or be the target.
     */
    public final void moveCategoryUp(int index) {
    }
    
    /**
     * Moves category at [index] one position down.
     * Index 0 ("Alle") cannot be moved.
     */
    public final void moveCategoryDown(int index) {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Series series) {
    }
}