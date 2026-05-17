package com.nativeplayer.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import com.nativeplayer.data.model.CategoryVisibilitySettings;
import com.nativeplayer.data.repository.PreferencesManager;
import kotlinx.coroutines.flow.StateFlow;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J\u0006\u0010\u0013\u001a\u00020\u0012J\u0016\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/nativeplayer/ui/viewmodel/CategoryVisibilityViewModel;", "Landroidx/lifecycle/ViewModel;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lcom/nativeplayer/data/repository/PreferencesManager;)V", "_categoryVisibility", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/nativeplayer/data/model/CategoryVisibilitySettings;", "categoryVisibility", "Lkotlinx/coroutines/flow/StateFlow;", "getCategoryVisibility", "()Lkotlinx/coroutines/flow/StateFlow;", "isCategoryHidden", "", "type", "", "categoryName", "loadCategoryVisibility", "", "resetVisibility", "toggleCategoryVisibility", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class CategoryVisibilityViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.nativeplayer.data.model.CategoryVisibilitySettings> _categoryVisibility = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.CategoryVisibilitySettings> categoryVisibility = null;
    
    @javax.inject.Inject()
    public CategoryVisibilityViewModel(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.nativeplayer.data.model.CategoryVisibilitySettings> getCategoryVisibility() {
        return null;
    }
    
    private final void loadCategoryVisibility() {
    }
    
    public final void toggleCategoryVisibility(@org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryName) {
    }
    
    public final boolean isCategoryHidden(@org.jetbrains.annotations.NotNull()
    java.lang.String type, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryName) {
        return false;
    }
    
    public final void resetVisibility() {
    }
}