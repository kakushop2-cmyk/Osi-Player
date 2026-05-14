package com.nativeplayer.data.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.nativeplayer.data.model.FavoriteEntity;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bH\'J\u001c\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\b2\u0006\u0010\f\u001a\u00020\u0005H\'J\u0016\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0016\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\b2\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0016\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/nativeplayer/data/repository/FavoriteDao;", "", "deleteByItemId", "", "itemId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/nativeplayer/data/model/FavoriteEntity;", "getByType", "type", "insert", "favorite", "(Lcom/nativeplayer/data/model/FavoriteEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isFavorite", "", "isFavoriteSync", "app_debug"})
@androidx.room.Dao()
public abstract interface FavoriteDao {
    
    @androidx.room.Query(value = "SELECT * FROM favorites ORDER BY addedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.FavoriteEntity>> getAll();
    
    @androidx.room.Query(value = "SELECT * FROM favorites WHERE type = :type ORDER BY addedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.FavoriteEntity>> getByType(@org.jetbrains.annotations.NotNull()
    java.lang.String type);
    
    @androidx.room.Query(value = "SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Boolean> isFavorite(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId);
    
    @androidx.room.Query(value = "SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object isFavoriteSync(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.FavoriteEntity favorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM favorites WHERE itemId = :itemId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteByItemId(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}