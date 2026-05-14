package com.nativeplayer.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movieId: String,
    moviesViewModel: MoviesViewModel = hiltViewModel(),
    detailViewModel: MovieDetailViewModel = hiltViewModel()
) {
    val moviesByCategory by moviesViewModel.moviesByCategory.collectAsState()
    val movieDetails by detailViewModel.movieDetails.collectAsState()
    val baseMovie = remember(moviesByCategory, movieId) {
        moviesByCategory.values.flatten().find { it.id == movieId }
    }
    // Verwende Details aus API wenn verfügbar, sonst Basis-Movie aus der Liste
    val movie = movieDetails ?: baseMovie
    val isFavorite by detailViewModel.isFavorite.collectAsState()
    val watchHistory by detailViewModel.watchHistory.collectAsState()

    LaunchedEffect(Unit) {
        moviesViewModel.loadMovies()
        detailViewModel.load(movieId)
    }

    // Bei Rückkehr vom Player neu laden, damit WatchHistory aktualisiert wird
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                detailViewModel.load(movieId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "Film Details", color = WarmTextPrimary, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                actions = {
                    if (movie != null) {
                        IconButton(onClick = { detailViewModel.toggleFavorite(movie) }) {
                            Icon(
                                if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                "Favorit",
                                tint = if (isFavorite) WarmError else WarmTextPrimary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        if (movie != null) {
            MovieDetailContent(movie, navController, padding, watchHistory)
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = WarmGold)
            }
        }
    }
}

/**
 * Prüft ob ein sinnvoller Resume-Fortschritt existiert.
 * Mindestens 30s gesehen und nicht in den letzten 2 Minuten.
 */
private fun shouldOfferResume(watchHistory: com.nativeplayer.data.model.WatchHistoryEntity?): Boolean {
    if (watchHistory == null) return false
    val pos = watchHistory.position
    val dur = watchHistory.duration
    if (pos <= 30_000) return false
    if (dur > 0 && pos >= dur - 120_000) return false
    return true
}

private fun formatResumeTime(ms: Long): String {
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

@Composable
private fun MovieDetailContent(
    movie: com.nativeplayer.data.model.Movie,
    navController: NavController,
    padding: PaddingValues,
    watchHistory: com.nativeplayer.data.model.WatchHistoryEntity?
) {
    var expanded by remember { mutableStateOf(false) }
    var showResumeDialog by remember { mutableStateOf(false) }

    // ── Focus Restore: Gespeicherten Fokus auslesen ──
    val lastFocus = remember { FocusRestoreManager.getLastFocus("movie_detail_screen") }
    val resumeFocusRequester = remember { FocusRequester() }
    val playFocusRequester = remember { FocusRequester() }

    // Wiederherstellen des Fokus auf "Weiterschauen" oder "Abspielen"
    LaunchedEffect(lastFocus) {
        delay(300)
        when (lastFocus?.itemKey) {
            "resume" -> try { resumeFocusRequester.requestFocus() } catch (_: Exception) {}
            "play" -> try { playFocusRequester.requestFocus() } catch (_: Exception) {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
    ) {
        // Poster
        Box(Modifier.fillMaxWidth().height(250.dp).background(WarmSurfaceElevated), contentAlignment = Alignment.Center) {
            if (!movie.posterUrl.isNullOrEmpty()) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl).crossfade(true).build(),
                    contentDescription = movie.title, modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
            } else {
                Icon(Icons.Filled.Movie, null, Modifier.size(64.dp), tint = WarmTextSecondary)
            }
        }

        Column(Modifier.padding(16.dp)) {
            // Title & Rating
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(movie.title, color = WarmTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Star, null, Modifier.size(20.dp), tint = WarmGold)
                Text(" ${movie.rating}", color = WarmTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(4.dp))
            Text("${movie.year}  •  ${movie.category}", color = WarmTextSecondary, fontSize = 13.sp)

            // Resume banner
            if (watchHistory != null && watchHistory.duration > 0) {
                Spacer(Modifier.height(12.dp))
                val progress = (watchHistory.position.toFloat() / watchHistory.duration).coerceIn(0f, 1f)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(resumeFocusRequester)
                        .clickable {
                            FocusRestoreManager.saveFocus("movie_detail_screen", "resume")
                            navController.navigate(Screen.FullScreenPlayer.createRoute(
                                url = movie.streamUrl,
                                type = "movie",
                                contentId = movie.id,
                                title = movie.title,
                                resumePosition = watchHistory?.position ?: 0L,
                                imageUrl = movie.posterUrl ?: ""
                            ))
                        },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmGold.copy(alpha = 0.15f))
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayCircle, null, Modifier.size(36.dp), tint = WarmGold)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Weiterschauen", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = WarmGold,
                                trackColor = WarmSurfaceElevated
                            )
                        }
                        Icon(Icons.Filled.ChevronRight, null, Modifier.size(24.dp), tint = WarmGold)
                    }
                }
            }

            // Plot
            Spacer(Modifier.height(16.dp))
            Text("Handlung", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(6.dp))
            if (movie.plot.isNotBlank()) {
                Text(movie.plot, color = WarmTextPrimary, fontSize = 14.sp,
                    maxLines = if (expanded) Int.MAX_VALUE else 4, lineHeight = 20.sp)
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Weniger" else "Mehr", color = WarmGold, fontSize = 12.sp)
                }
            } else {
                Text("Keine Handlung verfügbar", color = WarmTextSecondary, fontSize = 14.sp, fontStyle = FontStyle.Italic)
            }

            // Play Button
            Spacer(Modifier.height(24.dp))
                Button(
                onClick = {
                    FocusRestoreManager.saveFocus("movie_detail_screen", "play")
                    if (shouldOfferResume(watchHistory)) {
                        showResumeDialog = true
                    } else {
                        navController.navigate(Screen.FullScreenPlayer.createRoute(
                            url = movie.streamUrl,
                            type = "movie",
                            contentId = movie.id,
                            title = movie.title,
                            imageUrl = movie.posterUrl ?: ""
                        ))
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp).focusRequester(playFocusRequester),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = WarmGold)
            ) {
                Icon(Icons.Filled.PlayArrow, null, Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text("Abspielen", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // Resume-Dialog: Fragt ob vom gespeicherten Standort fortgesetzt werden soll
    if (showResumeDialog && watchHistory != null) {
        AlertDialog(
            onDismissRequest = { showResumeDialog = false },
            title = { Text("Weiterschauen?", color = WarmTextPrimary) },
            text = {
                Text(
                    "Du hast zuletzt bei ${formatResumeTime(watchHistory.position)} aufgehört. Möchtest du weiterschauen oder von Anfang an starten?",
                    color = WarmTextPrimary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResumeDialog = false
                        navController.navigate(Screen.FullScreenPlayer.createRoute(
                            url = movie.streamUrl,
                            type = "movie",
                            contentId = movie.id,
                            title = movie.title,
                            resumePosition = watchHistory.position,
                            imageUrl = movie.posterUrl ?: ""
                        ))
                    }
                ) {
                    Text("Weiterschauen", color = WarmGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showResumeDialog = false
                        navController.navigate(Screen.FullScreenPlayer.createRoute(
                            url = movie.streamUrl,
                            type = "movie",
                            contentId = movie.id,
                            title = movie.title,
                            imageUrl = movie.posterUrl ?: ""
                        ))
                    }
                ) {
                    Text("Von Anfang an", color = WarmTextSecondary)
                }
            },
            containerColor = WarmSurface
        )
    }
}
