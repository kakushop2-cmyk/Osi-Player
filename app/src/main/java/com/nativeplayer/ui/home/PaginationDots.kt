package com.nativeplayer.ui.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated

/**
 * Netflix-style pagination dots for carousel indicators.
 *
 * @param pageCount       Total number of pages
 * @param currentPage     Currently selected page (0-indexed)
 * @param modifier        Modifier applied to the row container
 * @param activeColor     Color for the active dot (default: WarmGold)
 * @param inactiveColor   Color for inactive dots (default: WarmSurfaceElevated)
 * @param dotSize         Base diameter for inactive dots
 * @param activeDotSize   Diameter for the active dot (wider pill shape)
 * @param spacing         Space between dots
 */
@Composable
fun PaginationDots(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    onPageSelected: ((Int) -> Unit)? = null,
    activeColor: androidx.compose.ui.graphics.Color = WarmGold,
    inactiveColor: androidx.compose.ui.graphics.Color = WarmSurfaceElevated,
    dotSize: Dp = 8.dp,
    activeDotSize: Dp = 24.dp,
    spacing: Dp = 6.dp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage

            val width by animateDpAsState(
                targetValue = if (isActive) activeDotSize else dotSize,
                label = "dot_width"
            )
            val height by animateDpAsState(
                targetValue = if (isActive) dotSize else dotSize,
                label = "dot_height"
            )
            val color by animateColorAsState(
                targetValue = if (isActive) activeColor else inactiveColor,
                label = "dot_color"
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .clip(CircleShape)
                    .background(color)
                    .then(
                        if (onPageSelected != null) {
                            Modifier.clickable { onPageSelected(index) }
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}
