package com.nativeplayer.ui.resume

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
class ResumeViewModel @Inject constructor(
    watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    val recentWatch: StateFlow<List<WatchHistoryEntity>> = watchHistoryRepository.getRecent(50)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
