package com.nativeplayer.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.Movie
import com.nativeplayer.ui.theme.*

@Composable
fun RecommendationCard(
    movie: Movie,
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
            .height(200.dp)
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
        Box(modifier = Modifier.fillMaxSize()) {
            // Poster image
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
                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    WarmBackground.copy(alpha = 0.2f),
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
                        Icons.Filled.Star,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = WarmTextSecondary.copy(alpha = 0.3f)
                    )
                }
            }

            // Rating badge
            if (movie.rating > 0f) {
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd),
                    shape = RoundedCornerShape(4.dp),
                    color = WarmGold.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(10.dp),
                            tint = WarmTextOnGold
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            String.format("%.1f", movie.rating),
                            color = WarmTextOnGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Content info (bottom)
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
            ) {
                Text(
                    movie.title,
                    color = WarmTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
                if (movie.year.isNotEmpty()) {
                    Text(
                        movie.year,
                        color = WarmTextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
