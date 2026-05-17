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
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000P\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001ab\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u008e\u0001\u0010\u000e\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\u0012\u001a\u00020\u00132\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u000e\b\u0002\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u000b2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00010\u000bH\u0003\u001a\u0010\u0010\u0015\u001a\u00020\u00012\u0006\u0010\u0016\u001a\u00020\u0017H\u0003\u001a\u001a\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u001a2\b\b\u0002\u0010\u001b\u001a\u00020\u001cH\u0007\u001a\u0016\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u001e\u00a8\u0006\""}, d2 = {"CategoryItem", "", "category", "Lcom/nativeplayer/data/model/Category;", "isSelected", "", "isSortMode", "isActive", "canMoveUp", "canMoveDown", "onClick", "Lkotlin/Function0;", "onMoveUp", "onMoveDown", "ChannelItem", "channel", "Lcom/nativeplayer/data/model/Channel;", "isFavorite", "modifier", "Landroidx/compose/ui/Modifier;", "onFavoriteClick", "EpgItemView", "epg", "Lcom/nativeplayer/data/model/EpgItem;", "LiveScreen", "navController", "Landroidx/navigation/NavController;", "viewModel", "Lcom/nativeplayer/ui/live/LiveViewModel;", "formatCurrentTime", "", "utcTimeMs", "", "timezoneId", "app_debug"})
public final class LiveScreenKt {
    
    @androidx.compose.runtime.Composable()
    public static final void LiveScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.live.LiveViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CategoryItem(com.nativeplayer.data.model.Category category, boolean isSelected, boolean isSortMode, boolean isActive, boolean canMoveUp, boolean canMoveDown, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onMoveUp, kotlin.jvm.functions.Function0<kotlin.Unit> onMoveDown) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void ChannelItem(com.nativeplayer.data.model.Channel channel, boolean isSelected, boolean isFavorite, boolean isSortMode, boolean isActive, boolean canMoveUp, boolean canMoveDown, androidx.compose.ui.Modifier modifier, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onMoveUp, kotlin.jvm.functions.Function0<kotlin.Unit> onMoveDown, kotlin.jvm.functions.Function0<kotlin.Unit> onFavoriteClick) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void EpgItemView(com.nativeplayer.data.model.EpgItem epg) {
    }
    
    /**
     * Formats time for display in the current timezone
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String formatCurrentTime(long utcTimeMs, @org.jetbrains.annotations.NotNull()
    java.lang.String timezoneId) {
        return null;
    }
}