package com.nativeplayer.ui.settings.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a[\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0005\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00040\u00070\u00062\u0006\u0010\b\u001a\u0002H\u00022\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\n2\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\u00a2\u0006\u0002\u0010\r\u00a8\u0006\u000e"}, d2 = {"SegmentedRow", "", "T", "label", "", "options", "", "Lkotlin/Pair;", "selected", "onSelect", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Landroidx/compose/ui/Modifier;)V", "app_debug"})
public final class SegmentedRowKt {
    
    /**
     * A row with a label and a horizontal group of buttons for discrete values.
     *
     * @param label Display label
     * @param options List of (value, display text) pairs
     * @param selected Currently selected value
     * @param onSelect Called with newly selected value
     */
    @androidx.compose.runtime.Composable()
    public static final <T extends java.lang.Object>void SegmentedRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends kotlin.Pair<? extends T, java.lang.String>> options, T selected, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> onSelect, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}