package com.nativeplayer.ui.components;

import androidx.compose.foundation.layout.ExperimentalLayoutApi;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aQ\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00040\u00062!\u0010\u0007\u001a\u001d\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\t\u0012\b\b\n\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\u00010\b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u001a0\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u0013"}, d2 = {"CategoriesCheckboxGrid", "", "categories", "", "", "hiddenCategories", "", "onCategoryToggle", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "categoryName", "modifier", "Landroidx/compose/ui/Modifier;", "CategoryCheckboxItem", "isVisible", "", "onToggle", "Lkotlin/Function0;", "app_debug"})
public final class CategoriesCheckboxGridKt {
    
    /**
     * Displays a grid of checkboxes for category visibility control.
     * Shows all available categories, with checked state for visible categories.
     * Unchecked = hidden category.
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable()
    public static final void CategoriesCheckboxGrid(@org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> categories, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> hiddenCategories, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCategoryToggle, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Individual checkbox item for a single category.
     * Checked = visible, Unchecked = hidden.
     */
    @androidx.compose.runtime.Composable()
    public static final void CategoryCheckboxItem(@org.jetbrains.annotations.NotNull()
    java.lang.String categoryName, boolean isVisible, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}