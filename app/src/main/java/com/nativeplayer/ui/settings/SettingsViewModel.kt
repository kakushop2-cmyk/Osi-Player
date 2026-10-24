package com.nativeplayer.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.UserInfo
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AccountInfoState(
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _accountInfoState = MutableStateFlow(AccountInfoState())
    val accountInfoState: StateFlow<AccountInfoState> = _accountInfoState.asStateFlow()

    /** Ob die IPTV-Daten (Kanäle/Filme/Serien) komplett geladen wurden. */
    val isDataLoaded: Boolean get() = repository.isLoaded()

    /** Lädt Account-Info aus dem Cache oder frisch von der API. */
    fun loadAccountInfo() {
        viewModelScope.launch {
            // Zuerst Cache anzeigen (sofort)
            val cached = repository.getCachedUserInfo()
            _accountInfoState.value = AccountInfoState(
                userInfo = cached,
                isLoading = true
            )

            // Dann frisch von der API laden
            try {
                val fresh = repository.fetchUserInfo()
                _accountInfoState.value = AccountInfoState(
                    userInfo = fresh,
                    isLoading = false,
                    error = if (fresh == null && cached == null) "Keine Account-Info verfügbar" else null
                )
            } catch (e: Exception) {
                _accountInfoState.value = AccountInfoState(
                    userInfo = _accountInfoState.value.userInfo,
                    isLoading = false,
                    error = "Fehler: ${e.message ?: "Unbekannt"}"
                )
            }
        }
    }

    /** Setzt den State zurück (Dialog schließen). */
    fun resetAccountInfo() {
        _accountInfoState.value = AccountInfoState()
    }
}
