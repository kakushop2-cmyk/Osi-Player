package com.nativeplayer.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = watchHistoryRepository.getRecent(10)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
