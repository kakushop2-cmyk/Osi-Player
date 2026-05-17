package com.nativeplayer.ui.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmTextPrimary

/**
 * Bottom bar component displaying current EPG program and timeline progress
 * Shows program name, current time in timezone, progress bar, and remaining minutes
 */
@Composable
fun EPGTimelineBar(
    currentEpgItem: EpgItem?,
    epgProgress: Float,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    modifier: Modifier = Modifier
) {
    if (currentEpgItem == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmSurface.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Current Time (Left)
            Text(
                text = timeDisplayString,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WarmTextPrimary,
                modifier = Modifier.widthIn(min = 50.dp)
            )

            // Program Name (Middle)
            Text(
                text = currentEpgItem.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WarmTextPrimary,
                modifier = Modifier
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Progress Bar
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(horizontal = 4.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (epgProgress / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFFFD700), // Gold
                    trackColor = Color.Gray.copy(alpha = 0.4f)
                )
                Text(
                    text = "${epgProgress.toInt()}%",
                    fontSize = 10.sp,
                    color = WarmTextPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Remaining Time (Right)
            Text(
                text = "Ends in: ${remainingMinutes}m",
                fontSize = 12.sp,
                color = WarmTextPrimary,
                modifier = Modifier.widthIn(min = 80.dp)
            )
        }
    }
}
