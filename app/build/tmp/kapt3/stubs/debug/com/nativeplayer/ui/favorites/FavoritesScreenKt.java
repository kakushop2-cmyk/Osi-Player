package com.nativeplayer.ui.favorites;

import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.vector.ImageVector;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.FavoriteEntity;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000<\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\u001aH\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\tH\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a\u001a\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u0011H\u0007\u001a\u0018\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0003\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0017"}, d2 = {"FavItem", "", "fav", "Lcom/nativeplayer/data/model/FavoriteEntity;", "accentColor", "Landroidx/compose/ui/graphics/Color;", "modifier", "Landroidx/compose/ui/Modifier;", "onClick", "Lkotlin/Function0;", "onRemove", "FavItem-iJQMabo", "(Lcom/nativeplayer/data/model/FavoriteEntity;JLandroidx/compose/ui/Modifier;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function0;)V", "FavoritesScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/favorites/FavoritesViewModel;", "SectionHeader", "title", "", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "app_debug"})
public final class FavoritesScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void FavoritesScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.favorites.FavoritesViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void SectionHeader(java.lang.String title, androidx.compose.ui.graphics.vector.ImageVector icon) {
    }
}