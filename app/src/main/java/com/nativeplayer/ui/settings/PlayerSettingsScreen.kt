package com.nativeplayer.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.AspectRatio
import com.nativeplayer.ui.settings.components.*
import com.nativeplayer.ui.theme.*
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.imePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsScreen(
    navController: NavController,
    viewModel: PlayerSettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    var isSaving by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // Beobachte das Speicher-Ergebnis aus dem ViewModel (asynchron via SharedFlow)
    LaunchedEffect(Unit) {
        viewModel.saveResult.collectLatest { success ->
            if (success) {
                navController.popBackStack()
            } else {
                isSaving = false
                errorMessage = "Fehler beim Speichern der Einstellungen. Bitte versuche es erneut."
            }
        }
    }

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Player-Einstellungen",
                        color = WarmTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        enabled = !isSaving
                    ) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // ── Wiedergabe ─────────────────────────────────────────
            item { SectionHeader("Wiedergabe") }

            item {
                SliderRow(
                    label = "Rücksprung beim Fortsetzen",
                    value = settings.resumeOffsetMs.toFloat() / 1000f,
                    valueRange = 0f..30f,
                    step = 1f,
                    valueText = "${(settings.resumeOffsetMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(resumeOffsetMs = (it * 1000).toLong())) }
                )
            }

            item {
                SegmentedRow(
                    label = "Standard-Geschwindigkeit",
                    options = listOf(0.5f to "0.5×", 1.0f to "1×", 1.5f to "1.5×", 2.0f to "2×"),
                    selected = settings.defaultPlaybackSpeed,
                    onSelect = { viewModel.update(settings.copy(defaultPlaybackSpeed = it)) }
                )
            }

            // ── Seek-Buttons ───────────────────────────────────────
            item { SectionHeader("Seek-Buttons") }

            item {
                SliderRow(
                    label = "Vorspulen",
                    value = settings.seekForwardMs.toFloat() / 1000f,
                    valueRange = 5f..300f,
                    step = 5f,
                    valueText = "${(settings.seekForwardMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(seekForwardMs = (it * 1000).toLong())) }
                )
            }

            item {
                SliderRow(
                    label = "Zurückspulen",
                    value = settings.seekBackwardMs.toFloat() / 1000f,
                    valueRange = 5f..300f,
                    step = 5f,
                    valueText = "${(settings.seekBackwardMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(seekBackwardMs = (it * 1000).toLong())) }
                )
            }

            // ── Verhalten ──────────────────────────────────────────
            item { SectionHeader("Verhalten") }

            item {
                SliderRow(
                    label = "Controls ausblenden nach",
                    value = settings.autoHideControlsMs.toFloat() / 1000f,
                    valueRange = 2f..15f,
                    step = 1f,
                    valueText = "${(settings.autoHideControlsMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(autoHideControlsMs = (it * 1000).toLong())) }
                )
            }

            item {
                SwitchRow(
                    label = "Nächste Episode automatisch abspielen",
                    checked = settings.autoPlayNext,
                    onCheckedChange = { viewModel.update(settings.copy(autoPlayNext = it)) }
                )
            }

            item {
                SliderRow(
                    label = "Puffer-Größe",
                    value = settings.bufferSizeSeconds.toFloat(),
                    valueRange = 10f..120f,
                    step = 5f,
                    valueText = "${settings.bufferSizeSeconds} s",
                    onValueChange = { viewModel.update(settings.copy(bufferSizeSeconds = it.toInt())) }
                )
            }

            // ── Bild ───────────────────────────────────────────────
            item { SectionHeader("Bild") }

            item {
                SegmentedRow(
                    label = "Standard-Seitenverhältnis",
                    options = listOf(AspectRatio.FIT, AspectRatio.FILL).map { it to it.displayName() },
                    selected = settings.defaultAspectRatio,
                    onSelect = { viewModel.update(settings.copy(defaultAspectRatio = it)) }
                )
            }

            item {
                SwitchRow(
                    label = "Bildschirm anlassen",
                    checked = settings.keepScreenOn,
                    onCheckedChange = { viewModel.update(settings.copy(keepScreenOn = it)) }
                )
            }

            // ── Audio ──────────────────────────────────────────────
            item { SectionHeader("Audio") }

            item {
                TextFieldRow(
                    label = "Bevorzugte Audiosprache",
                    value = settings.preferredAudioLanguage,
                    placeholder = "z.B. de, en, fr",
                    onValueChange = { viewModel.update(settings.copy(preferredAudioLanguage = it)) }
                )
            }

            item {
                SwitchRow(
                    label = "Lautstärke normalisieren",
                    checked = settings.loudnessNormalization,
                    onCheckedChange = { viewModel.update(settings.copy(loudnessNormalization = it)) }
                )
            }

            // ── Fehler-Meldung ─────────────────────────────────────
            if (errorMessage.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFCDD2)
                        )
                    ) {
                        Text(
                            errorMessage,
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFC62828),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // ── Speichern Button ─────────────────────────────────────
            item {
                Button(
                    onClick = {
                        isSaving = true
                        errorMessage = ""
                        android.util.Log.d("PlayerSettingsScreen", "Save button clicked")
                        viewModel.save()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .height(52.dp)
                        .navigationBarsPadding()
                        .imePadding(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmGold,
                        contentColor = Color.Black
                    ),
                    enabled = !isSaving
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    }
                    Text(
                        if (isSaving) "Speichern..." else "Einstellungen speichern",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Sicherheits-Spacer damit Button nicht von Navbar verdeckt wird
            item { Spacer(Modifier.navigationBarsPadding()) }
        }
    }
}
