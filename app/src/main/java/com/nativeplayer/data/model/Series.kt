package com.nativeplayer.data.model

data class Series(
    val id: String,
    val title: String,
    val posterUrl: String? = null,
    val year: String = "",
    val rating: Float = 0f,
    val plot: String = "",
    val category: String = "All",
    val seasons: List<Season> = emptyList()
)

data class Season(
    val number: Int,
    val episodes: List<Episode>
)

data class Episode(
    val id: String,
    val title: String,
    val number: Int,
    val streamUrl: String,
    val duration: String = ""
)
