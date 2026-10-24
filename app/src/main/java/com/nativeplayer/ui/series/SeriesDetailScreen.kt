package com.nativeplayer.ui.series

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.nativeplayer.data.model.Episode
import com.nativeplayer.data.model.Season
import com.nativeplayer.data.model.Series
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesDetailScreen(
    navController: NavController,
    seriesId: String,
    detailViewModel: SeriesDetailViewModel = hiltViewModel()
) {
    val seriesState by detailViewModel.series.collectAsState()
    val series = seriesState
    val isFavorite by detailViewModel.isFavorite.collectAsState()
    val watchHistory by detailViewModel.watchHistory.collectAsState()

    LaunchedEffect(Unit) {
        detailViewModel.load(seriesId)
    }

    // Bei Rückkehr vom Player neu laden, damit WatchHistory aktualisiert wird
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                detailViewModel.load(seriesId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = { Text(series?.title ?: "Serien Details", color = WarmTextPrimary, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                actions = {
                    if (series != null) {
                        IconButton(onClick = { detailViewModel.toggleFavorite(series!!) }) {
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
        if (series != null) {
            SeriesDetailContent(series!!, navController, padding, watchHistory)
        } else {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = WarmGold)
            }
        }
    }
}

@Composable
private fun SeriesDetailContent(
    series: Series,
    navController: NavController,
    padding: PaddingValues,
    watchHistory: com.nativeplayer.data.model.WatchHistoryEntity?
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSeason by remember { mutableIntStateOf(0) }

    // ── Focus Restore: Gespeicherten Fokus auslesen ──
    val lastFocus = remember { FocusRestoreManager.getLastFocus("series_detail_screen") }
    val resumeFocusRequester = remember { FocusRequester() }

    // Pulse animation for "Weiterschauen" card
    var isFocusedResume by remember { mutableStateOf(false) }
    val pulseScaleResume = remember { Animatable(1f) }
    var prevFocusedResume by remember { mutableStateOf(false) }

    // Pulse animation on focus
    LaunchedEffect(isFocusedResume) {
        if (isFocusedResume && !prevFocusedResume) {
            pulseScaleResume.snapTo(1f)
            pulseScaleResume.animateTo(1.08f, spring(stiffness = Spring.StiffnessMedium))
            pulseScaleResume.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
        prevFocusedResume = isFocusedResume
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
    ) {
        // Poster
        Box(Modifier.fillMaxWidth().height(220.dp).background(WarmSurfaceElevated), contentAlignment = Alignment.Center) {
            if (!series.posterUrl.isNullOrEmpty()) {
                AsyncImage(model = ImageRequest.Builder(LocalContext.current)
                    .data(series.posterUrl).crossfade(true).build(),
                    contentDescription = series.title, modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop)
            } else {
                Icon(Icons.Filled.Theaters, null, Modifier.size(64.dp), tint = WarmTextSecondary)
            }
        }

        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(series.title, color = WarmTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Star, null, Modifier.size(20.dp), tint = WarmGold)
                Text(" ${series.rating}", color = WarmTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(4.dp))
            Text("${series.year}  •  ${series.category}", color = WarmTextSecondary, fontSize = 13.sp)
            Text("${series.seasons.size} Staffeln", color = WarmTextSecondary, fontSize = 12.sp)

            // Resume banner
            if (watchHistory != null && watchHistory.duration > 0) {
                Spacer(Modifier.height(12.dp))
                val progress = (watchHistory.position.toFloat() / watchHistory.duration).coerceIn(0f, 1f)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(resumeFocusRequester)
                        .onFocusChanged { isFocusedResume = it.isFocused }
                        .clickable {
                            FocusRestoreManager.saveFocus("series_detail_screen", "resume")
                            navController.navigate(Screen.FullScreenPlayer.createRoute(
                                url = watchHistory.streamUrl,
                                type = "series",
                                contentId = series.id,
                                title = watchHistory.title,
                                imageUrl = series.posterUrl ?: "",
                                season = watchHistory.seasonNumber,
                                episode = watchHistory.episodeNumber,
                                resumePosition = watchHistory.position
                            ))
                        }
                        .graphicsLayer { scaleX = pulseScaleResume.value; scaleY = pulseScaleResume.value },
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmGold.copy(alpha = 0.15f))
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.PlayCircle, null, Modifier.size(36.dp), tint = WarmGold)
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Weiterschauen", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("S${watchHistory.seasonNumber} E${watchHistory.episodeNumber}",
                                color = WarmGold.copy(alpha = 0.7f), fontSize = 12.sp)
                            Spacer(Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxWidth().height(4.dp),
                                color = WarmGold, trackColor = WarmSurfaceElevated
                            )
                        }
                        Icon(Icons.Filled.ChevronRight, null, Modifier.size(24.dp), tint = WarmGold)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text("Handlung", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(6.dp))
            if (series.plot.isNotBlank()) {
                Text(series.plot, color = WarmTextPrimary, fontSize = 14.sp, maxLines = if (expanded) Int.MAX_VALUE else 4, lineHeight = 20.sp)
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Weniger" else "Mehr", color = WarmGold, fontSize = 12.sp)
                }
            } else {
                Text("Keine Handlung verfügbar", color = WarmTextSecondary, fontSize = 14.sp, fontStyle = FontStyle.Italic)
            }

            // Seasons tabs
            if (series.seasons.isNotEmpty()) {
                Spacer(Modifier.height(20.dp))
                Text("Staffeln", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))

                ScrollableTabRow(
                    selectedTabIndex = selectedSeason.coerceIn(0, series.seasons.size - 1),
                    containerColor = WarmSurface, edgePadding = 0.dp, contentColor = WarmGold
                ) {
                    series.seasons.forEachIndexed { index, season ->
                        Tab(
                            selected = selectedSeason == index,
                            onClick = { selectedSeason = index },
                            text = { Text("Staffel ${season.number}", color = if (selectedSeason == index) WarmGold else WarmTextSecondary, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                val currentSeason = series.seasons.getOrNull(selectedSeason)
                currentSeason?.episodes?.forEach { episode ->
                    EpisodeItem(
                        episode = episode,
                        series = series,
                        navController = navController,
                        seasonNumber = currentSeason.number,
                        isRestored = lastFocus?.itemKey == "ep_${episode.number}"
                    )
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EpisodeItem(
    episode: Episode,
    series: Series,
    navController: NavController,
    seasonNumber: Int = 1,
    isRestored: Boolean = false
) {
    val focusRequester = remember(episode.number) { FocusRequester() }

    // Pulse animation on focus
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

    // Wenn dies die wiederherzustellende Episode ist, setze den Fokus
    LaunchedEffect(isRestored) {
        if (isRestored) {
            delay(300)
            try { focusRequester.requestFocus() } catch (_: Exception) {}
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(WarmSurface, RoundedCornerShape(8.dp))
            .clickable(onClick = {
                FocusRestoreManager.saveFocus("series_detail_screen", "ep_${episode.number}")
                navController.navigate(Screen.FullScreenPlayer.createRoute(
                    url = episode.streamUrl,
                    type = "series",
                    contentId = series.id,
                    title = "${series.title} — S${seasonNumber}E${episode.number}",
                    imageUrl = series.posterUrl ?: "",
                    season = seasonNumber,
                    episode = episode.number
                ))
            })
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .padding(12.dp)
            .graphicsLayer { scaleX = pulseScale.value; scaleY = pulseScale.value },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp).background(OsiStarYellow.copy(alpha = 0.2f), RoundedCornerShape(6.dp)),
            contentAlignment = Alignment.Center) {
            Text("${episode.number}", color = OsiStarYellow, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(episode.title, color = WarmTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            if (episode.duration.isNotEmpty()) {
                Text(episode.duration, color = WarmTextSecondary, fontSize = 11.sp)
            }
        }
        Icon(Icons.Filled.PlayArrow, null, Modifier.size(24.dp), tint = OsiStarYellow)
    }
}
