package com.nativeplayer.ui.resume

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(
    navController: NavController,
    viewModel: ResumeViewModel = hiltViewModel()
) {
    val recentWatch by viewModel.recentWatch.collectAsState()
    val lastFocus = remember { FocusRestoreManager.getLastFocus("resume_screen") }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmSurface)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigateUp() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                "Zurück",
                                tint = WarmTextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Weiterschauen",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = WarmGold,
                            letterSpacing = 1.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    // Goldene Gradient-Linie
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(WarmGold.copy(alpha = 0f), WarmGold.copy(alpha = 0.6f), WarmGold.copy(alpha = 0f))
                                )
                            )
                    )
                }
            }
        }
    ) { padding ->
        if (recentWatch.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(WarmBackground),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.History,
                        null,
                        Modifier.size(64.dp),
                        tint = WarmTextSecondary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Keine Einträge vorhanden",
                        color = WarmTextSecondary,
                        fontSize = 16.sp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Schau dir Filme, Serien oder Live TV an",
                        color = WarmTextSecondary.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentWatch, key = { it.id }) { entry ->
                    val isRestoredItem = lastFocus?.itemKey == "resume_${entry.id}"
                    val focusRequester = remember(entry.id) { FocusRequester() }

                    LaunchedEffect(isRestoredItem) {
                        if (isRestoredItem) {
                            delay(300)
                            try { focusRequester.requestFocus() } catch (_: Exception) {}
                        }
                    }

                    ResumeListItem(
                        entry = entry,
                        navController = navController,
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            FocusRestoreManager.saveFocus("resume_screen", "resume_${entry.id}")
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumeListItem(
    entry: WatchHistoryEntity,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val progress = if (entry.duration > 0) (entry.position.toFloat() / entry.duration).coerceIn(0f, 1f) else 0f
    val remainingMinutes = if (entry.duration > entry.position) ((entry.duration - entry.position) / 60000).toInt() else 0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
                when (entry.type) {
                    "movie" -> {
                        if (entry.itemId.isNotEmpty()) {
                            navController.navigate(Screen.MovieDetail.createRoute(entry.itemId))
                        }
                    }
                    "series" -> {
                        if (entry.itemId.isNotEmpty()) {
                            navController.navigate(Screen.SeriesDetail.createRoute(entry.itemId))
                        }
                    }
                    else -> {
                        if (entry.streamUrl.isNotEmpty()) {
                            navController.navigate(Screen.FullScreenPlayer.createRoute(
                                url = entry.streamUrl,
                                type = entry.type,
                                contentId = entry.itemId,
                                title = entry.title,
                                imageUrl = entry.imageUrl
                            ))
                        }
                    }
                }
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster/Bild
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                if (entry.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(entry.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = entry.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        when (entry.type) {
                            "movie" -> Icons.Filled.Movie
                            "series" -> Icons.Filled.Theaters
                            else -> Icons.Filled.LiveTv
                        },
                        null,
                        Modifier.size(32.dp),
                        tint = WarmTextSecondary
                    )
                }
            }
            Spacer(Modifier.width(16.dp))
            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.title,
                    color = WarmTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.type == "series" && entry.episodeTitle.isNotEmpty()) {
                    Text(
                        "Staffel ${entry.seasonNumber}: ${entry.episodeTitle}",
                        color = WarmTextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(4.dp),
                    color = WarmGold,
                    trackColor = WarmSurfaceElevated
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    if (remainingMinutes > 0) "Noch ca. $remainingMinutes Min. verbleibend" else "Fast fertig",
                    color = WarmGold,
                    fontSize = 12.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Icon(
                Icons.Filled.PlayArrow,
                null,
                Modifier.size(32.dp),
                tint = WarmGold
            )
        }
    }
}
