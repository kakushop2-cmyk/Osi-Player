package com.nativeplayer.ui.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmTextPrimary

/**
 * Sidebar component displaying current, previous, and next program logos
 * Located on right side of fullscreen Live TV view
 */
@Composable
fun EPGLogosSidebar(
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(WarmSurface.copy(alpha = 0.85f))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Previous Program (if available)
        if (previousEpgItem != null) {
            EPGProgramCard(
                epgItem = previousEpgItem,
                isActive = false
            )
        } else {
            Spacer(modifier = Modifier.height(0.dp))
        }

        // Current Program (Active)
        if (currentEpgItem != null) {
            EPGProgramCard(
                epgItem = currentEpgItem,
                isActive = true
            )
        }

        // Next Program (if available)
        if (nextEpgItem != null) {
            EPGProgramCard(
                epgItem = nextEpgItem,
                isActive = false
            )
        } else {
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}

/**
 * Individual program card with logo placeholder and name
 */
@Composable
private fun EPGProgramCard(
    epgItem: EpgItem,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Logo Container (Placeholder with gradient)
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
                .then(
                    if (isActive) {
                        Modifier.border(
                            width = 4.dp,
                            color = Color(0xFFFF8C00), // Dark orange-gold
                            shape = RoundedCornerShape(4.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .background(
                    color = Color.Gray.copy(
                        alpha = if (isActive) 1f else 0.5f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder letter or icon
            Text(
                text = epgItem.title.firstOrNull()?.uppercase() ?: "?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(
                    alpha = if (isActive) 1f else 0.5f
                )
            )
        }

        // Program Name
        Text(
            text = epgItem.title,
            fontSize = if (isActive) 14.sp else 12.sp,
            fontWeight = FontWeight.Normal,
            color = WarmTextPrimary.copy(alpha = if (isActive) 1f else 0.5f),
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}
