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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmTextPrimary

/** Breite der EPG-Seitenleiste (ca. 22 % des Bildschirms). */
private const val SIDEBAR_WIDTH_FRACTION = 0.22f

/**
 * Rechte EPG-Seitenleiste für den Live-TV-Vollbildmodus.
 * Zeigt 3 Kanäle: vorheriger, aktueller, nächster Sender.
 * Jeder Slot zeigt das Senderlogo und den Programmnamen.
 *
 * @param prevLogoUrl    Logo des vorherigen Senders (aus Channel-Liste).
 * @param currentLogoUrl Logo des aktuellen Senders.
 * @param nextLogoUrl    Logo des nächsten Senders (aus Channel-Liste).
 * @param previousEpgItem EPG-Daten des vorherigen Programms (optional).
 * @param currentEpgItem EPG-Daten des aktuellen Programms.
 * @param nextEpgItem    EPG-Daten des nächsten Programms (optional).
 */
@Composable
fun EPGLogosSidebar(
    prevLogoUrl: String?,
    currentLogoUrl: String?,
    nextLogoUrl: String?,
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(SIDEBAR_WIDTH_FRACTION)
            .background(WarmSurface.copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Vorheriger Sender ──
            ChannelProgramCard(
                logoUrl = prevLogoUrl,
                epgItem = previousEpgItem,
                isActive = false,
                label = "VORHER"
            )

            // Trennlinie
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(1.dp)
                    .background(Color.Gray.copy(alpha = 0.2f))
            )

            // ── Aktueller Sender ──
            ChannelProgramCard(
                logoUrl = currentLogoUrl,
                epgItem = currentEpgItem,
                isActive = true,
                label = "JETZT"
            )

            // Trennlinie
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(1.dp)
                    .background(Color.Gray.copy(alpha = 0.2f))
            )

            // ── Nächster Sender ──
            ChannelProgramCard(
                logoUrl = nextLogoUrl,
                epgItem = nextEpgItem,
                isActive = false,
                label = "NÄCHSTES"
            )
        }
    }
}

/**
 * Einzelne Kanal-/Programm-Karte mit Logo und Titel.
 *
 * @param logoUrl  URL des Senderlogos (kanalspezifisch).
 * @param epgItem  EPG-Daten für den Programmnamen (kann null sein).
 * @param isActive [currentEpgItem] → goldener Rand + volle Deckkraft.
 * @param label    Kleine Bezeichnung (VORHER / JETZT / NÄCHSTES).
 */
@Composable
private fun ChannelProgramCard(
    logoUrl: String?,
    epgItem: EpgItem?,
    isActive: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    val alpha = if (isActive) 1f else 0.5f
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Label (VORHER / JETZT / NÄCHSTES)
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color(0xFFFFD700).copy(alpha = 0.85f) else Color.Gray.copy(alpha = alpha * 0.7f),
            letterSpacing = 2.sp
        )

        // Logo-Container (60 × 60 dp)
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(6.dp))
                .then(
                    if (isActive) {
                        Modifier.border(
                            width = 2.dp,
                            color = Color(0xFFFF8C00).copy(alpha = 0.7f),
                            shape = RoundedCornerShape(6.dp)
                        )
                    } else Modifier
                )
                .background(
                    color = Color.Gray.copy(alpha = if (isActive) 0.12f else 0.06f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!logoUrl.isNullOrEmpty()) {
                // ── Senderlogo ──
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(logoUrl)
                        .crossfade(300)
                        .build(),
                    contentDescription = "Senderlogo",
                    modifier = Modifier.fillMaxSize().padding(2.dp),
                    contentScale = ContentScale.Fit
                )
            } else {
                // ── Platzhalter (erster Buchstabe) ──
                Text(
                    text = epgItem?.title?.firstOrNull()?.uppercase() ?: "TV",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = alpha * 0.6f)
                )
            }
        }

        // Programmname (max. 2 Zeilen)
        Text(
            text = epgItem?.title ?: "Keine Info",
            fontSize = if (isActive) 12.sp else 11.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = WarmTextPrimary.copy(alpha = alpha * 0.7f),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
