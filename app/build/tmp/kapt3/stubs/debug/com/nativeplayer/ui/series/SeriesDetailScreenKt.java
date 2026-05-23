package com.nativeplayer.ui.series;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontWeight;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.Episode;
import com.nativeplayer.data.model.Season;
import com.nativeplayer.data.model.Series;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;
import androidx.compose.animation.core.Spring;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a4\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a*\u0010\f\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0003\u001a\"\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0012\u001a\u00020\u00132\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u0007\u00a8\u0006\u0016"}, d2 = {"EpisodeItem", "", "episode", "Lcom/nativeplayer/data/model/Episode;", "series", "Lcom/nativeplayer/data/model/Series;", "navController", "Landroidx/navigation/NavController;", "seasonNumber", "", "isRestored", "", "SeriesDetailContent", "padding", "Landroidx/compose/foundation/layout/PaddingValues;", "watchHistory", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "SeriesDetailScreen", "seriesId", "", "detailViewModel", "Lcom/nativeplayer/ui/series/SeriesDetailViewModel;", "app_debug"})
public final class SeriesDetailScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SeriesDetailScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    java.lang.String seriesId, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.series.SeriesDetailViewModel detailViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SeriesDetailContent(com.nativeplayer.data.model.Series series, androidx.navigation.NavController navController, androidx.compose.foundation.layout.PaddingValues padding, com.nativeplayer.data.model.WatchHistoryEntity watchHistory) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EpisodeItem(com.nativeplayer.data.model.Episode episode, com.nativeplayer.data.model.Series series, androidx.navigation.NavController navController, int seasonNumber, boolean isRestored) {
    }
}