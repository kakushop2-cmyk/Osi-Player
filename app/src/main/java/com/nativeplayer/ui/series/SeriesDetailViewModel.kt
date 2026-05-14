package com.nativeplayer.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.Series
import com.nativeplayer.data.model.WatchHistoryEntity
import com.nativeplayer.data.repository.FavoriteRepository
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.WatchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SeriesDetailViewModel @Inject constructor(
    private val iptvRepository: IptvRepository,
    private val favoriteRepository: FavoriteRepository,
    private val watchHistoryRepository: WatchHistoryRepository
) : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _watchHistory = MutableStateFlow<WatchHistoryEntity?>(null)
    val watchHistory: StateFlow<WatchHistoryEntity?> = _watchHistory.asStateFlow()

    private val _series = MutableStateFlow<Series?>(null)
    val series: StateFlow<Series?> = _series.asStateFlow()

    fun load(seriesId: String) {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavoriteSync(seriesId)
            _watchHistory.value = watchHistoryRepository.getByItemId(seriesId)
            _series.value = iptvRepository.loadSeriesInfo(seriesId)
        }
    }

    fun toggleFavorite(series: Series) {
        viewModelScope.launch {
            favoriteRepository.toggle(
                FavoriteEntity(
                    id = "series_${series.id}", type = "series", itemId = series.id,
                    title = series.title, subtitle = series.category, imageUrl = series.posterUrl ?: ""
                )
            )
            _isFavorite.value = favoriteRepository.isFavoriteSync(series.id)
        }
    }
}
