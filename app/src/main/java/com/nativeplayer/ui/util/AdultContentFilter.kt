package com.nativeplayer.ui.util

/**
 * Filters out adult content based on category name keywords.
 *
 * Uses case-insensitive substring matching against the category name
 * to detect adult-oriented categories like "XXX", "Adult", "Porn", etc.
 *
 * Usage:
 *   val safeMovies = AdultContentFilter.filterByCategory(movies) { it.category }
 *   val safeSeries = AdultContentFilter.filterByCategory(series) { it.category }
 *   val isAdult = AdultContentFilter.isAdultCategory("XXX 4K") // true
 */
object AdultContentFilter {

    private val adultKeywords = setOf(
        "xxx",
        "porn",
        "adult",
        "erotik",
        "18+",
        "sex",
        "fkk",
        "nur für erwachsene",
        "adults only"
    )

    /**
     * Checks if the given category name indicates adult content.
     * Match is case-insensitive and checks for substring presence.
     *
     * Examples:
     *   "XXX"       -> true
     *   "XXX 4K"    -> true
     *   "Adult"     -> true
     *   "Action"    -> false
     *   "Sci-Fi"    -> false
     */
    fun isAdultCategory(category: String): Boolean {
        val lower = category.lowercase()
        return adultKeywords.any { lower.contains(it) }
    }

    /**
     * Filters a list of items, removing those whose category matches
     * any adult keyword.
     *
     * @param items The list to filter (e.g. List<Movie>, List<Series>)
     * @param categoryExtractor Function that extracts the category string from an item
     * @return Filtered list without adult-category items
     */
    fun <T> filterByCategory(
        items: List<T>,
        categoryExtractor: (T) -> String
    ): List<T> {
        return items.filter { !isAdultCategory(categoryExtractor(it)) }
    }
}
