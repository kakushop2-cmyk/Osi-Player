package com.nativeplayer.data.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.nativeplayer.data.model.WatchHistoryEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bH\'J\u0018\u0010\u000b\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001c\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\r\u001a\u00020\u000eH\'J\u0016\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006\u0012"}, d2 = {"Lcom/nativeplayer/data/repository/WatchHistoryDao;", "", "deleteByItemId", "", "itemId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/nativeplayer/data/model/WatchHistoryEntity;", "getByItemId", "getRecent", "limit", "", "upsert", "entry", "(Lcom/nativeplayer/data/model/WatchHistoryEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface WatchHistoryDao {
    
    @androidx.room.Query(value = "SELECT * FROM watch_history ORDER BY lastWatchedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getAll();
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE type != \'live\' ORDER BY lastWatchedAt DESC LIMIT :limit")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.WatchHistoryEntity>> getRecent(int limit);
    
    @androidx.room.Query(value = "SELECT * FROM watch_history WHERE itemId = :itemId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByItemId(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.nativeplayer.data.model.WatchHistoryEntity> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object upsert(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.WatchHistoryEntity entry, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM watch_history WHERE itemId = :itemId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteByItemId(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}