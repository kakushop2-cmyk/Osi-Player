# Category Visibility Management Feature - Design Specification

**Date**: 2025-05-16  
**Feature**: Selective category hiding in Live TV, Movies, and Series screens  
**Status**: Design Phase

---

## 1. Overview

Users should be able to selectively hide individual categories in three screens:
- **Live TV Screen**: Hide categories like "Sport", "News", "18+", etc.
- **Movies Screen**: Hide categories like "Action", "Drama", "Horror", etc.
- **Series Screen**: Hide categories like "Thriller", "Comedy", etc.

Categories are **dynamically loaded from the API server** and visibility settings are **persistently stored** in SharedPreferences.

---

## 2. Requirements

### Functional Requirements
- **FR1**: Display toggleable checkboxes for each category in Settings screen
- **FR2**: Persist visibility preferences to SharedPreferences
- **FR3**: Filter categories based on visibility settings when displaying screens
- **FR4**: Load all available categories from API for each screen type
- **FR5**: Support three independent category lists (Live TV, Movies, Series)

### Non-Functional Requirements
- **NFR1**: Settings changes take effect immediately on next screen load
- **NFR2**: Performance: Category filtering must not cause noticeable lag
- **NFR3**: Memory: Store only hidden category IDs (not all categories)

---

## 3. Data Model

### New Data Class: `CategoryVisibilitySettings`

```kotlin
data class CategoryVisibilitySettings(
    val hiddenCategoriesLiveTV: Set<String> = emptySet(),
    val hiddenCategoriesMovies: Set<String> = emptySet(),
    val hiddenCategoriesSeries: Set<String> = emptySet()
)
```

**Rationale**: 
- Use `Set<String>` for O(1) lookup performance
- Store only hidden categories (not visible ones) to save space
- Separate sets for each screen type for independent control

### Storage Format (JSON)

```json
{
  "hiddenCategoriesLiveTV": ["18+", "Adult"],
  "hiddenCategoriesMovies": ["Horror"],
  "hiddenCategoriesSeries": []
}
```

---

## 4. Architecture

### 4.1 Data Layer

**Extend `PreferencesManager`**:
```kotlin
fun saveCategoryVisibility(settings: CategoryVisibilitySettings): Boolean
fun getCategoryVisibility(): CategoryVisibilitySettings
```

**Implementation**:
- Serialize to JSON using existing serialization library (Gson/kotlinx.serialization)
- Store in SharedPreferences key: `"category_visibility_settings"`
- Return default (all categories visible) if not found

### 4.2 Domain Layer

**New ViewModel: `CategoryVisibilityViewModel`** (in ui/settings package)

**State**:
```kotlin
private val _visibilitySettings = MutableStateFlow<CategoryVisibilitySettings>
val visibilitySettings: StateFlow<CategoryVisibilitySettings>

private val _availableCategories = MutableStateFlow<Map<ScreenType, List<Category>>>
val availableCategories: StateFlow<Map<ScreenType, List<Category>>>
```

**Functions**:
```kotlin
fun loadAvailableCategories()  // Load from Live/Movies/Series ViewModels
fun toggleCategoryVisibility(screenType: ScreenType, categoryId: String)
fun save()
```

### 4.3 Presentation Layer

**UI Components**:
- New component: `CategoriesCheckboxGrid()` - 2-column grid with toggles
- New section in `SettingsScreen` for each screen type

**Integration in Screens**:
- `LiveScreen`, `MoviesScreen`, `SeriesScreen` - Add filtering logic in ViewModel

---

## 5. UI Design

### Settings Screen Layout

```
┌──────────────────────────────────────┐
│ 📺 Live TV Kategorien                │
├──────────────────────────────────────┤
│  ☑️ News          │  ☐ Sport         │
│  ☑️ Musik         │  ☐ 18+           │
│  ☑️ Kinder        │  ☐ Dokumentation │
│  ☑️ Spielshow     │  ☐ Sonstiges     │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│ 🎬 Filme Kategorien                  │
├──────────────────────────────────────┤
│  ☑️ Action        │  ☐ Drama         │
│  ☑️ Comedy        │  ☐ Horror        │
│  ☑️ Thriller      │  ☐ Science-Fi    │
│  ☑️ Animation     │  ☐ Sonstiges     │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│ 📺 Series Kategorien                 │
├──────────────────────────────────────┤
│  ☑️ Drama         │  ☐ Comedy        │
│  ☑️ Thriller      │  ☐ Animation     │
│  ☑️ Crime         │  ☐ Sonstiges     │
└──────────────────────────────────────┘
```

**Visual**:
- 2-column grid layout with checkboxes
- Consistent styling with existing Settings components
- Section headers with icons for each screen type
- Save button to persist changes

---

## 6. Data Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Settings Screen                          │
│                                                             │
│  1. User toggles checkbox                                  │
│         ↓                                                   │
│  2. CategoryVisibilityViewModel.toggleCategoryVisibility() │
│         ↓                                                   │
│  3. Update internal state                                  │
│         ↓                                                   │
│  4. User clicks Save                                       │
│         ↓                                                   │
│  5. ViewModel.save()                                       │
│         ↓                                                   │
│  6. PreferencesManager.saveCategoryVisibility()            │
│         ↓                                                   │
│  7. Serialize to JSON + SharedPreferences                  │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              Live/Movies/Series Screen                      │
│                                                             │
│  1. ViewModel.init() or Screen.recompose()                │
│         ↓                                                   │
│  2. Get categories from API (existing)                     │
│         ↓                                                   │
│  3. Load hidden categories from PreferencesManager         │
│         ↓                                                   │
│  4. Filter: keep categories NOT in hidden set              │
│         ↓                                                   │
│  5. Display filtered categories in bar                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 7. Implementation Details

### 7.1 Extend PlayerSettings or Create New Class?

**Decision**: Keep `CategoryVisibilitySettings` separate from `PlayerSettings` because:
- Different persistence strategy (loaded on app start vs. on-demand)
- Different validation rules
- Cleaner separation of concerns

### 7.2 Filtering Logic

**In each ViewModel** (LiveViewModel, MoviesViewModel, SeriesViewModel):

```kotlin
private fun filterCategories(
    allCategories: List<Category>,
    hiddenIds: Set<String>
): List<Category> {
    return allCategories.filter { category ->
        !hiddenIds.contains(category.id)
    }
}
```

### 7.3 Loading Strategy

**Option A** (Lazy Load):
- Load hidden categories only when Settings screen opens
- Faster app startup

**Option B** (Eager Load):
- Load hidden categories on app start
- Immediate filtering without delay

**Recommendation**: Option A (Lazy Load) - Load when entering Settings screen, cache result

### 7.4 Error Handling

- If JSON deserialization fails: Use default (all categories visible)
- If SharedPreferences.commit() fails: Log error, keep in-memory state
- If API doesn't return categories: Show empty category list

---

## 8. File Structure

```
app/src/main/java/com/nativeplayer/
├── data/
│   ├── model/
│   │   └── CategoryVisibilitySettings.kt  [NEW]
│   └── repository/
│       └── PreferencesManager.kt          [MODIFIED]
├── ui/
│   ├── settings/
│   │   ├── CategoryVisibilityViewModel.kt [NEW]
│   │   ├── SettingsScreen.kt              [MODIFIED]
│   │   └── components/
│   │       └── CategoriesCheckboxGrid.kt  [NEW]
│   ├── live/
│   │   └── LiveViewModel.kt               [MODIFIED]
│   ├── movies/
│   │   └── MoviesViewModel.kt             [MODIFIED]
│   └── series/
│       └── SeriesViewModel.kt             [MODIFIED]
```

---

## 9. Testing Strategy

### Unit Tests
- `CategoryVisibilityViewModelTest`: Toggle, save, load
- `PreferencesManagerTest`: Serialization/deserialization
- `FilterLogicTest`: Filtering categories correctly

### Integration Tests
- Settings screen: Save preferences and verify in LiveScreen
- Verify persistence across app restarts

### Manual Tests
- Toggle categories and verify immediate UI update
- Clear app data and verify defaults
- Toggle and navigate back/forth between screens

---

## 10. Success Criteria

✅ Users can see all available categories as checkboxes in Settings  
✅ Toggling a checkbox immediately updates UI state  
✅ Clicking Save persists preferences to SharedPreferences  
✅ Hidden categories disappear from category bars in Live/Movies/Series screens  
✅ Settings survive app restart  
✅ All 3 screens (Live TV, Movies, Series) work independently  
✅ No performance degradation in category filtering  

---

## 11. Acceptance Criteria

- [ ] Feature implemented and tested
- [ ] No regression in existing functionality
- [ ] Code follows project conventions (Kotlin style, Compose patterns)
- [ ] Tests pass (unit + integration)
- [ ] Design spec reviewed and approved

---

## 12. Timeline & Phases

**Phase 1** (Data Layer): 
- Create `CategoryVisibilitySettings` model
- Extend `PreferencesManager` with save/load functions

**Phase 2** (Domain Layer):
- Create `CategoryVisibilityViewModel`
- Implement load/toggle/save logic

**Phase 3** (UI Layer - Settings):
- Create `CategoriesCheckboxGrid` component
- Add sections to `SettingsScreen`

**Phase 4** (UI Layer - Screens):
- Add filtering logic to `LiveViewModel`, `MoviesViewModel`, `SeriesViewModel`
- Integrate filtering in screens

**Phase 5** (Testing & Polish):
- Unit + integration tests
- Error handling
- Performance verification

---

## 13. Dependencies & Libraries

- **Serialization**: Gson or kotlinx.serialization (existing)
- **Persistence**: SharedPreferences (existing)
- **Reactive**: Kotlin Flow (existing)
- **DI**: Hilt (existing)

---

## 14. Known Limitations & Future Enhancements

**Current**:
- Categories loaded from API only (not user-created)
- No "Select All / Deselect All" button (can be added later)
- No sync across devices (single device only)

**Future**:
- Cloud sync of visibility preferences
- Auto-refresh category list
- Favorite categories feature
- Category search/filter in Settings

---

## 15. Glossary

| Term | Definition |
|------|-----------|
| **ScreenType** | Enum: LIVE_TV, MOVIES, SERIES |
| **Category** | Content category from API (e.g., "Sport", "Drama") |
| **Visibility Settings** | User preferences for which categories to hide |
| **Filtering** | Process of removing hidden categories from display list |

---

**Document Status**: Ready for Implementation ✅
