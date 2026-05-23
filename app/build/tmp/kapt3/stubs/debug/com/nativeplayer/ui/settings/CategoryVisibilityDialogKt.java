package com.nativeplayer.ui.settings;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.window.DialogProperties;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a0\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0003\u001aJ\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\u00032\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00030\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0006\u0010\u0012\u001a\u00020\u0013H\u0007\u00a8\u0006\u0014"}, d2 = {"CategoryToggleItem", "", "categoryName", "", "isVisible", "", "onToggle", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "CategoryVisibilityDialog", "title", "categoryType", "availableCategories", "", "hiddenCategories", "", "onDismiss", "categoryVisibilityViewModel", "Lcom/nativeplayer/ui/viewmodel/CategoryVisibilityViewModel;", "app_debug"})
public final class CategoryVisibilityDialogKt {
    
    /**
     * Dialog zum Ein-/Ausblenden von Kategorien für Live TV, Filme und Serien.
     */
    @androidx.compose.runtime.Composable()
    public static final void CategoryVisibilityDialog(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String categoryType, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> availableCategories, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> hiddenCategories, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel categoryVisibilityViewModel) {
    }
    
    /**
     * Einzelnes Toggle-Item für eine Kategorie im Dialog.
     */
    @androidx.compose.runtime.Composable()
    private static final void CategoryToggleItem(java.lang.String categoryName, boolean isVisible, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, androidx.compose.ui.Modifier modifier) {
    }
}