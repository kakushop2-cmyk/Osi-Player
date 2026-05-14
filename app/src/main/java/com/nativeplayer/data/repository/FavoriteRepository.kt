package com.nativeplayer.data.repository

import com.nativeplayer.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepository @Inject constructor(
    private val favoriteDao: FavoriteDao
) {
    fun getAll(): Flow<List<FavoriteEntity>> = favoriteDao.getAll()
    fun getByType(type: String): Flow<List<FavoriteEntity>> = favoriteDao.getByType(type)
    fun isFavorite(itemId: String): Flow<Boolean> = favoriteDao.isFavorite(itemId)

    suspend fun toggle(item: FavoriteEntity) {
        if (favoriteDao.isFavoriteSync(item.itemId)) {
            favoriteDao.deleteByItemId(item.itemId)
        } else {
            favoriteDao.insert(item)
        }
    }

    suspend fun add(item: FavoriteEntity) = favoriteDao.insert(item)
    suspend fun remove(itemId: String) = favoriteDao.deleteByItemId(itemId)
    suspend fun isFavoriteSync(itemId: String): Boolean = favoriteDao.isFavoriteSync(itemId)
}
