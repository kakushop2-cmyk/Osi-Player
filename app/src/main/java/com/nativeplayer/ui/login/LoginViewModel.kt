package com.nativeplayer.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LoginTab { XTREAM, M3U }

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data class Error(val message: String) : LoginState()
    data object Success : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    // ── Tab state ──────────────────────────────────
    var selectedTab: LoginTab by mutableStateOf(LoginTab.XTREAM)
        private set

    // ── Xtream form fields ─────────────────────────
    var serverUrl by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var showPassword by mutableStateOf(false)

    // ── M3U form field ─────────────────────────────
    var m3uUrl by mutableStateOf("")

    // ── Connection state (flow for composable) ────
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // ── Per-field validation errors ────────────────
    var serverError by mutableStateOf<String?>(null)
    var usernameError by mutableStateOf<String?>(null)
    var passwordError by mutableStateOf<String?>(null)
    var m3uError by mutableStateOf<String?>(null)

    // ── Tab switching ──────────────────────────────
    fun selectTab(tab: LoginTab) {
        selectedTab = tab
        clearErrors()
        _state.value = LoginState.Idle
    }

    // ── Field change handlers (clear errors) ─────
    fun onServerUrlChange(value: String) {
        serverUrl = value
        serverError = null
    }

    fun onUsernameChange(value: String) {
        username = value
        usernameError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        passwordError = null
    }

    fun onM3uUrlChange(value: String) {
        m3uUrl = value
        m3uError = null
    }

    fun onShowPasswordToggle() {
        showPassword = !showPassword
    }

    // ── Main connect action ────────────────────────
    fun connect() {
        when (selectedTab) {
            LoginTab.XTREAM -> connectXtream()
            LoginTab.M3U -> connectM3u()
        }
    }

    // ── Xtream Codes login ─────────────────────────
    private fun connectXtream() {
        var hasError = false
        if (serverUrl.isBlank()) {
            serverError = "Server URL erforderlich"
            hasError = true
        }
        if (username.isBlank()) {
            usernameError = "Benutzername erforderlich"
            hasError = true
        }
        if (password.isBlank()) {
            passwordError = "Passwort erforderlich"
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val domain = serverUrl.trim().trimEnd('/')
                val fullDomain = if (!domain.startsWith("http://") && !domain.startsWith("https://")) {
                    "http://$domain"
                } else domain

                // Create a temporary account for the authenticate + load cycle
                val account = Account(
                    id = "login_${System.currentTimeMillis()}",
                    name = username.trim(),
                    domain = fullDomain,
                    username = username.trim(),
                    password = password.trim()
                )

                val success = repository.loadAllFromXtream(account)
                if (success) {
                    // loadAllFromXtream already saves account + sets active
                    _state.value = LoginState.Success
                } else {
                    _state.value = LoginState.Error(
                        "Login fehlgeschlagen — Benutzername oder Passwort falsch"
                    )
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error(
                    "Keine Verbindung: ${e.message ?: "Server antwortet nicht"}"
                )
            }
        }
    }

    // ── M3U playlist login ─────────────────────────
    private fun connectM3u() {
        if (m3uUrl.isBlank()) {
            m3uError = "Playlist URL erforderlich"
            return
        }
        if (!m3uUrl.trim().startsWith("http://") && !m3uUrl.trim().startsWith("https://")) {
            m3uError = "URL muss mit http:// oder https:// beginnen"
            return
        }

        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                repository.fetchPlaylist(m3uUrl.trim())
                preferencesManager.saveM3uUrl(m3uUrl.trim())
                _state.value = LoginState.Success
            } catch (e: Exception) {
                _state.value = LoginState.Error(
                    "Playlist konnte nicht geladen werden: ${e.message ?: "Unbekannter Fehler"}"
                )
            }
        }
    }

    private fun clearErrors() {
        serverError = null
        usernameError = null
        passwordError = null
        m3uError = null
    }
}
