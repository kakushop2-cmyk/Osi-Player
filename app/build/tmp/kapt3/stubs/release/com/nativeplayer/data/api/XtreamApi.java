package com.nativeplayer.data.api;

import com.nativeplayer.data.model.*;
import retrofit2.http.GET;
import retrofit2.http.Query;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0007J2\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ2\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ2\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ2\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ6\u0010\u0012\u001a\u00020\u00132\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\u0014\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J6\u0010\u0017\u001a\u00020\u00182\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\u0019\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J2\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\n0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\fJ6\u0010\u001b\u001a\u00020\u001c2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u00052\b\b\u0001\u0010\u001d\u001a\u00020\u0015H\u00a7@\u00a2\u0006\u0002\u0010\u0016J2\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\u001f0\t2\b\b\u0001\u0010\u0004\u001a\u00020\u00052\b\b\u0001\u0010\u0006\u001a\u00020\u00052\b\b\u0003\u0010\u000b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\f\u00a8\u0006 "}, d2 = {"Lcom/nativeplayer/data/api/XtreamApi;", "", "authenticate", "Lcom/nativeplayer/data/model/XtreamLoginResponse;", "username", "", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLiveCategories", "", "Lcom/nativeplayer/data/model/XtreamCategory;", "action", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getLiveStreams", "Lcom/nativeplayer/data/model/XtreamLiveStream;", "getSeries", "Lcom/nativeplayer/data/model/XtreamSeries;", "getSeriesCategories", "getSeriesInfo", "Lcom/nativeplayer/data/model/XtreamSeriesInfo;", "seriesId", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getShortEpg", "Lcom/nativeplayer/data/model/XtreamEpgResponse;", "streamId", "getVodCategories", "getVodInfo", "Lcom/nativeplayer/data/model/XtreamVodInfo;", "vodId", "getVodStreams", "Lcom/nativeplayer/data/model/XtreamVodStream;", "app_release"})
public abstract interface XtreamApi {
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object authenticate(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.XtreamLoginResponse> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLiveStreams(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamLiveStream>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getLiveCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamCategory>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getVodStreams(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamVodStream>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getVodInfo(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @retrofit2.http.Query(value = "vod_id")
    int vodId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.XtreamVodInfo> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getVodCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamCategory>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSeries(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamSeries>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSeriesCategories(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.nativeplayer.data.model.XtreamCategory>> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getSeriesInfo(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @retrofit2.http.Query(value = "series_id")
    int seriesId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.XtreamSeriesInfo> $completion);
    
    @retrofit2.http.GET(value = "player_api.php")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getShortEpg(@retrofit2.http.Query(value = "username")
    @org.jetbrains.annotations.NotNull()
    java.lang.String username, @retrofit2.http.Query(value = "password")
    @org.jetbrains.annotations.NotNull()
    java.lang.String password, @retrofit2.http.Query(value = "action")
    @org.jetbrains.annotations.NotNull()
    java.lang.String action, @retrofit2.http.Query(value = "stream_id")
    int streamId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.XtreamEpgResponse> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}