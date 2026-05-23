package com.nativeplayer.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nativeplayer.ui.theme.*

// ═══════════════════════════════════════════════════════════════════════════════
// PlayerBar Demo Screen — Showcases the new "Floating Pill" Playerbar Design
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun PlayerBarDemoScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OsiBlack)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        // ── Top Bar ──────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(40.dp)
                    .background(OsiSurfaceHigh, CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Zurück",
                    tint = OsiTextPrimary
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                "Playerbar Redesign — Floating Pill",
                color = OsiTextPrimary,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ── Section 1: VOD Normal State ─────────────────────────
        DemoSection(title = "1. VOD — Normal State") {
            PlayerBarFloatingPill(
                isPlaying = false,
                currentPosition = 734_000L, // 12:14
                totalDuration = 5_520_000L, // 1:32:00
                title = "Inception (2010)",
                onPlayPause = {},
                onSeekBack = {},
                onSeekForward = {},
                onMoreClick = {}
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Section 2: VOD Playing State ────────────────────────
        DemoSection(title = "2. VOD — Playing / Fokus auf Play") {
            PlayerBarFloatingPill(
                isPlaying = true,
                currentPosition = 2_145_000L, // 35:45
                totalDuration = 5_520_000L,
                title = "Breaking Bad S5E14",
                onPlayPause = {},
                onSeekBack = {},
                onSeekForward = {},
                onMoreClick = {},
                highlightPlay = true
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Section 3: More Menu Expanded ───────────────────────
        DemoSection(title = "3. VOD — More-Menü offen") {
            PlayerBarWithMoreMenu(
                isPlaying = true,
                currentPosition = 1_800_000L,
                totalDuration = 7_200_000L,
                title = "The Matrix"
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Section 4: Live TV Variant ──────────────────────────
        DemoSection(title = "4. Live TV — EPG Floating Pill") {
            LiveTvFloatingPill(
                channelName = "Das Erste HD",
                programTitle = "Tagesschau",
                programProgress = 0.65f,
                programTime = "20:00 – 20:15"
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Section 5: Buffering State ──────────────────────────
        DemoSection(title = "5. Buffering State") {
            PlayerBarFloatingPill(
                isPlaying = true,
                currentPosition = 734_000L,
                totalDuration = 5_520_000L,
                title = "Loading...",
                onPlayPause = {},
                onSeekBack = {},
                onSeekForward = {},
                onMoreClick = {},
                isBuffering = true
            )
        }

        Spacer(Modifier.height(32.dp))

        // ── Section 6: Design Specs ─────────────────────────────
        DemoSection(title = "6. Design-Spezifikation") {
            DesignSpecsCard()
        }

        Spacer(Modifier.height(48.dp))
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// FLOATING PILL PLAYERBAR — Main Component
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun PlayerBarFloatingPill(
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    title: String,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    onMoreClick: () -> Unit,
    highlightPlay: Boolean = false,
    isBuffering: Boolean = false
) {
    val progress = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f
    val playFocusRequester = remember { FocusRequester() }

    // Glassmorphism Pill Container
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                color = Color(0xCC0A0A0A) // 80% dark with slight warmth
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        OsiCyanBorder.copy(alpha = 0.3f),
                        OsiBorder,
                        OsiCyanBorder.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ── Timeline Row ────────────────────────────────────
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Progress Slider
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Track Background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF2A2A2A))
                    )
                    // Progress Fill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(4.dp)
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(OsiCyan, OsiCyan.copy(alpha = 0.7f))
                                )
                            )
                    )
                    // Seek Thumb
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .wrapContentSize(Alignment.CenterEnd)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .shadow(4.dp, CircleShape)
                                .clip(CircleShape)
                                .background(OsiCyan)
                                .border(2.dp, Color.White, CircleShape)
                        )
                    }
                }

                // Time Labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTime(currentPosition),
                        color = OsiTextSecondary,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = title,
                        color = OsiTextPrimary.copy(alpha = 0.6f),
                        fontSize = 16.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = formatTime(totalDuration),
                        color = OsiTextSecondary,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // ── Control Buttons Row ─────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Rewind 30s
                PillControlButton(
                    icon = Icons.Filled.FastRewind,
                    label = "30s",
                    onClick = onSeekBack,
                    size = 44.dp
                )

                // Play/Pause — Primary Button
                if (isBuffering) {
                    // Buffering indicator replaces play button
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(OsiCyan.copy(alpha = 0.15f))
                            .border(2.dp, OsiCyan.copy(alpha = 0.4f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(42.dp),
                            color = OsiCyan,
                            strokeWidth = 2.5.dp
                        )
                    }
                } else {
                    PillPlayPauseButton(
                        isPlaying = isPlaying,
                        onClick = onPlayPause,
                        isHighlighted = highlightPlay,
                        modifier = Modifier.focusRequester(playFocusRequester)
                    )
                }

                // Forward 30s
                PillControlButton(
                    icon = Icons.Filled.FastForward,
                    label = "30s",
                    onClick = onSeekForward,
                    size = 44.dp
                )

                // More Button
                PillControlButton(
                    icon = Icons.Filled.MoreVert,
                    label = "Mehr",
                    onClick = onMoreClick,
                    size = 44.dp
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// PLAY/PAUSE BUTTON — Circular with Glow Effect
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun PillPlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    isHighlighted: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val glowAlpha by animateFloatAsState(
        targetValue = if (isHighlighted || isFocused) 0.6f else 0.2f,
        animationSpec = tween(300),
        label = "glowAlpha"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Box(
        modifier = modifier
            .size(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(
                elevation = if (isHighlighted || isFocused) 12.dp else 4.dp,
                shape = CircleShape,
                ambientColor = OsiCyan.copy(alpha = glowAlpha),
                spotColor = OsiCyan.copy(alpha = glowAlpha)
            )
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        OsiCyan,
                        OsiCyan.copy(alpha = 0.85f)
                    )
                )
            )
            .border(
                width = if (isFocused) 2.dp else 0.dp,
                color = if (isFocused) Color.White else Color.Transparent,
                shape = CircleShape
            )
            .clickable(onClick = onClick)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play",
            tint = Color.Black,
            modifier = Modifier.size(42.dp)
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONTROL BUTTON — Small with Label
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun PillControlButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    size: Dp = 44.dp
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = if (isFocused) OsiCyan else Color.Transparent,
        animationSpec = tween(200),
        label = "borderColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.08f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .clip(CircleShape)
                .background(
                    if (isFocused) OsiCyanDim else Color(0xFF1A1A1A)
                )
                .border(
                    width = 1.5.dp,
                    color = borderColor,
                    shape = CircleShape
                )
                .clickable(onClick = onClick)
                .onFocusChanged { isFocused = it.isFocused }
                .focusable(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isFocused) OsiCyan else OsiTextPrimary,
                modifier = Modifier.size(33.dp)
            )
        }
        Text(
            text = label,
            color = if (isFocused) OsiCyan else OsiTextSecondary,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// MORE MENU EXPANDED — Slide-Up Panel
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun PlayerBarWithMoreMenu(
    isPlaying: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    title: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Expanded More Menu (Slide-Up Panel)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xE60A0A0A))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            OsiCyanBorder.copy(alpha = 0.2f),
                            OsiBorder
                        )
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                MoreMenuItem(icon = Icons.Filled.Speed, label = "1.0x", isActive = true)
                MoreMenuItem(icon = Icons.Filled.AspectRatio, label = "FIT")
                MoreMenuItem(icon = Icons.Filled.Subtitles, label = "Deutsch")
                MoreMenuItem(icon = Icons.Filled.Audiotrack, label = "Stereo")
                MoreMenuItem(icon = Icons.Filled.Stop, label = "Stop")
            }
        }

        // Main Playerbar below
        PlayerBarFloatingPill(
            isPlaying = isPlaying,
            currentPosition = currentPosition,
            totalDuration = totalDuration,
            title = title,
            onPlayPause = {},
            onSeekBack = {},
            onSeekForward = {},
            onMoreClick = {}
        )
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    when {
                        isActive -> OsiCyanDim
                        isFocused -> Color(0xFF1A1A1A)
                        else -> Color.Transparent
                    }
                )
                .border(
                    width = if (isFocused || isActive) 1.dp else 0.dp,
                    color = when {
                        isActive -> OsiCyan.copy(alpha = 0.5f)
                        isFocused -> OsiCyan
                        else -> Color.Transparent
                    },
                    shape = RoundedCornerShape(10.dp)
                )
                .onFocusChanged { isFocused = it.isFocused }
                .focusable()
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) OsiCyan else OsiTextPrimary,
                modifier = Modifier.size(33.dp)
            )
        }
        Text(
            text = label,
            color = if (isActive) OsiCyan else OsiTextSecondary,
            fontSize = 15.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// LIVE TV FLOATING PILL — EPG-Focused
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun LiveTvFloatingPill(
    channelName: String,
    programTitle: String,
    programProgress: Float,
    programTime: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xCC0A0A0A))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        OsiStarYellow.copy(alpha = 0.3f),
                        OsiBorder,
                        OsiStarYellow.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Channel + Program Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(OsiStarYellowDim)
                        .border(1.dp, OsiStarYellow.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = channelName,
                        color = OsiStarYellow,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = programTime,
                    color = OsiTextSecondary,
                    fontSize = 16.5.sp
                )
            }

            // Program Title
            Text(
                text = programTitle,
                color = OsiTextPrimary,
                fontSize = 22.5.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // EPG Progress Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF2A2A2A))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(programProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(OsiStarYellow, OsiStarYellow.copy(alpha = 0.7f))
                            )
                        )
                )
            }

            // Channel Navigation Hint
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    contentDescription = null,
                    tint = OsiTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "CH+ / CH−",
                    color = OsiTextSecondary,
                    fontSize = 15.sp
                )
                Spacer(Modifier.width(4.dp))
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    tint = OsiTextSecondary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DESIGN SPECS CARD
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun DesignSpecsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(OsiSurfaceHigh)
            .border(1.dp, OsiBorder, RoundedCornerShape(14.dp))
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Design Tokens", color = OsiCyan, fontSize = 21.sp, fontWeight = FontWeight.Bold)

            SpecRow("Container", "RoundedCorner 14dp, 80% #0A0A0A")
            SpecRow("Border", "1dp Gradient (OsiCyanBorder 30% → OsiBorder)")
            SpecRow("Padding (innen)", "16dp allseitig")
            SpecRow("Margin (außen)", "16dp horizontal")
            SpecRow("Button Größe", "Play: 56dp, Controls: 44dp")
            SpecRow("Glow Radius", "12dp (fokussiert), 4dp (normal)")
            SpecRow("Progress Bar", "4dp Höhe, Thumb 12dp mit Shadow")
            SpecRow("Animation", "Spring medium (Focus), 300ms tween (Glow)")
            SpecRow("Live Accent", "OsiStarYellow #FFD700 statt OsiCyan")

            Spacer(Modifier.height(8.dp))
            Text("Vergleich Alt → Neu", color = OsiCyan, fontSize = 21.sp, fontWeight = FontWeight.Bold)

            SpecRow("Buttons", "8 flach → 4 primär + More-Menü")
            SpecRow("Layout", "Full-width Row → Floating Pill (16dp Margin)")
            SpecRow("Background", "Linear Gradient → Glassmorphism (blur + border)")
            SpecRow("Focus", "Nur Farbwechsel → Scale + Glow + Border")
            SpecRow("Navigation", "8 Stops D-Pad → 4 Stops + More Panel")
            SpecRow("Slider", "M3 Slider → Custom mit Gradient + Thumb")
        }
    }
}

@Composable
private fun SpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = OsiTextSecondary,
            fontSize = 16.5.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = OsiTextPrimary,
            fontSize = 16.5.sp
        )
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO SECTION WRAPPER
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
private fun DemoSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            color = OsiCyan,
            fontSize = 19.5.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp)
        )
        // Simulated video background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0D1B2A),
                            Color(0xFF1B263B),
                            Color(0xFF0D1B2A)
                        )
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Fake video content hint
            Text(
                "Video Content",
                color = OsiTextMuted,
                fontSize = 18.sp,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 40.dp)
            )
            // Playerbar overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp)
            ) {
                content()
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// UTILITIES
// ═══════════════════════════════════════════════════════════════════════════════

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
