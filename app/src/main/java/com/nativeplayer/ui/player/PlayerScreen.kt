package com.nativeplayer.ui.player

import android.util.Log
import android.view.SurfaceView
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.media3.ui.SubtitleView
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.data.model.AspectRatio
import com.nativeplayer.player.PlayerManager
import com.nativeplayer.player.PlayerState
import com.nativeplayer.player.SubtitleTrack
import com.nativeplayer.player.AudioTrack
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.live.BackNavigationState
import com.nativeplayer.ui.util.FocusRestoreManager
import com.nativeplayer.ui.live.components.EPGLogosSidebar
import com.nativeplayer.ui.live.components.EPGTimelineBar
import com.nativeplayer.data.model.EpgItem
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Calendar
import java.util.Locale
import java.util.Date
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Verfügbare Wiedergabegeschwindigkeiten
private val SPEED_OPTIONS = listOf(0.5f, 1.0f, 1.5f, 2.0f)

@Composable
private fun SeekButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = OsiTextPrimary,
            modifier = Modifier.size(60.dp)
        )
        Text(
            text = label,
            color = OsiTextPrimary.copy(alpha = 0.9f),
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
fun PlayerScreen(
    navController: NavController,
    url: String,
    contentType: String = "movie",
    contentId: String = "",
    contentTitle: String = "",
    resumePosition: Long = 0L,
    imageUrl: String = "",
    seasonNumber: Int = -1,
    episodeNumber: Int = -1,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentView = LocalView.current
    val contentTyp = ContentType.fromString(contentType)

    // ── Dynamische Inhaltsdaten (für Live-Channel-Switching mutable) ──
    var currentUrl by remember { mutableStateOf(url) }
    var currentContentId by remember { mutableStateOf(contentId) }
    var currentTitle by remember { mutableStateOf(contentTitle) }
    var currentImageUrl by remember { mutableStateOf(imageUrl) }
    var currentResumePos by remember { mutableLongStateOf(resumePosition) }
    var currentSeason by remember { mutableIntStateOf(seasonNumber) }
    var currentEpisode by remember { mutableIntStateOf(episodeNumber) }

    var playerManager by remember { mutableStateOf<PlayerManager?>(null) }
    var surfaceView by remember { mutableStateOf<SurfaceView?>(null) }
    var subtitleView by remember { mutableStateOf<SubtitleView?>(null) }
    var isSurfaceReady by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(true) }
    var playerState by remember { mutableStateOf(PlayerState.IDLE) }
    val currentPositionState = remember { mutableLongStateOf(0L) }
    var currentPosition by currentPositionState
    val durationState = remember { mutableLongStateOf(0L) }
    var duration by durationState
    var isDragging by remember { mutableStateOf(false) }
    var showControls by remember { mutableStateOf(true) }
    var playbackSpeed by remember { mutableFloatStateOf(1.0f) }
    var showSpeedMenu by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var retryTrigger by remember { mutableIntStateOf(0) }
    var isChannelSwitching by remember { mutableStateOf(false) }
    
    // ── Track Selection State ──
    var showSubtitleMenu by remember { mutableStateOf(false) }
    var showAudioMenu by remember { mutableStateOf(false) }
    var subtitleTracks by remember { mutableStateOf<List<SubtitleTrack>>(emptyList()) }
    var audioTracks by remember { mutableStateOf<List<AudioTrack>>(emptyList()) }
    var currentSubtitleTrack by remember { mutableStateOf<SubtitleTrack?>(null) }
    var currentAudioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var tracksLoaded by remember { mutableStateOf(false) }

    // ── Aspect Ratio State ──
    var currentAspectRatio by remember { mutableStateOf(AspectRatio.FIT) }
    val ASPECT_RATIO_CYCLE = listOf(
        AspectRatio.FIT,
        AspectRatio.FILL
    )

    // ── Countdown / Auto-Play Next Episode State ──
    var showCountdown by remember { mutableStateOf(false) }
    var countdownSeconds by remember { mutableIntStateOf(5) }
    var nextEpisodeForAutoplay by remember { mutableStateOf<NextEpisodeInfo?>(null) }
    var autoPlayTriggered by remember { mutableStateOf(false) }

    // ── Player-Einstellungen (einmalig beim Start geladen) ──────
    var playerSettings by remember { mutableStateOf(PlayerSettings()) }
    LaunchedEffect(Unit) {
        playerSettings = viewModel.getPlayerSettings()
        currentAspectRatio = playerSettings.defaultAspectRatio
    }

    // ── Abgeleitete Sichtbarkeit der Track-Buttons ──
    // subtitleTracks enthält immer mind. 1 "Keine"-Option → ab 1 Element anzeigen
    // (d.h. immer wenn Tracks geladen wurden, auch wenn nur die Aus-Option vorhanden ist)
    val showSubtitleButton = tracksLoaded && subtitleTracks.isNotEmpty()
    // audioTracks: anzeigen sobald mind. 1 Audiospur vorhanden
    val showAudioButton = tracksLoaded && audioTracks.isNotEmpty()

    // ── Live Channel Switching Overlay ──
    var showChannelOverlay by remember { mutableStateOf(false) }
    var channelOverlayText by remember { mutableStateOf("") }
    var channelOverlayJob by remember { mutableStateOf<Job?>(null) }

    val coroutineScope = rememberCoroutineScope()
    var hideJob by remember { mutableStateOf<Job?>(null) }

    // ── EPG Reactive State (for Live TV) ──────────────────────────
    // Diese States werden aktualisiert, wenn EPG-Daten geladen werden
    // oder sich der Fortschritt ändert → lösen Recomposition aus.
    var epgRefreshTrigger by remember { mutableIntStateOf(0) }
    var epgProgress by remember { mutableFloatStateOf(PlayerContext.epgProgress) }
    var epgRemainingMinutes by remember { mutableIntStateOf(PlayerContext.remainingMinutes) }
    var epgCurrentTimeInZone by remember { mutableLongStateOf(PlayerContext.currentTimeInZone) }

    // ── D‑Pad / TV Focus Management ─────────────────────────────
    val backFocus = remember { FocusRequester() }
    val sliderFocus = remember { FocusRequester() }
    val rewindFocus = remember { FocusRequester() }
    val playPauseFocus = remember { FocusRequester() }
    val forwardFocus = remember { FocusRequester() }
    val speedFocus = remember { FocusRequester() }
    val aspectRatioFocus = remember { FocusRequester() }
    val stopFocus = remember { FocusRequester() }
    val retryFocus = remember { FocusRequester() }
    val backTextFocus = remember { FocusRequester() }
    val subtitleFocus = remember { FocusRequester() }
    val audioFocus = remember { FocusRequester() }

    // ── Live Channel Switching ──────────────────────────────────
    fun showChannelSwitchOverlay(text: String) {
        channelOverlayText = text
        showChannelOverlay = true
        channelOverlayJob?.cancel()
        channelOverlayJob = coroutineScope.launch {
            delay(2500)
            showChannelOverlay = false
        }
    }

    fun switchToNextChannel(next: Boolean) {
        if (contentTyp != ContentType.LIVE) return
        // Error-Status sofort löschen, bevor currentUrl den LaunchedEffect restartet.
        // Sonst blitzt kurz "Fehler bei der Wiedergabe" auf, weil der alte Player
        // während release() noch ERROR-Callbacks feuern kann.
        errorMessage = null
        playerState = PlayerState.IDLE
        isChannelSwitching = true
        val channel = if (next) PlayerContext.next() else PlayerContext.previous()
        channel?.let { ch ->
            currentUrl = ch.url
            currentContentId = ch.id
            currentTitle = ch.name
            currentImageUrl = ch.logo ?: ""
            currentResumePos = 0L
            
            // EPG-Daten für neuen Kanal aktualisieren
            PlayerContext.channelLogoUrl = ch.logo
            // EPG zurücksetzen während geladen wird
            PlayerContext.resetEpgItems()
            // EPG im Hintergrund laden
            coroutineScope.launch {
                val epgList = viewModel.loadEpgForChannel(ch.id)
                if (epgList.isNotEmpty()) {
                    val now = System.currentTimeMillis()
                    // ── Local-Time-As-Epoch → echte UTC ──────
                    // EPG-Timestamps sind in Lokalzeit der Category
                    // (z.B. 16:40 Berlin = epoch(16:40) = 1779036000).
                    // Für Vergleich mit now (echte UTC) Offset abziehen:
                    val tz = TimeZone.getTimeZone(PlayerContext.categoryTimezone)
                    val offsetMs = tz.getOffset(now)
                    val sorted = epgList
                        .map { item ->
                            item.copy(
                                startTime = item.startTime - offsetMs,
                                endTime = item.endTime - offsetMs
                            )
                        }
                        .sortedBy { it.startTime }
                    val current = sorted.firstOrNull { item ->
                        item.startTime <= now && item.endTime > now
                    }
                    if (current != null) {
                        val idx = sorted.indexOf(current)
                        val remaining = ((current.endTime - now) / 60000).toInt()
                        val progress = ((now - current.startTime).toFloat() / (current.endTime - current.startTime) * 100f).coerceIn(0f, 100f)
                        PlayerContext.setEpgData(
                            previousEpgItem = if (idx > 0) sorted[idx - 1] else null,
                            currentEpgItem = current,
                            nextEpgItem = if (idx < sorted.size - 1) sorted[idx + 1] else null,
                            channelLogoUrl = PlayerContext.channelLogoUrl,
                            epgProgress = progress,
                            currentTimeInZone = now,
                            remainingMinutes = remaining,
                            categoryTimezone = PlayerContext.categoryTimezone
                        )
                        // Reaktive States aktualisieren → sofortige Recomposition
                        epgProgress = progress
                        epgRemainingMinutes = remaining
                        epgCurrentTimeInZone = now
                    }
                }
                epgRefreshTrigger++ // Erzwingt Recomposition der EPG-UI
            }
            
            // retryTrigger++ würde auch funktionieren, aber currentUrl-Change
            // reicht bereits, da LaunchedEffect darauf lauscht.
            // Wir zeigen das Overlay sofort:
            showChannelSwitchOverlay(ch.name)
        }
    }

    // ── Bildschirm anlassen während Wiedergabe ─────────────────
    LaunchedEffect(isPlaying, playerState, playerSettings) {
        currentView.keepScreenOn = playerSettings.keepScreenOn &&
            (isPlaying || playerState == PlayerState.BUFFERING)
    }

    // ── Timeline-Position kontinuierlich aktualisieren ─────────
    LaunchedEffect(isPlaying, isDragging) {
        if (!isPlaying || isDragging) return@LaunchedEffect
        while (true) {
            delay(500)
            val pm = playerManager
            if (!isDragging && pm != null) {
                val pos = pm.getCurrentPosition()
                if (pos >= 0) {
                    currentPositionState.longValue = pos
                }
                val dur = pm.getDuration()
                if (dur > 0 && dur != durationState.longValue) {
                    durationState.longValue = dur
                }
            }
        }
    }

    // ── EPG-Daten nachladen wenn sie fehlen (z.B. aus SearchScreen) ──
    LaunchedEffect(contentTyp, currentContentId) {
        if (contentTyp == ContentType.LIVE && PlayerContext.currentEpgItem == null) {
            val ch = PlayerContext.current() ?: return@LaunchedEffect
            val epgList = viewModel.loadEpgForChannel(ch.id)
            if (epgList.isNotEmpty()) {
                val now = System.currentTimeMillis()
                val tz = java.util.TimeZone.getTimeZone(PlayerContext.categoryTimezone)
                val offsetMs = tz.getOffset(now)
                val sorted = epgList
                    .map { item ->
                        item.copy(
                            startTime = item.startTime - offsetMs,
                            endTime = item.endTime - offsetMs
                        )
                    }
                    .sortedBy { it.startTime }
                val current = sorted.firstOrNull { item ->
                    item.startTime <= now && item.endTime > now
                }
                if (current != null) {
                    val idx = sorted.indexOf(current)
                    val remaining = ((current.endTime - now) / 60000).toInt()
                    val progress = ((now - current.startTime).toFloat() / (current.endTime - current.startTime) * 100f).coerceIn(0f, 100f)
                    PlayerContext.setEpgData(
                        previousEpgItem = if (idx > 0) sorted[idx - 1] else null,
                        currentEpgItem = current,
                        nextEpgItem = if (idx < sorted.size - 1) sorted[idx + 1] else null,
                        channelLogoUrl = PlayerContext.channelLogoUrl,
                        epgProgress = progress,
                        currentTimeInZone = now,
                        remainingMinutes = remaining,
                        categoryTimezone = PlayerContext.categoryTimezone
                    )
                    epgProgress = progress
                    epgRemainingMinutes = remaining
                    epgCurrentTimeInZone = now
                    epgRefreshTrigger++
                }
            }
        }
    }

    // ── EPG-Fortschritt kontinuierlich aktualisieren (Live-TV) ───
    LaunchedEffect(contentTyp, isPlaying, epgRefreshTrigger) {
        if (contentTyp != ContentType.LIVE || !isPlaying) return@LaunchedEffect
        while (true) {
            delay(1000)
            val current = PlayerContext.currentEpgItem ?: continue
            val now = System.currentTimeMillis()
            val total = current.endTime - current.startTime
            if (total > 0) {
                val elapsed = now - current.startTime
                epgProgress = ((elapsed.toFloat() / total) * 100f).coerceIn(0f, 100f)
                epgRemainingMinutes = maxOf(0, ((current.endTime - now) / 60000).toInt())
                epgCurrentTimeInZone = now
            }
        }
    }

    // ── Echter Vollbildmodus: System-Bars ausblenden ────────────
    // ── isChannelSwitching nach 2s zurücksetzen ─────────────────
    LaunchedEffect(isChannelSwitching) {
        if (isChannelSwitching) {
            delay(2000)
            isChannelSwitching = false
        }
    }
    val window = (context as? android.app.Activity)?.window
    DisposableEffect(Unit) {
        window?.let { w ->
            WindowCompat.setDecorFitsSystemWindows(w, false)
            val controller = WindowInsetsControllerCompat(w, currentView)
            controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        onDispose {
            window?.let { w ->
                WindowCompat.setDecorFitsSystemWindows(w, true)
                val controller = WindowInsetsControllerCompat(w, currentView)
                controller.show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
            }
        }
    }

    // Auto-focusiere den Play/Pause‑Button, wenn die Controls erscheinen
    LaunchedEffect(showControls) {
        if (showControls) {
            // Warte zwei Frames, bis AnimatedVisibility die Buttons in den Baum gehängt hat
            withFrameMillis { }
            withFrameMillis { }
            try { playPauseFocus.requestFocus() } catch (_: Exception) {}
        }
    }

    // ── Auto-Hide Timer ──────────────────────────────────────────
    fun resetHideTimer() {
        showControls = true
        hideJob?.cancel()
        hideJob = coroutineScope.launch {
            delay(playerSettings.autoHideControlsMs)
            if (isPlaying && playerState != PlayerState.ERROR) {
                showControls = false
            }
        }
    }

    // ── Activity Lifecycle Handling ─────────────────────────────
    // Pausiert den Player wenn die App in den Hintergrund geht (Home-Taste),
    // setzt fort wenn zurückgekehrt.
    // ON_STOP: Stoppt den Stream komplett (nicht nur Pause), damit keine
    //   gepufferten Audio-Daten mehr ausgegeben werden. Dies verhindert
    //   doppelte Streams wenn der User zurück zu LiveScreen navigiert.
    // release() wird zentral in DisposableEffect(Unit).onDispose behandelt.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    playerManager?.pause()
                }
                Lifecycle.Event.ON_STOP -> {
                    // Stream komplett stoppen (nicht nur pausieren) damit
                    // keine Audio-Reste im Buffer weiterlaufen
                    playerManager?.stop()
                }
                Lifecycle.Event.ON_RESUME -> {
                    // Nur fortsetzen, wenn der Player noch existiert und nicht im Error-State ist
                    val pm = playerManager
                    if (pm != null && pm.getState() != PlayerState.ERROR) {
                        pm.play()
                        isPlaying = true
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // ── Player initialisieren (sobald surfaceView verfügbar + Surface bereit) ────
    // WICHTIG: Alter Player muss VOR Erstellung des neuen freigegeben werden.
    // Sonst entsteht ein Memory-Leak wenn dieses LaunchedEffect erneut feuert
    // (z.B. bei Surface-Rekreation oder Retry). Der alte ExoPlayer läuft dann
    // weiter im Hintergrund und erzeugt doppelte Audio-Streams.
    LaunchedEffect(surfaceView, subtitleView, isSurfaceReady, retryTrigger, currentUrl) {
        Log.d("PlayerScreen", "Player init check: surfaceView=${surfaceView != null}, isSurfaceReady=$isSurfaceReady, url=$currentUrl")
        if (surfaceView != null && isSurfaceReady) {
            Log.d("PlayerScreen", "Initializing player with url=$currentUrl")
            errorMessage = null
            playerState = PlayerState.IDLE
            playerManager?.release()   // Alten Player freigeben
            playerManager = null       // Referenz löschen
            playerManager = PlayerManager(context, playerSettings.bufferSizeSeconds).also { pm ->
                pm.initialize(currentUrl, surfaceView!!)
                // SubtitleView binden, sobald sie verfügbar ist
                subtitleView?.let { pm.setSubtitleView(it) }
                pm.setOnStateChangeListener { state ->
                    Log.d("PlayerScreen", "Player state changed: $state")
                    playerState = state
                    isPlaying = state == PlayerState.PLAYING
                    if (state == PlayerState.ERROR) {
                        errorMessage = pm.getLastError() ?: "Unbekannter Fehler"
                        showControls = true
                    }
                    if (state == PlayerState.ENDED && contentTyp == ContentType.SERIES && playerSettings.autoPlayNext) {
                        if (currentSeason > 0 && currentEpisode > 0 && !autoPlayTriggered) {
                            autoPlayTriggered = true
                            coroutineScope.launch {
                                val nextEpisode = viewModel.loadNextEpisode(
                                    seriesId = currentContentId,
                                    currentSeason = currentSeason,
                                    currentEpisode = currentEpisode
                                )
                                if (nextEpisode != null) {
                                    viewModel.savePosition(
                                        url = currentUrl,
                                        position = currentPosition,
                                        duration = duration,
                                        type = ContentType.SERIES,
                                        contentId = currentContentId,
                                        title = currentTitle.ifEmpty { "Wiedergabe" },
                                        imageUrl = currentImageUrl,
                                        seasonNumber = currentSeason,
                                        episodeNumber = currentEpisode
                                    )
                                    nextEpisodeForAutoplay = nextEpisode
                                    countdownSeconds = 5
                                    showCountdown = true
                                    showControls = true
                                    hideJob?.cancel()
                                }
                            }
                        }
                    }
                }
                pm.setOnTimeUpdateListener { pos, dur ->
                    // Nicht überschreiben, während der User den Slider zieht
                    if (!isDragging) {
                        currentPosition = pos
                    }
                    duration = dur
                }
                // Wichtig: seekTo VOR play() aufrufen, damit Resume-Position
                // sofort korrekt gesetzt wird und nicht erst bei 0 beginnt.
                // Resume-Offset: ein paar Sekunden vor gespeicherter Position starten.
                if (currentResumePos > 0) {
                    val offsetPos = maxOf(0L, currentResumePos - playerSettings.resumeOffsetMs)
                    pm.seekTo(offsetPos)
                }
                pm.play()
                // Standard-Geschwindigkeit anwenden, wenn sie nicht 1.0× ist
                if (playerSettings.defaultPlaybackSpeed != 1.0f) {
                    pm.setPlaybackSpeed(playerSettings.defaultPlaybackSpeed)
                    playbackSpeed = playerSettings.defaultPlaybackSpeed
                }
            }
            resetHideTimer()
        }
    }

    // ── Periodische Positions-Speicherung (alle 15s) ────────────
    LaunchedEffect(playerManager, contentTyp) {
        if (contentTyp != ContentType.LIVE) {
            while (true) {
                delay(15_000)
                val pm = playerManager ?: continue
                try {
                    val pos = pm.getCurrentPosition()
                    val dur = pm.getDuration()
                    if (dur > 30_000) {
                        viewModel.savePosition(
                            url = currentUrl,
                            position = pos,
                            duration = dur,
                            type = contentTyp,
                            contentId = currentContentId,
                            title = currentTitle.ifEmpty { "Wiedergabe" },
                            imageUrl = currentImageUrl,
                            seasonNumber = currentSeason,
                            episodeNumber = currentEpisode
                        )
                    }
                } catch (_: Exception) {
                    // Player wurde freigegeben → beenden
                    break
                }
            }
        }
    }

    // ── Countdown für Auto-Play Next Episode ─────────────────────
    LaunchedEffect(showCountdown) {
        if (!showCountdown) return@LaunchedEffect
        while (countdownSeconds > 0) {
            delay(1000)
            countdownSeconds--
        }
        // Countdown abgelaufen → nächste Episode starten
        nextEpisodeForAutoplay?.let { next ->
            showCountdown = false
            navController.navigate(
                Screen.FullScreenPlayer.createRoute(
                    url = next.url,
                    type = "series",
                    contentId = currentContentId,
                    title = next.title,
                    season = next.seasonNumber,
                    episode = next.episodeNumber
                )
            )
        }
    }
    
    // ── Track-Informationen laden wenn Player bereit ────────────
    LaunchedEffect(playerManager, playerState) {
        if (playerState == PlayerState.READY || playerState == PlayerState.PLAYING) {
            val pm = playerManager ?: return@LaunchedEffect
            // Kurze Verzögerung um sicherzustellen dass Tracks verfügbar sind.
            // Nach dem delay nochmals prüfen, ob pm noch gültig (kann bei schnellem
            // Channel-Wechsel bereits released sein).
            delay(500)
            if (playerManager == null) return@LaunchedEffect
            subtitleTracks = pm.getSubtitleTracks()
            audioTracks = pm.getAudioTracks()
            currentSubtitleTrack = pm.getCurrentSubtitleTrack()
            currentAudioTrack = pm.getCurrentAudioTrack()
            tracksLoaded = true
            // Bevorzugte Audiosprache automatisch auswählen (falls konfiguriert)
            val lang = playerSettings.preferredAudioLanguage
            if (lang.isNotEmpty()) {
                val match = audioTracks.firstOrNull { it.language == lang }
                if (match != null && match.id != currentAudioTrack?.id) {
                    pm.selectAudioTrack(match.id)
                    currentAudioTrack = match
                }
            }
        }
    }

    // ── Hilfsfunktion: Player aufräumen und zurück navigieren ────
    fun cleanupAndGoBack() {
        // Aktuellen Kanal merken (mutableStateOf → löst Recomposition in LiveScreen aus)
        if (contentTyp == ContentType.LIVE) {
            PlayerContext.current()?.let { ch ->
                // FocusRestoreManager aktualisieren, damit remember() in LiveScreen neu evaluiert
                FocusRestoreManager.clearFocus("live_screen")
                FocusRestoreManager.saveFocus("live_screen", ch.id)
                BackNavigationState.selectedChannelId = ch.id
            }
        }
        playerManager?.let { pm ->
            val pos = pm.getCurrentPosition()
            val dur = pm.getDuration()
            if (dur > 30_000 && contentTyp != ContentType.LIVE) {
                viewModel.savePosition(
                    url = currentUrl,
                    position = pos,
                    duration = dur,
                    type = contentTyp,
                    contentId = currentContentId,
                    title = currentTitle.ifEmpty { "Wiedergabe" },
                    imageUrl = currentImageUrl,
                    seasonNumber = currentSeason,
                    episodeNumber = currentEpisode
                )
            }
            pm.release()
        }
        playerManager = null
        navController.popBackStack()
    }

    // ── System-Back-Button abfangen ──────────────────────────────
    // cleanupAndGoBack() gibt den Player VOLLSTÄNDIG frei (stop + release
    // + null) BEVOR popBackStack aufgerufen wird. Das ist der zentrale Fix
    // für doppelte Streams: Ohne release() hier lebt der Fullscreen-Player
    // noch während LiveScreen via ON_RESUME schon den Preview startet.
    BackHandler {
        cleanupAndGoBack()
    }

    // ── Aufräumen beim Verlassen (Navigation innerhalb der App) ──
    DisposableEffect(Unit) {
        onDispose {
            currentView.keepScreenOn = false
            playerManager?.let { pm ->
                val pos = pm.getCurrentPosition()
                val dur = pm.getDuration()
                if (dur > 30_000 && contentTyp != ContentType.LIVE) {
                viewModel.savePosition(
                    url = currentUrl,
                    position = pos,
                    duration = dur,
                    type = contentTyp,
                    contentId = currentContentId,
                    title = currentTitle.ifEmpty { "Wiedergabe" },
                    imageUrl = currentImageUrl,
                    seasonNumber = currentSeason,
                    episodeNumber = currentEpisode
                )
            }
            pm.stop()
            pm.release()
            }
            PlayerContext.clear()
        }
    }

    // ── UI ────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OsiBlack)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { resetHideTimer() }
            )
            .onKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
                when (event.key) {
                    Key.DirectionCenter -> {
                        if (!showControls) {
                            // Controls versteckt → zuerst Controls einblenden
                            resetHideTimer()
                            true
                        } else {
                            // Controls sichtbar → Play/Pause umschalten
                            if (isPlaying) {
                                playerManager?.pause()
                            } else {
                                playerManager?.play()
                            }
                            isPlaying = !isPlaying
                            resetHideTimer()
                            true
                        }
                    }
                    Key.DirectionUp -> {
                        if (contentTyp == ContentType.LIVE && PlayerContext.hasNext()) {
                            switchToNextChannel(next = true)
                            resetHideTimer()
                            true
                        } else {
                            resetHideTimer()
                            false
                        }
                    }
                    Key.DirectionDown -> {
                        if (contentTyp == ContentType.LIVE && PlayerContext.hasPrevious()) {
                            switchToNextChannel(next = false)
                            resetHideTimer()
                            true
                        } else {
                            resetHideTimer()
                            false
                        }
                    }
                    Key.DirectionLeft, Key.DirectionRight -> {
                        // Controls einblenden / Timer resetten; nicht konsumieren –
                        // das Focus‑System soll die Navigation trotzdem verarbeiten
                        resetHideTimer()
                        false
                    }
                    Key.Back -> {
                        cleanupAndGoBack()
                        true
                    }
                    Key.MediaPlayPause -> {
                        if (isPlaying) {
                            playerManager?.pause()
                        } else {
                            playerManager?.play()
                        }
                        isPlaying = !isPlaying
                        resetHideTimer()
                        true
                    }
                    Key.MediaFastForward -> {
                        val pos = (currentPosition + playerSettings.seekForwardMs).coerceAtMost(duration)
                        playerManager?.seekTo(pos)
                        if (!isDragging) currentPosition = pos
                        resetHideTimer()
                        true
                    }
                    Key.MediaRewind -> {
                        val pos = (currentPosition - playerSettings.seekBackwardMs).coerceAtLeast(0)
                        playerManager?.seekTo(pos)
                        if (!isDragging) currentPosition = pos
                        resetHideTimer()
                        true
                    }
                    Key.MediaStop -> {
                        playerManager?.stop()
                        isPlaying = false
                        resetHideTimer()
                        true
                    }
                    else -> false
                }
            }
    ) {
        // Video Surface + Subtitle View (als FrameLayout übereinander gestapelt)
        AndroidView(
            factory = { ctx ->
                FrameLayout(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                    // SurfaceView für Video-Rendering
                    val sv = SurfaceView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                isSurfaceReady = true
                            }
                            override fun surfaceChanged(
                                holder: SurfaceHolder,
                                format: Int,
                                width: Int,
                                height: Int
                            ) { /* wird nicht benötigt */ }
                            override fun surfaceDestroyed(holder: SurfaceHolder) {
                                isSurfaceReady = false
                            }
                        })
                        // SurfaceView darf keine Touch-Events konsumieren,
                        // sonst funktioniert das Tap-Einblenden der Controls nicht
                        isClickable = false
                        isFocusable = false
                    }.also { surfaceView = it }

                    // SubtitleView für Untertitel-Rendering (über der SurfaceView)
                    val sv2 = SubtitleView(ctx).apply {
                        layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )
                        // Touch-Events nicht konsumieren
                        isClickable = false
                        isFocusable = false
                        // Standard-UserStyle verwenden (Nutzer-Einstellungen aus dem System)
                        setUserDefaultStyle()
                        setUserDefaultTextSize()
                    }.also { subtitleView = it }

                    addView(sv)
                    addView(sv2)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // ── Live Channel Switch Overlay ────────────────────────────
        AnimatedVisibility(
            visible = showChannelOverlay,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = channelOverlayText,
                    color = OsiTextPrimary,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(OsiBlack.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }
        }

        // Buffering / Loading
        if (playerState == PlayerState.BUFFERING || playerState == PlayerState.IDLE) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = WarmGold, modifier = Modifier.size(72.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Lädt Stream...",
                        color = OsiTextPrimary.copy(alpha = 0.7f),
                        fontSize = 21.sp
                    )
                }
            }
        }

        // ── Error State (unterdrückt während Channel-Wechsel) ───
        if (playerState == PlayerState.ERROR && !isChannelSwitching) {
            Box(
                modifier = Modifier.fillMaxSize().background(OsiBlack.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        tint = WarmError
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Fehler bei der Wiedergabe",
                        color = OsiTextPrimary,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (errorMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            errorMessage!!,
                            color = OsiTextPrimary.copy(alpha = 0.7f),
                            fontSize = 19.5.sp,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    Spacer(Modifier.height(24.dp))

                    // Retry-Button
                    Button(
                        onClick = {
                            playerManager?.release()
                            playerManager = null
                            errorMessage = null
                            retryTrigger++
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = WarmGold),
                        modifier = Modifier
                            .height(48.dp)
                            .focusable()
                            .focusRequester(retryFocus)
                    ) {
                        Icon(Icons.Filled.Refresh, null, Modifier.size(30.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Neu laden", fontSize = 22.5.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(onClick = {
                        cleanupAndGoBack()
                    },
                    modifier = Modifier
                        .focusable()
                        .focusRequester(backTextFocus)
                    ) {
                        Text("Zurück", color = WarmTextSecondary, fontSize = 21.sp)
                    }
                }
            }
        }

        // ── Countdown Overlay (Nächste Episode Auto-Play) ──────────
        AnimatedVisibility(
            visible = showCountdown,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(200))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(OsiBlack.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { /* keine Aktion – Countdown erst bei 0 oder Button-Klick */ }
                        )
                ) {
                    Text(
                        "Nächste Episode",
                        color = WarmTextSecondary,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(10.dp))
                    nextEpisodeForAutoplay?.let { ep ->
                        Text(
                            "S${ep.seasonNumber}E${ep.episodeNumber}",
                            color = WarmGold,
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            ep.title,
                            color = OsiTextPrimary,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                    }
                    Spacer(Modifier.height(36.dp))
                    Text(
                        "startet in",
                        color = WarmTextSecondary,
                        fontSize = 21.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "$countdownSeconds",
                        color = WarmGold,
                        fontSize = 108.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(40.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                showCountdown = false
                                nextEpisodeForAutoplay = null
                                cleanupAndGoBack()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WarmTextSecondary
                            )
                        ) {
                            Icon(Icons.Filled.Close, null, Modifier.size(27.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Zurück")
                        }
                        Button(
                            onClick = {
                                nextEpisodeForAutoplay?.let { next ->
                                    showCountdown = false
                                    navController.navigate(
                                        Screen.FullScreenPlayer.createRoute(
                                            url = next.url,
                                            type = "series",
                                            contentId = currentContentId,
                                            title = next.title,
                                            season = next.seasonNumber,
                                            episode = next.episodeNumber
                                        )
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = WarmGold),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, null, Modifier.size(36.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Jetzt starten", fontSize = 24.sp)
                        }
                    }
                }
            }
        }

        // ── Top Overlay (Back, Titel, Speed) ─────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(Modifier.fillMaxSize()) {
                // Back Button (oben links)
                // cleanupAndGoBack() → Player freigeben + Position speichern + zurück
                IconButton(
                    onClick = {
                        cleanupAndGoBack()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(OsiBlack.copy(alpha = 0.5f), CircleShape)
                        .focusable()
                        .focusRequester(backFocus)
                        .focusProperties { down = playPauseFocus }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück",
                        tint = OsiTextPrimary,
                        modifier = Modifier.size(42.dp)
                    )
                }

                // Titel / EPG Info (oben Mitte)
                if (contentTyp == ContentType.LIVE && PlayerContext.currentEpgItem != null) {
                    // Live TV: Show current program title
                    Text(
                        PlayerContext.currentEpgItem?.title ?: "Live",
                        color = OsiTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(20.dp)
                            .background(OsiBlack.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                } else {
                    // VOD: Show title
                    Text(
                        currentTitle.ifEmpty {
                            when (contentTyp) {
                                ContentType.MOVIE -> "Film"
                                ContentType.SERIES -> "Serie"
                                ContentType.LIVE -> "Live"
                            }
                        },
                        color = OsiTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(20.dp)
                            .background(OsiBlack.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }

                // Speed Indicator (oben rechts)
                if (playbackSpeed != 1.0f) {
                    Text(
                        "${playbackSpeed}x",
                        color = WarmGold,
                        fontSize = 19.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .background(OsiBlack.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // ── Controls Overlay (fullscreen: sidebar + bottom) ──────
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(Modifier.fillMaxSize()) {
                
                 // Debug logging
                 LaunchedEffect(contentTyp) {
                     android.util.Log.d("PlayerScreen", "contentTyp = $contentTyp")
                     android.util.Log.d("PlayerScreen", "currentEpgItem = ${PlayerContext.currentEpgItem}")
                     android.util.Log.d("PlayerScreen", "categoryTimezone = ${PlayerContext.categoryTimezone}")
                 }
                 
                  // EPG Right Sidebar (only for Live TV)
                  // epgRefreshTrigger als Lese-Dependency, damit Recomposition
                  // nach EPG-Laden oder Fortschritts-Update erzwungen wird.
                  if (contentTyp == ContentType.LIVE) {
                      val channels = PlayerContext.channels
                      val currentIdx = PlayerContext.currentIndex
                      @Suppress("UNUSED_EXPRESSION")
                      epgRefreshTrigger
                      EPGLogosSidebar(
                          prevLogoUrl = channels.getOrNull(currentIdx - 1)?.logo,
                          currentLogoUrl = PlayerContext.channelLogoUrl,
                          nextLogoUrl = channels.getOrNull(currentIdx + 1)?.logo,
                          previousEpgItem = PlayerContext.previousEpgItem,
                          currentEpgItem = PlayerContext.currentEpgItem,
                          nextEpgItem = PlayerContext.nextEpgItem,
                          modifier = Modifier.align(Alignment.CenterEnd)
                      )
                  }
                 
                 // ═══════════════════════════════════════════════════════════════
                 // FLOATING PILL PLAYERBAR — New Design
                 // ═══════════════════════════════════════════════════════════════
                 Box(
                     modifier = Modifier
                         .align(Alignment.BottomCenter)
                         .fillMaxWidth()
                         .padding(bottom = 16.dp)
                 ) {
                     if (contentTyp == ContentType.LIVE) {
                         // ── Live TV: Floating Pill with EPG ──────────────────
                         LiveTvPlayerPill(
                             currentEpgItem = PlayerContext.currentEpgItem,
                             epgProgress = epgProgress,
                             channelName = currentTitle,
                             currentTimeInZone = epgCurrentTimeInZone,
                             remainingMinutes = epgRemainingMinutes,
                             timeDisplayString = formatCurrentTimeForDisplay(
                                 epgCurrentTimeInZone,
                                 PlayerContext.categoryTimezone
                             ),
                             epgRefreshTrigger = epgRefreshTrigger
                         )
                     } else {
                         // ── VOD: Floating Pill with Controls ─────────────────
                         var showMoreMenu by remember { mutableStateOf(false) }
                         
                         Column(
                             modifier = Modifier.fillMaxWidth(),
                             verticalArrangement = Arrangement.spacedBy(8.dp)
                         ) {
                             // More Menu (above pill, slide-up)
                             AnimatedVisibility(
                                 visible = showMoreMenu,
                                 enter = slideInVertically(
                                     initialOffsetY = { it },
                                     animationSpec = tween(250)
                                 ) + fadeIn(tween(200)),
                                 exit = slideOutVertically(
                                     targetOffsetY = { it },
                                     animationSpec = tween(200)
                                 ) + fadeOut(tween(150))
                             ) {
                                 PlayerMoreMenuPanel(
                                     playbackSpeed = playbackSpeed,
                                     currentAspectRatio = currentAspectRatio,
                                     currentSubtitleTrack = currentSubtitleTrack,
                                     subtitleTracks = subtitleTracks,
                                     audioTracks = audioTracks,
                                     currentAudioTrack = currentAudioTrack,
                                     showSubtitleButton = showSubtitleButton,
                                     showAudioButton = showAudioButton,
                                     onSpeedChange = { speed ->
                                         playbackSpeed = speed
                                         playerManager?.setPlaybackSpeed(speed)
                                         resetHideTimer()
                                     },
                                     onAspectRatioChange = {
                                         val currentIndex = ASPECT_RATIO_CYCLE.indexOf(currentAspectRatio)
                                         val nextIndex = (currentIndex + 1) % ASPECT_RATIO_CYCLE.size
                                         currentAspectRatio = ASPECT_RATIO_CYCLE[nextIndex]
                                         playerManager?.setAspectRatio(ASPECT_RATIO_CYCLE[nextIndex])
                                         resetHideTimer()
                                     },
                                     onSubtitleSelect = { track ->
                                         playerManager?.selectSubtitleTrack(
                                             if (track.id == "none") null else track.id
                                         )
                                         currentSubtitleTrack = if (track.id == "none") null else track
                                         resetHideTimer()
                                     },
                                     onAudioSelect = { track ->
                                         playerManager?.selectAudioTrack(track.id)
                                         currentAudioTrack = track
                                         resetHideTimer()
                                     },
                                     onStop = {
                                         playerManager?.let {
                                             viewModel.savePosition(
                                                 url = currentUrl,
                                                 position = it.getCurrentPosition(),
                                                 duration = it.getDuration(),
                                                 type = contentTyp,
                                                 contentId = currentContentId,
                                                 title = currentTitle.ifEmpty { "Wiedergabe" },
                                                 imageUrl = currentImageUrl
                                             )
                                         }
                                         playerManager?.stop()
                                         isPlaying = false
                                         resetHideTimer()
                                     },
                                     onDismiss = { showMoreMenu = false },
                                     speedFocus = speedFocus,
                                     aspectRatioFocus = aspectRatioFocus,
                                     stopFocus = stopFocus,
                                     subtitleFocus = subtitleFocus,
                                     audioFocus = audioFocus
                                 )
                             }
                             
                             // Main Floating Pill
                             Box(
                                 modifier = Modifier
                                     .fillMaxWidth()
                                     .padding(horizontal = 16.dp)
                                     .clip(RoundedCornerShape(14.dp))
                                     .background(Color(0xCC0A0A0A))
                                     .border(
                                         width = 1.dp,
                                         brush = Brush.linearGradient(
                                             colors = listOf(
                                                 OsiCyan.copy(alpha = 0.3f),
                                                 OsiBorder,
                                                 OsiCyan.copy(alpha = 0.1f)
                                             )
                                         ),
                                         shape = RoundedCornerShape(14.dp)
                                     )
                                     .padding(16.dp)
                             ) {
                                 Column(
                                     modifier = Modifier.fillMaxWidth(),
                                     verticalArrangement = Arrangement.spacedBy(12.dp)
                                 ) {
                                     // ── Progress Bar ────────────────────────
                                     Column(
                                         modifier = Modifier.fillMaxWidth(),
                                         verticalArrangement = Arrangement.spacedBy(4.dp)
                                     ) {
                                          // Custom Slider with Cyan Gradient
                                          Box(
                                              modifier = Modifier
                                                  .fillMaxWidth()
                                                  .height(20.dp)
                                                  .focusRequester(sliderFocus)
                                                  .focusProperties { down = playPauseFocus }
                                                  .focusable(),
                                              contentAlignment = Alignment.Center
                                          ) {
                                             // Track Background
                                             Box(
                                                 modifier = Modifier
                                                     .fillMaxWidth()
                                                     .height(4.dp)
                                                     .clip(RoundedCornerShape(2.dp))
                                                     .background(Color(0xFF2A2A2A))
                                             )
                                             // Progress Fill
                                             val progress = if (duration > 0) {
                                                 (currentPosition.toFloat() / duration).coerceIn(0f, 1f)
                                             } else 0f
                                             Box(
                                                 modifier = Modifier
                                                     .fillMaxWidth(progress)
                                                     .height(4.dp)
                                                     .align(Alignment.CenterStart)
                                                     .clip(RoundedCornerShape(2.dp))
                                                     .background(
                                                         Brush.horizontalGradient(
                                                             colors = listOf(OsiCyan, OsiCyan.copy(alpha = 0.7f))
                                                         )
                                                     )
                                             )
                                             // Seek Thumb
                                             Box(
                                                 modifier = Modifier
                                                     .fillMaxWidth(progress)
                                                     .wrapContentSize(Alignment.CenterEnd)
                                             ) {
                                                 Box(
                                                     modifier = Modifier
                                                         .size(12.dp)
                                                         .shadow(4.dp, CircleShape)
                                                         .clip(CircleShape)
                                                         .background(OsiCyan)
                                                         .border(2.dp, Color.White, CircleShape)
                                                 )
                                             }
                                             // Invisible Slider for actual seek interaction
                                             Slider(
                                                 value = if (duration > 0) {
                                                     (currentPosition.toFloat() / duration).coerceIn(0f, 1f)
                                                 } else 0f,
                                                 onValueChange = { fraction ->
                                                     val pos = (fraction * duration).toLong()
                                                     currentPosition = pos
                                                     isDragging = true
                                                 },
                                                 onValueChangeFinished = {
                                                     playerManager?.seekTo(currentPosition)
                                                     isDragging = false
                                                     resetHideTimer()
                                                 },
                                                 modifier = Modifier
                                                     .fillMaxWidth()
                                                     .height(20.dp)
                                                     .graphicsLayer(alpha = 0f),
                                                 colors = SliderDefaults.colors(
                                                     thumbColor = Color.Transparent,
                                                     activeTrackColor = Color.Transparent,
                                                     inactiveTrackColor = Color.Transparent
                                                 )
                                             )
                                         }
                                         
                                          // Time Labels
                                          Row(
                                              modifier = Modifier.fillMaxWidth(),
                                              verticalAlignment = Alignment.CenterVertically
                                          ) {
                                              Text(
                                                  text = formatTime(currentPosition),
                                                  color = OsiTextSecondary,
                                                  fontSize = 16.5.sp,
                                                  fontWeight = FontWeight.Medium
                                              )
                                              Text(
                                                  text = currentTitle.ifEmpty { "Wiedergabe" },
                                                  color = OsiTextPrimary.copy(alpha = 0.6f),
                                                  fontSize = 16.5.sp,
                                                  maxLines = 1,
                                                  overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                  modifier = Modifier
                                                      .weight(1f)
                                                      .padding(horizontal = 12.dp),
                                                  textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                              )
                                              Text(
                                                  text = formatTime(duration),
                                                  color = OsiTextSecondary,
                                                  fontSize = 16.5.sp,
                                                  fontWeight = FontWeight.Medium
                                              )
                                              // More Options (three dots) in seekbar
                                              IconButton(
                                                  onClick = {
                                                      showMoreMenu = !showMoreMenu
                                                      resetHideTimer()
                                                  },
                                                  modifier = Modifier.size(42.dp)
                                              ) {
                                                  Icon(
                                                      imageVector = Icons.Filled.MoreVert,
                                                      contentDescription = "Mehr Optionen",
                                                      tint = OsiTextSecondary,
                                                      modifier = Modifier.size(27.dp)
                                                  )
                                              }
                                          }
                                     }
                                     
                                     // ── Control Buttons Row ─────────────────
                                     Row(
                                         modifier = Modifier.fillMaxWidth(),
                                         horizontalArrangement = Arrangement.SpaceEvenly,
                                         verticalAlignment = Alignment.CenterVertically
                                     ) {
                                         // Rewind
                                         PlayerPillControlButton(
                                             icon = Icons.Filled.FastRewind,
                                             label = formatSeekLabel(playerSettings.seekBackwardMs),
                                             onClick = {
                                                 val pos = (currentPosition - playerSettings.seekBackwardMs).coerceAtLeast(0)
                                                 playerManager?.seekTo(pos)
                                                 currentPosition = pos
                                                 resetHideTimer()
                                             },
                                             modifier = Modifier
                                                 .focusRequester(rewindFocus)
                                                 .focusProperties {
                                                     right = playPauseFocus
                                                     up = sliderFocus
                                                 }
                                         )
                                         
                                         // Play/Pause — Primary Button
                                         if (playerState == PlayerState.BUFFERING) {
                                             // Buffering indicator
                                             Box(
                                                 modifier = Modifier
                                                     .size(56.dp)
                                                     .clip(CircleShape)
                                                     .background(OsiCyan.copy(alpha = 0.15f))
                                                     .border(2.dp, OsiCyan.copy(alpha = 0.4f), CircleShape),
                                                 contentAlignment = Alignment.Center
                                             ) {
                                                 CircularProgressIndicator(
                                                     modifier = Modifier.size(42.dp),
                                                     color = OsiCyan,
                                                     strokeWidth = 2.5.dp
                                                 )
                                             }
                                         } else {
                                             PlayerPillPlayPauseButton(
                                                 isPlaying = isPlaying,
                                                 onClick = {
                                                     if (isPlaying) {
                                                         playerManager?.pause()
                                                         isPlaying = false
                                                     } else {
                                                         playerManager?.play()
                                                         isPlaying = true
                                                     }
                                                     resetHideTimer()
                                                 },
                                                 modifier = Modifier
                                                     .focusRequester(playPauseFocus)
                                                     .focusProperties {
                                                         left = rewindFocus
                                                         right = forwardFocus
                                                         up = sliderFocus
                                                     }
                                             )
                                         }
                                         
                                          // Forward
                                          PlayerPillControlButton(
                                              icon = Icons.Filled.FastForward,
                                              label = formatSeekLabel(playerSettings.seekForwardMs),
                                              onClick = {
                                                  val pos = (currentPosition + playerSettings.seekForwardMs).coerceAtMost(duration)
                                                  playerManager?.seekTo(pos)
                                                  currentPosition = pos
                                                  resetHideTimer()
                                              },
                                               modifier = Modifier
                                                   .focusRequester(forwardFocus)
                                                   .focusProperties {
                                                       left = playPauseFocus
                                                       up = sliderFocus
                                                   }
                                          )
                                     }
                                 }
                             }
                         }
                     }
                 }  // Close Floating Pill bottom box
        }  // Close fillMaxSize Box
    }  // Close AnimatedVisibility
}  // Close main Box
}  // Close PlayerScreen function

/**
 * Formatiert Millisekunden als lesbares Seek-Label (z.B. "30s", "2m", "1m30s").
 */
private fun formatSeekLabel(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return when {
        minutes == 0L -> "${seconds}s"
        seconds == 0L -> "${minutes}m"
        else -> "${minutes}m${seconds}s"
    }
}

/**
 * Formatiert Millisekunden als hh:mm:ss oder mm:ss.
 */
private fun formatTime(ms: Long): String {
    if (ms <= 0) return "00:00"
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}

/**
 * Formatiert Unix-Timestamp als HH:MM in der angegebenen Timezone.
 */
private fun formatCurrentTimeForDisplay(timestamp: Long, timezoneName: String): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone(timezoneName)
    return formatter.format(Date(timestamp))
}

// ═══════════════════════════════════════════════════════════════════════════════
// FLOATING PILL COMPONENTS — New Playerbar Design
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Play/Pause Button — Circular with Glow Effect and Spring Animation.
 * 56dp diameter, radial gradient OsiCyan, shadow glow on focus.
 */
@Composable
private fun PlayerPillPlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val glowAlpha by animateFloatAsState(
        targetValue = if (isFocused) 0.6f else 0.2f,
        animationSpec = tween(300),
        label = "glowAlpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Box(
        modifier = modifier
            .size(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(
                elevation = if (isFocused) 12.dp else 4.dp,
                shape = CircleShape,
                ambientColor = OsiCyan.copy(alpha = glowAlpha),
                spotColor = OsiCyan.copy(alpha = glowAlpha)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        OsiCyan,
                        OsiCyan.copy(alpha = 0.85f)
                    )
                )
            )
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.Black,
            modifier = Modifier.size(42.dp)
        )
    }
}

/**
 * Control Button — 44dp circle with scale + border animation on focus.
 */
@Composable
private fun PlayerPillControlButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) OsiCyan else Color.Transparent,
        animationSpec = tween(200),
        label = "borderColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.08f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = modifier
                .size(size)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clip(CircleShape)
                .background(
                    if (isFocused) OsiCyan.copy(alpha = 0.1f) else Color(0xFF1A1A1A)
                )
                .border(
                    width = 1.5.dp,
                    color = borderColor,
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
                .onFocusChanged { isFocused = it.isFocused }
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isFocused) OsiCyan else OsiTextPrimary,
                modifier = Modifier.size(33.dp)
            )
        }
        Text(
            text = label,
            color = if (isFocused) OsiCyan else OsiTextSecondary,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * More Menu Panel — Slide-up panel with Speed, Aspect, Subtitles, Audio, Stop.
 * Glassmorphism container matching the pill design.
 */
@Composable
private fun PlayerMoreMenuPanel(
    playbackSpeed: Float,
    currentAspectRatio: AspectRatio,
    currentSubtitleTrack: SubtitleTrack?,
    subtitleTracks: List<SubtitleTrack>,
    audioTracks: List<AudioTrack>,
    currentAudioTrack: AudioTrack?,
    showSubtitleButton: Boolean,
    showAudioButton: Boolean,
    onSpeedChange: (Float) -> Unit,
    onAspectRatioChange: () -> Unit,
    onSubtitleSelect: (SubtitleTrack) -> Unit,
    onAudioSelect: (AudioTrack) -> Unit,
    onStop: () -> Unit,
    onDismiss: () -> Unit,
    speedFocus: FocusRequester,
    aspectRatioFocus: FocusRequester,
    stopFocus: FocusRequester,
    subtitleFocus: FocusRequester,
    audioFocus: FocusRequester
) {
    var showSpeedSubmenu by remember { mutableStateOf(false) }
    var showSubtitleSubmenu by remember { mutableStateOf(false) }
    var showAudioSubmenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xE60A0A0A))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        OsiCyan.copy(alpha = 0.2f),
                        OsiBorder
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Main menu items row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Speed
                MoreMenuItemButton(
                    icon = Icons.Filled.Speed,
                    label = "${playbackSpeed}x",
                    isActive = playbackSpeed != 1.0f,
                    onClick = { showSpeedSubmenu = !showSpeedSubmenu },
                    modifier = Modifier
                        .focusRequester(speedFocus)
                        .focusProperties {
                            right = aspectRatioFocus
                        }
                )
                // Aspect Ratio
                MoreMenuItemButton(
                    icon = Icons.Filled.AspectRatio,
                    label = if (currentAspectRatio == AspectRatio.FIT) "FIT" else "FILL",
                    isActive = currentAspectRatio != AspectRatio.FIT,
                    onClick = onAspectRatioChange,
                    modifier = Modifier
                        .focusRequester(aspectRatioFocus)
                        .focusProperties {
                            left = speedFocus
                            right = if (showSubtitleButton) subtitleFocus else stopFocus
                        }
                )
                // Subtitles (conditional)
                if (showSubtitleButton) {
                    MoreMenuItemButton(
                        icon = Icons.Filled.Subtitles,
                        label = currentSubtitleTrack?.label?.take(8) ?: "Aus",
                        isActive = currentSubtitleTrack != null,
                        onClick = { showSubtitleSubmenu = !showSubtitleSubmenu },
                        modifier = Modifier
                            .focusRequester(subtitleFocus)
                            .focusProperties {
                                left = aspectRatioFocus
                                right = if (showAudioButton) audioFocus else stopFocus
                            }
                    )
                }
                // Audio (conditional)
                if (showAudioButton) {
                    MoreMenuItemButton(
                        icon = Icons.Filled.Audiotrack,
                        label = currentAudioTrack?.label?.take(8) ?: "Audio",
                        isActive = false,
                        onClick = { showAudioSubmenu = !showAudioSubmenu },
                        modifier = Modifier
                            .focusRequester(audioFocus)
                            .focusProperties {
                                left = if (showSubtitleButton) subtitleFocus else aspectRatioFocus
                                right = stopFocus
                            }
                    )
                }
                // Stop
                MoreMenuItemButton(
                    icon = Icons.Filled.Stop,
                    label = "Stop",
                    isActive = false,
                    onClick = {
                        onStop()
                        onDismiss()
                    },
                    modifier = Modifier
                        .focusRequester(stopFocus)
                        .focusProperties {
                            left = if (showAudioButton) audioFocus
                                   else if (showSubtitleButton) subtitleFocus
                                   else aspectRatioFocus
                        }
                )
            }

            // Speed Submenu
            AnimatedVisibility(
                visible = showSpeedSubmenu,
                enter = expandVertically(tween(200)) + fadeIn(tween(150)),
                exit = shrinkVertically(tween(150)) + fadeOut(tween(100))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SPEED_OPTIONS.forEach { speed ->
                        val isSelected = speed == playbackSpeed
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) OsiCyan.copy(alpha = 0.15f) else Color.Transparent
                                )
                                .border(
                                    width = if (isSelected) 1.dp else 0.dp,
                                    color = if (isSelected) OsiCyan.copy(alpha = 0.5f) else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable {
                                    onSpeedChange(speed)
                                    showSpeedSubmenu = false
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .focusable(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${speed}x",
                                color = if (isSelected) OsiCyan else OsiTextPrimary,
                                fontSize = 19.5.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Subtitle Submenu
            AnimatedVisibility(
                visible = showSubtitleSubmenu,
                enter = expandVertically(tween(200)) + fadeIn(tween(150)),
                exit = shrinkVertically(tween(150)) + fadeOut(tween(100))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    subtitleTracks.forEach { track ->
                        val isSelected = track.id == currentSubtitleTrack?.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) OsiCyan.copy(alpha = 0.1f) else Color.Transparent
                                )
                                .clickable {
                                    onSubtitleSelect(track)
                                    showSubtitleSubmenu = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .focusable()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.Check,
                                        null,
                                        tint = OsiCyan,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    text = track.label,
                                    color = if (isSelected) OsiCyan else OsiTextPrimary,
                                    fontSize = 19.5.sp
                                )
                            }
                        }
                    }
                }
            }

            // Audio Submenu
            AnimatedVisibility(
                visible = showAudioSubmenu,
                enter = expandVertically(tween(200)) + fadeIn(tween(150)),
                exit = shrinkVertically(tween(150)) + fadeOut(tween(100))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    audioTracks.forEach { track ->
                        val isSelected = track.id == currentAudioTrack?.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) OsiCyan.copy(alpha = 0.1f) else Color.Transparent
                                )
                                .clickable {
                                    onAudioSelect(track)
                                    showAudioSubmenu = false
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                .focusable()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.Check,
                                        null,
                                        tint = OsiCyan,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    text = track.label,
                                    color = if (isSelected) OsiCyan else OsiTextPrimary,
                                    fontSize = 19.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Single item in the More Menu — 48dp rounded square with icon + label.
 */
@Composable
private fun MoreMenuItemButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    when {
                        isActive -> OsiCyan.copy(alpha = 0.1f)
                        isFocused -> Color(0xFF1A1A1A)
                        else -> Color.Transparent
                    }
                )
                .border(
                    width = if (isFocused || isActive) 1.dp else 0.dp,
                    color = when {
                        isActive -> OsiCyan.copy(alpha = 0.5f)
                        isFocused -> OsiCyan
                        else -> Color.Transparent
                    },
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable(onClick = onClick)
                .onFocusChanged { isFocused = it.isFocused }
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive || isFocused) OsiCyan else OsiTextPrimary,
                modifier = Modifier.size(33.dp)
            )
        }
        Text(
            text = label,
            color = if (isActive || isFocused) OsiCyan else OsiTextSecondary,
            fontSize = 15.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
        )
    }
}

/**
 * Live TV Floating Pill — EPG-focused with StarYellow accent.
 * Shows channel name, current program, progress bar, and channel switch hints.
 */
@Composable
private fun LiveTvPlayerPill(
    currentEpgItem: EpgItem?,
    epgProgress: Float,
    channelName: String,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    epgRefreshTrigger: Int
) {
    // Force recomposition on EPG updates
    @Suppress("UNUSED_EXPRESSION")
    epgRefreshTrigger

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xCC0A0A0A))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        OsiStarYellow.copy(alpha = 0.3f),
                        OsiBorder,
                        OsiStarYellow.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Channel + Time Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(OsiStarYellow.copy(alpha = 0.1f))
                        .border(1.dp, OsiStarYellow.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = channelName,
                        color = OsiStarYellow,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
                // Current Time
                Text(
                    text = timeDisplayString,
                    color = OsiTextSecondary,
                    fontSize = 16.5.sp
                )
            }

            // Program Title
            Text(
                text = currentEpgItem?.title ?: "Kein EPG",
                color = OsiTextPrimary,
                fontSize = 22.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )

            // EPG Progress Bar (StarYellow gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2A2A2A))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(epgProgress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(OsiStarYellow, OsiStarYellow.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            // Remaining time + Channel navigation hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (remainingMinutes > 0) {
                    Text(
                        text = "Noch ${remainingMinutes} Min.",
                        color = OsiTextSecondary,
                        fontSize = 15.sp
                    )
                } else {
                    Spacer(Modifier.width(1.dp))
                }
                
                // Channel Navigation Hint
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = null,
                        tint = OsiTextSecondary,
                        modifier = Modifier.size(21.dp)
                    )
                    Text(
                        text = "CH",
                        color = OsiTextSecondary,
                        fontSize = 13.5.sp
                    )
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = OsiTextSecondary,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}