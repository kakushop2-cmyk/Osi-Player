package com.nativeplayer.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nativeplayer.data.model.FavoriteEntity
import com.nativeplayer.data.model.WatchHistoryEntity

@Database(entities = [FavoriteEntity::class, WatchHistoryEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
    abstract fun watchHistoryDao(): WatchHistoryDao
}
