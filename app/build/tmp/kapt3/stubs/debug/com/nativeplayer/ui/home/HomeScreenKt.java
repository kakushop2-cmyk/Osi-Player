package com.nativeplayer.ui.home;

import androidx.compose.animation.*;
import androidx.compose.animation.core.Spring;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.input.key.*;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.Movie;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import androidx.compose.foundation.ExperimentalFoundationApi;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000|\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a^\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00052\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u00052\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u0003\u001a\"\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a\u001a\u0010\u0014\u001a\u00020\u00012\u0006\u0010\u0015\u001a\u00020\u00162\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a\u001a\u0010\u0017\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0007\u001a\"\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a8\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010#2\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a\u0010\u0010$\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0003\u001a0\u0010%\u001a\u00020\u00012\u0006\u0010&\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u001f2\b\b\u0002\u0010\u0012\u001a\u00020\u00132\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010#H\u0003\u001a\u001a\u0010\'\u001a\u00020\u00012\u0006\u0010&\u001a\u00020\u00162\b\b\u0002\u0010\u0012\u001a\u00020\u0013H\u0003\u001a\u001e\u0010(\u001a\u00020\u00012\f\u0010)\u001a\b\u0012\u0004\u0012\u00020*0\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u0003\u001a&\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u00162\f\u0010-\u001a\b\u0012\u0004\u0012\u00020.0\u00052\u0006\u0010\u000e\u001a\u00020\u000fH\u0003\u001a\u001e\u0010/\u001a\u00020\u00012\u0006\u0010-\u001a\u00020.2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00010#H\u0003\u001a*\u00100\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00062\b\b\u0002\u0010\u0012\u001a\u00020\u00132\u000e\b\u0002\u00101\u001a\b\u0012\u0004\u0012\u00020\u00010#H\u0003\u001a:\u00102\u001a\u00020\u00012\f\u00103\u001a\b\u0012\u0004\u0012\u00020\u00060\u00052\u0006\u00104\u001a\u0002052\u0012\u00106\u001a\u000e\u0012\u0004\u0012\u000205\u0012\u0004\u0012\u00020\u0001072\u0006\u0010\u000e\u001a\u00020\u000fH\u0003\u00a8\u00068"}, d2 = {"ContentSection", "", "selectedCategory", "Lcom/nativeplayer/ui/home/HomeCategory;", "fuerDichMovies", "", "Lcom/nativeplayer/data/model/Movie;", "sportChannels", "Lcom/nativeplayer/data/model/Channel;", "recentWatch", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "heroMoviesByCategory", "Lcom/nativeplayer/ui/home/HomeViewModel$HeroCategoryMovie;", "topRatedMovies", "navController", "Landroidx/navigation/NavController;", "ContinueWatchingTile", "entry", "modifier", "Landroidx/compose/ui/Modifier;", "EmptyState", "message", "", "HomeScreen", "viewModel", "Lcom/nativeplayer/ui/home/HomeViewModel;", "MovieListItem", "movie", "NavButton", "label", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "isSelected", "", "onClick", "Lkotlin/Function0;", "QuickAccessSection", "QuickTile", "title", "SectionHeader", "SeriesCategoriesSection", "categories", "Lcom/nativeplayer/ui/home/HomeViewModel$SeriesCategoryGroup;", "SeriesCategoryRow", "category", "series", "Lcom/nativeplayer/data/model/Series;", "SeriesTile", "TopRatedCarouselCard", "onDetailsClick", "TopRatedSection", "movies", "selectedIndex", "", "onUpdateIndex", "Lkotlin/Function1;", "app_debug"})
public final class HomeScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void HomeScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.home.HomeViewModel viewModel) {
    }
    
    /**
     * TOP BEWERTET — Full-screen carousel section with dark background
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void TopRatedSection(java.util.List<com.nativeplayer.data.model.Movie> movies, int selectedIndex, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.Unit> onUpdateIndex, androidx.navigation.NavController navController) {
    }
    
    /**
     * SERIEN ANGEBOTE — Kategorien mit Poster-Kacheln (Titel + Bewertung)
     */
    @androidx.compose.runtime.Composable()
    private static final void SeriesCategoriesSection(java.util.List<com.nativeplayer.ui.home.HomeViewModel.SeriesCategoryGroup> categories, androidx.navigation.NavController navController) {
    }
    
    /**
     * Einzelne Kategorie-Zeile: Titel + horizontal scrollbare Serien-Kacheln
     */
    @androidx.compose.runtime.Composable()
    private static final void SeriesCategoryRow(java.lang.String category, java.util.List<com.nativeplayer.data.model.Series> series, androidx.navigation.NavController navController) {
    }
    
    /**
     * Serien-Kachel: Poster + Titel + Bewertung
     */
    @androidx.compose.runtime.Composable()
    private static final void SeriesTile(com.nativeplayer.data.model.Series series, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    /**
     * Content section — scrollable within its LazyColumn item.
     * Uses a nested Column (not LazyColumn) to avoid nested scrolling issues.
     */
    @androidx.compose.runtime.Composable()
    private static final void ContentSection(com.nativeplayer.ui.home.HomeCategory selectedCategory, java.util.List<com.nativeplayer.data.model.Movie> fuerDichMovies, java.util.List<com.nativeplayer.data.model.Channel> sportChannels, java.util.List<com.nativeplayer.data.model.WatchHistoryEntity> recentWatch, java.util.List<com.nativeplayer.ui.home.HomeViewModel.HeroCategoryMovie> heroMoviesByCategory, java.util.List<com.nativeplayer.data.model.Movie> topRatedMovies, androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionHeader(java.lang.String title, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickAccessSection(androidx.navigation.NavController navController) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void QuickTile(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ContinueWatchingTile(com.nativeplayer.data.model.WatchHistoryEntity entry, androidx.navigation.NavController navController, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void MovieListItem(com.nativeplayer.data.model.Movie movie, androidx.navigation.NavController navController, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Navigation button in the top bar — gold bold text with pulse-zoom on focus.
     */
    @androidx.compose.runtime.Composable()
    private static final void NavButton(java.lang.String label, androidx.compose.ui.graphics.vector.ImageVector icon, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EmptyState(java.lang.String message, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Single card in the Top Rated carousel.
     * Shows poster/background, title, rating badge, and Details button.
     * Uses the darker CarouselSurface/CarouselOverlay palette.
     */
    @androidx.compose.runtime.Composable()
    private static final void TopRatedCarouselCard(com.nativeplayer.data.model.Movie movie, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onDetailsClick) {
    }
}