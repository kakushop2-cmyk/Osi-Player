package com.nativeplayer.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.*

enum class HomeCategory(val label: String, val icon: ImageVector) {
    ALLE("Alle", Icons.Filled.Home),
    SPORTS("Sports", Icons.Filled.SportsSoccer),
    NEU("Neu", Icons.Filled.AutoAwesome),
    BELIEBT("Beliebt", Icons.Filled.LocalFireDepartment),
    KATEGORIEN("Kategorien", Icons.Filled.Apps)
}

@Composable
fun CategoryBar(
    selectedCategory: HomeCategory,
    onCategorySelected: (HomeCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // ── Chips Row ──────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            HomeCategory.entries.forEach { category ->
                CategoryChip(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // ── Goldener Gradient-Trenner ─────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 16.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            WarmGold.copy(alpha = 0f),
                            WarmGold.copy(alpha = 0.3f),
                            WarmGold.copy(alpha = 0f)
                        )
                    )
                )
        )
    }
}

@Composable
private fun CategoryChip(
    category: HomeCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
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

    // Animierte Farbwechsel
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> WarmGold
            isFocused -> WarmGold.copy(alpha = 0.18f)
            else -> WarmSurface
        },
        animationSpec = tween(durationMillis = 250),
        label = "chipBg"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isSelected -> WarmTextOnGold
            isFocused -> WarmGold
            else -> WarmTextSecondary
        },
        animationSpec = tween(durationMillis = 250),
        label = "chipContent"
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            isSelected -> WarmTextOnGold
            isFocused -> WarmGold
            else -> WarmTextSecondary.copy(alpha = 0.7f)
        },
        animationSpec = tween(durationMillis = 250),
        label = "chipIcon"
    )

    val borderColor = when {
        isSelected -> WarmGold.copy(alpha = 0.8f)
        isFocused -> WarmGold.copy(alpha = 0.4f)
        else -> WarmBorder
    }

    val elevation by animateColorAsState(
        targetValue = if (isFocused && !isSelected) WarmGold.copy(alpha = 0.08f) else Color.Transparent,
        animationSpec = tween(durationMillis = 200),
        label = "chipGlow"
    )

    Surface(
        modifier = modifier
            .height(42.dp)
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() }
            // D-Pad CENTER / ENTER zum Auswählen
            .onKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.DirectionCenter || event.key == Key.Enter)
                ) {
                    onClick()
                    true
                } else false
            }
            .graphicsLayer { scaleX = pulseScale.value; scaleY = pulseScale.value },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (isFocused) 4.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Subtiler Glow-Hintergrund bei Fokus
            if (isFocused && !isSelected) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    WarmGold.copy(alpha = 0.06f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            // Content: Icon + Text
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = iconColor
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text = category.label,
                    color = contentColor,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}
