package com.nativeplayer.data.repository;

import com.nativeplayer.data.model.WatchHistoryEntity;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006J\u0018\u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\fJ\u001c\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\b\b\u0002\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u000bH\u0086@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/nativeplayer/data/repository/WatchHistoryRepository;", "", "watchHistoryDao", "Lcom/nativeplayer/data/repository/WatchHistoryDao;", "(Lcom/nativeplayer/data/repository/WatchHistoryDao;)V", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "getByItemId", "itemId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getRecent", "limit", "", "remove", "", "savePosition", "entry", "(Lcom/nativeplayer/data/model/WatchHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"})
public final class WatchHistoryRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.WatchHistoryDao watchHistoryDao = null;
    
    @javax.inject.Inject()
    public WatchHistoryRepository(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.WatchHistoryDao watchHistoryDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getAll() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getRecent(int limit) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object savePosition(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.WatchHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getByItemId(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.WatchHistoryEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object remove(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}