package com.nativeplayer.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {
    val liveFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("live")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val movieFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("movie")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val seriesFavorites: StateFlow<List<FavoriteEntity>> = favoriteRepository.getByType("series")
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun removeFavorite(itemId: String) {
        viewModelScope.launch { favoriteRepository.remove(itemId) }
    }
}
