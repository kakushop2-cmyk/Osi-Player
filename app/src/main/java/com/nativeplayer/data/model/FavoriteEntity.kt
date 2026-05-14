package com.nativeplayer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val type: String,
    val itemId: String,
    val title: String,
    val subtitle: String = "",
    val imageUrl: String = "",
    val streamUrl: String = "",
    val addedAt: Long = System.currentTimeMillis()
)
