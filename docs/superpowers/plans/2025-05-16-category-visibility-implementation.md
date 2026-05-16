# Category Visibility Management Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement selective category hiding feature allowing users to hide individual categories in Live TV, Movies, and Series screens through Settings UI.

**Architecture:** 
- Data layer: `CategoryVisibilitySettings` model + `PreferencesManager` extension for persistence
- Domain layer: `CategoryVisibilityViewModel` for state management
- Presentation layer: Settings UI components + filtering logic in Live/Movies/Series ViewModels
- Storage: JSON serialization via Gson to SharedPreferences

**Tech Stack:** 
- Kotlin + Compose UI
- Hilt dependency injection
- Kotlin Flow for reactive state
- Gson for JSON serialization
- SharedPreferences for persistence

---

## File Structure Overview

**New Files:**
- `app/src/main/java/com/nativeplayer/data/model/CategoryVisibilitySettings.kt`
- `app/src/main/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModel.kt`
- `app/src/main/java/com/nativeplayer/ui/settings/components/CategoriesCheckboxGrid.kt`
- `app/src/test/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModelTest.kt`

**Modified Files:**
- `app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt`
- `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt`
- `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt`
- `app/src/main/java/com/nativeplayer/ui/movies/MoviesViewModel.kt`
- `app/src/main/java/com/nativeplayer/ui/series/SeriesViewModel.kt`

---

## Task 1: Create CategoryVisibilitySettings Data Model

**Files:**
- Create: `app/src/main/java/com/nativeplayer/data/model/CategoryVisibilitySettings.kt`

- [ ] **Step 1: Create the data class**

```kotlin
package com.nativeplayer.data.model

data class CategoryVisibilitySettings(
    val hiddenCategoriesLiveTV: Set<String> = emptySet(),
    val hiddenCategoriesMovies: Set<String> = emptySet(),
    val hiddenCategoriesSeries: Set<String> = emptySet()
)
```

- [ ] **Step 2: Commit**

```bash
cd /home/osi61/nativeplayer
git add app/src/main/java/com/nativeplayer/data/model/CategoryVisibilitySettings.kt
git commit -m "feat: add CategoryVisibilitySettings data model"
```

---

## Task 2: Extend PreferencesManager with Visibility Persistence

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt`

- [ ] **Step 1: Read current PreferencesManager file**

```bash
cd /home/osi61/nativeplayer
cat app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt | head -50
```

Expected: File opens and shows existing SharedPreferences initialization and methods.

- [ ] **Step 2: Add imports at the top of PreferencesManager**

After the existing imports, add:
```kotlin
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import android.util.Log
```

- [ ] **Step 3: Add private Gson instance and constants**

Inside the `PreferencesManager` class, add these properties:

```kotlin
companion object {
    private const val CATEGORY_VISIBILITY_KEY = "category_visibility_settings"
    private const val TAG = "PreferencesManager"
}

private val gson = Gson()
```

- [ ] **Step 4: Add save method for category visibility**

Add this method to the `PreferencesManager` class:

```kotlin
fun saveCategoryVisibility(settings: CategoryVisibilitySettings): Boolean {
    return try {
        val json = gson.toJson(settings)
        Log.d(TAG, "Saving category visibility: $json")
        sharedPreferences.edit().putString(CATEGORY_VISIBILITY_KEY, json).commit()
    } catch (e: Exception) {
        Log.e(TAG, "Error saving category visibility", e)
        false
    }
}
```

- [ ] **Step 5: Add load method for category visibility**

Add this method to the `PreferencesManager` class:

```kotlin
fun getCategoryVisibility(): CategoryVisibilitySettings {
    return try {
        val json = sharedPreferences.getString(CATEGORY_VISIBILITY_KEY, null)
        if (json != null) {
            Log.d(TAG, "Loading category visibility: $json")
            gson.fromJson(json, CategoryVisibilitySettings::class.java)
        } else {
            Log.d(TAG, "No category visibility settings found, using defaults")
            CategoryVisibilitySettings()
        }
    } catch (e: JsonSyntaxException) {
        Log.e(TAG, "Error parsing category visibility settings", e)
        CategoryVisibilitySettings()  // Return defaults on parse error
    }
}
```

- [ ] **Step 6: Verify imports include CategoryVisibilitySettings**

At the top of PreferencesManager.kt, add:
```kotlin
import com.nativeplayer.data.model.CategoryVisibilitySettings
```

- [ ] **Step 7: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 8: Commit**

```bash
git add app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt
git commit -m "feat: add category visibility persistence to PreferencesManager"
```

---

## Task 3: Create CategoryVisibilityViewModel

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModel.kt`

- [ ] **Step 1: Create the ViewModel class**

```kotlin
package com.nativeplayer.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.nativeplayer.data.model.CategoryVisibilitySettings
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryVisibilityViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    companion object {
        private const val TAG = "CategoryVisibilityViewModel"
    }

    private val _visibilitySettings = MutableStateFlow(
        preferencesManager.getCategoryVisibility()
    )
    val visibilitySettings: StateFlow<CategoryVisibilitySettings> = _visibilitySettings.asStateFlow()

    fun toggleCategoryVisibility(screenType: String, categoryName: String) {
        val current = _visibilitySettings.value
        val updated = when (screenType) {
            "LIVE_TV" -> {
                val hidden = current.hiddenCategoriesLiveTV.toMutableSet()
                if (hidden.contains(categoryName)) {
                    hidden.remove(categoryName)
                } else {
                    hidden.add(categoryName)
                }
                current.copy(hiddenCategoriesLiveTV = hidden)
            }
            "MOVIES" -> {
                val hidden = current.hiddenCategoriesMovies.toMutableSet()
                if (hidden.contains(categoryName)) {
                    hidden.remove(categoryName)
                } else {
                    hidden.add(categoryName)
                }
                current.copy(hiddenCategoriesMovies = hidden)
            }
            "SERIES" -> {
                val hidden = current.hiddenCategoriesSeries.toMutableSet()
                if (hidden.contains(categoryName)) {
                    hidden.remove(categoryName)
                } else {
                    hidden.add(categoryName)
                }
                current.copy(hiddenCategoriesSeries = hidden)
            }
            else -> return
        }
        _visibilitySettings.value = updated
        Log.d(TAG, "Toggled category '$categoryName' in $screenType. Hidden: ${updated.hiddenCategoriesLiveTV}")
    }

    fun isCategoryHidden(screenType: String, categoryName: String): Boolean {
        return when (screenType) {
            "LIVE_TV" -> _visibilitySettings.value.hiddenCategoriesLiveTV.contains(categoryName)
            "MOVIES" -> _visibilitySettings.value.hiddenCategoriesMovies.contains(categoryName)
            "SERIES" -> _visibilitySettings.value.hiddenCategoriesSeries.contains(categoryName)
            else -> false
        }
    }

    fun save() {
        viewModelScope.launch(Dispatchers.IO) {
            val success = preferencesManager.saveCategoryVisibility(_visibilitySettings.value)
            if (success) {
                Log.d(TAG, "Category visibility settings saved successfully")
            } else {
                Log.e(TAG, "Failed to save category visibility settings")
            }
        }
    }

    fun reset() {
        _visibilitySettings.value = CategoryVisibilitySettings()
    }
}
```

- [ ] **Step 2: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModel.kt
git commit -m "feat: add CategoryVisibilityViewModel for settings management"
```

---

## Task 4: Create CategoriesCheckboxGrid Compose Component

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/CategoriesCheckboxGrid.kt`

- [ ] **Step 1: Create the Compose component**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun CategoriesCheckboxGrid(
    title: String,
    icon: ImageVector,
    categories: List<String>,
    hiddenCategories: Set<String>,
    onCategoryToggle: (categoryName: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = WarmGold,
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                title,
                color = WarmGold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        // Card wrapper
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(WarmSurface, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = WarmSurface)
        ) {
            // Grid of checkboxes (2 columns)
            val gridItems = categories.chunked(2)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                gridItems.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { category ->
                            CategoryCheckboxItem(
                                category = category,
                                isHidden = hiddenCategories.contains(category),
                                onToggle = { onCategoryToggle(category) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Filler if odd number of items
                        if (row.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryCheckboxItem(
    category: String,
    isHidden: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onToggle() }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Checkbox(
            checked = !isHidden,  // Show is checked, hidden is unchecked
            onCheckedChange = { onToggle() },
            modifier = Modifier.size(20.dp)
        )
        Text(
            category,
            color = if (isHidden) WarmTextSecondary else WarmTextPrimary,
            fontSize = 13.sp,
            maxLines = 1
        )
    }
}
```

- [ ] **Step 2: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/components/CategoriesCheckboxGrid.kt
git commit -m "feat: add CategoriesCheckboxGrid Compose component"
```

---

## Task 5: Add Category Visibility Section to SettingsScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt`

- [ ] **Step 1: Read current SettingsScreen (lines 1-100)**

```bash
cd /home/osi61/nativeplayer
head -100 app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt
```

Expected: Shows existing imports and initial Scaffold structure.

- [ ] **Step 2: Add imports for new components**

After existing imports in SettingsScreen.kt, add:

```kotlin
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Videocam
import androidx.hilt.navigation.compose.hiltViewModel
import com.nativeplayer.ui.settings.CategoryVisibilityViewModel
import com.nativeplayer.ui.settings.components.CategoriesCheckboxGrid
```

- [ ] **Step 3: Add CategoryVisibilityViewModel to SettingsScreen composable**

Modify the `SettingsScreen` function signature to include:

```kotlin
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel(),
    categoryVisibilityViewModel: CategoryVisibilityViewModel = hiltViewModel()  // Add this line
) {
```

- [ ] **Step 4: Add state collection in SettingsScreen**

Inside the SettingsScreen function, after existing state declarations, add:

```kotlin
val visibilitySettings by categoryVisibilityViewModel.visibilitySettings.collectAsState()
```

- [ ] **Step 5: Find the LazyColumn location in SettingsScreen**

Read lines 69-150 to find where to add the new sections:

```bash
cd /home/osi61/nativeplayer
sed -n '69,150p' app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt
```

Expected: Shows LazyColumn with items. Add new sections after the accounts section (before the closing of LazyColumn).

- [ ] **Step 6: Add category visibility sections to LazyColumn**

Before the closing `}` of the LazyColumn (typically after all account items), add these items:

```kotlin
            // Category Visibility Sections Divider
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Kategorien",
                    color = WarmGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            // Live TV Categories Section
            item {
                val liveCategories = listOf(
                    "News", "Sport", "Musik", "Kinder",
                    "Spielshow", "18+", "Dokumentation", "Sonstiges"
                )
                CategoriesCheckboxGrid(
                    title = "📺 Live TV Kategorien",
                    icon = Icons.Filled.LiveTv,
                    categories = liveCategories,
                    hiddenCategories = visibilitySettings.hiddenCategoriesLiveTV,
                    onCategoryToggle = { categoryName ->
                        categoryVisibilityViewModel.toggleCategoryVisibility("LIVE_TV", categoryName)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Movies Categories Section
            item {
                val movieCategories = listOf(
                    "Action", "Comedy", "Drama", "Horror",
                    "Thriller", "Animation", "Science-Fi", "Sonstiges"
                )
                CategoriesCheckboxGrid(
                    title = "🎬 Filme Kategorien",
                    icon = Icons.Filled.Movie,
                    categories = movieCategories,
                    hiddenCategories = visibilitySettings.hiddenCategoriesMovies,
                    onCategoryToggle = { categoryName ->
                        categoryVisibilityViewModel.toggleCategoryVisibility("MOVIES", categoryName)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Series Categories Section
            item {
                val seriesCategories = listOf(
                    "Drama", "Comedy", "Thriller", "Animation",
                    "Crime", "Romance", "Sci-Fi", "Sonstiges"
                )
                CategoriesCheckboxGrid(
                    title = "📺 Series Kategorien",
                    icon = Icons.Filled.Videocam,
                    categories = seriesCategories,
                    hiddenCategories = visibilitySettings.hiddenCategoriesSeries,
                    onCategoryToggle = { categoryName ->
                        categoryVisibilityViewModel.toggleCategoryVisibility("SERIES", categoryName)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Save Button
            item {
                Button(
                    onClick = {
                        categoryVisibilityViewModel.save()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = WarmGold)
                ) {
                    Text(
                        "Kategorien speichern",
                        color = WarmSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
```

- [ ] **Step 7: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 8: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt
git commit -m "feat: add category visibility sections to SettingsScreen"
```

---

## Task 6: Add Filtering Logic to LiveViewModel

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt`

- [ ] **Step 1: Read current LiveViewModel to understand structure**

Already done in context - file has `categories` StateFlow and `loadData()` method.

- [ ] **Step 2: Add PreferencesManager import (if not already present)**

Check if already imported:
```bash
grep "PreferencesManager" /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt
```

If not present, add to imports:
```kotlin
import com.nativeplayer.data.repository.PreferencesManager
```

- [ ] **Step 3: Add hidden categories state to LiveViewModel**

In the LiveViewModel class, after the existing state definitions, add:

```kotlin
private val _hiddenCategories = MutableStateFlow<Set<String>>(emptySet())
val hiddenCategories: StateFlow<Set<String>> = _hiddenCategories.asStateFlow()

private val _displayedCategories = MutableStateFlow<List<Category>>(emptyList())
val displayedCategories: StateFlow<List<Category>> = _displayedCategories.asStateFlow()
```

- [ ] **Step 4: Modify loadData() to filter categories**

Replace the existing `loadData()` method with:

```kotlin
fun loadData() {
    val allCategories = repository.getCachedCategories()
    _categories.value = allCategories
    
    // Load hidden categories
    val settings = preferencesManager.getCategoryVisibility()
    _hiddenCategories.value = settings.hiddenCategoriesLiveTV
    
    // Filter categories
    val filtered = allCategories.filter { category ->
        !settings.hiddenCategoriesLiveTV.contains(category.name)
    }
    _displayedCategories.value = filtered
    
    // Select first visible category if available
    if (filtered.isNotEmpty() && _selectedCategory.value == null) {
        _selectedCategory.value = filtered.first()
    } else if (filtered.isNotEmpty() && !filtered.contains(_selectedCategory.value)) {
        // If previously selected category is now hidden, select first visible
        _selectedCategory.value = filtered.first()
    }
}
```

- [ ] **Step 5: Update selectCategory to use only visible categories**

Modify the `selectCategory()` method to handle only visible categories:

```kotlin
fun selectCategory(category: Category) {
    if (!_hiddenCategories.value.contains(category.name)) {
        _selectedCategory.value = category
        _selectedChannel.value = null
    }
}
```

- [ ] **Step 6: Add refresh method for visibility changes**

Add a new method to reload categories when visibility settings change:

```kotlin
fun refreshWithVisibilityChanges() {
    loadData()
}
```

- [ ] **Step 7: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 8: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt
git commit -m "feat: add category filtering to LiveViewModel"
```

---

## Task 7: Add Filtering Logic to MoviesViewModel

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MoviesViewModel.kt`

- [ ] **Step 1: Read current MoviesViewModel**

```bash
cd /home/osi61/nativeplayer
cat app/src/main/java/com/nativeplayer/ui/movies/MoviesViewModel.kt | head -100
```

Expected: Shows ViewModel structure with categories StateFlow.

- [ ] **Step 2: Add imports**

Ensure these imports exist (add if missing):
```kotlin
import com.nativeplayer.data.repository.PreferencesManager
```

- [ ] **Step 3: Add hidden categories and displayed categories state**

In the class, after existing StateFlow definitions, add:

```kotlin
private val _hiddenCategories = MutableStateFlow<Set<String>>(emptySet())
val hiddenCategories: StateFlow<Set<String>> = _hiddenCategories.asStateFlow()

private val _displayedCategories = MutableStateFlow<List<Category>>(emptySet())
val displayedCategories: StateFlow<List<Category>> = _displayedCategories.asStateFlow()
```

- [ ] **Step 4: Find and modify loadData() or equivalent method**

The method that loads categories (may be called differently). Apply the same filtering logic:

```kotlin
fun loadData() {
    val allCategories = repository.getCachedMovieCategories()  // Adjust method name as needed
    _categories.value = allCategories
    
    // Load hidden categories
    val settings = preferencesManager.getCategoryVisibility()
    _hiddenCategories.value = settings.hiddenCategoriesMovies
    
    // Filter categories
    val filtered = allCategories.filter { category ->
        !settings.hiddenCategoriesMovies.contains(category.name)
    }
    _displayedCategories.value = filtered
    
    // Select first visible category if available
    if (filtered.isNotEmpty() && _selectedCategory.value == null) {
        _selectedCategory.value = filtered.first()
    }
}
```

- [ ] **Step 5: Add refreshWithVisibilityChanges method**

```kotlin
fun refreshWithVisibilityChanges() {
    loadData()
}
```

- [ ] **Step 6: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/movies/MoviesViewModel.kt
git commit -m "feat: add category filtering to MoviesViewModel"
```

---

## Task 8: Add Filtering Logic to SeriesViewModel

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesViewModel.kt`

- [ ] **Step 1: Read current SeriesViewModel**

```bash
cd /home/osi61/nativeplayer
cat app/src/main/java/com/nativeplayer/ui/series/SeriesViewModel.kt | head -100
```

Expected: Shows ViewModel structure with categories StateFlow.

- [ ] **Step 2: Add imports**

Ensure these imports exist (add if missing):
```kotlin
import com.nativeplayer.data.repository.PreferencesManager
```

- [ ] **Step 3: Add hidden categories and displayed categories state**

In the class, after existing StateFlow definitions, add:

```kotlin
private val _hiddenCategories = MutableStateFlow<Set<String>>(emptySet())
val hiddenCategories: StateFlow<Set<String>> = _hiddenCategories.asStateFlow()

private val _displayedCategories = MutableStateFlow<List<Category>>(emptySet())
val displayedCategories: StateFlow<List<Category>> = _displayedCategories.asStateFlow()
```

- [ ] **Step 4: Find and modify loadData() or equivalent method**

Apply the same filtering logic as MovieViewModel:

```kotlin
fun loadData() {
    val allCategories = repository.getCachedSeriesCategories()  // Adjust method name as needed
    _categories.value = allCategories
    
    // Load hidden categories
    val settings = preferencesManager.getCategoryVisibility()
    _hiddenCategories.value = settings.hiddenCategoriesSeries
    
    // Filter categories
    val filtered = allCategories.filter { category ->
        !settings.hiddenCategoriesSeries.contains(category.name)
    }
    _displayedCategories.value = filtered
    
    // Select first visible category if available
    if (filtered.isNotEmpty() && _selectedCategory.value == null) {
        _selectedCategory.value = filtered.first()
    }
}
```

- [ ] **Step 5: Add refreshWithVisibilityChanges method**

```kotlin
fun refreshWithVisibilityChanges() {
    loadData()
}
```

- [ ] **Step 6: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 7: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/series/SeriesViewModel.kt
git commit -m "feat: add category filtering to SeriesViewModel"
```

---

## Task 9: Update LiveScreen to Use Filtered Categories

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt`

- [ ] **Step 1: Read current LiveScreen to find category bar rendering**

```bash
cd /home/osi61/nativeplayer
grep -n "categories\|Category" app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt | head -20
```

Expected: Shows lines where categories are displayed.

- [ ] **Step 2: Find the LazyRow or Row that displays categories**

Look for rendering code that looks like:
```kotlin
LazyRow(...) {
    items(viewModel.categories)...
}
```

- [ ] **Step 3: Replace categories with displayedCategories**

Change from:
```kotlin
items(viewModel.categories.collectAsState().value) { category ->
```

To:
```kotlin
items(viewModel.displayedCategories.collectAsState().value) { category ->
```

**Note**: If the code uses `viewModel.categories.collectAsState()`, replace that state collection's usage with `viewModel.displayedCategories.collectAsState()`.

- [ ] **Step 4: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt
git commit -m "feat: use filtered categories in LiveScreen"
```

---

## Task 10: Update MoviesScreen to Use Filtered Categories

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt`

- [ ] **Step 1: Find category bar rendering in MoviesScreen**

```bash
cd /home/osi61/nativeplayer
grep -n "categories\|Category" app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt | head -20
```

Expected: Shows lines where categories are displayed.

- [ ] **Step 2: Replace categories with displayedCategories**

Change from:
```kotlin
items(viewModel.categories.collectAsState().value) { category ->
```

To:
```kotlin
items(viewModel.displayedCategories.collectAsState().value) { category ->
```

- [ ] **Step 3: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/movies/MoviesScreen.kt
git commit -m "feat: use filtered categories in MoviesScreen"
```

---

## Task 11: Update SeriesScreen to Use Filtered Categories

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt`

- [ ] **Step 1: Find category bar rendering in SeriesScreen**

```bash
cd /home/osi61/nativeplayer
grep -n "categories\|Category" app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt | head -20
```

Expected: Shows lines where categories are displayed.

- [ ] **Step 2: Replace categories with displayedCategories**

Change from:
```kotlin
items(viewModel.categories.collectAsState().value) { category ->
```

To:
```kotlin
items(viewModel.displayedCategories.collectAsState().value) { category ->
```

- [ ] **Step 3: Build and test compilation**

```bash
cd /home/osi61/nativeplayer
./gradlew compileDebugKotlin 2>&1 | tail -20
```

Expected: Build succeeds with no compilation errors.

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/series/SeriesScreen.kt
git commit -m "feat: use filtered categories in SeriesScreen"
```

---

## Task 12: Create Unit Tests for CategoryVisibilityViewModel

**Files:**
- Create: `app/src/test/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModelTest.kt`

- [ ] **Step 1: Create test file**

```kotlin
package com.nativeplayer.ui.settings

import androidx.lifecycle.Dispatchers
import com.nativeplayer.data.model.CategoryVisibilitySettings
import com.nativeplayer.data.repository.PreferencesManager
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers as RealDispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class CategoryVisibilityViewModelTest {

    private lateinit var preferencesManager: PreferencesManager
    private lateinit var viewModel: CategoryVisibilityViewModel

    @Before
    fun setup() {
        RealDispatchers.setMain(StandardTestDispatcher())
        preferencesManager = mockk(relaxed = true)
        coEvery { preferencesManager.getCategoryVisibility() } returns CategoryVisibilitySettings()
        viewModel = CategoryVisibilityViewModel(preferencesManager)
    }

    @Test
    fun testToggleCategoryVisibilityLiveTV() {
        // Initially should be empty
        assertFalse(viewModel.isCategoryHidden("LIVE_TV", "Sport"))

        // Toggle to hide
        viewModel.toggleCategoryVisibility("LIVE_TV", "Sport")
        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "Sport"))

        // Toggle to show
        viewModel.toggleCategoryVisibility("LIVE_TV", "Sport")
        assertFalse(viewModel.isCategoryHidden("LIVE_TV", "Sport"))
    }

    @Test
    fun testToggleCategoryVisibilityMovies() {
        // Initially should be empty
        assertFalse(viewModel.isCategoryHidden("MOVIES", "Action"))

        // Toggle to hide
        viewModel.toggleCategoryVisibility("MOVIES", "Action")
        assertTrue(viewModel.isCategoryHidden("MOVIES", "Action"))
    }

    @Test
    fun testToggleCategoryVisibilitySeries() {
        // Initially should be empty
        assertFalse(viewModel.isCategoryHidden("SERIES", "Drama"))

        // Toggle to hide
        viewModel.toggleCategoryVisibility("SERIES", "Drama")
        assertTrue(viewModel.isCategoryHidden("SERIES", "Drama"))
    }

    @Test
    fun testSaveCallsPreferencesManager() {
        viewModel.toggleCategoryVisibility("LIVE_TV", "News")
        viewModel.save()

        // Verify save was called
        verify { preferencesManager.saveCategoryVisibility(any()) }
    }

    @Test
    fun testReset() {
        viewModel.toggleCategoryVisibility("LIVE_TV", "News")
        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "News"))

        viewModel.reset()
        assertFalse(viewModel.isCategoryHidden("LIVE_TV", "News"))
    }

    @Test
    fun testMultipleCategoriesCanBeHidden() {
        viewModel.toggleCategoryVisibility("LIVE_TV", "News")
        viewModel.toggleCategoryVisibility("LIVE_TV", "Sport")
        viewModel.toggleCategoryVisibility("LIVE_TV", "Music")

        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "News"))
        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "Sport"))
        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "Music"))
    }

    @Test
    fun testCategoriesIndependentPerScreen() {
        viewModel.toggleCategoryVisibility("LIVE_TV", "News")
        viewModel.toggleCategoryVisibility("MOVIES", "Action")

        assertTrue(viewModel.isCategoryHidden("LIVE_TV", "News"))
        assertFalse(viewModel.isCategoryHidden("LIVE_TV", "Action"))
        assertFalse(viewModel.isCategoryHidden("MOVIES", "News"))
        assertTrue(viewModel.isCategoryHidden("MOVIES", "Action"))
    }
}
```

- [ ] **Step 2: Build and run tests**

```bash
cd /home/osi61/nativeplayer
./gradlew testDebugUnitTest --tests "com.nativeplayer.ui.settings.CategoryVisibilityViewModelTest" -v
```

Expected: All tests pass (7 passed).

- [ ] **Step 3: Commit**

```bash
git add app/src/test/java/com/nativeplayer/ui/settings/CategoryVisibilityViewModelTest.kt
git commit -m "test: add unit tests for CategoryVisibilityViewModel"
```

---

## Task 13: Full Build and Verification

**Files:** None modified - verification only

- [ ] **Step 1: Clean build**

```bash
cd /home/osi61/nativeplayer
./gradlew clean assembleDebug 2>&1 | tail -30
```

Expected: Build succeeds with no errors. Output ends with "BUILD SUCCESSFUL".

- [ ] **Step 2: Run all tests**

```bash
cd /home/osi61/nativeplayer
./gradlew testDebugUnitTest -v 2>&1 | tail -40
```

Expected: All tests pass including new CategoryVisibilityViewModelTest.

- [ ] **Step 3: Check git status**

```bash
cd /home/osi61/nativeplayer
git status
```

Expected: Working tree clean (no uncommitted changes).

- [ ] **Step 4: View git log**

```bash
cd /home/osi61/nativeplayer
git log --oneline -15
```

Expected: Shows all 11 feature commits related to category visibility.

- [ ] **Step 5: Final commit of plan**

```bash
cd /home/osi61/nativeplayer
git log --oneline -1
```

Output confirms all implementation commits are in history.

---

## Summary of Changes

| File | Type | Purpose |
|------|------|---------|
| `CategoryVisibilitySettings.kt` | New | Data model for visibility preferences |
| `PreferencesManager.kt` | Modified | Add save/load persistence methods |
| `CategoryVisibilityViewModel.kt` | New | State management for visibility toggles |
| `CategoriesCheckboxGrid.kt` | New | Reusable Compose component for checkboxes |
| `SettingsScreen.kt` | Modified | Add category visibility sections + save button |
| `LiveViewModel.kt` | Modified | Add filtering logic for Live TV categories |
| `MoviesViewModel.kt` | Modified | Add filtering logic for Movie categories |
| `SeriesViewModel.kt` | Modified | Add filtering logic for Series categories |
| `LiveScreen.kt` | Modified | Use filtered categories in display |
| `MoviesScreen.kt` | Modified | Use filtered categories in display |
| `SeriesScreen.kt` | Modified | Use filtered categories in display |
| `CategoryVisibilityViewModelTest.kt` | New | Unit tests for ViewModel |

---

## Success Criteria Checklist

✅ Data model created and persisted  
✅ PreferencesManager handles serialization  
✅ ViewModel manages toggle state  
✅ Settings UI displays checkboxes  
✅ Categories filter in all 3 screens  
✅ Filtered state persists across app restarts  
✅ Independent filtering per screen type  
✅ Unit tests pass  
✅ Full build succeeds  
✅ No regression in existing functionality  

---

**Plan Status**: Ready for Execution ✅

Plan complete and saved to `docs/superpowers/plans/2025-05-16-category-visibility-implementation.md`. 

**Two execution options:**

**1. Subagent-Driven (recommended)** - I dispatch a fresh subagent per task, review between tasks, fast iteration

**2. Inline Execution** - Execute tasks in this session using executing-plans skill, batch execution with checkpoints

**Which approach do you prefer?**
