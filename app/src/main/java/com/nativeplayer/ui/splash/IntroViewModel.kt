package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow<IntroState>(IntroState.Loading)
    val state: StateFlow<IntroState> = _state.asStateFlow()

    init {
        loadDataInBackground()
    }

    private fun loadDataInBackground() {
        viewModelScope.launch {
            _state.value = IntroState.Loading

            val activeAccount = preferencesManager.getActiveAccount()

            if (activeAccount == null || activeAccount.domain.isBlank()) {
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
