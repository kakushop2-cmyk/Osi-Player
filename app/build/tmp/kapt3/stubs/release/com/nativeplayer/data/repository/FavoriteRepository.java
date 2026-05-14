package com.nativeplayer.data.repository;

import com.nativeplayer.data.model.FavoriteEntity;
import kotlinx.coroutines.flow.Flow;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0012\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000bJ\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\f0\u000b2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000b2\u0006\u0010\u0012\u001a\u00020\u000fJ\u0016\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u000fH\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/nativeplayer/data/repository/FavoriteRepository;", "", "favoriteDao", "Lcom/nativeplayer/data/repository/FavoriteDao;", "(Lcom/nativeplayer/data/repository/FavoriteDao;)V", "add", "", "item", "Lcom/nativeplayer/data/model/FavoriteEntity;", "(Lcom/nativeplayer/data/model/FavoriteEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "getByType", "type", "", "isFavorite", "", "itemId", "isFavoriteSync", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "remove", "toggle", "app_release"})
public final class FavoriteRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.nativeplayer.data.repository.FavoriteDao favoriteDao = null;
    
    @javax.inject.Inject()
    public FavoriteRepository(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.repository.FavoriteDao favoriteDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.FavoriteEntity>> getAll() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.nativeplayer.data.model.FavoriteEntity>> getByType(@org.jetbrains.annotations.NotNull()
    java.lang.String type) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> isFavorite(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object toggle(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.FavoriteEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object add(@org.jetbrains.annotations.NotNull()
    com.nativeplayer.data.model.FavoriteEntity item, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object remove(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object isFavoriteSync(@org.jetbrains.annotations.NotNull()
    java.lang.String itemId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}