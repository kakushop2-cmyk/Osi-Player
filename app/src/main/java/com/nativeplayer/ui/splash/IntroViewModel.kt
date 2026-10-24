package com.nativeplayer.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class IntroState {
    data object Loading : IntroState()
    data object Success : IntroState()
    data class Error(val message: String) : IntroState()
    data object NoAccount : IntroState()
}

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val preferencesManager: PreferencesManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<IntroState>(IntroState.Loading)
    val state: StateFlow<IntroState> = _state.asStateFlow()

    init {
        loadDataInBackground()
    }

    private fun loadDataInBackground() {
        viewModelScope.launch {
            _state.value = IntroState.Loading

            // Demo-Modus: gebündelte, rechtlich unbedenkliche Teststreams.
            // Wird für die Play-Store-Prüfung und zum Ausprobieren ohne Konto genutzt.
            if (preferencesManager.isDemoMode()) {
                try {
                    val playlist = context.assets.open("demo.m3u")
                        .bufferedReader()
                        .use { it.readText() }
                    repository.fetchPlaylistFromString(playlist)
                    _state.value = IntroState.Success
                } catch (e: Exception) {
                    _state.value = IntroState.Error(
                        "Demo konnte nicht geladen werden: ${e.message ?: "Unbekannter Fehler"}"
                    )
                }
                return@launch
            }

            val activeAccount = preferencesManager.getActiveAccount()
            val m3uUrl = preferencesManager.getM3uUrl()

            if (activeAccount == null || activeAccount.domain.isBlank()) {
                // M3U-Playlist aus einer früheren Sitzung wieder laden.
                if (m3uUrl.isNotBlank()) {
                    try {
                        repository.fetchPlaylist(m3uUrl)
                        _state.value = IntroState.Success
                    } catch (e: Exception) {
                        _state.value = IntroState.Error(
                            "Playlist konnte nicht geladen werden: ${e.message ?: "Unbekannter Fehler"}"
                        )
                    }
                    return@launch
                }
                _state.value = IntroState.NoAccount
                return@launch
            }

            try {
                val success = repository.loadAllFromXtream(activeAccount)
                if (success) {
                    _state.value = IntroState.Success
                } else {
                    _state.value = IntroState.Error(
                        "Authentifizierung fehlgeschlagen. Zugangsdaten prüfen."
                    )
                }
            } catch (e: Exception) {
                _state.value = IntroState.Error(
                    "Verbindungsfehler: ${e.message ?: "Unbekannter Fehler"}"
                )
            }
        }
    }

    fun retry() {
        loadDataInBackground()
    }
}
