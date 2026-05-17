package com.nativeplayer.ui.live

import android.view.SurfaceView
import android.view.SurfaceHolder
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Category
import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.player.PlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.nativeplayer.ui.navigation.Screen
import com.nativeplayer.ui.player.PlayerContext
import com.nativeplayer.ui.theme.*
import com.nativeplayer.ui.util.FocusRestoreManager
import com.nativeplayer.ui.live.components.FullscreenLiveOverlay
import com.nativeplayer.ui.live.BackNavigationState
import java.text.SimpleDateFormat
import java.util.TimeZone
import java.util.Calendar
import java.util.Locale
import java.util.Date

@Composable
fun LiveScreen(
    navController: NavController,
    viewModel: LiveViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val filteredCategories by viewModel.filteredCategories.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedChannel by viewModel.selectedChannel.collectAsState()
    val epgItems by viewModel.epgItems.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val isCategorySortMode by viewModel.isCategorySortMode.collectAsState()
    val isChannelSortMode by viewModel.isChannelSortMode.collectAsState()

    // ── EPG State Flows ──────────────────────────────────────
    val previousEpgItem by viewModel.previousEpgItem.collectAsState()
    val currentEpgItem by viewModel.currentEpgItem.collectAsState()
    val nextEpgItem by viewModel.nextEpgItem.collectAsState()
    val epgProgress by viewModel.epgProgress.collectAsState()
    val categoryTimezone by viewModel.categoryTimezone.collectAsState()
    val currentTimeInZone by viewModel.currentTimeInZone.collectAsState()
    val remainingMinutes by viewModel.remainingMinutes.collectAsState()

    // ── Fullscreen State ────────────────────────────────────
    var isFullscreen by remember { mutableStateOf(false) }

    // ── Sort-Zustand: welches Item wurde zuletzt verschoben ────
    var categorySortActiveIndex by remember { mutableIntStateOf(-1) }
    var channelSortActiveIndex by remember { mutableIntStateOf(-1) }
    val categoryListState = rememberLazyListState()
    val channelListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    var playerManager by remember { mutableStateOf<PlayerManager?>(null) }
    var surfaceView by remember { mutableStateOf<SurfaceView?>(null) }
    var isSurfaceReady by remember { mutableStateOf(false) }
    var restartKey by remember { mutableIntStateOf(0) }
    var lastClickedChannelId by remember { mutableStateOf("") }

    // ── Focus Restore ──────────────────────────────────────────
    // Keyed auf BackNavigationState.selectedChannelId, damit remember() bei
    // Rückkehr aus Fullscreen den NEUEN Kanal aus FocusRestoreManager liest
    val lastFocus = remember(BackNavigationState.selectedChannelId) { FocusRestoreManager.getLastFocus("live_screen") }

    // ── Sort-Modus OFF → Active-Index zurücksetzen ─────────────
    LaunchedEffect(isCategorySortMode) { if (!isCategorySortMode) categorySortActiveIndex = -1 }
    LaunchedEffect(isChannelSortMode) { if (!isChannelSortMode) channelSortActiveIndex = -1 }
    // Kategorie gewechselt → Sender-Active-Index zurücksetzen
    LaunchedEffect(selectedCategory) { channelSortActiveIndex = -1 }

    LaunchedEffect(Unit) { viewModel.loadData() }

      // ── Lifecycle Observer ─────────────────────────────────────
      val lifecycleOwner = LocalLifecycleOwner.current
      DisposableEffect(lifecycleOwner) {
          val observer = LifecycleEventObserver { _, event ->
              when (event) {
                  Lifecycle.Event.ON_PAUSE -> {
                      playerManager?.stop()
                      playerManager?.release()
                      playerManager = null
                  }
                   Lifecycle.Event.ON_RESUME -> {
                       restartKey++
                   }
                  else -> {}
              }
          }
          lifecycleOwner.lifecycle.addObserver(observer)
          onDispose {
              lifecycleOwner.lifecycle.removeObserver(observer)
          }
      }
      
      // ── Fokus nach Rückkehr aus Fullscreen wiederherstellen ──
      // BackNavigationState.selectedChannelId ist ein mutableStateOf.
      // cleanupAndGoBack() setzt den Wert + aktualisiert FocusRestoreManager.
      // Die Änderung löst sofort Recomposition aus → das neu keyed remember(…)
      // liefert den korrekten Fokus + dieser LaunchedEffect selectiert den Kanal.
      LaunchedEffect(BackNavigationState.selectedChannelId) {
          BackNavigationState.selectedChannelId?.let { channelId ->
              BackNavigationState.selectedChannelId = null // konsumieren
              viewModel.selectChannelById(channelId)
              // Zur korrekten Position scrollen (damit Kanal sichtbar)
              kotlinx.coroutines.delay(300)
              val channels = selectedCategory?.channels ?: return@let
              val index = channels.indexOfFirst { it.id == channelId }
              if (index >= 0) {
                  try { channelListState.scrollToItem(index) } catch (_: Exception) {}
              }
          }
      }
      
     // ── Player initialisieren ──────────────────────────────────
    LaunchedEffect(selectedChannel, restartKey) {
        val channel = selectedChannel ?: return@LaunchedEffect
        var sv = surfaceView
        var attempts = 0
        while (sv == null && attempts < 200) {
            delay(50)
            sv = surfaceView
            attempts++
        }
        if (sv == null) return@LaunchedEffect
        playerManager?.release()
        playerManager = PlayerManager(context, viewModel.getBufferSizeSeconds()).also {
            it.initialize(channel.url, sv)
            it.play()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            playerManager?.stop()
            playerManager?.release()
        }
    }

    val channels = selectedCategory?.channels ?: emptyList()

     Box(modifier = Modifier.fillMaxSize().background(WarmBackground)) {
         if (isFullscreen && selectedChannel != null) {
             // Fullscreen mode: Show video with EPG Overlay (auto-hide)
             Box(modifier = Modifier.fillMaxSize()) {
                 // Video player (full screen)
                 Box(
                     modifier = Modifier
                         .fillMaxSize()
                         .background(Color.Black),
                     contentAlignment = Alignment.Center
                 ) {
                     AndroidView(
                         factory = { ctx ->
                             SurfaceView(ctx).apply {
                                 holder.addCallback(object : SurfaceHolder.Callback {
                                     override fun surfaceCreated(holder: SurfaceHolder) {
                                         isSurfaceReady = true
                                     }
                                     override fun surfaceChanged(
                                         holder: SurfaceHolder, format: Int, width: Int, height: Int
                                     ) {}
                                     override fun surfaceDestroyed(holder: SurfaceHolder) {
                                         isSurfaceReady = false
                                     }
                                 })
                                 isClickable = false
                                 isFocusable = false
                             }.also { surfaceView = it }
                         },
                         modifier = Modifier.fillMaxSize()
                     )
                 }
                 
                   // EPG Overlay on top of video
                   FullscreenLiveOverlay(
                       channelLogoUrl = selectedChannel?.logo,
                       previousEpgItem = previousEpgItem,
                       currentEpgItem = currentEpgItem,
                       nextEpgItem = nextEpgItem,
                       epgProgress = epgProgress,
                       currentTimeInZone = currentTimeInZone,
                       remainingMinutes = remainingMinutes,
                       timeDisplayString = formatCurrentTime(currentTimeInZone, categoryTimezone),
                       modifier = Modifier.fillMaxSize()
                   )
                 
                 // Exit fullscreen button (top-left)
                 IconButton(
                     onClick = { isFullscreen = false },
                     modifier = Modifier
                         .align(Alignment.TopStart)
                         .padding(8.dp)
                         .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                 ) {
                     Icon(
                         imageVector = Icons.Filled.FullscreenExit,
                         contentDescription = "Exit Fullscreen",
                         tint = WarmGold,
                         modifier = Modifier.size(32.dp)
                     )
                 }
             }
         } else {
            // Normal mode: 3-column layout
            Row(modifier = Modifier.fillMaxSize()) {

            // ── Left 25%: Kategorien ───────────────────────────
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
                            .background(if (isCategorySortMode) WarmGoldDim else Color.Transparent)
                            .clickable {
                                categorySortActiveIndex = -1
                                viewModel.toggleCategorySortMode()
                            }
                            .padding(horizontal = 5.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            if (isCategorySortMode) Icons.Filled.Check else Icons.Filled.UnfoldMore,
                            contentDescription = null,
                            tint = if (isCategorySortMode) WarmGold else WarmTextSecondary,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            if (isCategorySortMode) "Fertig" else "Kat. verschieben",
                            fontSize = 9.sp,
                            color = if (isCategorySortMode) WarmGold else WarmTextSecondary
                        )
                    }
                }

                LazyColumn(state = categoryListState) {
                    itemsIndexed(filteredCategories, key = { _, cat -> cat.name }) { index, category ->
                        val isSelected = selectedCategory?.name == category.name
                        val isActive = categorySortActiveIndex == index
                        CategoryItem(
                            category = category,
                            isSelected = isSelected,
                            isSortMode = isCategorySortMode,
                            isActive = isActive,
                            canMoveUp = index > 0,
                            canMoveDown = index < filteredCategories.size - 1,
                            onClick = { if (!isCategorySortMode) viewModel.selectCategory(category) },
                            onMoveUp = {
                                val newIndex = index - 1
                                viewModel.moveCategoryUp(index)
                                categorySortActiveIndex = newIndex
                                coroutineScope.launch {
                                    try { categoryListState.animateScrollToItem(maxOf(0, newIndex)) }
                                    catch (_: Exception) {}
                                }
                            },
                            onMoveDown = {
                                val newIndex = index + 1
                                viewModel.moveCategoryDown(index)
                                categorySortActiveIndex = newIndex
                                coroutineScope.launch {
                                    try { categoryListState.animateScrollToItem(minOf(categories.size - 1, newIndex)) }
                                    catch (_: Exception) {}
                                }
                            }
                        )
                    }
                }
            }

            // ── Middle 25%: Sender ─────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.25f)
                    .background(WarmSurfaceElevated)
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
                        selectedCategory?.name ?: "Sender",
                        color = WarmGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedCategory != null) {
                        // Sort-Button mit Beschriftung
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isChannelSortMode) WarmGoldDim else Color.Transparent)
                                .clickable {
                                    channelSortActiveIndex = -1
                                    viewModel.toggleChannelSortMode()
                                }
                                .padding(horizontal = 5.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                if (isChannelSortMode) Icons.Filled.Check else Icons.Filled.UnfoldMore,
                                contentDescription = null,
                                tint = if (isChannelSortMode) WarmGold else WarmTextSecondary,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                if (isChannelSortMode) "Fertig" else "Sender verschieben",
                                fontSize = 9.sp,
                                color = if (isChannelSortMode) WarmGold else WarmTextSecondary
                            )
                        }
                    }
                }

                LazyColumn(state = channelListState) {
                    itemsIndexed(channels, key = { _, ch -> ch.id }) { index, channel ->
                        val isSelected = selectedChannel?.id == channel.id
                        val isActive = channelSortActiveIndex == index
                        val isRestoredItem = lastFocus?.itemKey == channel.id
                        val focusRequester = remember(channel.id) { FocusRequester() }

                        LaunchedEffect(isRestoredItem) {
                            if (isRestoredItem) {
                                delay(300)
                                try { focusRequester.requestFocus() } catch (_: Exception) {}
                            }
                        }

                        ChannelItem(
                            channel = channel,
                            isSelected = isSelected,
                            isFavorite = channel.id in favoriteIds,
                            isSortMode = isChannelSortMode,
                            isActive = isActive,
                            canMoveUp = index > 0,
                            canMoveDown = index < channels.size - 1,
                            modifier = Modifier.focusRequester(focusRequester),
                             onClick = {
                                 if (!isChannelSortMode) {
                                      if (selectedChannel?.id == channel.id) {
                                          FocusRestoreManager.saveFocus("live_screen", channel.id)
                                          val currentChannels = selectedCategory?.channels ?: emptyList()
                                          val idx = currentChannels.indexOfFirst { it.id == channel.id }
                                          PlayerContext.setLiveContext(currentChannels, idx)
                                          
                                          // CRITICAL: Load EPG data synchronously before navigation!
                                          coroutineScope.launch {
                                              viewModel.loadEpgSync(channel)
                                              
                                              // Brief delay for StateFlow propagation to collectAsState()
                                              delay(100)
                                              
                                              playerManager?.stop()
                                              playerManager?.release()
                                              playerManager = null
                                              
                                              // Debug: Log EPG data before setting
                                              android.util.Log.d("LiveScreen", "After loadEpgSync - EPG data:")
                                              android.util.Log.d("LiveScreen", "  currentEpgItem = ${viewModel.currentEpgItem.value}")
                                              android.util.Log.d("LiveScreen", "  previousEpgItem = ${viewModel.previousEpgItem.value}")
                                              android.util.Log.d("LiveScreen", "  nextEpgItem = ${viewModel.nextEpgItem.value}")
                                              
                                              // Set EPG data in PlayerContext before navigation
                                               PlayerContext.setEpgData(
                                                   previousEpgItem = viewModel.previousEpgItem.value,
                                                   currentEpgItem = viewModel.currentEpgItem.value,
                                                   nextEpgItem = viewModel.nextEpgItem.value,
                                                   channelLogoUrl = channel.logo,
                                                   epgProgress = viewModel.epgProgress.value,
                                                   currentTimeInZone = viewModel.currentTimeInZone.value,
                                                   remainingMinutes = viewModel.remainingMinutes.value,
                                                   categoryTimezone = viewModel.categoryTimezone.value
                                               )
                                              
                                              navController.navigate(
                                                  Screen.FullScreenPlayer.createRoute(
                                                      url = channel.url,
                                                      type = "live",
                                                      contentId = channel.id,
                                                      title = channel.name,
                                                      imageUrl = channel.logo ?: ""
                                                  )
                                              )
                                          }
                                      } else {
                                         viewModel.selectChannel(channel)
                                         lastClickedChannelId = channel.id
                                     }
                                 }
                             },
                            onMoveUp = {
                                val newIndex = index - 1
                                viewModel.moveChannelUp(index)
                                channelSortActiveIndex = newIndex
                                coroutineScope.launch {
                                    try { channelListState.animateScrollToItem(maxOf(0, newIndex)) }
                                    catch (_: Exception) {}
                                }
                            },
                            onMoveDown = {
                                val newIndex = index + 1
                                viewModel.moveChannelDown(index)
                                channelSortActiveIndex = newIndex
                                coroutineScope.launch {
                                    try { channelListState.animateScrollToItem(minOf(channels.size - 1, newIndex)) }
                                    catch (_: Exception) {}
                                }
                            },
                            onFavoriteClick = { viewModel.toggleFavorite(channel) }
                        )
                    }
                }
            }

            // ── Right 50%: Stream + EPG ────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.50f)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedChannel != null) {
                        AndroidView(
                            factory = { ctx ->
                                SurfaceView(ctx).apply {
                                    holder.addCallback(object : SurfaceHolder.Callback {
                                        override fun surfaceCreated(holder: SurfaceHolder) {
                                            isSurfaceReady = true
                                        }
                                        override fun surfaceChanged(
                                            holder: SurfaceHolder, format: Int, width: Int, height: Int
                                        ) {}
                                        override fun surfaceDestroyed(holder: SurfaceHolder) {
                                            isSurfaceReady = false
                                        }
                                    })
                                    isClickable = false
                                    isFocusable = false
                                }.also { surfaceView = it }
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Filled.LiveTv,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = WarmTextSecondary
                            )
                            Text("Sender auswählen", color = WarmTextSecondary, fontSize = 14.sp)
                        }
                    }

                    // Fullscreen Button (top-right) - with semi-transparent background for visibility
                    IconButton(
                        onClick = { isFullscreen = true },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Fullscreen,
                            contentDescription = "Enter Fullscreen",
                            tint = WarmGold,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .background(WarmSurface)
                        .padding(4.dp)
                ) {
                    Text(
                        "EPG - Programm",
                        color = WarmGold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp, 4.dp)
                    )
                    if (epgItems.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Kein Programm geladen", color = WarmTextSecondary, fontSize = 12.sp)
                        }
                    } else {
                        LazyColumn {
                            items(epgItems) { epg -> EpgItemView(epg) }
                }
            }
        }
        }  // Close else block for normal mode
    }
}
    }
}

// ── CategoryItem ───────────────────────────────────────────────
@Composable
private fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    isSortMode: Boolean,
    isActive: Boolean,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onClick: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
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
            .then(if (!isSortMode) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSortMode) {
            Icon(
                Icons.Filled.DragHandle,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = if (isActive) WarmError else WarmTextSecondary
            )
            Spacer(Modifier.width(4.dp))
        }
        Text(
            category.name,
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
            Text("${category.channels.size}", color = WarmTextSecondary, fontSize = 11.sp)
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMoveUp, enabled = canMoveUp, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Nach oben",
                        modifier = Modifier.size(16.dp),
                        tint = if (canMoveUp) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                    )
                }
                IconButton(onClick = onMoveDown, enabled = canMoveDown, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Nach unten",
                        modifier = Modifier.size(16.dp),
                        tint = if (canMoveDown) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

// ── ChannelItem ────────────────────────────────────────────────
@Composable
private fun ChannelItem(
    channel: Channel,
    isSelected: Boolean,
    isFavorite: Boolean,
    isSortMode: Boolean = false,
    isActive: Boolean = false,
    canMoveUp: Boolean = false,
    canMoveDown: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onMoveUp: () -> Unit = {},
    onMoveDown: () -> Unit = {},
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(
                when {
                    isActive            -> WarmError.copy(alpha = 0.15f)
                    isSelected && !isSortMode -> WarmGoldDim
                    else                -> Color.Transparent
                }
            )
            .then(if (!isSortMode) Modifier.clickable(onClick = onClick) else Modifier)
            .focusable()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSortMode) {
            Icon(
                Icons.Filled.DragHandle,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = if (isActive) WarmError else WarmTextSecondary
            )
            Spacer(Modifier.width(4.dp))
        } else {
            if (!channel.logo.isNullOrEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(channel.logo).crossfade(true).build(),
                    contentDescription = channel.name,
                    modifier = Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(WarmSurfaceElevated),
                    contentScale = ContentScale.Fit
                )
            } else {
                Icon(
                    Icons.Filled.LiveTv,
                    contentDescription = null,
                    modifier = Modifier.size(10.dp),
                    tint = if (isSelected) WarmGold else WarmTextSecondary
                )
            }
            Spacer(Modifier.width(6.dp))
        }
        Text(
            channel.name,
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
            IconButton(onClick = onFavoriteClick, modifier = Modifier.size(14.dp)) {
                Icon(
                    if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorit",
                    tint = if (isFavorite) WarmError else WarmTextSecondary,
                    modifier = Modifier.size(9.dp)
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMoveUp, enabled = canMoveUp, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Nach oben",
                        modifier = Modifier.size(16.dp),
                        tint = if (canMoveUp) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                    )
                }
                IconButton(onClick = onMoveDown, enabled = canMoveDown, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Nach unten",
                        modifier = Modifier.size(16.dp),
                        tint = if (canMoveDown) WarmGold else WarmTextSecondary.copy(alpha = 0.4f)
                    )
                }
            }
        }
    }
}

// ── EpgItemView ────────────────────────────────────────────────
@Composable
private fun EpgItemView(epg: EpgItem) {
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(WarmSurfaceElevated, RoundedCornerShape(4.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            timeFormat.format(Date(epg.startTime)),
            color = WarmGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.width(48.dp)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                epg.title,
                color = WarmTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (epg.description.isNotEmpty()) {
                Text(
                    epg.description,
                    color = WarmTextSecondary,
                    fontSize = 10.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Formats time for display in the current timezone
 */
fun formatCurrentTime(utcTimeMs: Long, timezoneId: String): String {
    val timezone = TimeZone.getTimeZone(timezoneId)
    val calendar = Calendar.getInstance(timezone).apply { timeInMillis = utcTimeMs }
    val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
    return "$hour:$minute"
}
