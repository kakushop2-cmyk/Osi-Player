package com.nativeplayer.data.model

data class Category(
    val name: String,
    val channels: List<Channel> = emptyList()
)
