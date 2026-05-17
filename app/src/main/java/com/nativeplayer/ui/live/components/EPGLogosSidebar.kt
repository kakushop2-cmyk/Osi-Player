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
 * Zeigt maximal 3 Programme (vorheriges, aktuelles, nächstes) mit
 * Senderlogos und vollständigen Programmtiteln.
 *
 * @param channelLogoUrl URL des Senderlogos (für alle Programme gleich).
 * @param previousEpgItem Vorheriges Programm (kann null sein → Platzhalter).
 * @param currentEpgItem Aktuell laufendes Programm.
 * @param nextEpgItem Nächstes Programm (kann null sein → Platzhalter).
 */
@Composable
fun EPGLogosSidebar(
    channelLogoUrl: String?,
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(SIDEBAR_WIDTH_FRACTION)
            .background(WarmSurface.copy(alpha = 0.88f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Vorheriges Programm ──
            EPGProgramCard(
                epgItem = previousEpgItem,
                logoUrl = channelLogoUrl,
                isActive = false,
                label = "VORHER"
            )

            // Trennlinie
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(1.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            // ── Aktuelles Programm ──
            EPGProgramCard(
                epgItem = currentEpgItem,
                logoUrl = channelLogoUrl,
                isActive = true,
                label = "JETZT"
            )

            // Trennlinie
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(1.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )

            // ── Nächstes Programm ──
            EPGProgramCard(
                epgItem = nextEpgItem,
                logoUrl = channelLogoUrl,
                isActive = false,
                label = "NÄCHSTES"
            )
        }
    }
}

/**
 * Einzelne Programm-Karte mit Logo und Titel.
 *
 * @param epgItem EPG-Daten (kann null sein → Platzhalter).
 * @param logoUrl  URL des Senderlogos (optional).
 * @param isActive [currentEpgItem] → goldener Rand + volle Deckkraft.
 * @param label    Kleine Bezeichnung über dem Programm (VORHER / JETZT / NÄCHSTES).
 */
@Composable
private fun EPGProgramCard(
    epgItem: EpgItem?,
    logoUrl: String?,
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
            color = if (isActive) Color(0xFFFFD700) else Color.Gray.copy(alpha = alpha),
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
                            width = 4.dp,
                            color = Color(0xFFFF8C00),
                            shape = RoundedCornerShape(6.dp)
                        )
                    } else Modifier
                )
                .background(
                    color = Color.Gray.copy(alpha = if (isActive) 0.15f else 0.08f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (!logoUrl.isNullOrEmpty()) {
                // ── Senderlogo (auch wenn EPG null ist) ──
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(logoUrl)
                        .crossfade(300)
                        .build(),
                    contentDescription = epgItem?.title ?: "Senderlogo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            } else {
                // ── Platzhalter (erster Buchstabe) ──
                Text(
                    text = epgItem?.title?.firstOrNull()?.uppercase() ?: "?",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = alpha)
                )
            }
        }

        // Programmname (max. 2 Zeilen)
        Text(
            text = epgItem?.title ?: "Keine Info",
            fontSize = if (isActive) 13.sp else 11.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = WarmTextPrimary.copy(alpha = alpha),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
