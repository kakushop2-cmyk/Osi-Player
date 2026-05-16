package com.nativeplayer.ui.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.Movie
import com.nativeplayer.data.repository.FavoriteRepository
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.ui.viewmodel.CategoryVisibilityViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository,
    private val categoryVisibilityViewModel: CategoryVisibilityViewModel
) : ViewModel() {
    private val _moviesByCategory = MutableStateFlow<Map<String, List<Movie>>>(emptyMap())
    val moviesByCategory: StateFlow<Map<String, List<Movie>>> = _moviesByCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()
    
    /**
     * Gefilterte Kategorien (versteckte Kategorien werden ausgeblendet).
     * Diese StateFlow kombiniert _categories mit der CategoryVisibilityViewModel.
     */
    val filteredCategories: StateFlow<List<String>> = 
        combine(_categories, categoryVisibilityViewModel.categoryVisibility) { cats, visibility ->
            cats.filter { it == "Alle" || !visibility.hiddenCategoriesMovies.contains(it) }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // ── Sort mode ─────────────────────────────────────────────
    private val _isSortMode = MutableStateFlow(false)
    val isSortMode: StateFlow<Boolean> = _isSortMode.asStateFlow()

    init {
        loadMovies()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("movie").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }

    fun loadMovies() {
        val grouped = repository.getMoviesByCategory()
        _moviesByCategory.value = grouped
        // Use the persisted ordering from repository instead of simple sort
        val orderedKeys = repository.getOrderedMovieCategories()
        _categories.value = listOf("Alle") + orderedKeys
        if (_selectedCategory.value == null) {
            _selectedCategory.value = "Alle"
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleSortMode() {
        _isSortMode.value = !_isSortMode.value
    }

    /**
     * Moves category at [index] one position up.
     * Index 0 ("Alle") is protected and cannot be moved or be the target.
     */
    fun moveCategoryUp(index: Int) {
        if (index <= 1) return  // "Alle" is at 0, nothing can move above index 1
        val list = _categories.value.toMutableList()
        val temp = list[index]; list[index] = list[index - 1]; list[index - 1] = temp
        _categories.value = list
        repository.reorderMovieCategories(list.drop(1))  // skip "Alle"
    }

    /**
     * Moves category at [index] one position down.
     * Index 0 ("Alle") cannot be moved.
     */
    fun moveCategoryDown(index: Int) {
        val list = _categories.value
        if (index == 0 || index >= list.size - 1) return
        val mutableList = list.toMutableList()
        val temp = mutableList[index]; mutableList[index] = mutableList[index + 1]; mutableList[index + 1] = temp
        _categories.value = mutableList
        repository.reorderMovieCategories(mutableList.drop(1))  // skip "Alle"
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            favoriteRepository.toggle(
                FavoriteEntity(
                    id = "movie_${movie.id}", type = "movie", itemId = movie.id,
                    title = movie.title, subtitle = movie.category, imageUrl = movie.posterUrl ?: ""
                )
            )
        }
    }
}
