package com.nativeplayer.ui.favorites

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
import androidx.compose.ui.graphics.Color
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
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay

@Composable
fun FavoritesScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val liveFavs by viewModel.liveFavorites.collectAsState()
    val movieFavs by viewModel.movieFavorites.collectAsState()
    val seriesFavs by viewModel.seriesFavorites.collectAsState()
    val allEmpty = liveFavs.isEmpty() && movieFavs.isEmpty() && seriesFavs.isEmpty()

    // ── Focus Restore: Gespeicherten Fokus auslesen ──
    val lastFocus = remember { FocusRestoreManager.getLastFocus("favorites_screen") }

    if (allEmpty) {
        Box(Modifier.fillMaxSize().background(WarmBackground), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Filled.FavoriteBorder, null, Modifier.size(32.dp), tint = WarmTextSecondary)
                Spacer(Modifier.height(8.dp))
                Text("Keine Favoriten", color = WarmTextSecondary, fontSize = 8.sp)
                Text("Tippe auf das Herz-Icon", color = WarmTextSecondary, fontSize = 6.sp)
            }
        }
    } else {
        LazyColumn(Modifier.fillMaxSize().background(WarmBackground).padding(horizontal = 12.dp)) {
            if (liveFavs.isNotEmpty()) {
                item { SectionHeader("Live TV", Icons.Filled.LiveTv) }
                items(liveFavs, key = { it.itemId }) { fav ->
                    val isRestoredItem = lastFocus?.itemKey == fav.itemId
                    val focusRequester = remember(fav.itemId) { FocusRequester() }

                    LaunchedEffect(isRestoredItem) {
                        if (isRestoredItem) {
                            delay(300)
                            try { focusRequester.requestFocus() } catch (_: Exception) {}
                        }
                    }

                    FavItem(
                        fav = fav,
                        accentColor = WarmGold,
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            FocusRestoreManager.saveFocus("favorites_screen", fav.itemId)
                            navController.navigate(Screen.FullScreenPlayer.createRoute(
                                url = fav.streamUrl.ifEmpty { fav.itemId },
                                type = "live",
                                contentId = fav.itemId,
                                title = fav.title,
                                imageUrl = fav.imageUrl
                            ))
                        },
                        onRemove = { viewModel.removeFavorite(fav.itemId) }
                    )
                }
            }
            if (movieFavs.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Filme", Icons.Filled.Movie) }
                items(movieFavs, key = { it.itemId }) { fav ->
                    val isRestoredItem = lastFocus?.itemKey == fav.itemId
                    val focusRequester = remember(fav.itemId) { FocusRequester() }

                    LaunchedEffect(isRestoredItem) {
                        if (isRestoredItem) {
                            delay(300)
                            try { focusRequester.requestFocus() } catch (_: Exception) {}
                        }
                    }

                    FavItem(
                        fav = fav,
                        accentColor = WarmGold,
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            FocusRestoreManager.saveFocus("favorites_screen", fav.itemId)
                            navController.navigate(Screen.MovieDetail.createRoute(fav.itemId))
                        },
                        onRemove = { viewModel.removeFavorite(fav.itemId) }
                    )
                }
            }
            if (seriesFavs.isNotEmpty()) {
                item { Spacer(Modifier.height(12.dp)); SectionHeader("Serien", Icons.Filled.Theaters) }
                items(seriesFavs, key = { it.itemId }) { fav ->
                    val isRestoredItem = lastFocus?.itemKey == fav.itemId
                    val focusRequester = remember(fav.itemId) { FocusRequester() }

                    LaunchedEffect(isRestoredItem) {
                        if (isRestoredItem) {
                            delay(300)
                            try { focusRequester.requestFocus() } catch (_: Exception) {}
                        }
                    }

                    FavItem(
                        fav = fav,
                        accentColor = WarmGold,
                        modifier = Modifier.focusRequester(focusRequester),
                        onClick = {
                            FocusRestoreManager.saveFocus("favorites_screen", fav.itemId)
                            navController.navigate(Screen.SeriesDetail.createRoute(fav.itemId))
                        },
                        onRemove = { viewModel.removeFavorite(fav.itemId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, null, Modifier.size(10.dp), tint = WarmGold)
        Spacer(Modifier.width(4.dp))
        Text(title, color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 8.sp)
    }
}

@Composable
private fun FavItem(
    fav: FavoriteEntity,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(WarmSurface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Poster/Bild
        Box(Modifier.size(36.dp).clip(RoundedCornerShape(4.dp)).background(WarmSurfaceElevated), contentAlignment = Alignment.Center) {
            if (fav.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(fav.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = fav.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(Icons.Filled.Tv, null, Modifier.size(16.dp), tint = accentColor)
            }
        }
        Spacer(Modifier.width(8.dp))
        Column(Modifier.weight(1f)) {
            Text(fav.title, color = WarmTextPrimary, fontSize = 10.sp, fontWeight = FontWeight.Medium,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (fav.subtitle.isNotEmpty()) Text(fav.subtitle, color = WarmTextSecondary, fontSize = 8.sp)
        }
        IconButton(onClick = onRemove) {
            Icon(Icons.Filled.Delete, "Entfernen", tint = WarmTextSecondary, modifier = Modifier.size(12.dp))
        }
    }
}
