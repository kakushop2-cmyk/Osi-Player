package com.nativeplayer.ui.player;

import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.media3.ui.SubtitleView;
import androidx.compose.animation.*;
import androidx.compose.animation.core.*;
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
import androidx.compose.ui.text.style.TextAlign;
import androidx.compose.ui.text.style.TextOverflow;
import androidx.compose.ui.unit.Dp;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.compose.ui.text.font.FontWeight;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.navigation.NavController;
import com.nativeplayer.data.model.PlayerSettings;
import com.nativeplayer.ui.navigation.Screen;
import com.nativeplayer.data.model.AspectRatio;
import com.nativeplayer.player.PlayerManager;
import com.nativeplayer.player.PlayerState;
import com.nativeplayer.player.SubtitleTrack;
import com.nativeplayer.player.AudioTrack;
import com.nativeplayer.ui.theme.*;
import com.nativeplayer.ui.live.BackNavigationState;
import com.nativeplayer.ui.util.FocusRestoreManager;
import com.nativeplayer.data.model.EpgItem;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0000\n\u0002\u0010 \n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\t\u001aB\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\u00022\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\rH\u0003\u001a:\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\t2\b\b\u0002\u0010\u0014\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0003\u001a\u00e6\u0001\u0010\u001a\u001a\u00020\u00042\u0006\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001f0\u00012\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\"0\u00012\b\u0010#\u001a\u0004\u0018\u00010\"2\u0006\u0010$\u001a\u00020\u00152\u0006\u0010%\u001a\u00020\u00152\u0012\u0010&\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00040\'2\f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\u0012\u0010)\u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u00020\u00040\'2\u0012\u0010*\u001a\u000e\u0012\u0004\u0012\u00020\"\u0012\u0004\u0012\u00020\u00040\'2\f\u0010+\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020.2\u0006\u00100\u001a\u00020.2\u0006\u00101\u001a\u00020.2\u0006\u00102\u001a\u00020.H\u0003\u001aD\u00103\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\t2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u00104\u001a\u000205H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b6\u00107\u001a(\u00108\u001a\u00020\u00042\u0006\u00109\u001a\u00020\u00152\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0003\u001ah\u0010:\u001a\u00020\u00042\u0006\u0010;\u001a\u00020<2\u0006\u0010=\u001a\u00020\t2\b\b\u0002\u0010>\u001a\u00020\t2\b\b\u0002\u0010?\u001a\u00020\t2\b\b\u0002\u0010@\u001a\u00020\t2\b\b\u0002\u0010A\u001a\u00020\u000b2\b\b\u0002\u0010B\u001a\u00020\t2\b\b\u0002\u0010C\u001a\u00020\r2\b\b\u0002\u0010D\u001a\u00020\r2\b\b\u0002\u0010E\u001a\u00020FH\u0007\u001a8\u0010G\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\t2\u0006\u0010H\u001a\u00020\t2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00040\u00172\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0003\u001a\u0018\u0010I\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u000b2\u0006\u0010K\u001a\u00020\tH\u0002\u001a\u0010\u0010L\u001a\u00020\t2\u0006\u0010M\u001a\u00020\u000bH\u0002\u001a\u0010\u0010N\u001a\u00020\t2\u0006\u0010M\u001a\u00020\u000bH\u0002\"\u0014\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0007\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006O"}, d2 = {"SPEED_OPTIONS", "", "", "LiveTvPlayerPill", "", "currentEpgItem", "Lcom/nativeplayer/data/model/EpgItem;", "epgProgress", "channelName", "", "currentTimeInZone", "", "remainingMinutes", "", "timeDisplayString", "epgRefreshTrigger", "MoreMenuItemButton", "icon", "Landroidx/compose/ui/graphics/vector/ImageVector;", "label", "isActive", "", "onClick", "Lkotlin/Function0;", "modifier", "Landroidx/compose/ui/Modifier;", "PlayerMoreMenuPanel", "playbackSpeed", "currentAspectRatio", "Lcom/nativeplayer/data/model/AspectRatio;", "currentSubtitleTrack", "Lcom/nativeplayer/player/SubtitleTrack;", "subtitleTracks", "audioTracks", "Lcom/nativeplayer/player/AudioTrack;", "currentAudioTrack", "showSubtitleButton", "showAudioButton", "onSpeedChange", "Lkotlin/Function1;", "onAspectRatioChange", "onSubtitleSelect", "onAudioSelect", "onStop", "onDismiss", "speedFocus", "Landroidx/compose/ui/focus/FocusRequester;", "aspectRatioFocus", "stopFocus", "subtitleFocus", "audioFocus", "PlayerPillControlButton", "size", "Landroidx/compose/ui/unit/Dp;", "PlayerPillControlButton-M2VBTUQ", "(Landroidx/compose/ui/graphics/vector/ImageVector;Ljava/lang/String;Lkotlin/jvm/functions/Function0;Landroidx/compose/ui/Modifier;F)V", "PlayerPillPlayPauseButton", "isPlaying", "PlayerScreen", "navController", "Landroidx/navigation/NavController;", "url", "contentType", "contentId", "contentTitle", "resumePosition", "imageUrl", "seasonNumber", "episodeNumber", "viewModel", "Lcom/nativeplayer/ui/player/PlayerViewModel;", "SeekButton", "contentDescription", "formatCurrentTimeForDisplay", "timestamp", "timezoneName", "formatSeekLabel", "ms", "formatTime", "app_debug"})
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
    java.lang.String imageUrl, int seasonNumber, int episodeNumber, @org.jetbrains.annotations.NotNull()
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
    
    /**
     * Play/Pause Button — Circular with Glow Effect and Spring Animation.
     * 56dp diameter, radial gradient OsiCyan, shadow glow on focus.
     */
    @androidx.compose.runtime.Composable()
    private static final void PlayerPillPlayPauseButton(boolean isPlaying, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * More Menu Panel — Slide-up panel with Speed, Aspect, Subtitles, Audio, Stop.
     * Glassmorphism container matching the pill design.
     */
    @androidx.compose.runtime.Composable()
    private static final void PlayerMoreMenuPanel(float playbackSpeed, com.nativeplayer.data.model.AspectRatio currentAspectRatio, com.nativeplayer.player.SubtitleTrack currentSubtitleTrack, java.util.List<com.nativeplayer.player.SubtitleTrack> subtitleTracks, java.util.List<com.nativeplayer.player.AudioTrack> audioTracks, com.nativeplayer.player.AudioTrack currentAudioTrack, boolean showSubtitleButton, boolean showAudioButton, kotlin.jvm.functions.Function1<? super java.lang.Float, kotlin.Unit> onSpeedChange, kotlin.jvm.functions.Function0<kotlin.Unit> onAspectRatioChange, kotlin.jvm.functions.Function1<? super com.nativeplayer.player.SubtitleTrack, kotlin.Unit> onSubtitleSelect, kotlin.jvm.functions.Function1<? super com.nativeplayer.player.AudioTrack, kotlin.Unit> onAudioSelect, kotlin.jvm.functions.Function0<kotlin.Unit> onStop, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss, androidx.compose.ui.focus.FocusRequester speedFocus, androidx.compose.ui.focus.FocusRequester aspectRatioFocus, androidx.compose.ui.focus.FocusRequester stopFocus, androidx.compose.ui.focus.FocusRequester subtitleFocus, androidx.compose.ui.focus.FocusRequester audioFocus) {
    }
    
    /**
     * Single item in the More Menu — 48dp rounded square with icon + label.
     */
    @androidx.compose.runtime.Composable()
    private static final void MoreMenuItemButton(androidx.compose.ui.graphics.vector.ImageVector icon, java.lang.String label, boolean isActive, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
    
    /**
     * Live TV Floating Pill — EPG-focused with StarYellow accent.
     * Shows channel name, current program, progress bar, and channel switch hints.
     */
    @androidx.compose.runtime.Composable()
    private static final void LiveTvPlayerPill(com.nativeplayer.data.model.EpgItem currentEpgItem, float epgProgress, java.lang.String channelName, long currentTimeInZone, int remainingMinutes, java.lang.String timeDisplayString, int epgRefreshTrigger) {
    }
}