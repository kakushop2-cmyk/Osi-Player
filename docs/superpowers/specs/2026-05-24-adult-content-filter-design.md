# Adult Content Filter for HomeScreen Hero Trailer

**Date:** 2026-05-24
**Status:** Approved

## Problem

Der HeroTrailer auf dem HomeScreen zeigt potenziell XXX/Adult-Filme aus dem Xtream-Codes-Backend an. Es gibt keine Filterung nach Kategorie, sodass alle Filme — auch aus Adult-Kategorien wie "XXX", "Adult", "Erotik" — im Hero-Karussell, in "Für Dich", "Top Bewertet" und den Serien-Kacheln erscheinen.

## Solution

Eine zentrale `AdultContentFilter`-Utility, die an allen relevanten Stellen im `HomeViewModel` Adult-Inhalte anhand des Kategorienamens herausfiltert.

### Component: AdultContentFilter

**File:** `app/src/main/java/com/nativeplayer/ui/util/AdultContentFilter.kt`

```kotlin
object AdultContentFilter {
    private val adultKeywords = setOf(
        "xxx", "porn", "adult", "erotik", "18+",
        "sex", "fkk", "nur für erwachsene", "adults only"
    )

    fun isAdultCategory(category: String): Boolean {
        val lower = category.lowercase()
        return adultKeywords.any { lower.contains(it) }
    }

    fun <T> filterByCategory(items: List<T>, categoryExtractor: (T) -> String): List<T> {
        return items.filter { !isAdultCategory(categoryExtractor(it)) }
    }
}
```

**Properties:**
- `adultKeywords`: Case-insensitive String-Set mit deutschen und englischen Adult-Keywords
- `isAdultCategory()`: Prüft, ob ein Kategoriename ein Adult-Keyword enthält (Teilstring-Match)
- `filterByCategory()`: Generischer Filter für `List<Movie>` und `List<Series>`

### Integration: HomeViewModel (5 Änderungen)

| Methode | Date | Change |
|---------|------|--------|
| `computeHeroCategories()` | Line 211 | `filterByCategory(movies) { it.category }` |
| `computeLatestContent()` | Line 188 | `filterByCategory(allMovies) { it.category }` |
| `loadFuerDichPool()` | Line 282 | `filterByCategory(pool) { it.category }` |
| `computeTopRatedMovies()` | Line 324 | `filterByCategory(movies) { it.category }` |
| `computeSeriesGroups()` | Line 352 | `filterByCategory(series) { it.category }` |

### Files Modified

1. **NEW:** `app/src/main/java/com/nativeplayer/ui/util/AdultContentFilter.kt`
2. **MODIFIED:** `app/src/main/java/com/nativeplayer/ui/home/HomeViewModel.kt`

### Testing

- `isAdultCategory("XXX")` → `true`
- `isAdultCategory("XXX 4K")` → `true`
- `isAdultCategory("Adult Movies")` → `true`
- `isAdultCategory("Action")` → `false`
- `isAdultCategory("Sci-Fi")` → `false`
- `filterByCategory` with mixed movies returns only non-adult
