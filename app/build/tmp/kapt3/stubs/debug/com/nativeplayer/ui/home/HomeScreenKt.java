package com.nativeplayer.ui.home;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.ui.favorites.FavoritesScreen;
import com.nativeplayer.ui.live.LiveScreen;
import com.nativeplayer.ui.movies.MoviesScreen;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.series.SeriesScreen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000D\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u001a\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u001a\u0010\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a\u0010\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a0\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0003\u001a0\u0010\u0010\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000fH\u0003\u001a\u0018\u0010\u0011\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0003\u001a\u0010\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u001a<\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u00192\u0012\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00010\u001bH\u0003\u00a8\u0006\u001c"}, d2 = {"FavoritesTabContent", "", "navController", "Landroidx/navigation/NavController;", "HomeScreen", "homeViewModel", "Lcom/nativeplayer/ui/home/HomeViewModel;", "LiveTabContent", "MoviesTabContent", "ResumeCard", "entry", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "Lkotlin/Function0;", "ResumeListItem", "ResumeTabContent", "SeriesTabContent", "TabItem", "title", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "index", "", "selectedIndex", "Lkotlin/Function1;", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.home.HomeViewModel homeViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void TabItem(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, int index, int selectedIndex, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void LiveTabContent(androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MoviesTabContent(androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SeriesTabContent(androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void FavoritesTabContent(androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResumeTabContent(androidx.navigation.NavController navController, com.nativeplayer.ui.home.HomeViewModel homeViewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResumeListItem(com.nativeplayer.data.model.WatchHistoryEntity entry, androidx.navigation.NavController navController, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ResumeCard(com.nativeplayer.data.model.WatchHistoryEntity entry, androidx.navigation.NavController navController, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}