package com.nativeplayer.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Settings : Screen("settings")
    data object Home : Screen("home")
    data object Live : Screen("live")
    data object Movies : Screen("movies")
    data object Series : Screen("series")
    data object Favorites : Screen("favorites")
    data object Resume : Screen("resume")
    data object MovieDetail : Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
    data object SeriesDetail : Screen("series_detail/{seriesId}") {
        fun createRoute(seriesId: String) = "series_detail/$seriesId"
    }
    data object Search : Screen("search")
    data object PlayerSettings : Screen("player_settings")
    data object FullScreenPlayer : Screen("player/{type}/{url}?contentId={contentId}&title={title}&resumePosition={resumePosition}&imageUrl={imageUrl}") {
        fun createRoute(
            url: String,
            type: String,
            contentId: String = "",
            title: String = "",
            resumePosition: Long = 0,
            imageUrl: String = ""
        ) =
            "player/$type/${java.net.URLEncoder.encode(url, "UTF-8")}" +
                "?contentId=${java.net.URLEncoder.encode(contentId, "UTF-8")}" +
                "&title=${java.net.URLEncoder.encode(title, "UTF-8")}" +
                "&resumePosition=$resumePosition" +
                "&imageUrl=${java.net.URLEncoder.encode(imageUrl, "UTF-8")}"
    }
}
