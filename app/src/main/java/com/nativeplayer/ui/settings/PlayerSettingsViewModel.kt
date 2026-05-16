package com.nativeplayer.ui.settings

import androidx.lifecycle.ViewModel
import android.util.Log
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import javax.inject.Inject

@HiltViewModel
class PlayerSettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private const val TAG = "PlayerSettingsViewModel"
    }

    private val _settings = MutableStateFlow(preferencesManager.getPlayerSettings())
    val settings: StateFlow<PlayerSettings> = _settings.asStateFlow()

    /** Emits true on success, false on failure */
    private val _saveResult = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val saveResult: SharedFlow<Boolean> = _saveResult.asSharedFlow()

    fun update(settings: PlayerSettings) {
        _settings.value = settings
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = saveInternal()
            _saveResult.emit(result)
        }
    }

    private fun saveInternal(): Boolean {
        return try {
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

            Log.d(TAG, "Saving player settings: $validated")
            val commitSuccess = preferencesManager.savePlayerSettings(validated)

            if (!commitSuccess) {
                Log.e(TAG, "SharedPreferences.commit() returned false!")
            } else {
                Log.d(TAG, "Settings saved successfully")
            }
            commitSuccess
        } catch (e: Exception) {
            Log.e(TAG, "Error saving settings", e)
            false
        }
    }
}
