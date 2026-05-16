package com.nativeplayer.ui.series

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.Series
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
class SeriesViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository,
    private val categoryVisibilityViewModel: CategoryVisibilityViewModel
) : ViewModel() {
    private val _seriesByCategory = MutableStateFlow<Map<String, List<Series>>>(emptyMap())
    val seriesByCategory: StateFlow<Map<String, List<Series>>> = _seriesByCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()
    
    /**
     * Gefilterte Kategorien (versteckte Kategorien werden ausgeblendet).
     * Diese StateFlow kombiniert _categories mit der CategoryVisibilityViewModel.
     */
    val filteredCategories: StateFlow<List<String>> = 
        combine(_categories, categoryVisibilityViewModel.categoryVisibility) { cats, visibility ->
            cats.filter { it == "Alle" || !visibility.hiddenCategoriesSeries.contains(it) }
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
        loadSeries()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("series").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }

    fun loadSeries() {
        val grouped = repository.getSeriesByCategory()
        _seriesByCategory.value = grouped
        // Use the persisted ordering from repository instead of simple sort
        val orderedKeys = repository.getOrderedSeriesCategories()
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
        repository.reorderSeriesCategories(list.drop(1))  // skip "Alle"
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
        repository.reorderSeriesCategories(mutableList.drop(1))  // skip "Alle"
    }

    fun toggleFavorite(series: Series) {
        viewModelScope.launch {
            favoriteRepository.toggle(
                FavoriteEntity(
                    id = "series_${series.id}", type = "series", itemId = series.id,
                    title = series.title, subtitle = series.category, imageUrl = series.posterUrl ?: ""
                )
            )
        }
    }
}
