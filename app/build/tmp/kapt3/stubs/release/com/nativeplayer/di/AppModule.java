package com.nativeplayer.di;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.room.Room;
import com.nativeplayer.data.api.XtreamApi;
import com.nativeplayer.data.repository.*;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\bH\u0007J\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0007J\b\u0010\u0013\u001a\u00020\u0010H\u0007J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0007J\u0012\u0010\u0017\u001a\u00020\u00162\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\t\u001a\u00020\u0004H\u0007J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\f\u001a\u00020\u0019H\u0007\u00a8\u0006\u001c"}, d2 = {"Lcom/nativeplayer/di/AppModule;", "", "()V", "provideAppDatabase", "Lcom/nativeplayer/data/repository/AppDatabase;", "context", "Landroid/content/Context;", "provideFavoriteDao", "Lcom/nativeplayer/data/repository/FavoriteDao;", "db", "provideFavoriteRepository", "Lcom/nativeplayer/data/repository/FavoriteRepository;", "dao", "provideIptvRepository", "Lcom/nativeplayer/data/repository/IptvRepository;", "okHttpClient", "Lokhttp3/OkHttpClient;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "provideOkHttpClient", "providePreferencesManager", "prefs", "Landroid/content/SharedPreferences;", "provideSharedPreferences", "provideWatchHistoryDao", "Lcom/nativeplayer/data/repository/WatchHistoryDao;", "provideWatchHistoryRepository", "Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "app_release"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class AppModule {
    @org.jetbrains.annotations.NotNull()
    public static final com.nativeplayer.di.AppModule INSTANCE = null;
    
    private AppModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final okhttp3.OkHttpClient provideOkHttpClient() {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final android.content.SharedPreferences provideSharedPreferences(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.PreferencesManager providePreferencesManager(@org.jetbrains.annotations.NotNull()
    android.content.SharedPreferences prefs) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.IptvRepository provideIptvRepository(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.AppDatabase provideAppDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.FavoriteDao provideFavoriteDao(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.WatchHistoryDao provideWatchHistoryDao(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.AppDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.FavoriteRepository provideFavoriteRepository(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteDao dao) {
        return null;
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final com.nativeplayer.data.repository.WatchHistoryRepository provideWatchHistoryRepository(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryDao dao) {
        return null;
    }
}