package com.nativeplayer.ui.live;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.layout.ContentScale;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.navigation.NavController;
import coil.request.ImageRequest;
import com.nativeplayer.data.model.Category;
import com.nativeplayer.data.model.Channel;
import com.nativeplayer.data.model.EpgItem;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.ui.player.PlayerContext;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.util.FocusRestoreManager;
import java.text.SimpleDateFormat;
import java.util.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a&\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001aF\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00052\b\b\u0002\u0010\f\u001a\u00020\r2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0011H\u0003\u001a\u001a\u0010\u0012\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0007\u00a8\u0006\u0017"}, d2 = {"CategoryItem", "", "category", "Lcom/nativeplayer/data/model/Category;", "isSelected", "", "onClick", "Lkotlin/Function0;", "ChannelItem", "channel", "Lcom/nativeplayer/data/model/Channel;", "isFavorite", "modifier", "Landroidx/compose/ui/Modifier;", "onFavoriteClick", "EpgItemView", "epg", "Lcom/nativeplayer/data/model/EpgItem;", "LiveScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/live/LiveViewModel;", "app_release"})
public final class LiveScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void LiveScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.live.LiveViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryItem(com.nativeplayer.data.model.Category category, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChannelItem(com.nativeplayer.data.model.Channel channel, boolean isSelected, boolean isFavorite, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onFavoriteClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EpgItemView(com.nativeplayer.data.model.EpgItem epg) {
    }
}