# Category Settings Redesign — Navigation-Based Category Visibility Screens

**Date**: 2026-05-17  
**Feature**: Replace inline category visibility sections with tile-based navigation to dedicated screens  
**Status**: Design Phase  

---

## 1. Overview

The current Settings screen shows Live TV, Movies, and Series categories as inline sections with hardcoded example categories in a FlowRow grid. This redesign replaces those with **3 tiles** (styled like the existing "Daten" and "Player" cards) that navigate to **dedicated screens** listing the **actual API-loaded categories** from the Xtream server.

---

## 2. Motivation

- **Dynamic categories**: Current hardcoded lists ("News", "Sports", "Movies"…) are placeholders. Real categories come from the Xtream API and differ per provider.
- **Scalability**: Inline sections with FlowRow work for ~7 items but break with 20+ categories. A dedicated screen with LazyColumn handles any number.
- **UX clarity**: Tiles with chevrons signal "tap to configure" — consistent with the existing "Daten" and "Player" cards. The user knows they're entering a sub-screen.

---

## 3. Requirements

### Functional
- **FR1**: 3 tiles in Settings: "Live TV Kategorien", "Film Kategorien", "Serien Kategorien"
- **FR2**: Each tile navigates to a dedicated screen showing categories from the API
- **FR3**: Categories shown in a vertical list with checkboxes (checked = visible, unchecked = hidden)
- **FR4**: Toggling a checkbox persists immediately via `CategoryVisibilityViewModel`
- **FR5**: Screen has a TopAppBar with back navigation and a title matching the tile

### Non-Functional
- **NFR1**: Categories loaded from `IptvRepository` cached data (no extra API calls)
- **NFR2**: Visibility state managed by existing `CategoryVisibilityViewModel` (no changes needed)
- **NFR3**: Follow existing WarmTheme styling (WarmBackground, WarmSurface, etc.)

---

## 4. Architecture

### 4.1 Navigation

A single parameterized route in `Screen.kt`:

```kotlin
data object CategorySettings : Screen("category_settings/{type}") {
    fun createRoute(type: String) = "category_settings/$type"
}
```

Where `type` is one of: `"live"`, `"movies"`, `"series"`.

One composable entry in `NavGraph.kt`:

```kotlin
composable(
    route = Screen.CategorySettings.route,
    arguments = listOf(navArgument("type") { type = NavType.StringType })
) { backStackEntry ->
    val type = backStackEntry.arguments?.getString("type") ?: "live"
    CategorySettingsScreen(navController = navController, type = type)
}
```

### 4.2 Screen Components

**`CategorySettingsScreen.kt`** (NEW)

```
CategorySettingsScreen(navController, type)
├── TopAppBar (title based on type)
├── LazyColumn
│   └── items(categories)
│       └── Row
│           ├── Checkbox (checked = visible)
│           └── Text (category name)
```

### 4.3 Category Sources

| Type | Source | Method |
|------|--------|--------|
| `"live"` | `IptvRepository` | `getCachedCategories()` → `.map { it.name }` |
| `"movies"` | `IptvRepository` | `getOrderedMovieCategories()` |
| `"series"` | `IptvRepository` | `getOrderedSeriesCategories()` |

### 4.4 Category Data Access

The new `CategorySettingsScreen` needs the **list of available category names** from the API (not just the hidden set). This comes from `IptvRepository`'s cached data. We use a simple query function — no API calls needed since data is cached during app loading.

**Approach**: `CategorySettingsScreen` creates its own `CategorySettingsViewModel` (Hilt-injected) that wraps `IptvRepository`'s getters:

```kotlin
@HiltViewModel
class CategorySettingsViewModel @Inject constructor(
    private val repository: IptvRepository
) : ViewModel() {
    fun getCategoryNames(type: String): List<String> = when (type) {
        "live" -> repository.getCachedCategories().map { it.name }
        "movies" -> repository.getOrderedMovieCategories()
        "series" -> repository.getOrderedSeriesCategories()
        else -> emptyList()
    }
}
```

This ViewModel is stateless (reads from the singleton repository's cache). No StateFlow needed since categories don't change while the user is on the settings screen.

### 4.5 Visibility State

Data flows through the existing `CategoryVisibilityViewModel`:
- `categoryVisibility` StateFlow provides `CategoryVisibilitySettings(hiddenCategoriesLiveTV, hiddenCategoriesMovies, hiddenCategoriesSeries)`
- `toggleCategoryVisibility(type, categoryName)` persists changes immediately

---

## 5. UI Design

### Settings Screen — 3 Tiles

```
┌──────────────────────────────────────────────┐
│  Einstellungen                           ←   │
├──────────────────────────────────────────────┤
│  Accounts [...]                              │
│                                              │
│  ┌────────────────────────────────────┐      │
│  │ 🗑  Weiterschauen löschen        → │      │
│  └────────────────────────────────────┘      │
│                                              │
│  Player                                      │
│  ┌────────────────────────────────────┐      │
│  │ ⚙  Wiedergabe-Einstellungen      → │      │
│  └────────────────────────────────────┘      │
│                                              │
│  Kategorien                                  │
│  ┌────────────────────────────────────┐      │
│  │ 📺  Live TV Kategorien            → │      │  ← NEW
│  │     Kategorien ein-/ausblenden      │      │
│  ├────────────────────────────────────┤      │
│  │ 🎬  Film Kategorien               → │      │  ← NEW
│  │     Kategorien ein-/ausblenden      │      │
│  ├────────────────────────────────────┤      │
│  │ 📺  Serien Kategorien             → │      │  ← NEW
│  │     Kategorien ein-/ausblenden      │      │
│  └────────────────────────────────────┘      │
└──────────────────────────────────────────────┘
```

Each tile is a `Card` with:
- **Icon** (Live TV: `LiveTv` or `Tv`, Movies: `Movie` or `VideoLibrary`, Series: `VideoLibrary` or `Queue`)
- **Title** (e.g., "Live TV Kategorien")
- **Subtitle** ("Kategorien ein-/ausblenden")
- **Chevron right** → consistent with existing cards

### Category Settings Screen

```
┌──────────────────────────────────────────────┐
│  Live TV Kategorien                     ←   │  ← TopAppBar
├──────────────────────────────────────────────┤
│                                              │
│  ☑ Nachrichten                              │  ← Checkbox + Text
│  ☑ Sport                                    │
│  ☐ Unterhaltung                             │
│  ☑ Dokumentation                            │
│  ☐ Kinder                                   │
│  ☐ 18+                                      │
│  ☐ ...                                      │
│                                              │
│  (scrollable LazyColumn)                     │
└──────────────────────────────────────────────┘
```

- Checkbox **checked** = category visible (not hidden)
- Checkbox **unchecked** = category hidden
- Changes applied **immediately** on toggle (no separate save button needed)
- Empty state: "Keine Kategorien gefunden" if list is empty

---

## 6. Data Flow

```
SettingsScreen
  └─ Klick auf Tile → navController.navigate(CategorySettings.createRoute("live"))
       └─ CategorySettingsScreen(type = "live")
            ├─ IptvRepository.getCachedCategories()
            │     → listOf("Nachrichten", "Sport", "Unterhaltung", …)
            │
            ├─ CategoryVisibilityViewModel.categoryVisibility
            │     → hiddenCategoriesLiveTV = setOf("Unterhaltung")
            │
            └─ LazyColumn mit Checkboxen
                 └─ Toggle → viewModel.toggleCategoryVisibility("live", "Unterhaltung")
                      └─ PreferencesManager.saveCategoryVisibility(...)
                           └─ StateFlow aktualisiert UI sofort
```

---

## 7. Files Changed

| File | Action |
|------|--------|
| `ui/navigation/Screen.kt` | **MODIFY** — Add `CategorySettings` route |
| `ui/navigation/NavGraph.kt` | **MODIFY** — Add composable for route |
| `ui/settings/SettingsScreen.kt` | **MODIFY** — Replace 3 sections with 3 tiles |
| **NEW: `ui/settings/CategorySettingsScreen.kt`** | **CREATE** — New screen composable |
| **NEW: `ui/settings/CategorySettingsViewModel.kt`** | **CREATE** — Lightweight VM for category access |
| `ui/components/CategoriesCheckboxGrid.kt` | **DELETE** — Replaced by vertical list in new screen |
| `ui/viewmodel/CategoryVisibilityViewModel.kt` | **NO CHANGE** — Already handles all types |

---

## 8. Implementation Plan

### Step 1: Screen.kt — Add route
Add `CategorySettings` with `{type}` parameter.

### Step 2: NavGraph.kt — Add composable
Add `composable` for `Screen.CategorySettings.route` that reads `type` argument.

### Step 3: Create CategorySettingsViewModel.kt
New ViewModel that injects `IptvRepository` and provides `getCategoryNames(type)`.

### Step 4: Create CategorySettingsScreen.kt
New file with:
- `@Composable fun CategorySettingsScreen(navController, type)` 
- TopAppBar with back arrow and dynamic title
- LazyColumn with category items
- Each item: Checkbox (checked=visible) + category name
- Uses `CategorySettingsViewModel` for category list
- Uses `CategoryVisibilityViewModel` for toggle state

### Step 5: Modify SettingsScreen.kt
Replace:
- `LiveTVCategorySection()` composable
- `MoviesCategorySection()` composable  
- `SeriesCategorySection()` composable
- The `"Kategorien"` header/Spacer

With:
- `"Kategorien"` section header
- 3 card tiles that navigate to `CategorySettings.createRoute("live")`, etc.

### Step 6: Remove CategoriesCheckboxGrid.kt
Delete the file (no longer needed — replaced by inline item in `CategorySettingsScreen.kt`).

### Step 7: Update tests if needed
`CategoryVisibilityViewModelTest` should still pass unchanged. No new test file needed (UI-only changes).

---

## 9. Testing

- **Existing unit tests** (`CategoryVisibilityViewModelTest`): Should pass unchanged
- **Manual verification**: 
  - Tap each tile → correct categories load
  - Toggle checkboxes → visibility persists
  - Back navigation works
  - Empty account → graceful "no data" state

---

## 10. Success Criteria

✅ 3 tiles visible in Settings under "Kategorien" header  
✅ Each tile navigates to a dedicated screen showing API-loaded categories  
✅ Categories displayed vertically with checkboxes  
✅ Toggling checkboxes persists immediately  
✅ Back navigation returns to Settings  
✅ Empty/good state handled gracefully  

---

## 11. Acceptance Checklist

- [ ] Route `Screen.CategorySettings` added to `Screen.kt`
- [ ] Composable entry added to `NavGraph.kt`
- [ ] `CategorySettingsScreen.kt` created with LazyColumn + checkboxes
- [ ] `CategorySettingsViewModel.kt` created
- [ ] `SettingsScreen.kt` updated — old sections replaced by 3 tiles
- [ ] `CategoriesCheckboxGrid.kt` removed
- [ ] Compiles & runs without errors
- [ ] Category visibility still works in Live/Movies/Series screens
- [ ] Existing `CategoryVisibilityViewModelTest` still passes

---

**Document Status**: Draft
