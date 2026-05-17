package com.nativeplayer.ui.live

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Compose State für die Synchronisation des ausgewählten Kanals
 * zwischen PlayerScreen (Fullscreen) und LiveScreen.
 *
 * [selectedChannelId] ist ein [mutableStateOf], damit Änderungen
 * (gesetzt in PlayerScreen.cleanupAndGoBack) sofort eine Recomposition
 * von LiveScreen auslösen, die dann per LaunchedEffect den
 * korrekten Kanal auswählt.
 */
object BackNavigationState {
    var selectedChannelId: String? by mutableStateOf(null)
}
