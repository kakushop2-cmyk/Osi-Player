package com.nativeplayer.ui.search

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val results by viewModel.results.collectAsState()
    val searchFocusRequester = remember { FocusRequester() }
    val hasQuery = query.length >= 2
    val hasResults = results.channels.isNotEmpty() || results.movies.isNotEmpty() || results.series.isNotEmpty()

    // ── Focus Restore: Gespeicherten Fokus auslesen ──
    val lastFocus = remember { FocusRestoreManager.getLastFocus("search_screen") }
    val hadPreviousFocus = lastFocus != null

    LaunchedEffect(Unit) {
        // Nur Fokus auf Suchfeld setzen, wenn kein vorheriger Fokus wiederhergestellt werden soll
        if (!hadPreviousFocus) {
            searchFocusRequester.requestFocus()
        }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    OutlinedTextField(
                        value = query,
                        onValueChange = { viewModel.onQueryChanged(it) },
                        placeholder = { Text("Suche nach Sendern, Filmen, Serien...", color = WarmTextSecondary, fontSize = 14.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = WarmTextPrimary,
                            unfocusedTextColor = WarmTextPrimary,
                            focusedBorderColor = WarmGold,
                            unfocusedBorderColor = WarmSurfaceElevated,
                            cursorColor = WarmGold
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().focusRequester(searchFocusRequester)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding).background(WarmBackground)) {
            if (!hasQuery) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.Search, null, Modifier.size(64.dp), tint = WarmTextSecondary)
                            Spacer(Modifier.height(12.dp))
                            Text("Gib mindestens 2 Zeichen ein", color = WarmTextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            } else if (!hasResults) {
                item {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.SearchOff, null, Modifier.size(48.dp), tint = WarmTextSecondary)
                            Spacer(Modifier.height(8.dp))
                            Text("Keine Ergebnisse", color = WarmTextSecondary, fontSize = 14.sp)
                        }
                    }
                }
            } else {
                if (results.channels.isNotEmpty()) {
                    item { SectionHeader("Live TV (${results.channels.size})", Icons.Filled.LiveTv) }
                    items(results.channels, key = { it.id }) { ch ->
                        val isRestoredItem = lastFocus?.itemKey == "ch_${ch.id}"
                        val focusRequester = remember(ch.id) { FocusRequester() }

                        LaunchedEffect(isRestoredItem) {
                            if (isRestoredItem) {
                                delay(300)
                                try { focusRequester.requestFocus() } catch (_: Exception) {}
                            }
                        }

                        SearchResultItem(
                            icon = Icons.Filled.LiveTv,
                            primary = ch.name,
                            secondary = ch.group,
                            modifier = Modifier.focusRequester(focusRequester),
                            onClick = {
                                FocusRestoreManager.saveFocus("search_screen", "ch_${ch.id}")
                                navController.navigate(Screen.FullScreenPlayer.createRoute(
                                    url = ch.url,
                                    type = "live",
                                    contentId = ch.id,
                                    title = ch.name,
                                    imageUrl = ch.logo ?: ""
                                ))
                            }
                        )
                    }
                }
                if (results.movies.isNotEmpty()) {
                    item { SectionHeader("Filme (${results.movies.size})", Icons.Filled.Movie) }
                    items(results.movies, key = { it.id }) { movie ->
                        val isRestoredItem = lastFocus?.itemKey == "movie_${movie.id}"
                        val focusRequester = remember(movie.id) { FocusRequester() }

                        LaunchedEffect(isRestoredItem) {
                            if (isRestoredItem) {
                                delay(300)
                                try { focusRequester.requestFocus() } catch (_: Exception) {}
                            }
                        }

                        SearchResultItem(
                            icon = Icons.Filled.Movie,
                            primary = movie.title,
                            secondary = "${movie.year} \u2022 ${movie.category}",
                            modifier = Modifier.focusRequester(focusRequester),
                            onClick = {
                                FocusRestoreManager.saveFocus("search_screen", "movie_${movie.id}")
                                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
                            }
                        )
                    }
                }
                if (results.series.isNotEmpty()) {
                    item { SectionHeader("Serien (${results.series.size})", Icons.Filled.Theaters) }
                    items(results.series, key = { it.id }) { s ->
                        val isRestoredItem = lastFocus?.itemKey == "series_${s.id}"
                        val focusRequester = remember(s.id) { FocusRequester() }

                        LaunchedEffect(isRestoredItem) {
                            if (isRestoredItem) {
                                delay(300)
                                try { focusRequester.requestFocus() } catch (_: Exception) {}
                            }
                        }

                        SearchResultItem(
                            icon = Icons.Filled.Theaters,
                            primary = s.title,
                            secondary = "${s.year} \u2022 ${s.category}",
                            modifier = Modifier.focusRequester(focusRequester),
                            onClick = {
                                FocusRestoreManager.saveFocus("search_screen", "series_${s.id}")
                                navController.navigate(Screen.SeriesDetail.createRoute(s.id))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(16.dp), tint = WarmGold)
        Spacer(Modifier.width(6.dp))
        Text(title, color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun SearchResultItem(
    icon: ImageVector,
    primary: String,
    secondary: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(WarmSurface)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(20.dp), tint = WarmGold)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(primary, color = WarmTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium,
                maxLines = 1, overflow = TextOverflow.Ellipsis)
            if (secondary.isNotEmpty()) Text(secondary, color = WarmTextSecondary, fontSize = 12.sp)
        }
        Icon(Icons.Filled.ChevronRight, null, Modifier.size(20.dp), tint = WarmTextSecondary)
    }
}
