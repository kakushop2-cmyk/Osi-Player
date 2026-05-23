package com.nativeplayer.ui.home

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Movie
import com.nativeplayer.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.random.Random

private const val CLIP_DURATION_MS = 60_000L // 60 seconds
private const val TAG = "HeroVideoPlayer"

@OptIn(ExperimentalMaterial3Api::class, UnstableApi::class)
@Composable
fun HeroVideoPlayer(
    movie: Movie?,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {},
    // hasFocus wird nicht mehr für die Play-Logik verwendet — stattdessen trackt diese
    // Komponente ihren eigenen Fokus via onFocusChanged, sodass der Trailer NUR startet
    // wenn der User per D-Pad tatsächlich auf dieser Karte ist.
    hasFocus: Boolean = false,
    isScreenActive: Boolean = true,
    isVisible80Percent: Boolean = true,
    badgeText: String = "NEU HINZUGEFÜGT",
    getOrCreatePlayer: (String) -> ExoPlayer,
    prepareAndPlay: (String) -> Unit,
    pausePlayer: (String) -> Unit
) {
    if (movie == null || movie.streamUrl.isEmpty()) {
        // Show poster fallback when no movie or empty URL
        if (movie != null) {
            Log.d(TAG, "No valid streamUrl, showing poster for: ${movie.title}")
            PosterFallback(movie = movie, modifier = modifier, onDetailsClick = onDetailsClick, badgeText = badgeText)
        }
        return
    }

    var isPlaying by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(60) }
    var hasStarted by remember { mutableStateOf(false) }
    var videoReady by remember { mutableStateOf(false) }
    var playerError by remember { mutableStateOf<String?>(null) }

    // ExoPlayer-Referenz — wird bei Fokus gesetzt, bei Fokusverlust null
    var exoPlayer by remember { mutableStateOf<ExoPlayer?>(null) }
    // Listener-Referenz für Cleanup
    var currentListener by remember { mutableStateOf<Player.Listener?>(null) }

    // ── ECHTER UI-FOKUS: Trailer läuft NUR wenn diese Karte per D-Pad fokussiert ist ──
    var isFocused by remember { mutableStateOf(false) }
    val pulseScale = remember { Animatable(1f) }
    var prevFocused by remember { mutableStateOf(false) }
    LaunchedEffect(isFocused) {
        if (isFocused && !prevFocused) {
            pulseScale.snapTo(1f)
            pulseScale.animateTo(1.08f, spring(stiffness = Spring.StiffnessMedium))
            pulseScale.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
        prevFocused = isFocused
    }

    // Cleanup wenn Composable disposed wird (z.B. VerticalPager scrollt weg)
    DisposableEffect(movie.streamUrl) {
        onDispose {
            Log.d(TAG, "onDispose → stopping player for: ${movie.title}")
            currentListener?.let { listener ->
                exoPlayer?.removeListener(listener)
            }
            pausePlayer(movie.streamUrl)
            exoPlayer = null
            currentListener = null
        }
    }

    // Fokus- & Screen-basierte Player-Steuerung (non-blocking — safe for fast D-pad scrolling)
    // Der Trailer startet NUR wenn: Karte hat echten UI-Fokus + Screen ist aktiv + sichtbar
    LaunchedEffect(isFocused, isScreenActive, isVisible80Percent) {
        if (isFocused && isScreenActive && isVisible80Percent && movie.streamUrl.isNotEmpty()) {
            Log.d(TAG, "isFocused=true → creating/preparing player for: ${movie.title}")

            // Reset states
            videoReady = false
            hasStarted = false
            playerError = null
            isPlaying = false
            timeRemaining = 60

            // Player erstellen oder wiederverwenden
            val player = getOrCreatePlayer(movie.streamUrl)
            exoPlayer = player

            // Listener erstellen und speichern
            val listener = object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    Log.d(TAG, "onPlaybackStateChanged: $state")
                    when (state) {
                        Player.STATE_READY -> videoReady = true
                        Player.STATE_ENDED -> { isPlaying = false; hasStarted = false }
                        else -> {}
                    }
                }
                override fun onIsPlayingChanged(playing: Boolean) {
                    Log.d(TAG, "onIsPlayingChanged: $playing")
                    isPlaying = playing
                }
                override fun onPlayerError(error: PlaybackException) {
                    Log.e(TAG, "Player error: ${error.message}")
                    playerError = error.message
                    videoReady = false; isPlaying = false; hasStarted = false
                }
            }
            player.addListener(listener)
            currentListener = listener

            // Prepare und Play
            prepareAndPlay(movie.streamUrl)

            // NON-BLOCKING: Warte bis Video ready (suspend via snapshotFlow, cancellable)
            // Timeout nach 10s oder Fokusverlust — jeweils durch Cancellation sauber abbrechbar
            try {
                withTimeoutOrNull(10_000L) {
                    snapshotFlow { videoReady }.first { it }
                }
            } catch (_: Exception) {
                // Cancelled by focus change or composition exit — that's fine
            }

            if (!videoReady) {
                Log.d(TAG, "Focus lost or ready timeout → cleanup for: ${movie.title}")
                player.removeListener(listener)
                currentListener = null
                pausePlayer(movie.streamUrl)
                exoPlayer = null
                return@LaunchedEffect
            }

            // An zufällige Position seeken
            val duration = player.duration
            if (duration > 0 && duration != Long.MAX_VALUE) {
                val minSeek = (duration * 0.05).toLong()
                val maxSeek = (duration - CLIP_DURATION_MS * 2).coerceAtLeast(minSeek)
                val randomPos = if (maxSeek > minSeek) Random.nextLong(minSeek, maxSeek) else minSeek
                Log.d(TAG, "Seeking to ${randomPos}ms (duration: ${duration}ms)")
                player.seekTo(randomPos)
            }

            hasStarted = true
            Log.d(TAG, "Trailer started for: ${movie.title}")

            // NON-BLOCKING: Warte bis echter UI-Fokus, ScreenActive oder Visibility verloren geht
            snapshotFlow { isFocused && isScreenActive && isVisible80Percent }.first { !it }

            Log.d(TAG, "Focus/Visibility lost → stopping: ${movie.title}")
            player.removeListener(listener)
            currentListener = null
            pausePlayer(movie.streamUrl)
            isPlaying = false
            hasStarted = false
            videoReady = false
            exoPlayer = null

        } else if (!isFocused || !isScreenActive || !isVisible80Percent) {
            // Kein UI-Fokus oder Screen nicht aktiv = sicherstellen dass Player gestoppt ist
            Log.d(TAG, "isFocused=$isFocused isScreenActive=$isScreenActive → ensuring player stopped for: ${movie.title}")
            currentListener?.let { listener ->
                exoPlayer?.removeListener(listener)
                currentListener = null
            }
            pausePlayer(movie.streamUrl)
            exoPlayer = null
            isPlaying = false
            hasStarted = false
            videoReady = false
        }
    }

    // Countdown timer (non-blocking, cancellable)
    LaunchedEffect(hasStarted, isPlaying) {
        if (hasStarted && isPlaying) {
            timeRemaining = 60
            while (timeRemaining > 0) {
                delay(1000L)  // Suspending, not blocking — cancellable
                timeRemaining--
            }
            if (timeRemaining <= 0) {
                Log.d(TAG, "Clip duration reached, pausing")
                exoPlayer?.playWhenReady = false
                isPlaying = false
            }
        }
    }

    // ── UI ─────────────────────────────────────────────────────
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp
    val cardHeight = 317.dp

    // Äußere Box mit Caller-Modifier (fillMaxSize) — centert die innere Box
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Innere Box mit der SKALIERBAREN Höhe — fillMaxSize kann nicht stören
        Box(
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .height(cardHeight)
                .clip(RoundedCornerShape(16.dp))
                .background(WarmSurfaceElevated, RoundedCornerShape(16.dp))
                .border(
                    width = if (isFocused) 3.dp else 0.dp,
                    color = WarmGold,
                    shape = RoundedCornerShape(16.dp)
                )
                .onFocusChanged { isFocused = it.isFocused }
                .focusable()
                .clickable { onDetailsClick() }
                .graphicsLayer {
                    scaleX = pulseScale.value
                    scaleY = pulseScale.value
                }
        ) {
            // Poster IMMER als Hintergrund (verhindert schwarze Frames)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current.applicationContext)
                .data(movie.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Video Player — nur wenn exoPlayer existiert (Focus-basiert)
        exoPlayer?.let { player ->
            AndroidView(
                factory = { ctx ->
                    android.view.TextureView(ctx).also { textureView ->
                        player.setVideoTextureView(textureView)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // ── Pfeil-Indikatoren — zeigen dass weitere Filme warten ──
        Surface(
            modifier = Modifier
                .padding(start = 4.dp)
                .align(Alignment.CenterStart),
            shape = CircleShape,
            color = OsiBlack.copy(alpha = 0.25f)
        ) {
            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = "Weitere Filme",
                modifier = Modifier.size(44.dp).padding(10.dp),
                tint = OsiTextPrimary.copy(alpha = 0.55f)
            )
        }
        Surface(
            modifier = Modifier
                .padding(end = 4.dp)
                .align(Alignment.CenterEnd),
            shape = CircleShape,
            color = OsiBlack.copy(alpha = 0.25f)
        ) {
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Weitere Filme",
                modifier = Modifier.size(44.dp).padding(10.dp),
                tint = OsiTextPrimary.copy(alpha = 0.55f)
            )
        }

        // Badge (dynamischer Text)
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(4.dp),
            color = WarmGold
        ) {
            Text(
                badgeText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                color = WarmTextOnGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Remaining time indicator
        if (hasStarted && timeRemaining > 0 && isPlaying) {
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(4.dp),
                color = WarmBackground.copy(alpha = 0.7f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = WarmGold
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "${timeRemaining}s",
                        color = WarmGold,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Error indicator
        if (playerError != null) {
            Surface(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd),
                shape = RoundedCornerShape(4.dp),
                color = WarmError.copy(alpha = 0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Warning,
                        contentDescription = null,
                        modifier = Modifier.size(10.dp),
                        tint = OsiTextPrimary
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        "Stream Fehler",
                        color = OsiTextPrimary,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Content info (bottom) — 20% größer, höher, fettgedruckt
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 28.dp)
        ) {
            // ── Main Hero Title ──
            StrokedTitle(
                text = movie.title,
                fontSize = 25.sp,
                maxLines = 1,
            )
            Spacer(Modifier.height(4.dp))

            // Metadata row — 20% größer, alles fett
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (movie.year.isNotEmpty()) {
                    Text(
                        movie.year,
                        color = WarmTextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("·", color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                if (movie.category.isNotEmpty()) {
                    Text(
                        movie.category,
                        color = WarmTextSecondary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("·", color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                if (movie.rating > 0f) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = WarmGold
                    )
                    Text(
                        String.format("%.1f", movie.rating),
                        color = WarmGold,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("·", color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Text(
                    "HD",
                    color = WarmGold.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Plot - fett und größer
            if (movie.plot.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    movie.plot,
                    color = WarmTextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 19.sp
                )
            }

            // Klick-Hinweis
            Spacer(Modifier.height(6.dp))
            Text(
                "OK drücken für Details",
                color = WarmTextSecondary.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    } // ← innere Box (height(cardHeight))
    } // ← äußere Box (modifier vom Caller)
}

/**
 * Poster fallback when no video URL is available
 */
@Composable
private fun PosterFallback(
    movie: Movie,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {},
    badgeText: String = "NEU HINZUGEFÜGT"
) {
    // Äußere Box mit Caller-Modifier — centert die innere Box
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Innere Box mit der skalierbaren Höhe
        Box(
            modifier = Modifier
                .fillMaxWidth(1.0f)
                .height(317.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(WarmSurfaceElevated, RoundedCornerShape(16.dp))
                .clickable { onDetailsClick() }
        ) {
        // Poster image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current.applicationContext)
                .data(movie.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        // Gradient overlay (bottom)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            WarmBackground.copy(alpha = 0.7f),
                            WarmBackground.copy(alpha = 0.95f)
                        ),
                        startY = 0f,
                        endY = Float.MAX_VALUE
                    )
                )
        )

        // Badge (dynamischer Text)
        Surface(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopStart),
            shape = RoundedCornerShape(4.dp),
            color = WarmGold
        ) {
            Text(
                badgeText,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                color = WarmTextOnGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        // Content info (bottom) — 20% größer, höher, fett
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, top = 6.dp, end = 16.dp, bottom = 28.dp)
        ) {
            // ── PosterFallback Title ──
            StrokedTitle(
                text = movie.title,
                fontSize = 25.sp,
                maxLines = 1,
            )
            Spacer(Modifier.height(4.dp))

            // Metadata row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (movie.year.isNotEmpty()) {
                    Text(movie.year, color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("·", color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                if (movie.category.isNotEmpty()) {
                    Text(movie.category, color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("·", color = WarmTextSecondary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                if (movie.rating > 0f) {
                    Icon(
                        Icons.Filled.Star, contentDescription = null,
                        modifier = Modifier.size(17.dp), tint = WarmGold
                    )
                    Text(
                        String.format("%.1f", movie.rating),
                        color = WarmGold, fontSize = 16.sp, fontWeight = FontWeight.Bold
                    )
                }
            }

            // Klick-Hinweis
            Spacer(Modifier.height(6.dp))
            Text(
                "OK drücken für Details",
                color = WarmTextSecondary.copy(alpha = 0.7f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        }
    } // ← innere Box (height)
    } // ← äußere Box (modifier vom Caller)

// ── Stroked Title — Netflix-Rot mit schwarzem Rand ─────────────
@Composable
internal fun StrokedTitle(
    text: String,
    fillColor: Color = NetflixRed,
    strokeColor: Color = OsiBlack,
    strokeOffset: Float = 1.5f,
    fontSize: TextUnit = 18.sp,
    fontWeight: FontWeight = FontWeight.Bold,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = fillColor,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

    Text(
        text = text,
        maxLines = maxLines,
        overflow = overflow,
        style = textStyle.copy(color = Color.Transparent),
        modifier = modifier.drawWithContent {
            val layoutResult = textMeasurer.measure(
                text = AnnotatedString(text),
                style = textStyle,
                constraints = Constraints(
                    maxWidth = size.width.toInt().coerceAtLeast(1),
                    maxHeight = size.height.toInt().coerceAtLeast(1)
                ),
                maxLines = maxLines,
                overflow = overflow
            )
            // 8 offset copies for the black stroke
            val offsets = listOf(
                Offset(-strokeOffset, -strokeOffset), Offset(0f, -strokeOffset), Offset(strokeOffset, -strokeOffset),
                Offset(-strokeOffset, 0f),                                             Offset(strokeOffset, 0f),
                Offset(-strokeOffset, strokeOffset),  Offset(0f, strokeOffset),  Offset(strokeOffset, strokeOffset)
            )
            offsets.forEach { offset ->
                drawText(textLayoutResult = layoutResult, color = strokeColor, topLeft = offset)
            }
            // Fill: Netflix-Rot
            drawText(textLayoutResult = layoutResult, color = fillColor)
        }
    )
}
