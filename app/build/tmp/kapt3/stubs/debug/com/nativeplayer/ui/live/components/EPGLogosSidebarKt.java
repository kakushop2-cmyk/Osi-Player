package com.nativeplayer.ui.live.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.EpgItem;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001a6\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0003\u001aN\u0010\r\u001a\u00020\u00032\b\u0010\u000e\u001a\u0004\u0018\u00010\u00052\b\u0010\u000f\u001a\u0004\u0018\u00010\u00052\b\u0010\u0010\u001a\u0004\u0018\u00010\u00052\b\u0010\u0011\u001a\u0004\u0018\u00010\u00072\b\u0010\u0012\u001a\u0004\u0018\u00010\u00072\b\u0010\u0013\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\u000b\u001a\u00020\fH\u0007\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"SIDEBAR_WIDTH_FRACTION", "", "ChannelProgramCard", "", "logoUrl", "", "epgItem", "Lcom/nativeplayer/data/model/EpgItem;", "isActive", "", "label", "modifier", "Landroidx/compose/ui/Modifier;", "EPGLogosSidebar", "prevLogoUrl", "currentLogoUrl", "nextLogoUrl", "previousEpgItem", "currentEpgItem", "nextEpgItem", "app_debug"})
public final class EPGLogosSidebarKt {
    
    /**
     * Breite der EPG-Seitenleiste (ca. 22 % des Bildschirms).
     */
    private static final float SIDEBAR_WIDTH_FRACTION = 0.22F;
    
    /**
     * Rechte EPG-Seitenleiste für den Live-TV-Vollbildmodus.
     * Zeigt 3 Kanäle: vorheriger, aktueller, nächster Sender.
     * Jeder Slot zeigt das Senderlogo und den Programmnamen.
     *
     * @param prevLogoUrl    Logo des vorherigen Senders (aus Channel-Liste).
     * @param currentLogoUrl Logo des aktuellen Senders.
     * @param nextLogoUrl    Logo des nächsten Senders (aus Channel-Liste).
     * @param previousEpgItem EPG-Daten des vorherigen Programms (optional).
     * @param currentEpgItem EPG-Daten des aktuellen Programms.
     * @param nextEpgItem    EPG-Daten des nächsten Programms (optional).
     */
    @androidx.compose.runtime.Composable()
    public static final void EPGLogosSidebar(@org.jetbrains.annotations.Nullable()
    java.lang.String prevLogoUrl, @org.jetbrains.annotations.Nullable()
    java.lang.String currentLogoUrl, @org.jetbrains.annotations.Nullable()
    java.lang.String nextLogoUrl, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem previousEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem nextEpgItem, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Einzelne Kanal-/Programm-Karte mit Logo und Titel.
     *
     * @param logoUrl  URL des Senderlogos (kanalspezifisch).
     * @param epgItem  EPG-Daten für den Programmnamen (kann null sein).
     * @param isActive [currentEpgItem] → goldener Rand + volle Deckkraft.
     * @param label    Kleine Bezeichnung (VORHER / JETZT / NÄCHSTES).
     */
    @androidx.compose.runtime.Composable()
    private static final void ChannelProgramCard(java.lang.String logoUrl, com.nativeplayer.data.model.EpgItem epgItem, boolean isActive, java.lang.String label, androidx.compose.ui.Modifier modifier) {
    }
}