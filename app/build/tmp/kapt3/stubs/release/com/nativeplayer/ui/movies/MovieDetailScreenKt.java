package com.nativeplayer.ui.movies;

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
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0003\u001a,\u0010\n\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000e2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u001a\u0010\u0010\u0011\u001a\u00020\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0002\u001a\u0012\u0010\u0014\u001a\u00020\u00152\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0002\u00a8\u0006\u0016"}, d2 = {"MovieDetailContent", "", "movie", "Lcom/nativeplayer/data/model/Movie;", "navController", "Landroidx/navigation/NavController;", "padding", "Landroidx/compose/foundation/layout/PaddingValues;", "watchHistory", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "MovieDetailScreen", "movieId", "", "moviesViewModel", "Lcom/nativeplayer/ui/movies/MoviesViewModel;", "detailViewModel", "Lcom/nativeplayer/ui/movies/MovieDetailViewModel;", "formatResumeTime", "ms", "", "shouldOfferResume", "", "app_release"})
public final class MovieDetailScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void MovieDetailScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    java.lang.String movieId, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.movies.MoviesViewModel moviesViewModel, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.movies.MovieDetailViewModel detailViewModel) {
    }
    
    /**
     * Prüft ob ein sinnvoller Resume-Fortschritt existiert.
     * Mindestens 30s gesehen und nicht in den letzten 2 Minuten.
     */
    private static final boolean shouldOfferResume(com.nativeplayer.data.model.WatchHistoryEntity watchHistory) {
        return false;
    }
    
    private static final java.lang.String formatResumeTime(long ms) {
        return null;
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MovieDetailContent(com.nativeplayer.data.model.Movie movie, androidx.navigation.NavController navController, androidx.compose.foundation.layout.PaddingValues padding, com.nativeplayer.data.model.WatchHistoryEntity watchHistory) {
    }
}