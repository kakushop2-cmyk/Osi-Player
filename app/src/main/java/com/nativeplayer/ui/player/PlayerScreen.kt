package com.nativeplayer.ui.player

import android.view.SurfaceView
import android.view.SurfaceHolder
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.media3.ui.SubtitleView
import androidx.compose.animation.*
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
import com.nativeplayer.player.PlayerManager
import com.nativeplayer.player.PlayerState
import com.nativeplayer.player.SubtitleTrack
import com.nativeplayer.player.AudioTrack
import com.nativeplayer.ui.theme.*
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
            tint = Color.White,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 11.sp,
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
    
    // ── Track Selection State ──
    var showSubtitleMenu by remember { mutableStateOf(false) }
    var showAudioMenu by remember { mutableStateOf(false) }
    var subtitleTracks by remember { mutableStateOf<List<SubtitleTrack>>(emptyList()) }
    var audioTracks by remember { mutableStateOf<List<AudioTrack>>(emptyList()) }
    var currentSubtitleTrack by remember { mutableStateOf<SubtitleTrack?>(null) }
    var currentAudioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var tracksLoaded by remember { mutableStateOf(false) }

    // ── Player-Einstellungen (einmalig beim Start geladen) ──────
    var playerSettings by remember { mutableStateOf(PlayerSettings()) }
    LaunchedEffect(Unit) {
        playerSettings = viewModel.getPlayerSettings()
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

    // ── D‑Pad / TV Focus Management ─────────────────────────────
    val backFocus = remember { FocusRequester() }
    val sliderFocus = remember { FocusRequester() }
    val rewindFocus = remember { FocusRequester() }
    val playPauseFocus = remember { FocusRequester() }
    val forwardFocus = remember { FocusRequester() }
    val speedFocus = remember { FocusRequester() }
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
        val channel = if (next) PlayerContext.next() else PlayerContext.previous()
        channel?.let { ch ->
            currentUrl = ch.url
            currentContentId = ch.id
            currentTitle = ch.name
            currentImageUrl = ch.logo ?: ""
            currentResumePos = 0L
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

    // ── Echter Vollbildmodus: System-Bars ausblenden ────────────
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
        if (surfaceView != null && isSurfaceReady) {
            errorMessage = null
            playerState = PlayerState.IDLE
            playerManager?.release()   // Alten Player freigeben
            playerManager = null       // Referenz löschen
            playerManager = PlayerManager(context).also { pm ->
                pm.initialize(currentUrl, surfaceView!!)
                // SubtitleView binden, sobald sie verfügbar ist
                subtitleView?.let { pm.setSubtitleView(it) }
                pm.setOnStateChangeListener { state ->
                    playerState = state
                    isPlaying = state == PlayerState.PLAYING
                    if (state == PlayerState.ERROR) {
                        errorMessage = pm.getLastError() ?: "Unbekannter Fehler"
                        showControls = true
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
                            imageUrl = currentImageUrl
                        )
                    }
                } catch (_: Exception) {
                    // Player wurde freigegeben → beenden
                    break
                }
            }
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
                    imageUrl = currentImageUrl
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
                    imageUrl = currentImageUrl
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
            .background(Color.Black)
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
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }
        }

        // Buffering / Loading
        if (playerState == PlayerState.BUFFERING || playerState == PlayerState.IDLE) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = WarmGold, modifier = Modifier.size(48.dp))
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Lädt Stream...",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }

        // ── Error State ───────────────────────────────────────────
        if (playerState == PlayerState.ERROR) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = WarmError
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Fehler bei der Wiedergabe",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if (errorMessage != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(
                            errorMessage!!,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp,
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
                        Icon(Icons.Filled.Refresh, null, Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Neu laden", fontSize = 15.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    TextButton(onClick = {
                        cleanupAndGoBack()
                    },
                    modifier = Modifier
                        .focusable()
                        .focusRequester(backTextFocus)
                    ) {
                        Text("Zurück", color = WarmTextSecondary, fontSize = 14.sp)
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
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .focusable()
                        .focusRequester(backFocus)
                        .focusProperties { down = playPauseFocus }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Zurück",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Titel (oben Mitte)
                Text(
                    currentTitle.ifEmpty {
                        when (contentTyp) {
                            ContentType.MOVIE -> "Film"
                            ContentType.SERIES -> "Serie"
                            ContentType.LIVE -> "Live"
                        }
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(20.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                )

                // Speed Indicator (oben rechts)
                if (playbackSpeed != 1.0f) {
                    Text(
                        "${playbackSpeed}x",
                        color = WarmGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // ── Controls Overlay (unten) ─────────────────────────────
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Timeline
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        formatTime(currentPosition),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.width(52.dp)
                    )

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
                            .weight(1f)
                            .height(24.dp)
                            .focusable()
                            .focusRequester(sliderFocus)
                            .focusProperties { down = playPauseFocus },
                        colors = SliderDefaults.colors(
                            thumbColor = WarmGold,
                            activeTrackColor = WarmGold,
                            inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                        )
                    )

                    Text(
                        formatTime(duration),
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.width(52.dp)
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Control buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rewind
                    SeekButton(
                        icon = Icons.Filled.Replay,
                        label = formatSeekLabel(playerSettings.seekBackwardMs),
                        contentDescription = "Zurückspulen",
                        onClick = {
                            val pos = (currentPosition - playerSettings.seekBackwardMs).coerceAtLeast(0)
                            playerManager?.seekTo(pos)
                            currentPosition = pos
                            resetHideTimer()
                        },
                        modifier = Modifier
                            .focusable()
                            .focusRequester(rewindFocus)
                            .focusProperties {
                                right = playPauseFocus
                                up = sliderFocus
                            }
                    )

                    // Play/Pause
                    IconButton(
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
                            .size(56.dp)
                            .background(WarmGold, CircleShape)
                            .focusable()
                            .focusRequester(playPauseFocus)
                            .focusProperties {
                                left = rewindFocus
                                right = forwardFocus
                                up = sliderFocus
                            }
                    ) {
                        Icon(
                            if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Forward
                    SeekButton(
                        icon = Icons.Filled.Forward,
                        label = formatSeekLabel(playerSettings.seekForwardMs),
                        contentDescription = "Vorspulen",
                        onClick = {
                            val pos = (currentPosition + playerSettings.seekForwardMs).coerceAtMost(duration)
                            playerManager?.seekTo(pos)
                            currentPosition = pos
                            resetHideTimer()
                        },
                        modifier = Modifier
                            .focusable()
                            .focusRequester(forwardFocus)
                            .focusProperties {
                                left = playPauseFocus
                                right = speedFocus
                                up = sliderFocus
                            }
                    )

                    // Speed
                    Box {
                        IconButton(onClick = {
                            showSpeedMenu = !showSpeedMenu
                            resetHideTimer()
                        },
                        modifier = Modifier
                            .focusable()
                            .focusRequester(speedFocus)
                            .focusProperties {
                                left = forwardFocus
                                right = stopFocus
                                up = sliderFocus
                            }
                        ) {
                            Icon(
                                Icons.Filled.Speed,
                                contentDescription = "Geschwindigkeit",
                                tint = if (playbackSpeed != 1.0f) WarmGold else Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        // Speed dropdown menu
                        DropdownMenu(
                            expanded = showSpeedMenu,
                            onDismissRequest = { showSpeedMenu = false }
                        ) {
                            SPEED_OPTIONS.forEach { speed ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                "${speed}x",
                                                color = if (speed == playbackSpeed) WarmGold else WarmTextPrimary,
                                                fontSize = 14.sp
                                            )
                                            if (speed == 1.0f) {
                                                Spacer(Modifier.width(6.dp))
                                                Text(
                                                    "(normal)",
                                                    color = WarmTextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        playbackSpeed = speed
                                        playerManager?.setPlaybackSpeed(speed)
                                        showSpeedMenu = false
                                        resetHideTimer()
                                    },
                                    leadingIcon = if (speed == playbackSpeed) {
                                        {
                                            Icon(
                                                Icons.Filled.Check,
                                                null,
                                                tint = WarmGold,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    } else null
                                )
                            }
                        }
                    }

                    // Stop
                    IconButton(onClick = {
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
                    modifier = Modifier
                        .focusable()
                        .focusRequester(stopFocus)
                        .focusProperties {
                            left = speedFocus
                            // Nur auf subtitleFocus zeigen wenn der Button tatsächlich sichtbar ist,
                            // sonst bleibt right unverändert (FocusRequester nicht gebunden → Crash)
                            if (showSubtitleButton) right = subtitleFocus
                            up = sliderFocus
                        }
                    ) {
                        Icon(
                            Icons.Filled.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Subtitle Selection
                    if (showSubtitleButton) {
                        Box {
                            IconButton(onClick = {
                                showSubtitleMenu = !showSubtitleMenu
                                resetHideTimer()
                            },
                            modifier = Modifier
                                .focusable()
                                .focusRequester(subtitleFocus)
                                .focusProperties {
                                    left = stopFocus
                                    if (showAudioButton) right = audioFocus
                                    up = sliderFocus
                                }
                            ) {
                                Icon(
                                    Icons.Filled.ClosedCaption,
                                    contentDescription = "Untertitel",
                                    tint = if (currentSubtitleTrack != null) WarmGold else Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            // Subtitle dropdown menu
                            DropdownMenu(
                                expanded = showSubtitleMenu,
                                onDismissRequest = { showSubtitleMenu = false }
                            ) {
                                subtitleTracks.forEach { track ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                track.label,
                                                color = if (track.id == currentSubtitleTrack?.id) WarmGold else WarmTextPrimary,
                                                fontSize = 14.sp
                                            )
                                        },
                                        onClick = {
                                            playerManager?.selectSubtitleTrack(
                                                if (track.id == "none") null else track.id
                                            )
                                            currentSubtitleTrack = if (track.id == "none") null else track
                                            showSubtitleMenu = false
                                            resetHideTimer()
                                        },
                                        leadingIcon = if (track.id == currentSubtitleTrack?.id) {
                                            {
                                                Icon(
                                                    Icons.Filled.Check,
                                                    null,
                                                    tint = WarmGold,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        } else null
                                    )
                                }
                            }
                        }
                    }
                    
                    // Audio Track Selection
                    if (showAudioButton) {
                        Box {
                            IconButton(onClick = {
                                showAudioMenu = !showAudioMenu
                                resetHideTimer()
                            },
                            modifier = Modifier
                                .focusable()
                                .focusRequester(audioFocus)
                                .focusProperties {
                                    left = if (showSubtitleButton) subtitleFocus else stopFocus
                                    up = sliderFocus
                                }
                            ) {
                                Icon(
                                    Icons.Filled.VolumeUp,
                                    contentDescription = "Audio",
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            // Audio dropdown menu
                            DropdownMenu(
                                expanded = showAudioMenu,
                                onDismissRequest = { showAudioMenu = false }
                            ) {
                                audioTracks.forEach { track ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                track.label,
                                                color = if (track.id == currentAudioTrack?.id) WarmGold else WarmTextPrimary,
                                                fontSize = 14.sp
                                            )
                                        },
                                        onClick = {
                                            playerManager?.selectAudioTrack(track.id)
                                            currentAudioTrack = track
                                            showAudioMenu = false
                                            resetHideTimer()
                                        },
                                        leadingIcon = if (track.id == currentAudioTrack?.id) {
                                            {
                                                Icon(
                                                    Icons.Filled.Check,
                                                    null,
                                                    tint = WarmGold,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        } else null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

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