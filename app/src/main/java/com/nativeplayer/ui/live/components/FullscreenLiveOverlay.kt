package com.nativeplayer.ui.live.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nativeplayer.data.model.EpgItem
import kotlinx.coroutines.delay

/**
 * Overlay controller for fullscreen Live TV experience
 * Manages auto-hide behavior, user interaction detection, and component visibility
 *
 * Behavior:
 * - Shows sidebars on stream start
 * - Auto-hides after 5 seconds of no interaction
 * - Resets timer on user tap
 * - Rebuilds on channel change
 */
@Composable
fun FullscreenLiveOverlay(
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    epgProgress: Float,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    onChannelChanged: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }
    var hideTimerActive by remember { mutableStateOf(false) }

    // Auto-hide timer logic
    LaunchedEffect(isVisible, hideTimerActive) {
        if (isVisible && !hideTimerActive) {
            hideTimerActive = true
            delay(5000L) // 5 seconds
            isVisible = false
            hideTimerActive = false
        }
    }

    // Reset timer when channel changes
    LaunchedEffect(currentEpgItem) {
        isVisible = true
        hideTimerActive = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // User tapped the screen - show sidebars and reset timer
                isVisible = true
                hideTimerActive = false
            }
    ) {
        // Right Sidebar with Logos
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(0.225f) // 20-25% of screen width
        ) {
            EPGLogosSidebar(
                previousEpgItem = previousEpgItem,
                currentEpgItem = currentEpgItem,
                nextEpgItem = nextEpgItem
            )
        }

        // Bottom Timeline Bar
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            EPGTimelineBar(
                currentEpgItem = currentEpgItem,
                epgProgress = epgProgress,
                currentTimeInZone = currentTimeInZone,
                remainingMinutes = remainingMinutes,
                timeDisplayString = timeDisplayString
            )
        }
    }
}
