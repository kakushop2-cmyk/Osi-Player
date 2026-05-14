package com.nativeplayer.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.Movie
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
class MovieDetailViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val watchHistoryRepository: WatchHistoryRepository,
    private val iptvRepository: IptvRepository
) : ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _watchHistory = MutableStateFlow<WatchHistoryEntity?>(null)
    val watchHistory: StateFlow<WatchHistoryEntity?> = _watchHistory.asStateFlow()

    private val _movieDetails = MutableStateFlow<Movie?>(null)
    val movieDetails: StateFlow<Movie?> = _movieDetails.asStateFlow()

    fun load(movieId: String) {
        viewModelScope.launch {
            _isFavorite.value = favoriteRepository.isFavoriteSync(movieId)
            _watchHistory.value = watchHistoryRepository.getByItemId(movieId)
            _movieDetails.value = iptvRepository.loadVodInfo(movieId)
        }
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            favoriteRepository.toggle(
                FavoriteEntity(
                    id = "movie_${movie.id}", type = "movie", itemId = movie.id,
                    title = movie.title, subtitle = movie.category, imageUrl = movie.posterUrl ?: ""
                )
            )
            _isFavorite.value = favoriteRepository.isFavoriteSync(movie.id)
        }
    }
}
