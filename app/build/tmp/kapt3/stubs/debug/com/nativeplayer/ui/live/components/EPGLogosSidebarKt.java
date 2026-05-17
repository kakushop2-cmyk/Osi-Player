package com.nativeplayer.ui.live.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import com.nativeplayer.data.model.EpgItem;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a0\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00032\b\u0010\u0005\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0007\u001a\"\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\u0006\u001a\u00020\u0007H\u0003\u00a8\u0006\f"}, d2 = {"EPGLogosSidebar", "", "previousEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "currentEpgItem", "nextEpgItem", "modifier", "Landroidx/compose/ui/Modifier;", "EPGProgramCard", "epgItem", "isActive", "", "app_debug"})
public final class EPGLogosSidebarKt {
    
    /**
     * Sidebar component displaying current, previous, and next program logos
     * Located on right side of fullscreen Live TV view
     */
    @androidx.compose.runtime.Composable()
    public static final void EPGLogosSidebar(@org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem previousEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem nextEpgItem, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Individual program card with logo placeholder and name
     */
    @androidx.compose.runtime.Composable()
    private static final void EPGProgramCard(com.nativeplayer.data.model.EpgItem epgItem, boolean isActive, androidx.compose.ui.Modifier modifier) {
    }
}