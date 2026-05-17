package com.nativeplayer.ui.player;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.media3.ui.SubtitleView;
import androidx.compose.animation.*;
import androidx.compose.foundation.layout.*;
import androidx.compose.material.icons.Icons;
import androidx.compose.material.icons.filled.*;
import androidx.compose.material3.*;
import androidx.compose.runtime.*;
import androidx.compose.ui.Alignment;
import androidx.compose.ui.Modifier;
import androidx.compose.ui.focus.FocusRequester;
import androidx.compose.ui.graphics.Brush;
import androidx.compose.ui.input.key.*;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.compose.ui.text.font.FontWeight;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.navigation.NavController;
import com.nativeplayer.data.model.PlayerSettings;
import com.nativeplayer.data.model.AspectRatio;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.player.PlayerState;
import com.nativeplayer.player.SubtitleTrack;
import com.nativeplayer.player.AudioTrack;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.live.BackNavigationState;
import com.nativeplayer.data.model.EpgItem;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u001aT\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\b2\b\b\u0002\u0010\u000b\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\b2\b\b\u0002\u0010\u000f\u001a\u00020\u0010H\u0007\u001a8\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0003\u001a\u0018\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\r2\u0006\u0010\u001c\u001a\u00020\bH\u0002\u001a\u0010\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\rH\u0002\u001a\u0010\u0010\u001f\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\rH\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"SPEED_OPTIONS", "", "", "PlayerScreen", "", "navController", "Landroidx/navigation/NavController;", "url", "", "contentType", "contentId", "contentTitle", "resumePosition", "", "imageUrl", "viewModel", "Lcom/nativeplayer/ui/player/PlayerViewModel;", "SeekButton", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "contentDescription", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "formatCurrentTimeForDisplay", "timestamp", "timezoneName", "formatSeekLabel", "ms", "formatTime", "app_debug"})
public final class PlayerScreenKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<java.lang.Float> SPEED_OPTIONS = null;
    
    @androidx.compose.runtime.Composable()
    private static final void SeekButton(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, java.lang.String contentDescription, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void PlayerScreen(@org.jetbrains.annotations.NotNull()
    androidx.navigation.NavController navController, @org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    java.lang.String contentType, @org.jetbrains.annotations.NotNull()
    java.lang.String contentId, @org.jetbrains.annotations.NotNull()
    java.lang.String contentTitle, long resumePosition, @org.jetbrains.annotations.NotNull()
    java.lang.String imageUrl, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.ui.player.PlayerViewModel viewModel) {
    }
    
    /**
     * Formatiert Millisekunden als lesbares Seek-Label (z.B. "30s", "2m", "1m30s").
     */
    private static final java.lang.String formatSeekLabel(long ms) {
        return null;
    }
    
    /**
     * Formatiert Millisekunden als hh:mm:ss oder mm:ss.
     */
    private static final java.lang.String formatTime(long ms) {
        return null;
    }
    
    /**
     * Formatiert Unix-Timestamp als HH:MM in der angegebenen Timezone.
     */
    private static final java.lang.String formatCurrentTimeForDisplay(long timestamp, java.lang.String timezoneName) {
        return null;
    }
}