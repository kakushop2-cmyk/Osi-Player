package com.nativeplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watch_history")
data class WatchHistoryEntity(
    @PrimaryKey val id: String,
    val type: String,
    val itemId: String,
    val title: String,
    val imageUrl: String = "",
    val streamUrl: String = "",
    val position: Long = 0,
    val duration: Long = 0,
    val seasonNumber: Int = -1,
    val episodeNumber: Int = -1,
    val episodeTitle: String = "",
    val lastWatchedAt: Long = System.currentTimeMillis()
)
