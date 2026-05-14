package com.nativeplayer.data.repository;

import com.nativeplayer.data.api.XtreamApi;
import com.nativeplayer.data.model.*;
import com.nativeplayer.data.parser.M3uParser;
import kotlinx.coroutines.Dispatchers;
import android.util.Log;
import java.util.Date;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\t\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u0010H\u0002J\u001c\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\u0006\u0010\u001f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010 J\u001c\u0010!\u001a\b\u0012\u0004\u0012\u00020\u000b0\b2\u0006\u0010\"\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010 J\u0010\u0010#\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u0015H\u0002J\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\t0\bJ\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u000b0\bJ\u001c\u0010\'\u001a\b\u0012\u0004\u0012\u00020(0\b2\u0006\u0010)\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010 J\f\u0010*\u001a\b\u0012\u0004\u0012\u00020\r0\bJ\u0018\u0010+\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\b0\u000fJ\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00120\bJ\u0018\u0010-\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\b0\u000fJ\u0006\u0010.\u001a\u00020\u0017J\u0016\u0010/\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u00100J\u0018\u00101\u001a\u0004\u0018\u00010\u00122\u0006\u00102\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010 J\u0018\u00103\u001a\u0004\u0018\u00010\r2\u0006\u00104\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010 J#\u00105\u001a\u0004\u0018\u0001062\b\u00107\u001a\u0004\u0018\u00010\u00102\b\u00108\u001a\u0004\u0018\u00010\u0010H\u0002\u00a2\u0006\u0002\u00109R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\b0\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R \u0010\u0013\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\b0\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00120\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006:"}, d2 = {"Lcom/nativeplayer/data/repository/IptvRepository;", "", "okHttpClient", "Lokhttp3/OkHttpClient;", "preferencesManager", "Lcom/nativeplayer/data/repository/PreferencesManager;", "(Lokhttp3/OkHttpClient;Lcom/nativeplayer/data/repository/PreferencesManager;)V", "cachedCategories", "", "Lcom/nativeplayer/data/model/Category;", "cachedChannels", "Lcom/nativeplayer/data/model/Channel;", "cachedMovies", "Lcom/nativeplayer/data/model/Movie;", "cachedMoviesByCategory", "", "", "cachedSeries", "Lcom/nativeplayer/data/model/Series;", "cachedSeriesByCategory", "currentAccount", "Lcom/nativeplayer/data/model/Account;", "isDataLoaded", "", "sampleMovies", "sampleSeries", "xtreamApi", "Lcom/nativeplayer/data/api/XtreamApi;", "decodeEpgText", "text", "fetchPlaylist", "url", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchPlaylistFromString", "content", "getApi", "account", "getCachedCategories", "getCachedChannels", "getEpgForChannel", "Lcom/nativeplayer/data/model/EpgItem;", "channelId", "getMovies", "getMoviesByCategory", "getSeries", "getSeriesByCategory", "isLoaded", "loadAllFromXtream", "(Lcom/nativeplayer/data/model/Account;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "loadSeriesInfo", "seriesId", "loadVodInfo", "vodId", "parseEpgTimestamp", "", "timestampStr", "dateStr", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Long;", "app_release"})
public final class IptvRepository {
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient okHttpClient = null;
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.PreferencesManager preferencesManager = null;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.nativeplayer.data.model.Channel> cachedChannels;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.nativeplayer.data.model.Category> cachedCategories;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.nativeplayer.data.model.Movie> cachedMovies;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.String, ? extends java.util.List<com.nativeplayer.data.model.Movie>> cachedMoviesByCategory;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.nativeplayer.data.model.Series> cachedSeries;
    @org.jetbrains.annotations.NotNull()
    private java.util.Map<java.lang.String, ? extends java.util.List<com.nativeplayer.data.model.Series>> cachedSeriesByCategory;
    private boolean isDataLoaded = false;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.data.api.XtreamApi xtreamApi;
    @org.jetbrains.annotations.Nullable()
    private com.nativeplayer.data.model.Account currentAccount;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nativeplayer.data.model.Movie> sampleMovies = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.nativeplayer.data.model.Series> sampleSeries = null;
    
    @javax.inject.Inject()
    public IptvRepository(@org.jetbrains.annotations.NotNull()
    okhttp3.OkHttpClient okHttpClient, @org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.PreferencesManager preferencesManager) {
        super();
    }
    
    private final com.nativeplayer.data.api.XtreamApi getApi(com.nativeplayer.data.model.Account account) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadAllFromXtream(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.Account account, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadSeriesInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String seriesId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.Series> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object loadVodInfo(@org.jetbrains.annotations.NotNull()
    java.lang.String vodId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.Movie> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchPlaylist(@org.jetbrains.annotations.NotNull()
    java.lang.String url, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.Channel>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object fetchPlaylistFromString(@org.jetbrains.annotations.NotNull()
    java.lang.String content, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.Channel>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Channel> getCachedChannels() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Category> getCachedCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Movie> getMovies() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Movie>> getMoviesByCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.nativeplayer.data.model.Series> getSeries() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.util.List<com.nativeplayer.data.model.Series>> getSeriesByCategory() {
        return null;
    }
    
    public final boolean isLoaded() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getEpgForChannel(@org.jetbrains.annotations.NotNull()
    java.lang.String channelId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.EpgItem>> $completion) {
        return null;
    }
    
    private final java.lang.String decodeEpgText(java.lang.String text) {
        return null;
    }
    
    private final java.lang.Long parseEpgTimestamp(java.lang.String timestampStr, java.lang.String dateStr) {
        return null;
    }
}