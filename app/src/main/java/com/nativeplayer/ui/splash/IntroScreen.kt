package com.nativeplayer.ui.splash

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.nativeplayer.ui.theme.*
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.nativeplayer.R
import com.nativeplayer.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val phase: Float,   // individuelle Phase für Twinkling
    val speed: Float    // individuelle Twinkling-Geschwindigkeit
)

@OptIn(UnstableApi::class)
@Composable
fun IntroScreen(
    navController: NavController,
    viewModel: IntroViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var videoEnded by remember { mutableStateOf(false) }
    var showSkip by remember { mutableStateOf(false) }
    var showErrorOverlay by remember { mutableStateOf(false) }

    // Sterne einmalig generieren
    val stars = remember {
        List(200) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 2.2f + 0.4f,
                phase = Random.nextFloat() * Math.PI.toFloat() * 2f,
                speed = Random.nextFloat() * 1.5f + 0.5f
            )
        }
    }

    // Tick für Twinkling-Animation (~60fps)
    var tick by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            tick += 0.016f
        }
    }

    // Navigiere zu Login wenn kein Account konfiguriert
    LaunchedEffect(state) {
        when (state) {
            is IntroState.NoAccount -> {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    // Bei erfolgreichem Retry nach Fehler: navigiere zu Home
    LaunchedEffect(state) {
        if (showErrorOverlay && state is IntroState.Success) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Intro.route) { inclusive = true }
            }
        }
    }

    // Navigation nach Video-Ende (abhängig vom Ladestatus)
    LaunchedEffect(videoEnded) {
        if (videoEnded) {
            when (state) {
                is IntroState.Error -> {
                    showErrorOverlay = true
                }
                is IntroState.NoAccount -> {
                    // wird bereits durch state-LaunchedEffect behandelt
                }
                else -> { // Success oder Loading -> zu Home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Intro.route) { inclusive = true }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(2000)
        showSkip = true
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = Uri.parse("android.resource://${context.packageName}/${R.raw.app_intro}")
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) videoEnded = true
                }
            })
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OsiBlack)
    ) {
        // Layer 1: Weltraum-Nebel-Hintergrund (Radial-Gradient)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Zentraler bläulich-violetter Nebel
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x1A3A1A6E), // dunkles Violett, leicht sichtbar
                        Color(0x0D0D0D2A),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.4f),
                    radius = size.width * 0.7f
                ),
                radius = size.width * 0.7f,
                center = Offset(size.width * 0.5f, size.height * 0.4f)
            )
            // Zweiter Nebel rechts oben (bläulich)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x121A1A4A),
                        Color.Transparent
                    ),
                    center = Offset(size.width * 0.8f, size.height * 0.2f),
                    radius = size.width * 0.4f
                ),
                radius = size.width * 0.4f,
                center = Offset(size.width * 0.8f, size.height * 0.2f)
            )
        }

        // Layer 2: Animierte Sterne
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                val alpha = ((sin((tick * star.speed + star.phase).toDouble()) + 1.0) / 2.0)
                    .toFloat() * 0.75f + 0.25f
                drawCircle(
                    color = OsiTextPrimary.copy(alpha = alpha),
                    radius = star.radius,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        // Layer 3: Video zentriert (RESIZE_MODE_FIT = Weltraum ringsum sichtbar)
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    setBackgroundColor(android.graphics.Color.TRANSPARENT)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Layer 4: Überspringen-Button
        AnimatedVisibility(
            visible = showSkip,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(300)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            TextButton(onClick = { videoEnded = true }) {
                Text(
                    "Überspringen ›",
                    color = OsiTextPrimary.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
        }

        // Layer 5: Error-Overlay (bei Verbindungsfehler nach Video-Ende)
        AnimatedVisibility(
            visible = showErrorOverlay,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xCC000000))
                    .clickable(enabled = true) { /* Klicks konsumieren */ },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = WarmSurface
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = WarmError
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Verbindungsfehler",
                            color = WarmTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = (state as? IntroState.Error)?.message
                                ?: "Unbekannter Fehler",
                            color = WarmTextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = WarmGold
                                )
                            ) {
                                Text("Erneut versuchen")
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(Screen.Settings.route) {
                                        popUpTo(Screen.Intro.route) { inclusive = true }
                                    }
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = WarmTextSecondary
                                )
                            ) {
                                Text("Einstellungen")
                            }
                        }
                    }
                }
            }
        }
    }
}
