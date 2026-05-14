package com.nativeplayer.data.repository

import com.nativeplayer.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WatchHistoryRepository @Inject constructor(
    private val watchHistoryDao: WatchHistoryDao
) {
    fun getAll(): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getAll()
    fun getRecent(limit: Int = 10): Flow<List<WatchHistoryEntity>> = watchHistoryDao.getRecent(limit)
    suspend fun savePosition(entry: WatchHistoryEntity) = watchHistoryDao.upsert(entry)
    suspend fun getByItemId(itemId: String): WatchHistoryEntity? = watchHistoryDao.getByItemId(itemId)
    suspend fun remove(itemId: String) = watchHistoryDao.deleteByItemId(itemId)
    suspend fun clearAll() = watchHistoryDao.deleteAll()
}
