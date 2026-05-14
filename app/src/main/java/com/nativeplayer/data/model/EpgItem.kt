package com.nativeplayer.data.model

data class EpgItem(
    val channelId: String,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val description: String = ""
)
