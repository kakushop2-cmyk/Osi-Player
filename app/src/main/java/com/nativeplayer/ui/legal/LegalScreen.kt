package com.nativeplayer.ui.legal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nativeplayer.BuildConfig
import com.nativeplayer.ads.AdsManager
import com.nativeplayer.ui.theme.WarmBackground
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * In-App-Pflichtinformationen: Disclaimer, Datenschutz, Werbung.
 * Die vollständige Datenschutzerklärung liegt zusätzlich als Dokument vor
 * und muss für den Play Store unter einer öffentlichen URL gehostet werden.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current
    val activity = LocalContext.current as? android.app.Activity

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rechtliches",
                        color = WarmTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "Zurück",
                            tint = WarmTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            LegalSection(
                title = "Keine Inhalte durch Osi Player",
                text = "Osi Player ist ein reiner IPTV-Player. Die App stellt keine " +
                    "Fernsehprogramme, Filme, Serien oder Streams bereit. Für die " +
                    "Wiedergabe benötigst du ein eigenes Abonnement bei einem " +
                    "Anbieter deiner Wahl. Du bist selbst dafür verantwortlich, " +
                    "dass du die Inhalte rechtmäßig nutzen darfst."
            )

            LegalSection(
                title = "Zugangsdaten",
                text = "Server-Adresse, Benutzername und Passwort werden ausschließlich " +
                    "lokal auf deinem Gerät gespeichert und nur an den von dir " +
                    "konfigurierten IPTV-Server übertragen. Es gibt keine Übertragung " +
                    "an den Entwickler. Zugangsdaten sind von Cloud-Backups und " +
                    "Geräteumzügen ausgeschlossen."
            )

            LegalSection(
                title = "Werbung",
                text = "Die App ist kostenlos und zeigt beim Kaltstart eine " +
                    "App-Open-Anzeige von Google AdMob. Dabei kann Google " +
                    "Geräte-IDs und Nutzungsdaten verarbeiten. Die erforderliche " +
                    "Einwilligung wird über den Google-UMP-Dialog eingeholt. " +
                    "Ohne Einwilligung werden keine personalisierten Anzeigen " +
                    "angefordert."
            )

            LegalSection(
                title = "Keine Analyse, kein Konto beim Entwickler",
                text = "Es gibt kein Entwicklerkonto, keine Analyse-SDKs und keine " +
                    "Crash-Reporting-Dienste. Favoriten und Wiedergabefortschritt " +
                    "bleiben lokal auf dem Gerät."
            )

            if (activity != null) {
                TextButton(onClick = { AdsManager.showPrivacyOptions(activity) }) {
                    Text(
                        "Datenschutz-Einstellungen (Werbung)",
                        color = WarmGold,
                        fontSize = 14.sp
                    )
                }
            }

            if (BuildConfig.PRIVACY_POLICY_URL.isNotBlank()) {
                TextButton(onClick = { uriHandler.openUri(BuildConfig.PRIVACY_POLICY_URL) }) {
                    Text(
                        "Vollständige Datenschutzerklärung öffnen",
                        color = WarmGold,
                        fontSize = 14.sp
                    )
                }
            }

            Text(
                "Stand: 24. Oktober 2026",
                color = WarmTextSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun LegalSection(title: String, text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            title,
            color = WarmGold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text,
            color = WarmTextSecondary,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )
    }
}
