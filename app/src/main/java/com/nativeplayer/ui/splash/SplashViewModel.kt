package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashState {
    data object Loading : SplashState()
    data class LoadingWithProgress(val message: String, val progress: Float) : SplashState()
    data object Success : SplashState()
    data class Error(val message: String) : SplashState()
    data object NoAccount : SplashState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val preferencesManager: PreferencesManager,
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)
    val account: StateFlow<Account?> = _account.asStateFlow()

    init {
        loadData()
    }

    fun loadData(account: Account? = null) {
        viewModelScope.launch {
            _state.value = SplashState.Loading

            val activeAccount = account ?: preferencesManager.getActiveAccount()

            if (activeAccount == null || activeAccount.domain.isBlank()) {
                _state.value = SplashState.NoAccount
                return@launch
            }

            _account.value = activeAccount

            try {
                _state.value = SplashState.LoadingWithProgress("Verbinde mit Server...", 0.1f)
                val success = repository.loadAllFromXtream(activeAccount)

                if (success) {
                    _state.value = SplashState.LoadingWithProgress("Daten geladen!", 1f)
                    _state.value = SplashState.Success
                } else {
                    _state.value = SplashState.Error("Authentifizierung fehlgeschlagen. Zugangsdaten prüfen.")
                }
            } catch (e: Exception) {
                _state.value = SplashState.Error(
                    "Verbindungsfehler: ${e.message ?: "Unbekannter Fehler"}"
                )
            }
        }
    }

    fun getActiveAccount(): Account? = preferencesManager.getActiveAccount()
    fun getAccounts(): List<Account> = preferencesManager.getAccounts()
    fun saveAccount(account: Account) = preferencesManager.saveAccount(account)
    fun setActiveAccount(accountId: String) = preferencesManager.setActiveAccount(accountId)
    fun deleteAccount(accountId: String) = preferencesManager.deleteAccount(accountId)

    fun clearWatchHistory() {
        viewModelScope.launch {
            watchHistoryRepository.clearAll()
        }
    }
}
