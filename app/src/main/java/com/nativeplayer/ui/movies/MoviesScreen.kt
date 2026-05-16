package com.nativeplayer.ui.movies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Movie
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MoviesScreen(
    navController: NavController,
    viewModel: MoviesViewModel = hiltViewModel()
) {
    val moviesByCategory by viewModel.moviesByCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val filteredCategories by viewModel.filteredCategories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isSortMode by viewModel.isSortMode.collectAsState()

    // ── Sort-Zustand: welches Item wurde zuletzt verschoben ────
    var sortActiveIndex by remember { mutableIntStateOf(-1) }
    val categoryListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Sort-Modus OFF → Active-Index zurücksetzen
    LaunchedEffect(isSortMode) { if (!isSortMode) sortActiveIndex = -1 }

    LaunchedEffect(Unit) { viewModel.loadMovies() }

    // ── Focus Restore ──────────────────────────────────────────
    val lastFocus = remember { FocusRestoreManager.getLastFocus("movies_screen") }

    Row(modifier = Modifier.fillMaxSize().background(WarmBackground)) {
        // ── Left 25%: Kategorien ──────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.25f)
                .background(WarmSurface)
                .padding(4.dp)
        ) {
            // Header mit Legende-Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 6.dp, end = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Kategorien",
                    color = WarmGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                // Sort-Button mit Beschriftung
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isSortMode) WarmGoldDim else Color.Transparent)
                        .clickable {
                            sortActiveIndex = -1
                            viewModel.toggleSortMode()
                        }
                        .padding(horizontal = 5.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        if (isSortMode) Icons.Filled.Check else Icons.Filled.UnfoldMore,
                        contentDescription = null,
                        tint = if (isSortMode) WarmGold else WarmTextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        if (isSortMode) "Fertig" else "Kat. verschieben",
                        fontSize = 9.sp,
                        color = if (isSortMode) WarmGold else WarmTextSecondary
                    )
                }
            }

            LazyColumn(state = categoryListState) {
                itemsIndexed(filteredCategories, key = { _, cat -> cat }) { index, category ->
                    val isSelected = selectedCategory == category
                    val isAlle = index == 0
                    val isActive = sortActiveIndex == index && !isAlle

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                when {
                                    isActive            -> WarmError.copy(alpha = 0.15f)
                                    isSelected && !isSortMode -> WarmGoldDim
                                    else                -> Color.Transparent
                                }
                            )
                            .then(
                                if (!isSortMode) Modifier.clickable { viewModel.selectCategory(category) }
                                else Modifier
                            )
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // DragHandle nur für verschiebbare Items (nicht "Alle")
                        if (isSortMode && !isAlle) {
                            Icon(
                                Icons.Filled.DragHandle,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (isActive) WarmError else WarmTextSecondary
                            )
                            Spacer(Modifier.width(4.dp))
                        }

                        Text(
                            category,
                            color = when {
                                isActive                  -> WarmError
                                isSelected && !isSortMode -> WarmGold
                                else                      -> WarmTextPrimary
                            },
                            fontSize = 13.sp,
                            fontWeight = when {
                                isActive || (isSelected && !isSortMode) -> FontWeight.Bold
                                else -> FontWeight.Normal
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (!isSortMode) {
                            Text(
                                "${moviesByCategory[category]?.size ?: 0}",
                                color = WarmTextSecondary,
                                fontSize = 11.sp
                            )
                        } else if (!isAlle) {
                            // ▲▼-Buttons (nicht für "Alle")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        val newIndex = index - 1
                                        viewModel.moveCategoryUp(index)
                                        sortActiveIndex = newIndex
                                        coroutineScope.launch {
                                            try { categoryListState.animateScrollToItem(maxOf(0, newIndex)) }
                                            catch (_: Exception) {}
                                        }
                                    },
                                    enabled = index > 1,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowUp,
                                        contentDescription = "Nach oben",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (index > 1) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        val newIndex = index + 1
                                        viewModel.moveCategoryDown(index)
                                        sortActiveIndex = newIndex
                                        coroutineScope.launch {
                                            try { categoryListState.animateScrollToItem(minOf(categories.size - 1, newIndex)) }
                                            catch (_: Exception) {}
                                        }
                                    },
                                    enabled = index < filteredCategories.size - 1,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.KeyboardArrowDown,
                                        contentDescription = "Nach unten",
                                        modifier = Modifier.size(16.dp),
                                        tint = if (index < categories.size - 1) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Right 75%: Film-Poster ─────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.75f)
                .padding(4.dp)
        ) {
            Text(
                "Filme${selectedCategory?.let { " - $it" } ?: ""}",
                color = WarmGold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp, 8.dp, 8.dp, 4.dp)
            )
            val movies = selectedCategory?.let { moviesByCategory[it] } ?: moviesByCategory.values.flatten()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 70.dp),
                contentPadding = PaddingValues(2.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(movies, key = { it.id }) { movie ->
                    val isRestoredItem = lastFocus?.itemKey == movie.id
                    val focusRequester = remember(movie.id) { FocusRequester() }

                    LaunchedEffect(isRestoredItem) {
                        if (isRestoredItem) {
                            delay(300)
                            try { focusRequester.requestFocus() } catch (_: Exception) {}
                        }
                    }

                    MoviePosterItem(
                        movie = movie,
                        isFavorite = movie.id in favoriteIds,
                        modifier = Modifier.focusRequester(focusRequester),
                        onFavoriteClick = { viewModel.toggleFavorite(movie) },
                        onClick = {
                            FocusRestoreManager.saveFocus("movies_screen", movie.id)
                            navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviePosterItem(
    movie: Movie,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = WarmSurface)
    ) {
        Box {
            Column {
                Box(
                    Modifier.fillMaxWidth().height(90.dp).background(WarmSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    if (!movie.posterUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(movie.posterUrl).crossfade(true).build(),
                            contentDescription = movie.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Filled.Movie, null, Modifier.size(24.dp), tint = WarmTextSecondary)
                    }
                }
                Column(Modifier.padding(4.dp)) {
                    Text(
                        movie.title, color = WarmTextPrimary, fontSize = 6.sp,
                        fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Star, null, Modifier.size(6.dp), tint = WarmGold)
                        Text(" ${movie.rating}", color = WarmTextSecondary, fontSize = 5.sp)
                        Spacer(Modifier.weight(1f))
                        Text(movie.year, color = WarmTextSecondary, fontSize = 5.sp)
                    }
                }
            }
            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.TopEnd).size(16.dp)
            ) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    "Fav",
                    tint = if (isFavorite) WarmError else WarmTextSecondary,
                    modifier = Modifier.size(9.dp)
                )
            }
        }
    }
}
