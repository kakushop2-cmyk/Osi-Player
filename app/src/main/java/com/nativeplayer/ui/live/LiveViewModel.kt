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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject
import java.util.Calendar
import java.util.TimeZone

@HiltViewModel
class LiveViewModel @Inject constructor(
    private val repository: IptvRepository,
    private val favoriteRepository: FavoriteRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()
    
    /**
     * Gefilterte Kategorien (versteckte Kategorien werden ausgeblendet).
     * Diese StateFlow kombiniert _categories mit den Visibility-Einstellungen.
     */
    val filteredCategories: StateFlow<List<Category>> = 
        combine(_categories, preferencesManager.getCategoryVisibilityFlow()) { cats, visibility ->
            cats.filter { !visibility.hiddenCategoriesLiveTV.contains(it.name) }
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.Lazily,
            initialValue = emptyList()
        )
    
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

    // ── EPG State Flows ───────────────────────────────────────
    private val _previousEpgItem = MutableStateFlow<EpgItem?>(null)
    val previousEpgItem: StateFlow<EpgItem?> = _previousEpgItem.asStateFlow()

    private val _currentEpgItem = MutableStateFlow<EpgItem?>(null)
    val currentEpgItem: StateFlow<EpgItem?> = _currentEpgItem.asStateFlow()

    private val _nextEpgItem = MutableStateFlow<EpgItem?>(null)
    val nextEpgItem: StateFlow<EpgItem?> = _nextEpgItem.asStateFlow()

    private val _epgProgress = MutableStateFlow(0f)
    val epgProgress: StateFlow<Float> = _epgProgress.asStateFlow()

    private val _categoryTimezone = MutableStateFlow("UTC")
    val categoryTimezone: StateFlow<String> = _categoryTimezone.asStateFlow()

    private val _currentTimeInZone = MutableStateFlow(System.currentTimeMillis())
    val currentTimeInZone: StateFlow<Long> = _currentTimeInZone.asStateFlow()

    private val _remainingMinutes = MutableStateFlow(0)
    val remainingMinutes: StateFlow<Int> = _remainingMinutes.asStateFlow()

    init {
        loadData()
        observeFavorites()
        observeChannelChanges()
    }

    private fun observeChannelChanges() {
        viewModelScope.launch {
            selectedChannel.collect { channel ->
                if (channel != null) {
                    // Get category from selected category
                    val category = _selectedCategory.value
                    if (category != null) {
                        // Fetch timezone for this category
                        fetchAndSetCategoryTimezone(category.name)
                    }
                    
                    // Update EPG items for current channel
                    updateEpgItems(channel)
                    
                    // Start progress updates
                    startEpgProgressUpdate()
                }
            }
        }
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
            updateEpgItems(channel)
        }
    }

    /**
     * Synchronous version of selectChannel that suspends until EPG data is loaded.
     * Used before navigation to FullScreenPlayer so EPG data is available immediately.
     */
    suspend fun loadEpgSync(channel: Channel) {
        _selectedChannel.value = channel
        _epgItems.value = repository.getEpgForChannel(channel.id)
        updateEpgItems(channel)
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

    // ── EPG Helper Functions ──────────────────────────────────

    /**
     * Converts UTC timestamp to the given timezone
     */
    fun convertTimeToZone(utcTimeMs: Long, timezoneId: String): Calendar {
        val timezone = TimeZone.getTimeZone(timezoneId)
        return Calendar.getInstance(timezone).apply {
            timeInMillis = utcTimeMs
        }
    }

    /**
     * Calculates EPG progress as a percentage (0-100)
     */
    fun calculateEpgProgress(
        programStartMs: Long,
        programEndMs: Long,
        currentTimeMs: Long
    ): Float {
        if (programEndMs <= programStartMs) return 0f
        val progress = ((currentTimeMs - programStartMs).toFloat() / 
                        (programEndMs - programStartMs)) * 100
        return progress.coerceIn(0f, 100f)
    }

    /**
     * Calculates remaining minutes until program ends
     */
    fun calculateRemainingMinutes(programEndMs: Long, currentTimeMs: Long): Int {
        val remainingMs = programEndMs - currentTimeMs
        return (remainingMs / (1000 * 60)).toInt().coerceAtLeast(0)
    }

    /**
     * Formats time for display (HH:MM)
     */
    fun formatTimeForDisplay(calendar: Calendar): String {
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        return "$hour:$minute"
    }

    /**
     * Fetches timezone for category from backend and updates state
     * (Backend will provide this with category data)
     */
    suspend fun fetchAndSetCategoryTimezone(categoryId: String) {
        try {
            // TODO: This will be filled in when backend integration is complete
            // For now, we use a mapping (can be replaced with API call)
            val timezone = getCategoryTimezone(categoryId)
            _categoryTimezone.value = timezone
        } catch (e: Exception) {
            // Log error, default to UTC
            _categoryTimezone.value = "UTC"
        }
    }

    /**
     * Helper to map category names to timezones (temporary until backend provides this)
     */
    private fun getCategoryTimezone(categoryId: String): String {
        return when {
            categoryId.contains("uk", ignoreCase = true) -> "Europe/London"
            categoryId.contains("turk", ignoreCase = true) || categoryId.contains("tr", ignoreCase = true) -> "Europe/Istanbul"
            categoryId.contains("us", ignoreCase = true) -> "America/New_York"
            categoryId.contains("ger", ignoreCase = true) || categoryId.contains("de", ignoreCase = true) -> "Europe/Berlin"
            else -> "UTC"
        }
    }

    /**
     * Updates previous, current, and next EPG items for the selected channel
     */
    private suspend fun updateEpgItems(channel: Channel) {
        try {
            val allEpgItems = epgItems.value.filter { it.channelId == channel.id }
            
            if (allEpgItems.isEmpty()) {
                _previousEpgItem.value = null
                _currentEpgItem.value = null
                _nextEpgItem.value = null
                return
            }

            val now = System.currentTimeMillis()

            // Sort by start time
            val sortedItems = allEpgItems.sortedBy { it.startTime }

            // Find current program (program that contains 'now')
            val current = sortedItems.firstOrNull { item ->
                item.startTime <= now && item.endTime > now
            }

            if (current != null) {
                val currentIndex = sortedItems.indexOf(current)
                _previousEpgItem.value = if (currentIndex > 0) sortedItems[currentIndex - 1] else null
                _currentEpgItem.value = current
                _nextEpgItem.value = if (currentIndex < sortedItems.size - 1) sortedItems[currentIndex + 1] else null
            } else {
                // If no current program, use the next upcoming one
                val next = sortedItems.firstOrNull { it.startTime > now }
                _currentEpgItem.value = next
                _previousEpgItem.value = sortedItems.lastOrNull { it.startTime <= now }
                _nextEpgItem.value = if (next != null) {
                    val nextIndex = sortedItems.indexOf(next)
                    if (nextIndex < sortedItems.size - 1) sortedItems[nextIndex + 1] else null
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            // Handle error, log it
            e.printStackTrace()
        }
    }

    /**
     * Starts a coroutine to update EPG progress and time every second
     */
    fun startEpgProgressUpdate() {
        viewModelScope.launch {
            while (true) {
                delay(1000L) // Update every second
                
                val current = currentEpgItem.value ?: continue
                val now = System.currentTimeMillis()
                
                // Calculate progress
                val progress = calculateEpgProgress(
                    current.startTime,
                    current.endTime,
                    now
                )
                _epgProgress.value = progress
                
                // Calculate remaining minutes
                val remaining = calculateRemainingMinutes(current.endTime, now)
                _remainingMinutes.value = remaining
                
                // Update current time in zone
                val calendar = convertTimeToZone(now, categoryTimezone.value)
                _currentTimeInZone.value = calendar.timeInMillis
            }
        }
    }

    /**
     * Stops the EPG progress update coroutine
     */
    fun stopEpgProgressUpdate() {
        // Coroutine will naturally stop when ViewModel is cleared
    }
}
