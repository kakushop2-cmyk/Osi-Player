package com.nativeplayer.ui.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nativeplayer.data.model.Category
import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.repository.FavoriteRepository
import com.nativeplayer.data.repository.IptvRepository
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    private val _selectedChannel = MutableStateFlow<Channel?>(null)
    val selectedChannel: StateFlow<Channel?> = _selectedChannel.asStateFlow()

    private val _epgItems = MutableStateFlow<List<EpgItem>>(emptyList())
    val epgItems: StateFlow<List<EpgItem>> = _epgItems.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // ── Sort mode ─────────────────────────────────────────────
    private val _isCategorySortMode = MutableStateFlow(false)
    val isCategorySortMode: StateFlow<Boolean> = _isCategorySortMode.asStateFlow()

    private val _isChannelSortMode = MutableStateFlow(false)
    val isChannelSortMode: StateFlow<Boolean> = _isChannelSortMode.asStateFlow()

    init {
        loadData()
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteRepository.getByType("live").collect { favs ->
                _favoriteIds.value = favs.map { it.itemId }.toSet()
            }
        }
    }

    fun loadData() {
        _categories.value = repository.getCachedCategories()
        val cats = _categories.value
        if (cats.isNotEmpty() && _selectedCategory.value == null) {
            _selectedCategory.value = cats.first()
        }
    }

    fun selectCategory(category: Category) {
        _selectedCategory.value = category
        _selectedChannel.value = null
    }

    fun selectChannel(channel: Channel) {
        _selectedChannel.value = channel
        viewModelScope.launch {
            _epgItems.value = repository.getEpgForChannel(channel.id)
        }
    }

    fun toggleFavorite(channel: Channel) {
        viewModelScope.launch {
            favoriteRepository.toggle(
                FavoriteEntity(
                    id = "live_${channel.id}", type = "live", itemId = channel.id,
                    title = channel.name, subtitle = channel.group, imageUrl = channel.logo ?: "",
                    streamUrl = channel.url
                )
            )
        }
    }

    // ── Sort mode toggles ─────────────────────────────────────

    /** Toggles category sort mode. Disables channel sort mode if activated. */
    fun toggleCategorySortMode() {
        _isCategorySortMode.value = !_isCategorySortMode.value
        if (_isCategorySortMode.value) _isChannelSortMode.value = false
    }

    /** Toggles channel sort mode. Disables category sort mode if activated. */
    fun toggleChannelSortMode() {
        _isChannelSortMode.value = !_isChannelSortMode.value
        if (_isChannelSortMode.value) _isCategorySortMode.value = false
    }

    // ── Category reordering ───────────────────────────────────

    fun moveCategoryUp(index: Int) {
        if (index <= 0) return
        val list = _categories.value.toMutableList()
        val temp = list[index]; list[index] = list[index - 1]; list[index - 1] = temp
        _categories.value = list
        repository.reorderLiveCategories(list.map { it.name })
    }

    fun moveCategoryDown(index: Int) {
        val list = _categories.value.toMutableList()
        if (index >= list.size - 1) return
        val temp = list[index]; list[index] = list[index + 1]; list[index + 1] = temp
        _categories.value = list
        repository.reorderLiveCategories(list.map { it.name })
    }

    // ── Channel reordering (within selected category) ─────────

    fun moveChannelUp(index: Int) {
        val cat = _selectedCategory.value ?: return
        if (index <= 0) return
        val channels = cat.channels.toMutableList()
        val temp = channels[index]; channels[index] = channels[index - 1]; channels[index - 1] = temp
        val updated = cat.copy(channels = channels)
        _selectedCategory.value = updated
        _categories.value = _categories.value.map { if (it.name == cat.name) updated else it }
        repository.reorderChannelsInCategory(cat.name, channels.map { it.id })
    }

    fun moveChannelDown(index: Int) {
        val cat = _selectedCategory.value ?: return
        val channels = cat.channels.toMutableList()
        if (index >= channels.size - 1) return
        val temp = channels[index]; channels[index] = channels[index + 1]; channels[index + 1] = temp
        val updated = cat.copy(channels = channels)
        _selectedCategory.value = updated
        _categories.value = _categories.value.map { if (it.name == cat.name) updated else it }
        repository.reorderChannelsInCategory(cat.name, channels.map { it.id })
    }

    /** Gibt die konfigurierte Puffergröße aus den Player-Einstellungen zurück. */
    fun getBufferSizeSeconds(): Int = preferencesManager.getPlayerSettings().bufferSizeSeconds
}
