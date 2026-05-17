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

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a:\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u00072\b\u0010\t\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0007\u001a6\u0010\f\u001a\u00020\u00032\b\u0010\r\u001a\u0004\u0018\u00010\u00072\b\u0010\u000e\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"SIDEBAR_WIDTH_FRACTION", "", "EPGLogosSidebar", "", "channelLogoUrl", "", "previousEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "currentEpgItem", "nextEpgItem", "modifier", "Landroidx/compose/ui/Modifier;", "EPGProgramCard", "epgItem", "logoUrl", "isActive", "", "label", "app_debug"})
public final class EPGLogosSidebarKt {
    
    /**
     * Breite der EPG-Seitenleiste (ca. 22 % des Bildschirms).
     */
    private static final float SIDEBAR_WIDTH_FRACTION = 0.22F;
    
    /**
     * Rechte EPG-Seitenleiste für den Live-TV-Vollbildmodus.
     * Zeigt maximal 3 Programme (vorheriges, aktuelles, nächstes) mit
     * Senderlogos und vollständigen Programmtiteln.
     *
     * @param channelLogoUrl URL des Senderlogos (für alle Programme gleich).
     * @param previousEpgItem Vorheriges Programm (kann null sein → Platzhalter).
     * @param currentEpgItem Aktuell laufendes Programm.
     * @param nextEpgItem Nächstes Programm (kann null sein → Platzhalter).
     */
    @androidx.compose.runtime.Composable()
    public static final void EPGLogosSidebar(@org.jetbrains.annotations.Nullable()
    java.lang.String channelLogoUrl, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem previousEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, @org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem nextEpgItem, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Einzelne Programm-Karte mit Logo und Titel.
     *
     * @param epgItem EPG-Daten (kann null sein → Platzhalter).
     * @param logoUrl  URL des Senderlogos (optional).
     * @param isActive [currentEpgItem] → goldener Rand + volle Deckkraft.
     * @param label    Kleine Bezeichnung über dem Programm (VORHER / JETZT / NÄCHSTES).
     */
    @androidx.compose.runtime.Composable()
    private static final void EPGProgramCard(com.nativeplayer.data.model.EpgItem epgItem, java.lang.String logoUrl, boolean isActive, java.lang.String label, androidx.compose.ui.Modifier modifier) {
    }
}