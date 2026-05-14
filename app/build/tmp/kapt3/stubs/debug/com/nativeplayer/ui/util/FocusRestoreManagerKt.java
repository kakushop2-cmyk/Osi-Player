package com.nativeplayer.ui.util;

import androidx.compose.foundation.lazy.LazyListState;
import androidx.compose.foundation.lazy.grid.LazyGridState;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000J\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001a\u001a\u0010\n\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\tH\u0007\u001aV\u0010\u000b\u001a\u00020\u0001\"\u0004\b\u0000\u0010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u000e0\u00142\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00050\u0016H\u0007\u001aV\u0010\u0017\u001a\u00020\u0001\"\u0004\b\u0000\u0010\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u000e0\u00142\u0014\b\u0002\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00050\u0016H\u0007\u001a<\u0010\u0018\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00050\u0016\"\u0004\b\u0000\u0010\f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u00122\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u0002H\f\u0012\u0004\u0012\u00020\u000e0\u0014H\u0007\u001a0\u0010\u0019\u001a\u00020\u001a*\u00020\u001a2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u000e2\b\b\u0002\u0010\u001c\u001a\u00020\u00072\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a8\u0006\u001d"}, d2 = {"RequestFocusOnFirstItem", "", "listState", "Landroidx/compose/foundation/lazy/LazyListState;", "focusRequester", "Landroidx/compose/ui/focus/FocusRequester;", "itemCount", "", "enabled", "", "RequestFocusOnItem", "RestoreLazyGridFocus", "T", "screenRoute", "", "gridState", "Landroidx/compose/foundation/lazy/grid/LazyGridState;", "items", "", "keyExtractor", "Lkotlin/Function1;", "focusRequesters", "", "RestoreLazyListFocus", "rememberFocusRequesterMap", "focusRestorableItem", "Landroidx/compose/ui/Modifier;", "itemKey", "itemIndex", "app_debug"})
public final class FocusRestoreManagerKt {
    
    /**
     * Modifier, der den Fokus-Status eines Items speichert wenn es fokussiert wird.
     * Verwendet die Item-Key als Identifier.
     */
    @org.jetbrains.annotations.NotNull()
    public static final androidx.compose.ui.Modifier focusRestorableItem(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier $this$focusRestorableItem, @org.jetbrains.annotations.NotNull()
    java.lang.String screenRoute, @org.jetbrains.annotations.NotNull()
    java.lang.String itemKey, int itemIndex, @org.jetbrains.annotations.Nullable()
    androidx.compose.ui.focus.FocusRequester focusRequester) {
        return null;
    }
    
    /**
     * Composable, das den Fokus auf ein Item in einer LazyList wiederherstellt.
     * Muss im gleichen Composable wie die LazyList verwendet werden.
     *
     * @param screenRoute Die Route des Screens (z.B. Screen.Live.route)
     * @param listState Der LazyListState der Liste
     * @param items Die Items der Liste (zum Finden des Index)
     * @param keyExtractor Funktion, die den Key aus einem Item extrahiert
     * @param focusRequesters Map von Item-Key zu FocusRequester
     */
    @androidx.compose.runtime.Composable()
    public static final <T extends java.lang.Object>void RestoreLazyListFocus(@org.jetbrains.annotations.NotNull()
    java.lang.String screenRoute, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.lazy.LazyListState listState, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends T> items, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, java.lang.String> keyExtractor, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, androidx.compose.ui.focus.FocusRequester> focusRequesters) {
    }
    
    /**
     * Composable, das den Fokus auf ein Item in einem LazyGrid wiederherstellt.
     */
    @androidx.compose.runtime.Composable()
    public static final <T extends java.lang.Object>void RestoreLazyGridFocus(@org.jetbrains.annotations.NotNull()
    java.lang.String screenRoute, @org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.lazy.grid.LazyGridState gridState, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends T> items, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, java.lang.String> keyExtractor, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, androidx.compose.ui.focus.FocusRequester> focusRequesters) {
    }
    
    /**
     * Erstellt FocusRequester für ein Item und speichert sie in einer Map.
     * Verwendet rememberSaveable, um die Requester über Konfigurationsänderungen zu erhalten.
     */
    @androidx.compose.runtime.Composable()
    @org.jetbrains.annotations.NotNull()
    public static final <T extends java.lang.Object>java.util.Map<java.lang.String, androidx.compose.ui.focus.FocusRequester> rememberFocusRequesterMap(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends T> items, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, java.lang.String> keyExtractor) {
        return null;
    }
    
    /**
     * Hilfsfunktion, die den Fokus auf ein bestimmtes Element setzt.
     * Wird verwendet, wenn ein Screen zum ersten Mal angezeigt wird
     * oder wenn aus dem Vollbild zurückgekehrt wird.
     */
    @androidx.compose.runtime.Composable()
    public static final void RequestFocusOnItem(@org.jetbrains.annotations.NotNull()
    androidx.compose.ui.focus.FocusRequester focusRequester, boolean enabled) {
    }
    
    /**
     * Hilfsfunktion, die den Fokus auf das erste Item einer Liste setzt.
     * Nützlich als Fallback, wenn kein gespeicherter Fokus vorhanden ist.
     */
    @androidx.compose.runtime.Composable()
    public static final void RequestFocusOnFirstItem(@org.jetbrains.annotations.NotNull()
    androidx.compose.foundation.lazy.LazyListState listState, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.focus.FocusRequester focusRequester, int itemCount, boolean enabled) {
    }
}