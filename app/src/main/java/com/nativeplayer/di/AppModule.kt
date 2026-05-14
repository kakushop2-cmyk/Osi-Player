package com.nativeplayer.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.nativeplayer.data.api.XtreamApi
import com.nativeplayer.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("nativeplayer_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun providePreferencesManager(prefs: SharedPreferences): PreferencesManager =
        PreferencesManager(prefs)

    @Provides
    @Singleton
    fun provideIptvRepository(
        okHttpClient: OkHttpClient,
        preferencesManager: PreferencesManager
    ): IptvRepository = IptvRepository(okHttpClient, preferencesManager)

    // ── Room Database ─────────────────────────────────────────
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "nativeplayer_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()

    @Provides
    @Singleton
    fun provideWatchHistoryDao(db: AppDatabase): WatchHistoryDao = db.watchHistoryDao()

    @Provides
    @Singleton
    fun provideFavoriteRepository(dao: FavoriteDao): FavoriteRepository = FavoriteRepository(dao)

    @Provides
    @Singleton
    fun provideWatchHistoryRepository(dao: WatchHistoryDao): WatchHistoryRepository = WatchHistoryRepository(dao)
}
