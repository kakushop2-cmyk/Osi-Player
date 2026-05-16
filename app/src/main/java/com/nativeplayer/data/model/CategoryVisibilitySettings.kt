package com.nativeplayer.data.model

data class CategoryVisibilitySettings(
    val hiddenCategoriesLiveTV: Set<String> = emptySet(),
    val hiddenCategoriesMovies: Set<String> = emptySet(),
    val hiddenCategoriesSeries: Set<String> = emptySet()
)
