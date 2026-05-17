package com.nativeplayer.ui.live.components;

import androidx.compose.foundation.layout.*;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import com.nativeplayer.data.model.EpgItem;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aF\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000b2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u001a\u0018\u0010\u000f\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u000bH\u0002\u00a8\u0006\u0011"}, d2 = {"EPGTimelineBar", "", "currentEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "epgProgress", "", "currentTimeInZone", "", "remainingMinutes", "", "timeDisplayString", "", "timezoneName", "modifier", "Landroidx/compose/ui/Modifier;", "formatEpochToHHmm", "epochMs", "app_debug"})
public final class EPGTimelineBarKt {
    
    /**
     * Untere EPG-Timeline-Leiste für den Live-TV-Vollbildmodus.
     * Zeigt Programmname, Fortschrittsbalken, aktuelle Uhrzeit und Restlaufzeit.
     */
    @androidx.compose.runtime.Composable()
    public static final void EPGTimelineBar(@org.jetbrains.annotations.Nullable()
    com.nativeplayer.data.model.EpgItem currentEpgItem, float epgProgress, long currentTimeInZone, int remainingMinutes, @org.jetbrains.annotations.NotNull()
    java.lang.String timeDisplayString, @org.jetbrains.annotations.NotNull()
    java.lang.String timezoneName, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Formatiert einen Unix-Timestamp (Millisekunden seit Epoch) in HH:MM.
     * KEINE Zeitumrechnung – die Xtream-API liefert bereits Lokalzeit.
     */
    private static final java.lang.String formatEpochToHHmm(long epochMs, @kotlin.Suppress(names = {"UNUSED_PARAMETER"})
    java.lang.String timezoneName) {
        return null;
    }
}