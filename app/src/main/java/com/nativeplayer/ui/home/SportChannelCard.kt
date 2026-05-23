package com.nativeplayer.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Channel
import com.nativeplayer.ui.theme.*

@Composable
fun SportChannelCard(
    channel: Channel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
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
            .height(64.dp)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
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
        border = if (isFocused) {
            BorderStroke(1.5.dp, OsiCyan)
        } else {
            null
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Channel logo
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(WarmSurfaceElevated),
                contentAlignment = Alignment.Center
            ) {
                if (!channel.logo.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current.applicationContext)
                            .data(channel.logo)
                            .crossfade(true)
                            .build(),
                        contentDescription = channel.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Filled.LiveTv,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = WarmTextSecondary
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            // Channel info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    channel.name,
                    color = if (isFocused) OsiCyan else WarmTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    channel.group,
                    color = WarmTextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            // LIVE badge
            Surface(
                shape = CircleShape,
                color = WarmError
            ) {
                Text(
                    "LIVE",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    color = WarmTextPrimary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.width(8.dp))

            // Play indicator
            if (isFocused) {
                Icon(
                    Icons.Filled.PlayArrow,
                    contentDescription = "Abspielen",
                    modifier = Modifier.size(20.dp),
                    tint = OsiCyan
                )
            }
        }
    }
}
