package com.nativeplayer.ui.home;

import android.app.Application;
import android.net.Uri;
import androidx.lifecycle.ViewModel;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory;
import com.nativeplayer.data.model.Channel;
import com.nativeplayer.data.model.Episode;
import com.nativeplayer.data.model.Movie;
import com.nativeplayer.data.model.Season;
import com.nativeplayer.data.model.Series;
import com.nativeplayer.data.model.WatchHistoryEntity;
import com.nativeplayer.data.repository.IptvRepository;
import com.nativeplayer.data.repository.WatchHistoryRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import android.util.Log;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0004"}, d2 = {"CLIP_DURATION_MS", "", "TAG", "", "app_debug"})
public final class HomeViewModelKt {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "HomeViewModel";
    private static final long CLIP_DURATION_MS = 60000L;
}