package com.nativeplayer.ui.search;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a8\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\nH\u0003\u001a\u001a\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\u000fH\u0007\u001a\u0018\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0003H\u0003\u00a8\u0006\u0012"}, d2 = {"SearchResultItem", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "primary", "", "secondary", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "Lkotlin/Function0;", "SearchScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/search/SearchViewModel;", "SectionHeader", "title", "app_release"})
public final class SearchScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void SearchScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.search.SearchViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionHeader(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SearchResultItem(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String primary, java.lang.String secondary, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
}