package com.nativeplayer.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nativeplayer.data.model.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM watch_history ORDER BY lastWatchedAt DESC")
    fun getAll(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM watch_history WHERE type != 'live' ORDER BY lastWatchedAt DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<WatchHistoryEntity>>

    /** Liefert den neuesten Eintrag für eine itemId (z.B. die zuletzt gesehene Episode einer Serie). */
    @Query("SELECT * FROM watch_history WHERE itemId = :itemId ORDER BY lastWatchedAt DESC LIMIT 1")
    suspend fun getByItemId(itemId: String): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: WatchHistoryEntity)

    @Query("DELETE FROM watch_history WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: String)

    @Query("DELETE FROM watch_history")
    suspend fun deleteAll()
}
