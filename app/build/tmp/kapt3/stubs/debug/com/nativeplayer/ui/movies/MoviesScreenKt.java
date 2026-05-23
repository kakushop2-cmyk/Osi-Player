package com.nativeplayer.ui.movies;

import androidx.compose.foundation.layout.*;
import androidx.compose.foundation.lazy.grid.GridCells;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.animation.core.Spring;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.Movie;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000.\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a>\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u001a\u001a\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u00a8\u0006\u0010"}, d2 = {"MoviePosterItem", "", "movie", "Lcom/nativeplayer/data/model/Movie;", "isFavorite", "", "modifier", "Landroidx/compose/ui/Modifier;", "onFavoriteClick", "Lkotlin/Function0;", "onClick", "MoviesScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/movies/MoviesViewModel;", "app_debug"})
public final class MoviesScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void MoviesScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.movies.MoviesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoviePosterItem(com.nativeplayer.data.model.Movie movie, boolean isFavorite, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onFavoriteClick, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}