package com.nativeplayer.data.model

import com.google.gson.annotations.SerializedName

// ── Xtream Codes Login Response ──────────────────────────────
data class XtreamLoginResponse(
    @SerializedName("user_info") val userInfo: UserInfo,
    @SerializedName("server_info") val serverInfo: ServerInfo
)

data class UserInfo(
    val username: String,
    val password: String,
    val auth: Int,
    val status: String,
    @SerializedName("exp_date") val expDate: String,
    @SerializedName("is_trial") val isTrial: String,
    @SerializedName("active_cons") val activeConnections: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("max_connections") val maxConnections: String,
    @SerializedName("allowed_output_formats") val allowedOutputFormats: List<String> = emptyList()
)

data class ServerInfo(
    val url: String,
    val port: String,
    @SerializedName("https_port") val httpsPort: String,
    @SerializedName("server_protocol") val serverProtocol: String,
    @SerializedName("rtmp_port") val rtmpPort: String,
    val timezone: String,
    @SerializedName("timestamp_now") val timestampNow: Long,
    @SerializedName("time_now") val timeNow: String
)

// ── Xtream Codes Live Stream ─────────────────────────────────
data class XtreamLiveStream(
    val num: Int,
    val name: String,
    @SerializedName("stream_type") val streamType: String,
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("stream_icon") val streamIcon: String?,
    @SerializedName("epg_channel_id") val epgChannelId: String?,
    @SerializedName("added") val added: String?,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("custom_sid") val customSid: String?,
    @SerializedName("tv_archive") val tvArchive: Int = 0,
    @SerializedName("direct_source") val directSource: String = "",
    @SerializedName("tv_archive_duration") val tvArchiveDuration: Int = 0
)

// ── Xtream Codes VOD Stream ──────────────────────────────────
data class XtreamVodStream(
    val num: Int,
    val name: String,
    @SerializedName("stream_type") val streamType: String,
    @SerializedName("stream_id") val streamId: Int,
    @SerializedName("stream_icon") val streamIcon: String?,
    val rating: String?,
    @SerializedName("rating_5based") val rating5Based: Double?,
    val added: String?,
    val plot: String?,
    val cast: String?,
    val director: String?,
    val genre: String?,
    val releaseDate: String?,
    val youtube_trailer: String?,
    @SerializedName("episode_run_time") val episodeRunTime: String?,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String?,
    @SerializedName("container_extension") val containerExtension: String?
)

// ── Xtream Codes Series ──────────────────────────────────────
data class XtreamSeries(
    val num: Int,
    val name: String,
    @SerializedName("series_id") val seriesId: Int,
    val cover: String?,
    val plot: String?,
    val cast: String?,
    val director: String?,
    val genre: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("last_modified") val lastModified: String?,
    val rating: String?,
    @SerializedName("rating_5based") val rating5Based: Double?,
    @SerializedName("backdrop_path") val backdropPath: List<String>?,
    @SerializedName("youtube_trailer") val youtubeTrailer: String?,
    @SerializedName("episode_run_time") val episodeRunTime: String?,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String?
)

// ── Xtream Series Info (seasons/episodes) ────────────────────
data class XtreamSeriesInfo(
    val seasons: List<XtreamSeason>?,
    val info: XtreamSeriesInfoDetails?,
    val episodes: Map<String, List<XtreamEpisode>>?
)

data class XtreamSeason(
    @SerializedName("air_date") val airDate: String?,
    @SerializedName("episode_count") val episodeCount: Int,
    val id: Int,
    val name: String,
    @SerializedName("season_number") val seasonNumber: Int,
    val cover: String?,
    @SerializedName("cover_big") val coverBig: String?,
    @SerializedName("cover_tmdb") val coverTmdb: String?
)

data class XtreamSeriesInfoDetails(
    val name: String,
    val title: String?,
    val year: String?,
    val cover: String?,
    val plot: String?,
    val cast: String?,
    val director: String?,
    val genre: String?,
    val rating: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("last_modified") val lastModified: String?,
    @SerializedName("episode_run_time") val episodeRunTime: String?
)

data class XtreamEpisode(
    val id: String,
    @SerializedName("episode_num") val episodeNum: Int,
    val title: String,
    @SerializedName("container_extension") val containerExtension: String?,
    val info: XtreamEpisodeInfo?,
    @SerializedName("custom_sid") val customSid: String?,
    @SerializedName("added") val added: String?,
    @SerializedName("season") val season: Int?,
    @SerializedName("direct_source") val directSource: String = ""
)

data class XtreamEpisodeInfo(
    @SerializedName("tmdb_id") val tmdbId: Int?,
    @SerializedName("release_date") val releaseDate: String?,
    val plot: String?,
    @SerializedName("duration_secs") val durationSecs: Int?,
    @SerializedName("duration") val duration: String?,
    @SerializedName("movie_image") val movieImage: String?,
    val bitrate: Int?,
    val rating: Double?,
    val season: Int?
)

// ── Xtream VOD Info (detailed info for a single VOD) ────────
data class XtreamVodInfo(
    val info: XtreamVodInfoDetails?,
    @SerializedName("movie_data") val movieData: XtreamMovieData?
)

data class XtreamVodInfoDetails(
    val name: String,
    val title: String?,
    val year: String?,
    val cover: String?,
    val plot: String?,
    val cast: String?,
    val director: String?,
    val genre: String?,
    val rating: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("episode_run_time") val episodeRunTime: String?,
    val duration: String?,
    val bitrate: Int?,
    @SerializedName("youtube_trailer") val youtubeTrailer: String?
)

data class XtreamMovieData(
    @SerializedName("stream_id") val streamId: Int,
    val name: String,
    val title: String?,
    val year: String?,
    val cover: String?,
    val plot: String?,
    val cast: String?,
    val director: String?,
    val genre: String?,
    val rating: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("container_extension") val containerExtension: String?
)

// ── Xtream EPG ───────────────────────────────────────────────
data class XtreamEpgResponse(
    @SerializedName("epg_listings") val epgListings: List<XtreamEpgListing> = emptyList()
)

data class XtreamEpgListing(
    val id: String? = null,
    @SerializedName("epg_id") val epgId: String? = null,
    val title: String? = null,
    @SerializedName("name") val name: String? = null,
    val lang: String? = null,
    val start: String? = null,
    @SerializedName("start_timestamp") val startTimestamp: String? = null,
    val end: String? = null,
    @SerializedName("stop_timestamp") val stopTimestamp: String? = null,
    val description: String? = null,
    @SerializedName("desc") val desc: String? = null,
    @SerializedName("channel_id") val channelId: String? = null,
    @SerializedName("has_archive") val hasArchive: Int = 0
)

// ── Xtream Category ──────────────────────────────────────────
data class XtreamCategory(
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category_name") val categoryName: String,
    @SerializedName("parent_id") val parentId: Int = 0
)

// ── Xtream API Response Wrappers ────────────────────────────
// Xtream Codes API v2 wraps stream lists in { "data": [...] }
data class XtreamDataResponse<T>(
    val data: List<T> = emptyList()
)

data class XtreamLiveStreamsResponse(
    @SerializedName("data") val data: List<XtreamLiveStream> = emptyList()
)

data class XtreamVodStreamsResponse(
    @SerializedName("data") val data: List<XtreamVodStream> = emptyList()
)

data class XtreamSeriesListResponse(
    @SerializedName("data") val data: List<XtreamSeries> = emptyList()
)

data class XtreamCategoriesResponse(
    @SerializedName("data") val data: List<XtreamCategory> = emptyList()
)
