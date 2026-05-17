package com.nativeplayer.ui.live.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import com.nativeplayer.data.model.EpgItem;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aj\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00052\b\u0010\u0007\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u00032\u000e\b\u0002\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00010\u00102\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0007\u00a8\u0006\u0013"}, d2 = {"FullscreenLiveOverlay", "", "channelLogoUrl", "", "previousEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "currentEpgItem", "nextEpgItem", "epgProgress", "", "currentTimeInZone", "", "remainingMinutes", "", "timeDisplayString", "onChannelChanged", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "app_debug"})
public final class FullscreenLiveOverlayKt {
    
    /**
     * Overlay controller for fullscreen Live TV experience
     * Manages auto-hide behavior, user interaction detection, and component visibility
     *
     * Behavior:
     * - Shows sidebars on stream start
     * - Auto-hides after 5 seconds of no interaction
     * - Resets timer on user tap
     * - Rebuilds on channel change
     */
    @androidx.compose.runtime.Composable()
    public static final void FullscreenLiveOverlay(@org.jetbrains.annotations.Nullable()
    java.lang.String channelLogoUrl, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem previousEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem nextEpgItem, float epgProgress, long currentTimeInZone, int remainingMinutes, @org.jetbrains.annotations.NotNull()
    java.lang.String timeDisplayString, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onChannelChanged, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}