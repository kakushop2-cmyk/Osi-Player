package com.nativeplayer.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nativeplayer.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE type = :type ORDER BY addedAt DESC")
    fun getByType(type: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    fun isFavorite(itemId: String): Flow<Boolean>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE itemId = :itemId)")
    suspend fun isFavoriteSync(itemId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE itemId = :itemId")
    suspend fun deleteByItemId(itemId: String)
}
