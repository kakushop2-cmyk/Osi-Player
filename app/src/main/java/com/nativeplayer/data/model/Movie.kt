package com.nativeplayer.data.model

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String? = null,
    val year: String = "",
    val rating: Float = 0f,
    val plot: String = "",
    val category: String = "All",
    val streamUrl: String = ""
)
