package com.nativeplayer.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nativeplayer.ui.home.HomeScreen
import com.nativeplayer.ui.live.LiveScreen
import com.nativeplayer.ui.movies.MoviesScreen
import com.nativeplayer.ui.series.SeriesScreen
import com.nativeplayer.ui.favorites.FavoritesScreen
import com.nativeplayer.ui.resume.ResumeScreen
import com.nativeplayer.ui.movies.MovieDetailScreen
import com.nativeplayer.ui.series.SeriesDetailScreen
import com.nativeplayer.ui.player.PlayerScreen
import com.nativeplayer.ui.player.PlayerBarDemoScreen
import com.nativeplayer.ui.search.SearchScreen
import com.nativeplayer.ui.settings.SettingsScreen
import com.nativeplayer.ui.settings.PlayerSettingsScreen
import com.nativeplayer.ui.login.LoginScreen
import com.nativeplayer.ui.splash.SplashScreen
import com.nativeplayer.ui.splash.IntroScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Intro.route) {
            IntroScreen(navController = navController)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.PlayerSettings.route) {
            PlayerSettingsScreen(navController = navController)
        }

        composable(Screen.PlayerBarDemo.route) {
            PlayerBarDemoScreen(navController = navController)
        }

        // Home Hub - Main entry point
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        // Individual section screens
        composable(Screen.Live.route) {
            LiveScreen(navController = navController)
        }

        composable(Screen.Movies.route) {
            MoviesScreen(navController = navController)
        }

        composable(Screen.Series.route) {
            SeriesScreen(navController = navController)
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(navController = navController)
        }

        composable(Screen.Resume.route) {
            ResumeScreen(navController = navController)
        }

        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }

        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.StringType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
            MovieDetailScreen(navController = navController, movieId = movieId)
        }

        composable(
            route = Screen.SeriesDetail.route,
            arguments = listOf(navArgument("seriesId") { type = NavType.StringType })
        ) { backStackEntry ->
            val seriesId = backStackEntry.arguments?.getString("seriesId") ?: ""
            SeriesDetailScreen(navController = navController, seriesId = seriesId)
        }

        composable(
            route = Screen.FullScreenPlayer.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType },
                navArgument("contentId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("title") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("resumePosition") {
                    type = NavType.StringType
                    defaultValue = "0"
                },
                navArgument("imageUrl") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("season") {
                    type = NavType.StringType
                    defaultValue = "-1"
                },
                navArgument("episode") {
                    type = NavType.StringType
                    defaultValue = "-1"
                }
            )
        ) { backStackEntry ->
            val url = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("url") ?: "", "UTF-8"
            )
            val type = backStackEntry.arguments?.getString("type") ?: "movie"
            val contentId = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("contentId") ?: "", "UTF-8"
            )
            val contentTitle = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("title") ?: "", "UTF-8"
            )
            val resumePosition = backStackEntry.arguments?.getString("resumePosition")?.toLongOrNull() ?: 0L
            val imageUrl = java.net.URLDecoder.decode(
                backStackEntry.arguments?.getString("imageUrl") ?: "", "UTF-8"
            )
            val seasonNumber = backStackEntry.arguments?.getString("season")?.toIntOrNull() ?: -1
            val episodeNumber = backStackEntry.arguments?.getString("episode")?.toIntOrNull() ?: -1
            PlayerScreen(
                navController = navController,
                url = url,
                contentType = type,
                contentId = contentId,
                contentTitle = contentTitle,
                resumePosition = resumePosition,
                imageUrl = imageUrl,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber
            )
        }
    }
}
