package com.nativeplayer.data.repository

import com.nativeplayer.data.api.XtreamApi
import com.nativeplayer.data.model.*
import com.nativeplayer.data.parser.M3uParser
import kotlinx.coroutines.Dispatchers
import android.util.Log
import kotlinx.coroutines.withContext
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IptvRepository @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val preferencesManager: PreferencesManager
) {
    // ── Cached data ───────────────────────────────────────────
    private var cachedChannels: List<Channel> = emptyList()
    private var cachedCategories: List<Category> = emptyList()
    private var cachedMovies: List<Movie> = emptyList()
    private var cachedMoviesByCategory: Map<String, List<Movie>> = emptyMap()
    private var cachedMovieCategoryOrder: List<String> = emptyList()
    private var cachedSeries: List<Series> = emptyList()
    private var cachedSeriesByCategory: Map<String, List<Series>> = emptyMap()
    private var cachedSeriesCategoryOrder: List<String> = emptyList()
    private var isDataLoaded = false

    // ── Xtream API helper ─────────────────────────────────────
    private var xtreamApi: XtreamApi? = null
    private var currentAccount: Account? = null

    private fun getApi(account: Account): XtreamApi {
        if (xtreamApi == null || currentAccount?.baseUrl != account.baseUrl) {
            val retrofit = Retrofit.Builder()
                .baseUrl(account.baseUrl + "/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            xtreamApi = retrofit.create(XtreamApi::class.java)
            currentAccount = account
        }
        return xtreamApi!!
    }

    // ── Load all data from Xtream Codes API ───────────────────
    suspend fun loadAllFromXtream(account: Account): Boolean = withContext(Dispatchers.IO) {
        try {
            val api = getApi(account)

            // 1. Authenticate
            val loginResponse = api.authenticate(account.username, account.password)
            if (loginResponse.userInfo.auth != 1) {
                return@withContext false
            }

            // 2. Load live categories (used for mapping category_id -> name)
            val liveCategoryMap = try {
                api.getLiveCategories(account.username, account.password)
                    .filter { it.categoryId.isNotBlank() }
                    .associateBy({ it.categoryId }, { it.categoryName })
            } catch (e: Exception) {
                Log.e("IptvRepository", "Fehler beim Laden der Live-Kategorien", e)
                emptyMap()
            }

            // 3. Load live channels
            val liveStreams = api.getLiveStreams(account.username, account.password)
            cachedChannels = liveStreams.map { stream ->
                val channelUrl = "${account.baseUrl}/live/${account.username}/${account.password}/${stream.streamId}.ts"
                Channel(
                    id = "ch_${stream.streamId}",
                    name = stream.name,
                    url = channelUrl,
                    logo = stream.streamIcon,
                    group = liveCategoryMap[stream.categoryId] ?: "All",
                    epgId = stream.epgChannelId
                )
            }
            cachedCategories = applyUserCategoryAndChannelOrder(M3uParser.groupByCategory(cachedChannels))

            // 4. Load VOD categories + movies
            val vodCategoryMap = try {
                api.getVodCategories(account.username, account.password)
                    .filter { it.categoryId.isNotBlank() }
                    .associateBy({ it.categoryId }, { it.categoryName })
            } catch (e: Exception) {
                Log.e("IptvRepository", "Fehler beim Laden der VOD-Kategorien", e)
                emptyMap()
            }

            try {
                val vodStreams = api.getVodStreams(account.username, account.password)
                cachedMovies = vodStreams.map { vod ->
                    val movieUrl = "${account.baseUrl}/movie/${account.username}/${account.password}/${vod.streamId}.${vod.containerExtension ?: "mp4"}"
                    Movie(
                        id = "vod_${vod.streamId}",
                        title = vod.name,
                        posterUrl = vod.streamIcon,
                        year = vod.releaseDate?.take(4) ?: "",
                        rating = vod.rating5Based?.toFloat() ?: 0f,
                        plot = vod.plot ?: "",
                        category = vodCategoryMap[vod.categoryId] ?: "All",
                        streamUrl = movieUrl
                    )
                }
                cachedMoviesByCategory = cachedMovies.groupBy { it.category }
                cachedMovieCategoryOrder = applyUserStringOrder(cachedMoviesByCategory.keys.toList(), "movies")
            } catch (e: Exception) {
                Log.e("IptvRepository", "Fehler beim Laden der VOD-Streams", e)
                cachedMovies = sampleMovies
                cachedMoviesByCategory = sampleMovies.groupBy { it.category }
                cachedMovieCategoryOrder = applyUserStringOrder(cachedMoviesByCategory.keys.toList(), "movies")
            }

            // 5. Load series categories + series
            val seriesCategoryMap = try {
                api.getSeriesCategories(account.username, account.password)
                    .filter { it.categoryId.isNotBlank() }
                    .associateBy({ it.categoryId }, { it.categoryName })
            } catch (e: Exception) {
                Log.e("IptvRepository", "Fehler beim Laden der Serien-Kategorien", e)
                emptyMap()
            }

            try {
                val seriesList = api.getSeries(account.username, account.password)
                cachedSeries = seriesList.map { s ->
                    Series(
                        id = "series_${s.seriesId}",
                        title = s.name,
                        posterUrl = s.cover,
                        year = s.releaseDate?.take(4) ?: "",
                        rating = s.rating5Based?.toFloat() ?: 0f,
                        plot = s.plot ?: "",
                        category = seriesCategoryMap[s.categoryId] ?: "All",
                        seasons = emptyList()
                    )
                }
                cachedSeriesByCategory = cachedSeries.groupBy { it.category }
                cachedSeriesCategoryOrder = applyUserStringOrder(cachedSeriesByCategory.keys.toList(), "series")
            } catch (e: Exception) {
                Log.e("IptvRepository", "Fehler beim Laden der Serien-Liste", e)
                cachedSeries = sampleSeries
                cachedSeriesByCategory = sampleSeries.groupBy { it.category }
                cachedSeriesCategoryOrder = applyUserStringOrder(cachedSeriesByCategory.keys.toList(), "series")
            }

            preferencesManager.saveAccount(account)
            preferencesManager.setActiveAccount(account.id)
            isDataLoaded = true
            true
        } catch (e: Exception) {
            Log.e("IptvRepository", "Fehler im loadAllFromXtream-Hauptblock", e)
            false
        }
    }

    // ── Load series info (seasons/episodes) on demand ─────────
    suspend fun loadSeriesInfo(seriesId: String): Series? = withContext(Dispatchers.IO) {
        val account = preferencesManager.getActiveAccount() ?: return@withContext null
        val api = getApi(account)
        val numericId = seriesId.removePrefix("series_").toIntOrNull() ?: return@withContext null
        try {
            val info = api.getSeriesInfo(account.username, account.password, seriesId = numericId)

            // Alle Staffelnummern aus dem episodes-Map ableiten (zuverlässiger als info.seasons)
            val episodesBySeason: Map<String, List<XtreamEpisode>> = info.episodes ?: emptyMap()

            // Staffelnummern aus episodes-Map sammeln + aus seasons-Array (falls vorhanden)
            val seasonNumbersFromEpisodes = episodesBySeason.keys
                .mapNotNull { it.toIntOrNull() }
                .toSortedSet()

            val seasonNumbersFromSeasons = info.seasons
                ?.map { it.seasonNumber }
                ?.toSortedSet()
                ?: sortedSetOf()

            // Vereinigung: alle bekannten Staffelnummern
            val allSeasonNumbers = (seasonNumbersFromEpisodes + seasonNumbersFromSeasons)
                .toSortedSet()

            val seasons = allSeasonNumbers.map { seasonNum ->
                val episodeList = episodesBySeason[seasonNum.toString()] ?: emptyList()
                Season(
                    number = seasonNum,
                    episodes = episodeList
                        .sortedBy { it.episodeNum }
                        .map { ep ->
                            val epUrl = "${account.baseUrl}/series/${account.username}/${account.password}/${ep.id}.${ep.containerExtension ?: "mp4"}"
                            Episode(
                                id = ep.id,
                                title = ep.title,
                                number = ep.episodeNum,
                                streamUrl = epUrl,
                                duration = ep.info?.duration ?: ""
                            )
                        }
                )
            }.filter { it.episodes.isNotEmpty() }

            // Update the cached series with loaded seasons
            val existing = cachedSeries.find { it.id == seriesId }
            return@withContext existing?.copy(
                plot = info.info?.plot ?: existing.plot,
                seasons = seasons
            )
        } catch (e: Exception) {
            Log.e("IptvRepository", "Fehler beim Laden der Serien-Details (seriesId=$seriesId)", e)
            cachedSeries.find { it.id == seriesId }
        }
    }

    // ── Load VOD info (detailed plot, cast, etc.) ────────────
    suspend fun loadVodInfo(vodId: String): Movie? = withContext(Dispatchers.IO) {
        val account = preferencesManager.getActiveAccount() ?: return@withContext null
        val api = getApi(account)
        val numericId = vodId.removePrefix("vod_").toIntOrNull() ?: return@withContext null
        try {
            val info = api.getVodInfo(account.username, account.password, vodId = numericId)
            val existing = cachedMovies.find { it.id == vodId }
            val details = info.info
            val movieData = info.movieData
            
            return@withContext existing?.copy(
                plot = details?.plot ?: movieData?.plot ?: existing.plot,
                rating = details?.rating?.toFloatOrNull() ?: movieData?.rating?.toFloatOrNull() ?: existing.rating,
                year = details?.year ?: movieData?.year ?: details?.releaseDate?.take(4) ?: movieData?.releaseDate?.take(4) ?: existing.year,
                posterUrl = details?.cover ?: movieData?.cover ?: existing.posterUrl
            )
        } catch (e: Exception) {
            Log.e("IptvRepository", "Fehler beim Laden der VOD-Details (vodId=$vodId)", e)
            cachedMovies.find { it.id == vodId }
        }
    }

    // ── M3U Playlist ──────────────────────────────────────────
    suspend fun fetchPlaylist(url: String): List<Channel> = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()
        val response = okHttpClient.newCall(request).execute()
        val body = response.body?.string() ?: return@withContext emptyList<Channel>()
        cachedChannels = M3uParser.parse(body)
        cachedCategories = applyUserCategoryAndChannelOrder(M3uParser.groupByCategory(cachedChannels))
        cachedChannels
    }

    suspend fun fetchPlaylistFromString(content: String): List<Channel> = withContext(Dispatchers.IO) {
        cachedChannels = M3uParser.parse(content)
        cachedCategories = applyUserCategoryAndChannelOrder(M3uParser.groupByCategory(cachedChannels))
        cachedChannels
    }

    // ── Getters ───────────────────────────────────────────────
    fun getCachedChannels(): List<Channel> = cachedChannels
    fun getCachedCategories(): List<Category> = cachedCategories
    fun getMovies(): List<Movie> = cachedMovies
    fun getMoviesByCategory(): Map<String, List<Movie>> = cachedMoviesByCategory
    fun getOrderedMovieCategories(): List<String> = cachedMovieCategoryOrder
    fun getSeries(): List<Series> = cachedSeries
    fun getSeriesByCategory(): Map<String, List<Series>> = cachedSeriesByCategory
    fun getOrderedSeriesCategories(): List<String> = cachedSeriesCategoryOrder
    fun isLoaded(): Boolean = isDataLoaded

    // ── User-defined Reordering ───────────────────────────────

    /** Reorders live TV categories and persists the new order. */
    fun reorderLiveCategories(newOrder: List<String>) {
        preferencesManager.saveCategoryOrder("live", newOrder)
        val byName = cachedCategories.associateBy { it.name }
        val ordered = newOrder.mapNotNull { byName[it] }
        val remaining = cachedCategories.filter { it.name !in newOrder }
        cachedCategories = ordered + remaining
    }

    /** Reorders channels within a live TV category and persists the new order. */
    fun reorderChannelsInCategory(categoryName: String, channelIds: List<String>) {
        preferencesManager.saveChannelOrder(categoryName, channelIds)
        cachedCategories = cachedCategories.map { cat ->
            if (cat.name != categoryName) return@map cat
            val byId = cat.channels.associateBy { it.id }
            val ordered = channelIds.mapNotNull { byId[it] }
            val remaining = cat.channels.filter { it.id !in channelIds }
            cat.copy(channels = ordered + remaining)
        }
    }

    /** Reorders movie categories and persists the new order. */
    fun reorderMovieCategories(newOrder: List<String>) {
        preferencesManager.saveCategoryOrder("movies", newOrder)
        val allKeys = cachedMoviesByCategory.keys
        val ordered = newOrder.filter { it in allKeys }
        val remaining = (allKeys - newOrder.toSet()).sorted()
        cachedMovieCategoryOrder = ordered + remaining
    }

    /** Reorders series categories and persists the new order. */
    fun reorderSeriesCategories(newOrder: List<String>) {
        preferencesManager.saveCategoryOrder("series", newOrder)
        val allKeys = cachedSeriesByCategory.keys
        val ordered = newOrder.filter { it in allKeys }
        val remaining = (allKeys - newOrder.toSet()).sorted()
        cachedSeriesCategoryOrder = ordered + remaining
    }

    // ── Private helpers for applying saved order ──────────────

    /**
     * Applies the saved live category order and per-category channel order
     * to the freshly-loaded category list.
     */
    private fun applyUserCategoryAndChannelOrder(categories: List<Category>): List<Category> {
        // Apply channel orders within each category
        val withChannelOrder = categories.map { cat ->
            val savedChannelOrder = preferencesManager.getChannelOrder(cat.name)
                ?: return@map cat
            val byId = cat.channels.associateBy { it.id }
            val ordered = savedChannelOrder.mapNotNull { byId[it] }
            val remaining = cat.channels.filter { it.id !in savedChannelOrder }
            cat.copy(channels = ordered + remaining)
        }
        // Apply category order
        val savedCatOrder = preferencesManager.getCategoryOrder("live")
            ?: return withChannelOrder
        val byName = withChannelOrder.associateBy { it.name }
        val ordered = savedCatOrder.mapNotNull { byName[it] }
        val remaining = withChannelOrder.filter { it.name !in savedCatOrder }
        return ordered + remaining
    }

    /**
     * Applies the saved string-category order (movies or series).
     * Falls back to alphabetical sort if no saved order exists.
     */
    private fun applyUserStringOrder(keys: List<String>, type: String): List<String> {
        val savedOrder = preferencesManager.getCategoryOrder(type)
            ?: return keys.sorted()
        val ordered = savedOrder.filter { it in keys }
        val remaining = (keys - savedOrder.toSet()).sorted()
        return ordered + remaining
    }

    // ── EPG ───────────────────────────────────────────────────
    suspend fun getEpgForChannel(channelId: String): List<EpgItem> = withContext(Dispatchers.IO) {
        val account = preferencesManager.getActiveAccount() ?: return@withContext emptyList()
        val numericId = channelId.removePrefix("ch_").toIntOrNull() ?: return@withContext emptyList()
        val api = getApi(account)

        try {
            val response = api.getShortEpg(
                username = account.username,
                password = account.password,
                streamId = numericId
            )

            response.epgListings.mapNotNull { listing ->
                val startTime = parseEpgTimestamp(listing.startTimestamp, listing.start)
                val endTime = parseEpgTimestamp(listing.stopTimestamp, listing.end)

                if (startTime != null && endTime != null) {
                    val rawTitle = listing.title ?: listing.name ?: ""
                    val rawDescription = listing.description ?: listing.desc ?: ""
                    
                    val title = decodeEpgText(rawTitle)
                    val description = decodeEpgText(rawDescription)
                    
                    Log.d("IptvRepository", "EPG Item raw='$rawTitle' -> decoded='$title', start=$startTime")
                    
                    EpgItem(
                        channelId = channelId,
                        title = title.ifBlank { "Unbekannte Sendung" },
                        startTime = startTime,
                        endTime = endTime,
                        description = description
                    )
                } else null
            }
        } catch (e: Exception) {
            Log.e("IptvRepository", "Fehler beim Laden der EPG-Daten (channelId=$channelId)", e)
            emptyList()
        }
    }

    private fun decodeEpgText(text: String): String {
        if (text.isBlank()) return ""
        
        // Versuche Base64-Dekodierung
        val base64Decoded = try {
            val decoded = android.util.Base64.decode(text, android.util.Base64.DEFAULT)
            String(decoded, Charsets.UTF_8)
        } catch (_: Exception) { null }
        
        val result = base64Decoded ?: text
        
        // Entferne HTML-Tags
        return result.replace(Regex("<[^>]*>"), " ").replace(Regex("\\s+"), " ").trim()
    }

    private fun parseEpgTimestamp(timestampStr: String?, dateStr: String?): Long? {
        // Versuche Unix-Timestamp (Sekunden oder Millisekunden)
        timestampStr?.toLongOrNull()?.let { ts ->
            val result = if (ts < 1_000_000_000_000L) ts * 1000L else ts
            Log.d("IptvRepository", "Parsed timestamp: $timestampStr -> $result (${Date(result)})")
            return result
        }
        // Fallback: Parse Datum-String (z.B. "2023-01-01 12:00:00")
        dateStr?.let { date ->
            try {
                val format = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                format.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val result = format.parse(date)?.time
                Log.d("IptvRepository", "Parsed date string: $dateStr -> $result (${result?.let { Date(it) }})")
                return result
            } catch (_: Exception) { }
        }
        Log.w("IptvRepository", "Failed to parse timestamp: timestampStr=$timestampStr, dateStr=$dateStr")
        return null
    }

    // ── Sample fallback data ──────────────────────────────────
    private val sampleMovies = listOf(
        Movie("m1", "Inception", null, "2010", 8.8f, "A thief who steals corporate secrets...", "Action", ""),
        Movie("m2", "The Matrix", null, "1999", 8.7f, "A computer hacker learns...", "Action", ""),
        Movie("m3", "Interstellar", null, "2014", 8.6f, "A team of explorers travel...", "Sci-Fi", ""),
        Movie("m4", "The Dark Knight", null, "2008", 9.0f, "Batman faces the Joker...", "Action", ""),
        Movie("m5", "Pulp Fiction", null, "1994", 8.9f, "Various interconnected stories...", "Drama", ""),
        Movie("m6", "Fight Club", null, "1999", 8.8f, "An insomniac office worker...", "Drama", ""),
        Movie("m7", "Forrest Gump", null, "1994", 8.8f, "The presidencies of Kennedy...", "Drama", ""),
        Movie("m8", "Star Wars", null, "1977", 8.6f, "Luke Skywalker joins forces...", "Sci-Fi", "")
    )

    private val sampleSeries = listOf(
        Series("s1", "Breaking Bad", null, "2008", 9.5f, "A chemistry teacher...", "Drama",
            listOf(Season(1, listOf(Episode("e1", "Pilot", 1, ""), Episode("e2", "Cat's in the Bag", 2, ""))))),
        Series("s2", "Stranger Things", null, "2016", 8.7f, "Kids uncover a dark secret...", "Sci-Fi",
            listOf(Season(1, listOf(Episode("e1", "Chapter One", 1, ""))))),
        Series("s3", "Game of Thrones", null, "2011", 9.2f, "Nine noble families...", "Fantasy",
            listOf(Season(1, listOf(Episode("e1", "Winter Is Coming", 1, ""))))),
        Series("s4", "The Crown", null, "2016", 8.6f, "Queen Elizabeth II's reign...", "Drama",
            listOf(Season(1, listOf(Episode("e1", "Wolferton Splash", 1, ""))))),
        Series("s5", "The Office", null, "2005", 8.9f, "Mockumentary on a paper company...", "Comedy",
            listOf(Season(1, listOf(Episode("e1", "Pilot", 1, ""))))),
        Series("s6", "Black Mirror", null, "2011", 8.7f, "Twilight Zone-style stories...", "Sci-Fi",
            listOf(Season(1, listOf(Episode("e1", "The National Anthem", 1, "")))))
    )
}
