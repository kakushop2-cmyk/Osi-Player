package com.nativeplayer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = watchHistoryRepository.getRecent(10)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Letzter ausgewählter Tab (0=Live, 1=Filme, 2=Serien, 3=Favoriten, 4=Weiterschauen)
    private val _lastSelectedTab = MutableStateFlow(0)
    val lastSelectedTab: StateFlow<Int> = _lastSelectedTab

    fun saveSelectedTab(tabIndex: Int) {
        _lastSelectedTab.value = tabIndex
    }
}
