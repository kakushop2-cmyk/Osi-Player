package com.nativeplayer.ui.search

import androidx.lifecycle.ViewModel
import com.nativeplayer.data.model.Channel
import com.nativeplayer.data.model.Movie
import com.nativeplayer.data.model.Series
import com.nativeplayer.data.repository.IptvRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: IptvRepository
) : ViewModel() {
    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow(SearchResults())
    val results: StateFlow<SearchResults> = _results.asStateFlow()

    data class SearchResults(
        val channels: List<Channel> = emptyList(),
        val movies: List<Movie> = emptyList(),
        val series: List<Series> = emptyList()
    )

    /** Gibt alle gecachten Channels zurück (für PlayerContext-Initialisierung). */
    fun getAllChannels(): List<Channel> = repository.getCachedChannels()

    fun onQueryChanged(q: String) {
        _query.value = q
        if (q.length < 2) {
            _results.value = SearchResults()
            return
        }
        val lower = q.lowercase()
        _results.value = SearchResults(
            channels = repository.getCachedChannels().filter {
                it.name.lowercase().contains(lower) || it.group.lowercase().contains(lower)
            },
            movies = repository.getMovies().filter {
                it.title.lowercase().contains(lower) || it.category.lowercase().contains(lower)
            },
            series = repository.getSeries().filter {
                it.title.lowercase().contains(lower) || it.category.lowercase().contains(lower)
            }
        )
    }
}
