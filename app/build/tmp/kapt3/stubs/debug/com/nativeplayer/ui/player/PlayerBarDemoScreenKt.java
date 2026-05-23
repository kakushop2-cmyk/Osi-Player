package com.nativeplayer.ui.player;

import androidx.compose.animation.*;
import androidx.compose.animation.core.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.Dp;
import androidx.navigation.NavController;
import com.nativeplayer.ui.theme.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000R\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\r\u001a#\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0011\u0010\u0004\u001a\r\u0012\u0004\u0012\u00020\u00010\u0005\u00a2\u0006\u0002\b\u0006H\u0003\u001a\b\u0010\u0007\u001a\u00020\u0001H\u0003\u001a(\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0003H\u0003\u001a\"\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00032\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a:\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00032\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0019\u001a2\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00132\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010\u001c\u001a\u00020\u00132\b\b\u0002\u0010\u001d\u001a\u00020\u001eH\u0003\u001a\u0010\u0010\u001f\u001a\u00020\u00012\u0006\u0010 \u001a\u00020!H\u0007\u001at\u0010\"\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010\'\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\f\u0010)\u001a\b\u0012\u0004\u0012\u00020\u00010\u00052\b\b\u0002\u0010*\u001a\u00020\u00132\b\b\u0002\u0010+\u001a\u00020\u0013H\u0003\u001a(\u0010,\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00132\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020$2\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0018\u0010-\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00032\u0006\u0010.\u001a\u00020\u0003H\u0003\u001a\u0010\u0010/\u001a\u00020\u00032\u0006\u00100\u001a\u00020$H\u0002\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u00061"}, d2 = {"DemoSection", "", "title", "", "content", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "DesignSpecsCard", "LiveTvFloatingPill", "channelName", "programTitle", "programProgress", "", "programTime", "MoreMenuItem", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "isActive", "", "PillControlButton", "onClick", "size", "Landroidx/compose/ui/unit/Dp;", "PillControlButton-eqLRuRQ", "(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Lkotlin/jvm/functions/Function0;F)V", "PillPlayPauseButton", "isPlaying", "isHighlighted", "modifier", "Landroidx/compose/ui/Modifier;", "PlayerBarDemoScreen", "navController", "Landroidx/navigation/NavController;", "PlayerBarFloatingPill", "currentPosition", "", "totalDuration", "onPlayPause", "onSeekBack", "onSeekForward", "onMoreClick", "highlightPlay", "isBuffering", "PlayerBarWithMoreMenu", "SpecRow", "value", "formatTime", "ms", "app_debug"})
public final class PlayerBarDemoScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void PlayerBarDemoScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PlayerBarFloatingPill(boolean isPlaying, long currentPosition, long totalDuration, java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> onPlayPause, kotlin.jvm.functions.Function0<kotlin.Unit> onSeekBack, kotlin.jvm.functions.Function0<kotlin.Unit> onSeekForward, kotlin.jvm.functions.Function0<kotlin.Unit> onMoreClick, boolean highlightPlay, boolean isBuffering) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PillPlayPauseButton(boolean isPlaying, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, boolean isHighlighted, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void PlayerBarWithMoreMenu(boolean isPlaying, long currentPosition, long totalDuration, java.lang.String title) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoreMenuItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, boolean isActive) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LiveTvFloatingPill(java.lang.String channelName, java.lang.String programTitle, float programProgress, java.lang.String programTime) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DesignSpecsCard() {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SpecRow(java.lang.String label, java.lang.String value) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void DemoSection(java.lang.String title, kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
    
    private static final java.lang.String formatTime(long ms) {
        return null;
    }
}