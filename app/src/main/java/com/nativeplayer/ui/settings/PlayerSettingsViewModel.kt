package com.nativeplayer.ui.settings

import androidx.lifecycle.ViewModel
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerSettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _settings = MutableStateFlow(preferencesManager.getPlayerSettings())
    val settings: StateFlow<PlayerSettings> = _settings.asStateFlow()

    fun update(settings: PlayerSettings) {
        _settings.value = settings
    }

    fun save() {
        val validated = _settings.value.copy(
            resumeOffsetMs = _settings.value.resumeOffsetMs.coerceIn(0L, 30_000L),
            defaultPlaybackSpeed = _settings.value.defaultPlaybackSpeed.coerceIn(0.5f, 2.0f),
            seekForwardMs = _settings.value.seekForwardMs.coerceIn(5_000L, 300_000L),
            seekBackwardMs = _settings.value.seekBackwardMs.coerceIn(5_000L, 300_000L),
            autoHideControlsMs = _settings.value.autoHideControlsMs.coerceIn(2_000L, 15_000L),
            bufferSizeSeconds = _settings.value.bufferSizeSeconds.coerceIn(10, 120),
            preferredAudioLanguage = _settings.value.preferredAudioLanguage
                .trim()
                .take(3)
                .lowercase()
        )
        _settings.value = validated
        preferencesManager.savePlayerSettings(validated)
    }
}
