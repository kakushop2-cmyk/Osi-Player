package com.nativeplayer.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.zIndex
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Movie
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.ExperimentalFoundationApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val recentWatch by viewModel.recentWatch.collectAsState()
    val heroMoviesByCategory by viewModel.heroMoviesByCategory.collectAsState()
    val selectedHeroIndex by viewModel.selectedHeroIndex.collectAsState()
    val fuerDichMovies by viewModel.fuerDichMovies.collectAsState()
    val sportChannels by viewModel.sportChannels.collectAsState()
    val topRatedMovies by viewModel.topRatedMovies.collectAsState()
    val selectedTopRatedIndex by viewModel.selectedTopRatedIndex.collectAsState()
    val seriesByCategory by viewModel.seriesByCategory.collectAsState()

    var selectedCategory by remember { mutableStateOf(HomeCategory.ALLE) }
    var showExitDialog by remember { mutableStateOf(false) }

    // ── Screen-Aktiv tracking: Hero-Player nur wenn HomeScreen im Vordergrund ──
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isHomeActive = navBackStackEntry?.destination?.route == Screen.Home.route

    // Beim Verlassen des Screens alle Hero-Player sofort stoppen
    LaunchedEffect(isHomeActive) {
        if (!isHomeActive) {
            viewModel.stopAllHeroPlayers()
        }
    }

    BackHandler {
        showExitDialog = true
    }

    // Header-Höhe messen für Padding des scrollbaren Contents
    var headerHeightPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    // FocusRequester für die Header-Icons (D-Pad Navigation)
    val searchFocusRequester = remember { FocusRequester() }
    val settingsFocusRequester = remember { FocusRequester() }

    Scaffold(containerColor = WarmBackground) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // ═══════════════════════════════════════════════════════════
            // LAYER 1: HINTERGRUND — Scrollbarer Content
            // In Compose-Box gewinnt das zuletzt deklarierte Kind den
            // Touch-Input. Daher muss der Header NACH dem Content stehen,
            // damit seine Icons klickbar sind.
            // ═══════════════════════════════════════════════════════════
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = with(density) { headerHeightPx.toDp() })
                    .verticalScroll(scrollState)
            ) {
                // ── SECTION 0: Category Bar + Hero Carousel ─────────
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp, max = 600.dp)
                        .background(WarmBackground)
                ) {
                    // Category Bar
                    CategoryBar(
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    // Hero Carousel - Full width edge-to-edge Diashow
                    if (heroMoviesByCategory.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            val pagerState = rememberPagerState(
                                initialPage = selectedHeroIndex.coerceIn(0, (heroMoviesByCategory.size - 1).coerceAtLeast(0))
                            ) {
                                heroMoviesByCategory.size
                            }
                            val hScope = rememberCoroutineScope()
                            var isProgrammaticScroll by remember { mutableStateOf(false) }

                            // Sync ViewModel → Pager (only when index changes externally)
                            LaunchedEffect(selectedHeroIndex) {
                                if (selectedHeroIndex != pagerState.currentPage) {
                                    isProgrammaticScroll = true
                                    try {
                                        pagerState.animateScrollToPage(selectedHeroIndex)
                                    } finally {
                                        isProgrammaticScroll = false
                                    }
                                }
                            }
                            // Sync Pager → ViewModel (only on user swipe)
                            LaunchedEffect(pagerState.currentPage) {
                                if (!isProgrammaticScroll) {
                                    viewModel.updateHeroIndex(pagerState.currentPage)
                                }
                            }

                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            ) { page ->
                                val heroItem = heroMoviesByCategory[page]
                                Box(modifier = Modifier.fillMaxSize()) {
                                    HeroVideoPlayer(
                                        movie = heroItem.movie,
                                        modifier = Modifier.fillMaxSize(),
                                        isScreenActive = isHomeActive,
                                        isVisible80Percent = true,
                                        badgeText = "NEU HINZUGEFÜGT",
                                        getOrCreatePlayer = { url -> viewModel.getOrCreateHeroPlayer(url) },
                                        prepareAndPlay = { url -> viewModel.prepareAndPlayHero(url) },
                                        pausePlayer = { url -> viewModel.pauseHero(url) },
                                        onDetailsClick = {
                                            navController.navigate(Screen.MovieDetail.createRoute(heroItem.movie.id))
                                        }
                                    )
                                }
                            }

                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Lade Inhalte...", color = WarmTextSecondary)
                        }
                    }
                }

                // ── SECTION 1: TOP BEWERTET ─────────────────────────
                if (topRatedMovies.isNotEmpty()) {
                    TopRatedSection(
                        movies = topRatedMovies,
                        selectedIndex = selectedTopRatedIndex,
                        onUpdateIndex = { viewModel.updateTopRatedIndex(it) },
                        navController = navController
                    )
                }

                // ── SECTION 2: SERIEN ANGEBOTE (Kategorien mit Kacheln) ──
                if (seriesByCategory.isNotEmpty()) {
                    SeriesCategoriesSection(
                        categories = seriesByCategory,
                        navController = navController
                    )
                }

                // ── SECTION 3: Content ──────────────────────────────
                ContentSection(
                    selectedCategory = selectedCategory,
                    fuerDichMovies = fuerDichMovies,
                    sportChannels = sportChannels,
                    recentWatch = recentWatch,
                    heroMoviesByCategory = heroMoviesByCategory,
                    topRatedMovies = topRatedMovies,
                    navController = navController
                )

                Spacer(Modifier.height(24.dp))
            }

            // ═══════════════════════════════════════════════════════════
            // LAYER 2: HEADER (VORNE) — fixiert über scrollbarem Content
            // In Compose-Box gewinnt das ZULETZT deklarierte Kind die
            // Touch-Events. Daher Header NACH dem Content deklarieren,
            // damit Icons klickbar bleiben. zIndex(1f) sorgt für korrekte
            // Zeichenreihenfolge (visuell oben).
            // ═══════════════════════════════════════════════════════════
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .zIndex(1f)
                    .onGloballyPositioned { headerHeightPx = it.size.height },
                shadowElevation = 8.dp,
                tonalElevation = 2.dp,
                color = WarmSurface
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = com.nativeplayer.R.drawable.logo_osi_player),
                            contentDescription = "Osi Player Logo",
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .align(Alignment.CenterVertically),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.CenterStart
                        )
                        // ── SEARCH ICON ──────────────────────────────
                        var isSearchFocused by remember { mutableStateOf(false) }
                        val searchInteractionSource = remember { MutableInteractionSource() }
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSearchFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    if (isSearchFocused) BorderStroke(2.dp, OsiCyan) else BorderStroke(0.dp, Color.Transparent),
                                    RoundedCornerShape(8.dp)
                                )
                                .focusRequester(searchFocusRequester)
                                .onFocusChanged { isSearchFocused = it.isFocused }
                                .clickable(
                                    interactionSource = searchInteractionSource,
                                    indication = null
                                ) {
                                    navController.navigate(Screen.Search.route) {
                                        launchSingleTop = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Search,
                                "Suche",
                                tint = if (isSearchFocused) OsiCyan else WarmTextPrimary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(Modifier.width(4.dp))
                        // ── SETTINGS ICON ─────────────────────────────
                        var isSettingsFocused by remember { mutableStateOf(false) }
                        val settingsInteractionSource = remember { MutableInteractionSource() }
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSettingsFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface,
                                    RoundedCornerShape(8.dp)
                                )
                                .border(
                                    if (isSettingsFocused) BorderStroke(2.dp, OsiCyan) else BorderStroke(0.dp, Color.Transparent),
                                    RoundedCornerShape(8.dp)
                                )
                                .focusRequester(settingsFocusRequester)
                                .onFocusChanged { isSettingsFocused = it.isFocused }
                                .clickable(
                                    interactionSource = settingsInteractionSource,
                                    indication = null
                                ) {
                                    navController.navigate(Screen.Settings.route) {
                                        launchSingleTop = true
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                "Einstellungen",
                                tint = if (isSettingsFocused) OsiCyan else WarmTextSecondary,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }
                    // ── NAVIGATION BUTTONS (Live TV · Filme · Serien · Favoriten) ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        NavButton(
                            label = "Live TV",
                            icon = Icons.Filled.LiveTv,
                            isSelected = false,
                            onClick = { navController.navigate(Screen.Live.route) { launchSingleTop = true } },
                            modifier = Modifier.weight(1f)
                        )
                        NavButton(
                            label = "Filme",
                            icon = Icons.Filled.Movie,
                            isSelected = false,
                            onClick = { navController.navigate(Screen.Movies.route) { launchSingleTop = true } },
                            modifier = Modifier.weight(1f)
                        )
                        NavButton(
                            label = "Serien",
                            icon = Icons.Filled.Theaters,
                            isSelected = false,
                            onClick = { navController.navigate(Screen.Series.route) { launchSingleTop = true } },
                            modifier = Modifier.weight(1f)
                        )
                        NavButton(
                            label = "Favoriten",
                            icon = Icons.Filled.Favorite,
                            isSelected = false,
                            onClick = { navController.navigate(Screen.Favorites.route) { launchSingleTop = true } },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Gold gradient line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(OsiCyan.copy(alpha = 0f), OsiCyan.copy(alpha = 0.6f), OsiCyan.copy(alpha = 0f))
                                )
                            )
                    )
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
                TextButton(onClick = {
                    showExitDialog = false
                    (navController.context as? android.app.Activity)?.finish()
                }) {
                    Text("Ja", color = OsiCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Nein", color = WarmTextSecondary)
                }
            },
            containerColor = OsiCyan.copy(alpha = 0.08f)
        )
    }
}

// ═════════════════════════════════════════════════════════════════════
// SECTION COMPOSABLES
// ═════════════════════════════════════════════════════════════════════

/**
 * TOP BEWERTET — Full-screen carousel section with dark background
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TopRatedSection(
    movies: List<Movie>,
    selectedIndex: Int,
    onUpdateIndex: (Int) -> Unit,
    navController: NavController
) {
    val pagerState = rememberPagerState(initialPage = selectedIndex.coerceIn(0, (movies.size - 1).coerceAtLeast(0))) { movies.size }
    val scope = rememberCoroutineScope()
    var isProgrammaticScroll by remember { mutableStateOf(false) }

    // Sync ViewModel → Pager
    LaunchedEffect(selectedIndex) {
        if (pagerState.currentPage != selectedIndex) {
            isProgrammaticScroll = true
            try {
                pagerState.animateScrollToPage(selectedIndex)
            } finally {
                isProgrammaticScroll = false
            }
        }
    }
    // Sync Pager → ViewModel (only on user swipe)
    LaunchedEffect(pagerState.currentPage) {
        if (!isProgrammaticScroll && pagerState.currentPage != selectedIndex) {
            onUpdateIndex(pagerState.currentPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 306.dp, max = 459.dp)
            .background(CarouselBackground)
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StrokedTitle("TOP BEWERTET", fontSize = 18.sp, letterSpacing = 1.sp)
            Text(
                "★ ≥ 80%", color = Color(0xFF000000), fontSize = 13.sp, fontWeight = FontWeight.Medium,
                modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(OsiStarYellow).padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth().weight(1f),
            contentPadding = PaddingValues(horizontal = 32.dp),
            pageSpacing = 12.dp
        ) { page ->
            TopRatedCarouselCard(
                movie = movies[page],
                modifier = Modifier.fillMaxHeight().aspectRatio(16f / 9f),
                onDetailsClick = { navController.navigate(Screen.MovieDetail.createRoute(movies[page].id)) }
            )
        }
        Spacer(Modifier.height(12.dp))
        // Pagination dots entfernt
    }
}

/**
 * SERIEN ANGEBOTE — Kategorien mit Poster-Kacheln (Titel + Bewertung)
 */
@Composable
private fun SeriesCategoriesSection(
    categories: List<HomeViewModel.SeriesCategoryGroup>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(CarouselBackground)
    ) {
        // Golden divider line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(OsiCyan.copy(alpha = 0f), OsiCyan.copy(alpha = 0.6f), OsiCyan.copy(alpha = 0f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            StrokedTitle(
                text = "SERIEN ANGEBOTE",
                fontSize = 16.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(16.dp))

            // Scrollbare Liste von Kategorien — jede Kategorie mit horizontaler Kachel-Reihe
            // forEach is OK here since categories are limited (max 15)
            categories.forEach { group ->
                SeriesCategoryRow(
                    category = group.category,
                    series = group.series,
                    navController = navController
                )
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

/**
 * Einzelne Kategorie-Zeile: Titel + horizontal scrollbare Serien-Kacheln
 */
@Composable
private fun SeriesCategoryRow(
    category: String,
    series: List<com.nativeplayer.data.model.Series>,
    navController: NavController
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StrokedTitle(
                text = category.uppercase(),
                fontSize = 13.sp,
                letterSpacing = 0.8.sp
            )
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = WarmTextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(series, key = { it.id }) { seriesItem ->
                SeriesTile(
                    series = seriesItem,
                    onClick = { navController.navigate(Screen.SeriesDetail.createRoute(seriesItem.id)) }
                )
            }
        }
    }
}

/**
 * Serien-Kachel: Poster + Titel + Bewertung
 */
@Composable
private fun SeriesTile(
    series: com.nativeplayer.data.model.Series,
    onClick: () -> Unit
) {
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

    Card(
        modifier = Modifier
            .width(105.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable(onClick = onClick)
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface
        ),
        border = if (isFocused) BorderStroke(1.5.dp, OsiCyan) else null
    ) {
        Column {
            // Poster
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(155.dp)
                    .background(WarmSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                if (!series.posterUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(series.posterUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = series.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Filled.Theaters,
                        null,
                        Modifier.size(24.dp),
                        tint = WarmTextSecondary
                    )
                }
            }
            // Titel + Bewertung
            Column(Modifier.padding(6.dp)) {
                StrokedTitle(
                    text = series.title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        Modifier.size(12.dp),
                        tint = OsiStarYellow
                    )
                    Text(
                        " %.1f".format(series.rating),
                        color = OsiStarYellow,
                        fontSize = 10.sp
                    )
                    Spacer(Modifier.weight(1f))
                    if (series.year.isNotEmpty()) {
                        Text(
                            series.year,
                            color = WarmTextSecondary,
                            fontSize = 8.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Content section — scrollable within its LazyColumn item.
 * Uses a nested Column (not LazyColumn) to avoid nested scrolling issues.
 */
@Composable
private fun ContentSection(
    selectedCategory: HomeCategory,
    fuerDichMovies: List<Movie>,
    sportChannels: List<com.nativeplayer.data.model.Channel>,
    recentWatch: List<WatchHistoryEntity>,
    heroMoviesByCategory: List<HomeViewModel.HeroCategoryMovie>,
    topRatedMovies: List<Movie>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmBackground)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // FÜR DICH
        if (fuerDichMovies.isNotEmpty()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                StrokedTitle(
                    text = "FÜR DICH",
                    fontSize = 14.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                val screenWidth = LocalConfiguration.current.screenWidthDp
                val itemWidth = ((screenWidth - 32 - 9 * 10) / 10).dp
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(fuerDichMovies.take(10), key = { it.id }) { movie ->
                        RecommendationCard(
                            movie = movie,
                            modifier = Modifier.width(itemWidth),
                            onClick = { navController.navigate(Screen.MovieDetail.createRoute(movie.id)) }
                        )
                    }
                }
            }
        }

        // Category-specific content
        when (selectedCategory) {
            HomeCategory.ALLE -> {
                // Quick Access
                QuickAccessSection(navController = navController)
                // Sports
                if (sportChannels.isNotEmpty()) {
                    SectionHeader(title = "Sports Live", modifier = Modifier.padding(horizontal = 16.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(sportChannels, key = { it.id }) { channel ->
                            SportChannelCard(
                                channel = channel,
                                modifier = Modifier.width(280.dp),
                                onClick = {
                                    navController.navigate(
                                        Screen.FullScreenPlayer.createRoute(
                                            url = channel.url, type = "live", contentId = channel.id,
                                            title = channel.name, imageUrl = channel.logo ?: ""
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                // Weitersehen
                if (recentWatch.isNotEmpty()) {
                    SectionHeader(title = "Weitersehen", modifier = Modifier.padding(horizontal = 16.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(recentWatch.take(8), key = { it.id }) { entry ->
                            ContinueWatchingTile(entry = entry, navController = navController, modifier = Modifier.width(140.dp))
                        }
                    }
                }
            }
            HomeCategory.SPORTS -> {
                if (sportChannels.isEmpty()) {
                    EmptyState(message = "Keine Sport-Sender gefunden", modifier = Modifier.padding(32.dp))
                } else {
                    SectionHeader(title = "Alle Sport-Sender", modifier = Modifier.padding(horizontal = 16.dp))
                    sportChannels.forEach { channel ->
                        SportChannelCard(
                            channel = channel,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = {
                                navController.navigate(
                                    Screen.FullScreenPlayer.createRoute(
                                        url = channel.url, type = "live", contentId = channel.id,
                                        title = channel.name, imageUrl = channel.logo ?: ""
                                    )
                                )
                            }
                        )
                    }
                }
            }
            HomeCategory.NEU -> {
                if (heroMoviesByCategory.isEmpty()) {
                    EmptyState(message = "Keine neuen Inhalte gefunden", modifier = Modifier.padding(32.dp))
                } else {
                    SectionHeader(title = "Neueste pro Kategorie", modifier = Modifier.padding(horizontal = 16.dp))
                    heroMoviesByCategory.forEach { heroItem ->
                        MovieListItem(movie = heroItem.movie, navController = navController, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
            HomeCategory.BELIEBT -> {
                if (topRatedMovies.isEmpty()) {
                    EmptyState(message = "Keine bewerteten Inhalte gefunden", modifier = Modifier.padding(32.dp))
                } else {
                    SectionHeader(title = "Top bewertet", modifier = Modifier.padding(horizontal = 16.dp))
                    topRatedMovies.forEach { movie ->
                        MovieListItem(movie = movie, navController = navController, modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
            HomeCategory.KATEGORIEN -> {
                SectionHeader(title = "Kategorien", modifier = Modifier.padding(horizontal = 16.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTile(title = "Live TV", icon = Icons.Filled.LiveTv, modifier = Modifier.weight(1f), onClick = { navController.navigate(Screen.Live.route) })
                        QuickTile(title = "Filme", icon = Icons.Filled.Movie, modifier = Modifier.weight(1f), onClick = { navController.navigate(Screen.Movies.route) })
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTile(title = "Serien", icon = Icons.Filled.Theaters, modifier = Modifier.weight(1f), onClick = { navController.navigate(Screen.Series.route) })
                        QuickTile(title = "Favoriten", icon = Icons.Filled.Favorite, modifier = Modifier.weight(1f), onClick = { navController.navigate(Screen.Favorites.route) })
                    }
                }
            }
        }
    }
}

// ═════════════════════════════════════════════════════════════════════
// HELPER COMPOSABLES
// ═════════════════════════════════════════════════════════════════════

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    StrokedTitle(
        text = title,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        modifier = modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun QuickAccessSection(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Live TV",
                icon = Icons.Filled.LiveTv,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Live.route) }
            )
            QuickTile(
                title = "Filme",
                icon = Icons.Filled.Movie,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Movies.route) }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickTile(
                title = "Serien",
                icon = Icons.Filled.Theaters,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Series.route) }
            )
            QuickTile(
                title = "Favoriten",
                icon = Icons.Filled.Favorite,
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Favorites.route) }
            )
        }
    }
}

@Composable
private fun QuickTile(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
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

    Card(
        modifier = modifier
            .height(80.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 6.dp else 2.dp
        ),
        border = if (isFocused) BorderStroke(1.5.dp, OsiCyan) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isFocused) OsiCyan.copy(alpha = 0.25f)
                        else OsiCyan.copy(alpha = 0.12f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.size(22.dp),
                    tint = if (isFocused) OsiCyan else WarmTextSecondary
                )
            }
            Spacer(Modifier.width(12.dp))
            StrokedTitle(
                text = title,
                fontSize = 14.sp,
                fontWeight = if (isFocused) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ContinueWatchingTile(
    entry: WatchHistoryEntity,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val progress = if (entry.duration > 0) (entry.position.toFloat() / entry.duration).coerceIn(0f, 1f) else 0f
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

    Card(
        modifier = modifier
            .height(200.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
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
                            navController.navigate(
                                Screen.FullScreenPlayer.createRoute(
                                    url = entry.streamUrl,
                                    type = entry.type,
                                    contentId = entry.itemId,
                                    title = entry.title,
                                    imageUrl = entry.imageUrl
                                )
                            )
                        }
                    }
                }
            }
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface
        ),
        border = if (isFocused) BorderStroke(1.5.dp, OsiCyan) else null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster image
            if (entry.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current.applicationContext)
                        .data(entry.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = entry.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    WarmBackground.copy(alpha = 0.9f)
                                ),
                                startY = 0f,
                                endY = Float.MAX_VALUE
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WarmSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when (entry.type) {
                            "movie" -> Icons.Filled.Movie
                            "series" -> Icons.Filled.Theaters
                            else -> Icons.Filled.LiveTv
                        },
                        null,
                        Modifier.size(36.dp),
                        tint = WarmTextSecondary.copy(alpha = 0.3f)
                    )
                }
            }

            // Title + progress
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                StrokedTitle(
                    text = entry.title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 15.sp
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = OsiCyan,
                    trackColor = OsiCyan.copy(alpha = 0.15f)
                )
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: Movie,
    navController: NavController,
    modifier: Modifier = Modifier
) {
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable {
                navController.navigate(Screen.MovieDetail.createRoute(movie.id))
            }
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) OsiCyan.copy(alpha = 0.12f) else WarmSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 6.dp else 2.dp
        ),
        border = if (isFocused) BorderStroke(1.5.dp, OsiCyan) else null
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Poster
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp))
                    .background(WarmSurfaceElevated)
            ) {
                if (movie.posterUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current.applicationContext)
                            .data(movie.posterUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = movie.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Filled.Movie,
                        null,
                        Modifier.size(32.dp),
                        tint = WarmTextSecondary.copy(alpha = 0.3f)
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                StrokedTitle(
                    text = movie.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (movie.year.isNotEmpty()) {
                        Text(movie.year, color = WarmTextSecondary, fontSize = 12.sp)
                    }
                    if (movie.category.isNotEmpty()) {
                        Text("·", color = WarmTextSecondary, fontSize = 12.sp)
                        Text(movie.category, color = WarmTextSecondary, fontSize = 12.sp)
                    }
                }
                if (movie.rating > 0f) {
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            null,
                            modifier = Modifier.size(14.dp),
                            tint = OsiStarYellow
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            String.format("%.1f", movie.rating),
                            color = OsiStarYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                if (movie.plot.isNotEmpty()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        movie.plot,
                        color = WarmTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )
                }
            }

            // Arrow
            Icon(
                Icons.Filled.ChevronRight,
                null,
                modifier = Modifier.size(20.dp),
                tint = WarmTextSecondary
            )
            Spacer(Modifier.width(8.dp))
        }
    }
}

/**
 * Navigation button in the top bar — gold bold text with pulse-zoom on focus.
 */
@Composable
private fun NavButton(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val pulseScale = remember { Animatable(1f) }
    var prevFocused by remember { mutableStateOf(false) }

    // Pulse-zoom animation: scale up briefly then return to normal
    LaunchedEffect(isFocused) {
        if (isFocused && !prevFocused) {
            pulseScale.snapTo(1f)
            pulseScale.animateTo(1.12f, spring(stiffness = Spring.StiffnessMedium))
            pulseScale.animateTo(1f, spring(stiffness = Spring.StiffnessMedium))
        }
        prevFocused = isFocused
    }

    Surface(
        modifier = modifier
            .height(36.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter)
                ) {
                    onClick()
                    true
                } else false
            }
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(8.dp),
        color = if (isFocused) OsiCyan.copy(alpha = 0.15f) else Color.Transparent,
        border = if (isFocused) BorderStroke(1.dp, OsiCyan) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(16.dp),
                tint = if (isFocused) OsiCyan else WarmTextSecondary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = label,
                color = if (isFocused) OsiCyan else WarmTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = WarmTextSecondary,
            fontSize = 14.sp
        )
    }
}

/**
 * Single card in the Top Rated carousel.
 * Shows poster/background, title, rating badge, and Details button.
 * Uses the darker CarouselSurface/CarouselOverlay palette.
 */
@Composable
private fun TopRatedCarouselCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onDetailsClick: () -> Unit = {}
) {
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

    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onDetailsClick() }
            .graphicsLayer {
                scaleX = pulseScale.value
                scaleY = pulseScale.value
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CarouselSurface),
        border = if (isFocused) BorderStroke(3.dp, OsiCyan) else null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster / backdrop
            if (movie.posterUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current.applicationContext)
                        .data(movie.posterUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = movie.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Dark gradient overlay (heavier than hero)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                CarouselOverlay.copy(alpha = 0.4f),
                                CarouselOverlay.copy(alpha = 0.85f),
                                CarouselOverlay
                            ),
                            startY = 0f,
                            endY = Float.MAX_VALUE
                        )
                    )
            )

            // "TOP" badge — Sternengelb
            Surface(
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart),
                shape = RoundedCornerShape(6.dp),
                color = OsiStarYellow
            ) {
                Text(
                    "TOP ${String.format("%.0f", movie.rating * 100)}%",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    color = Color(0xFF000000),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Content (bottom)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                StrokedTitle(
                    text = movie.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))

                // Metadata — Kategorie, Serien & Bewertung in Sternengelb
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (movie.year.isNotEmpty()) {
                        Text(movie.year, color = OsiStarYellow, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("·", color = OsiStarYellow.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                    if (movie.category.isNotEmpty()) {
                        Text(movie.category, color = OsiStarYellow, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Text("·", color = OsiStarYellow.copy(alpha = 0.5f), fontSize = 14.sp)
                    }
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = OsiStarYellow
                    )
                    Text(
                        String.format("%.1f", movie.rating),
                        color = OsiStarYellow,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Plot
                if (movie.plot.isNotEmpty()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        movie.plot,
                        color = WarmTextSecondary,
                        fontSize = 13.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }

                // OK-Button für Details — Sternengelb
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onDetailsClick,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = OsiStarYellow
                    ),
                    border = BorderStroke(1.5.dp, OsiStarYellow),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = OsiStarYellow
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("OK", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = OsiStarYellow)
                }
            }
        }
    }
}
