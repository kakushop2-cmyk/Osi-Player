package com.nativeplayer.data.model

data class Channel(
    val id: String,
    val name: String,
    val url: String,
    val logo: String? = null,
    val group: String = "All",
    val epgId: String? = null
)
