package com.nativeplayer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
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
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recentWatch by viewModel.recentWatch.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }

    // Back-Handler: Zeige Exit-Dialog statt App sofort zu schließen
    BackHandler {
        showExitDialog = true
    }

    Scaffold(containerColor = WarmBackground) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // App Bar mit Suche und Einstellungen
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
                        Text(
                            "OSI IPTV",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = WarmGold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                        // Suche Icon
                        IconButton(
                            onClick = { navController.navigate(Screen.Search.route) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                "Suche",
                                tint = WarmTextPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        // Einstellungen Icon
                        IconButton(
                            onClick = { navController.navigate(Screen.Settings.route) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                "Einstellungen",
                                tint = WarmTextSecondary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Haupt-Kacheln (Grid mit 2 Spalten)
                item {
                    Text(
                        "Hauptmenü",
                        color = WarmGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeTile(
                            title = "Live TV",
                            icon = Icons.Filled.LiveTv,
                            color = WarmGold,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Screen.Live.route) }
                        )
                        HomeTile(
                            title = "Filme",
                            icon = Icons.Filled.Movie,
                            color = WarmGold.copy(alpha = 0.9f),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Screen.Movies.route) }
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeTile(
                            title = "Serien",
                            icon = Icons.Filled.Theaters,
                            color = WarmGold.copy(alpha = 0.8f),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Screen.Series.route) }
                        )
                        HomeTile(
                            title = "Favoriten",
                            icon = Icons.Filled.Favorite,
                            color = WarmError,
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(Screen.Favorites.route) }
                        )
                    }
                }

                item {
                    HomeTile(
                        title = "Weiterschauen",
                        icon = Icons.Filled.History,
                        color = WarmGold.copy(alpha = 0.7f),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(Screen.Resume.route) }
                    )
                }

                // Weiterschauen Preview (wenn vorhanden)
                if (recentWatch.isNotEmpty()) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Zuletzt angesehen",
                                color = WarmGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { navController.navigate(Screen.Resume.route) }
                            ) {
                                Text(
                                    "Alle anzeigen",
                                    color = WarmTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    items(recentWatch.take(3), key = { it.id }) { entry ->
                        ResumePreviewItem(
                            entry = entry,
                            navController = navController
                        )
                    }
                }
            }
        }
    }

    // Exit-Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("App beenden?", color = WarmTextPrimary) },
            text = { Text("Möchtest du die App wirklich beenden?", color = WarmTextPrimary) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        (navController.context as? android.app.Activity)?.finish()
                    }
                ) {
                    Text("Ja", color = WarmGold, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Nein", color = WarmTextSecondary)
                }
            },
            containerColor = WarmSurface
        )
    }
}

@Composable
private fun HomeTile(
    title: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .height(100.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .focusable()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) WarmGoldDim else WarmSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 8.dp else 4.dp
        ),
        border = if (isFocused) {
            BorderStroke(2.dp, WarmGold)
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isFocused)
                                color.copy(alpha = 0.3f)
                            else
                                color.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = title,
                        modifier = Modifier.size(28.dp),
                        tint = if (isFocused) WarmGold else color
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    title,
                    color = if (isFocused) WarmGold else WarmTextPrimary,
                    fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ResumePreviewItem(
    entry: WatchHistoryEntity,
    navController: NavController
) {
    val progress = if (entry.duration > 0) (entry.position.toFloat() / entry.duration).coerceIn(0f, 1f) else 0f
    val remainingMinutes = if (entry.duration > entry.position) ((entry.duration - entry.position) / 60000).toInt() else 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
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
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster/Bild
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(6.dp))
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
                        Modifier.size(24.dp),
                        tint = WarmTextSecondary
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.title,
                    color = WarmTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.type == "series" && entry.episodeTitle.isNotEmpty()) {
                    Text(
                        "S${entry.seasonNumber}: ${entry.episodeTitle}",
                        color = WarmTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = WarmGold,
                    trackColor = WarmSurfaceElevated
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    if (remainingMinutes > 0) "Noch ca. $remainingMinutes Min." else "Fast fertig",
                    color = WarmGold,
                    fontSize = 11.sp
                )
            }
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.Filled.PlayArrow,
                null,
                Modifier.size(24.dp),
                tint = WarmGold
            )
        }
    }
}
