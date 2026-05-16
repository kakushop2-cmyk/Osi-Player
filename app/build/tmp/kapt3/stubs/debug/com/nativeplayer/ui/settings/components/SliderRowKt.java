package com.nativeplayer.ui.settings.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.material3.SliderDefaults;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.input.key.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000(\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aT\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u00072\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00032\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00010\u000b2\b\b\u0002\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u000e"}, d2 = {"SliderRow", "", "label", "", "value", "", "valueRange", "Lkotlin/ranges/ClosedFloatingPointRange;", "step", "valueText", "onValueChange", "Lkotlin/Function1;", "modifier", "Landroidx/compose/ui/Modifier;", "app_debug"})
public final class SliderRowKt {
    
    /**
     * A row with a label, a Slider, and a value display.
     * D-Pad left/right adjusts value by [step].
     *
     * @param label Display label
     * @param value Current value (in valueRange units)
     * @param valueRange Valid range for the slider
     * @param step Step size for D-Pad navigation
     * @param valueText Formatted display string (e.g., "30 s")
     * @param onValueChange Called with new value while sliding or via D-Pad
     */
    @androidx.compose.runtime.Composable()
    public static final void SliderRow(@org.jetbrains.annotations.NotNull()
    java.lang.String label, float value, @org.jetbrains.annotations.NotNull()
    kotlin.ranges.ClosedFloatingPointRange<java.lang.Float> valueRange, float step, @org.jetbrains.annotations.NotNull()
    java.lang.String valueText, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onValueChange, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}