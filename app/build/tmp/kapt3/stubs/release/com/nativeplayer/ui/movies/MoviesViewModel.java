package com.nativeplayer.ui.movies;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.FavoriteEntity;
import com.nativeplayer.data.model.Movie;
import com.nativeplayer.data.repository.FavoriteRepository;
import com.nativeplayer.data.repository.IptvRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u001b\u001a\u00020\u001cJ\b\u0010\u001d\u001a\u00020\u001cH\u0002J\u000e\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020\nJ\u000e\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u000fR\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R&\u0010\r\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\t0\u000e0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R)\u0010\u0017\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\t0\u000e0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0019\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014\u00a8\u0006\""}, d2 = {"Lcom/nativeplayer/ui/movies/MoviesViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/nativeplayer/data/repository/IptvRepository;", "favoriteRepository", "Lcom/nativeplayer/data/repository/FavoriteRepository;", "(Lcom/nativeplayer/data/repository/IptvRepository;Lcom/nativeplayer/data/repository/FavoriteRepository;)V", "_categories", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "_favoriteIds", "", "_moviesByCategory", "", "Lcom/nativeplayer/data/model/Movie;", "_selectedCategory", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "favoriteIds", "getFavoriteIds", "moviesByCategory", "getMoviesByCategory", "selectedCategory", "getSelectedCategory", "loadMovies", "", "observeFavorites", "selectCategory", "category", "toggleFavorite", "movie", "app_release"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MoviesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.IptvRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.FavoriteRepository favoriteRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Movie>>> _moviesByCategory = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Movie>>> moviesByCategory = null;
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
    
    @javax.inject.Inject()
    public MoviesViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.IptvRepository repository, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteRepository favoriteRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Movie>>> getMoviesByCategory() {
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
    
    private final void observeFavorites() {
    }
    
    public final void loadMovies() {
    }
    
    public final void selectCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
    }
    
    public final void toggleFavorite(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Movie movie) {
    }
}