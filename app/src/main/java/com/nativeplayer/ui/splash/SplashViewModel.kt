package com.nativeplayer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.Account
import com.nativeplayer.data.repository.PreferencesManager
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {

    fun getActiveAccount(): Account? = preferencesManager.getActiveAccount()
    fun getAccounts(): List<Account> = preferencesManager.getAccounts()
    fun saveAccount(account: Account) = preferencesManager.saveAccount(account)
    fun setActiveAccount(accountId: String) = preferencesManager.setActiveAccount(accountId)
    fun deleteAccount(accountId: String) = preferencesManager.deleteAccount(accountId)
    fun isDemoMode(): Boolean = preferencesManager.isDemoMode()
    fun setDemoMode(enabled: Boolean) = preferencesManager.setDemoMode(enabled)

    fun clearWatchHistory() {
        viewModelScope.launch {
            watchHistoryRepository.clearAll()
        }
    }
}
