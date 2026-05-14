package com.nativeplayer.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.ui.favorites.FavoritesScreen
import com.nativeplayer.ui.live.LiveScreen
import com.nativeplayer.ui.movies.MoviesScreen
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.series.SeriesScreen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val lastSelectedTab by homeViewModel.lastSelectedTab.collectAsState()
    var selectedTab by remember { mutableIntStateOf(lastSelectedTab) }
    var showExitDialog by remember { mutableStateOf(false) }

    // Speichere Tab-Wechsel
    LaunchedEffect(selectedTab) {
        homeViewModel.saveSelectedTab(selectedTab)
    }

    // Back-Handler: Zeige Exit-Dialog statt App sofort zu schließen
    BackHandler {
        showExitDialog = true
    }

    Scaffold(containerColor = WarmBackground) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // App Bar — eleganter, mit Gradient-Unterzeile
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmSurface)
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "OSI IPTV",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = WarmGold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { navController.navigate(Screen.Search.route) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.Search, "Suche", tint = WarmTextPrimary, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = { navController.navigate(Screen.Settings.route) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Filled.Settings, "Einstellungen", tint = WarmTextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                    // Goldene Gradient-Linie als Abschluss
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

            // Tab Row — Pill-Chips Stil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = WarmSurface,
                    contentColor = WarmGold,
                    edgePadding = 0.dp,
                    indicator = {},  // Kein Standard-Indicator
                    divider = {}
                ) {
                    TabItem("Live", Icons.Filled.LiveTv, 0, selectedTab) { selectedTab = it }
                    TabItem("Filme", Icons.Filled.Movie, 1, selectedTab) { selectedTab = it }
                    TabItem("Serien", Icons.Filled.Theaters, 2, selectedTab) { selectedTab = it }
                    TabItem("Favoriten", Icons.Filled.Favorite, 3, selectedTab) { selectedTab = it }
                    TabItem("Weiterschauen", Icons.Filled.History, 4, selectedTab) { selectedTab = it }
                }
            }

            // Content based on selected tab
            when (selectedTab) {
                0 -> LiveTabContent(navController)
                1 -> MoviesTabContent(navController)
                2 -> SeriesTabContent(navController)
                3 -> FavoritesTabContent(navController)
                4 -> ResumeTabContent(navController, homeViewModel)
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
private fun TabItem(
    title: String,
    icon: ImageVector,
    index: Int,
    selectedIndex: Int,
    onClick: (Int) -> Unit
) {
    val isSelected = selectedIndex == index
    Tab(
        selected = isSelected,
        onClick = { onClick(index) },
        selectedContentColor = WarmGold,
        unselectedContentColor = WarmTextSecondary
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (isSelected)
                        Brush.horizontalGradient(listOf(WarmGold, WarmGold.copy(alpha = 0.8f)))
                    else
                        Brush.horizontalGradient(listOf(WarmSurfaceElevated, WarmSurfaceElevated))
                )
                .padding(horizontal = 12.dp, vertical = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(15.dp),
                    tint = if (isSelected) WarmBackground else WarmTextSecondary
                )
                Text(
                    title,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) WarmBackground else WarmTextSecondary
                )
            }
        }
    }
}

@Composable
private fun LiveTabContent(navController: NavController) {
    LiveScreen(navController = navController)
}

@Composable
private fun MoviesTabContent(navController: NavController) {
    MoviesScreen(navController = navController)
}

@Composable
private fun SeriesTabContent(navController: NavController) {
    SeriesScreen(navController = navController)
}

@Composable
private fun FavoritesTabContent(navController: NavController) {
    FavoritesScreen(navController = navController)
}

@Composable
private fun ResumeTabContent(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val recentWatch by homeViewModel.recentWatch.collectAsState()
    val lastFocus = remember { FocusRestoreManager.getLastFocus("resume_tab") }

    if (recentWatch.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Keine Einträge vorhanden", color = WarmTextSecondary, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        FocusRestoreManager.saveFocus("resume_tab", "resume_${entry.id}")
                    }
                )
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
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster/Bild
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)).background(WarmSurfaceElevated),
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
                        null, Modifier.size(16.dp), tint = WarmTextSecondary
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            // Text Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.title,
                    color = WarmTextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (entry.type == "series" && entry.episodeTitle.isNotEmpty()) {
                    Text(
                        "S${entry.seasonNumber}: ${entry.episodeTitle}",
                        color = WarmTextSecondary,
                        fontSize = 6.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(3.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = WarmGold,
                    trackColor = WarmSurfaceElevated
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    if (remainingMinutes > 0) "Noch ca. $remainingMinutes Min." else "Fast fertig",
                    color = WarmGold,
                    fontSize = 5.sp
                )
            }
            Spacer(Modifier.width(4.dp))
            Icon(Icons.Filled.PlayArrow, null, Modifier.size(14.dp), tint = WarmGold)
        }
    }
}

@Composable
private fun ResumeCard(
    entry: WatchHistoryEntity,
    navController: NavController,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .width(35.dp)
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
        shape = RoundedCornerShape(2.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface)
    ) {
        Column {
            Box(Modifier.fillMaxWidth().height(22.dp).background(WarmSurfaceElevated), contentAlignment = Alignment.Center) {
                Icon(
                    if (entry.type == "movie") Icons.Filled.Movie else Icons.Filled.Theaters,
                    null, Modifier.size(9.dp), tint = WarmTextSecondary
                )
            }
            Column(Modifier.padding(2.dp)) {
                Text(entry.title, color = WarmTextPrimary, fontSize = 4.sp, fontWeight = FontWeight.Medium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                if (entry.type == "series" && entry.episodeTitle.isNotEmpty()) {
                    Text("S${entry.seasonNumber}", color = WarmGold, fontSize = 3.sp)
                }
                Spacer(Modifier.height(1.dp))
                val progress = if (entry.duration > 0) (entry.position.toFloat() / entry.duration).coerceIn(0f, 1f) else 0f
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = WarmGold,
                    trackColor = WarmSurfaceElevated
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.PlayArrow, null, Modifier.size(3.dp), tint = WarmGold)
                    Text("Weiter", color = WarmGold, fontSize = 3.sp)
                }
            }
        }
    }
}
