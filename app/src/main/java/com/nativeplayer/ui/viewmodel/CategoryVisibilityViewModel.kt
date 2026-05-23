package com.nativeplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.CategoryVisibilitySettings
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryVisibilityViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val repository: IptvRepository
) : ViewModel() {

    private val _categoryVisibility = MutableStateFlow(CategoryVisibilitySettings())
    val categoryVisibility: StateFlow<CategoryVisibilitySettings> = _categoryVisibility.asStateFlow()

    // ── Available categories from Xtream API ──────────────────
    private val _availableLiveCategories = MutableStateFlow<List<String>>(emptyList())
    val availableLiveCategories: StateFlow<List<String>> = _availableLiveCategories.asStateFlow()

    private val _availableMovieCategories = MutableStateFlow<List<String>>(emptyList())
    val availableMovieCategories: StateFlow<List<String>> = _availableMovieCategories.asStateFlow()

    private val _availableSeriesCategories = MutableStateFlow<List<String>>(emptyList())
    val availableSeriesCategories: StateFlow<List<String>> = _availableSeriesCategories.asStateFlow()

    init {
        loadCategoryVisibility()
        loadAvailableCategories()
    }

    private fun loadCategoryVisibility() {
        _categoryVisibility.value = preferencesManager.getCategoryVisibility()
    }

    /**
     * Loads available categories from the cached Xtream API data.
     * These are the real category names from the server.
     */
    fun loadAvailableCategories() {
        _availableLiveCategories.value = repository.getLiveCategoryNames()
        _availableMovieCategories.value = repository.getMovieCategoryNames()
        _availableSeriesCategories.value = repository.getSeriesCategoryNames()
    }

    fun toggleCategoryVisibility(type: String, categoryName: String) {
        viewModelScope.launch {
            val current = _categoryVisibility.value
            val updated = when (type) {
                "live" -> {
                    val newHidden = current.hiddenCategoriesLiveTV.toMutableSet()
                    if (newHidden.contains(categoryName)) {
                        newHidden.remove(categoryName)
                    } else {
                        newHidden.add(categoryName)
                    }
                    current.copy(hiddenCategoriesLiveTV = newHidden)
                }
                "movies" -> {
                    val newHidden = current.hiddenCategoriesMovies.toMutableSet()
                    if (newHidden.contains(categoryName)) {
                        newHidden.remove(categoryName)
                    } else {
                        newHidden.add(categoryName)
                    }
                    current.copy(hiddenCategoriesMovies = newHidden)
                }
                "series" -> {
                    val newHidden = current.hiddenCategoriesSeries.toMutableSet()
                    if (newHidden.contains(categoryName)) {
                        newHidden.remove(categoryName)
                    } else {
                        newHidden.add(categoryName)
                    }
                    current.copy(hiddenCategoriesSeries = newHidden)
                }
                else -> current
            }
            _categoryVisibility.value = updated
            preferencesManager.saveCategoryVisibility(updated)
        }
    }

    fun isCategoryHidden(type: String, categoryName: String): Boolean {
        val current = _categoryVisibility.value
        return when (type) {
            "live" -> current.hiddenCategoriesLiveTV.contains(categoryName)
            "movies" -> current.hiddenCategoriesMovies.contains(categoryName)
            "series" -> current.hiddenCategoriesSeries.contains(categoryName)
            else -> false
        }
    }

    fun resetVisibility() {
        viewModelScope.launch {
            _categoryVisibility.value = CategoryVisibilitySettings()
            preferencesManager.saveCategoryVisibility(CategoryVisibilitySettings())
        }
    }
}
