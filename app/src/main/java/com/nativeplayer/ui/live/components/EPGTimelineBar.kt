package com.nativeplayer.ui.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Untere EPG-Timeline-Leiste für den Live-TV-Vollbildmodus.
 * Zeigt Programmname, Fortschrittsbalken, aktuelle Uhrzeit und Restlaufzeit.
 */
@Composable
fun EPGTimelineBar(
    currentEpgItem: EpgItem?,
    epgProgress: Float,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    timezoneName: String = "UTC",
    modifier: Modifier = Modifier
) {
    if (currentEpgItem == null) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(WarmSurface.copy(alpha = 0.25f))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        // ── Obere Reihe: Uhrzeit (links) | Programmname (zentriert) | Restzeit (rechts) ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aktuelle Uhrzeit
            Text(
                text = timeDisplayString,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                color = OsiStarYellow,
                modifier = Modifier.widthIn(min = 60.dp)
            )

            Spacer(Modifier.width(12.dp))

            // Programmname
            Text(
                text = currentEpgItem.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = WarmTextPrimary,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.width(12.dp))

            // Verbleibende Zeit
            Text(
                text = if (remainingMinutes > 0) "-${remainingMinutes} Min" else "Endet gleich",
                fontSize = 19.5.sp,
                fontWeight = FontWeight.Medium,
                color = WarmTextPrimary.copy(alpha = 0.8f),
                modifier = Modifier.widthIn(min = 80.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // ── Fortschrittsbalken ──
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Startzeit
            Text(
                text = formatEpochToHHmm(currentEpgItem.startTime, timezoneName),
                fontSize = 16.5.sp,
                color = Color.Gray,
                modifier = Modifier.widthIn(min = 40.dp)
            )

            Spacer(Modifier.width(8.dp))

            // Progress Bar
            LinearProgressIndicator(
                progress = { (epgProgress / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFFFF8C00),
                trackColor = Color.Gray.copy(alpha = 0.35f)
            )

            Spacer(Modifier.width(8.dp))

            // Endzeit
            Text(
                text = formatEpochToHHmm(currentEpgItem.endTime, timezoneName),
                fontSize = 16.5.sp,
                color = Color.Gray,
                modifier = Modifier.widthIn(min = 40.dp)
            )
        }
    }
}

/**
 * Formatiert einen Unix-Timestamp (Millisekunden seit Epoch UTC) in HH:MM
 * in der angegebenen Zeitzone.
 * [epochMs] muss in ECHTEN UTC-Millisekunden vorliegen (nicht Local-Time-As-Epoch).
 * Die Umrechnung Local-Time-As-Epoch → UTC erfolgt bereits in [LiveViewModel.updateEpgItems].
 */
private fun formatEpochToHHmm(epochMs: Long, timezoneName: String): String {
    return try {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone(timezoneName)
        sdf.format(Date(epochMs))
    } catch (_: Exception) {
        "--:--"
    }
}
