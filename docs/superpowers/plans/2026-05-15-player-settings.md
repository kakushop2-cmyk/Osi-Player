# Player Settings Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a dedicated PlayerSettingsScreen that persists player configuration and applies it when the player starts.

**Architecture:** New `PlayerSettings` data class stored via `PreferencesManager` (Gson/SharedPreferences). A `PlayerSettingsViewModel` drives a new `PlayerSettingsScreen` accessible from `SettingsScreen`. `PlayerScreen` reads settings once on startup via `LaunchedEffect(Unit)`.

**Tech Stack:** Kotlin, Jetpack Compose, Hilt, ExoPlayer (Media3), SharedPreferences + Gson

---

## File Map

### New Files
| File | Responsibility |
|---|---|
| `app/src/main/java/com/nativeplayer/data/model/PlayerSettings.kt` | Data class + AspectRatio enum |
| `app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsViewModel.kt` | StateFlow + load/save |
| `app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt` | Full settings UI |
| `app/src/main/java/com/nativeplayer/ui/settings/components/SectionHeader.kt` | Gold section heading + divider |
| `app/src/main/java/com/nativeplayer/ui/settings/components/SliderRow.kt` | Label + Slider + value display, D-Pad support |
| `app/src/main/java/com/nativeplayer/ui/settings/components/SegmentedRow.kt` | Label + button group for discrete values |
| `app/src/main/java/com/nativeplayer/ui/settings/components/SwitchRow.kt` | Label + Switch, full-width clickable |
| `app/src/main/java/com/nativeplayer/ui/settings/components/DropdownRow.kt` | Label + ExposedDropdownMenuBox |
| `app/src/main/java/com/nativeplayer/ui/settings/components/TextFieldRow.kt` | Label + OutlinedTextField |

### Modified Files
| File | Change |
|---|---|
| `app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt` | Add `getPlayerSettings()` + `savePlayerSettings()` |
| `app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt` | Add `Screen.PlayerSettings` |
| `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt` | Register `PlayerSettings` route |
| `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt` | Add "Player" card that navigates to PlayerSettings |
| `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt` | Replace hardcoded values with settings from PreferencesManager |
| `app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt` | Add `setBufferSize()` method using `DefaultLoadControl` |

---

## Task 1: PlayerSettings Data Class

**Files:**
- Create: `app/src/main/java/com/nativeplayer/data/model/PlayerSettings.kt`

- [ ] **Step 1: Create the data class**

```kotlin
package com.nativeplayer.data.model

data class PlayerSettings(
    // A – Playback Basics
    val resumeOffsetMs: Long = 5_000L,
    val defaultPlaybackSpeed: Float = 1.0f,

    // B – Seek Buttons
    val seekForwardMs: Long = 30_000L,
    val seekBackwardMs: Long = 10_000L,

    // C1 – Playback Behavior
    val autoHideControlsMs: Long = 5_000L,
    val autoPlayNext: Boolean = false,
    val bufferSizeSeconds: Int = 30,

    // C2 – Video
    val defaultAspectRatio: AspectRatio = AspectRatio.FIT,
    val keepScreenOn: Boolean = true,

    // C3 – Audio
    val preferredAudioLanguage: String = "",
    val loudnessNormalization: Boolean = false
)

enum class AspectRatio {
    FIT, FILL, STRETCH, RATIO_4_3, RATIO_16_9;

    fun displayName(): String = when (this) {
        FIT -> "Angepasst"
        FILL -> "Füllen"
        STRETCH -> "Strecken"
        RATIO_4_3 -> "4:3"
        RATIO_16_9 -> "16:9"
    }
}
```

- [ ] **Step 2: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL (or only unrelated warnings)

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/data/model/PlayerSettings.kt
git commit -m "feat: add PlayerSettings data class with AspectRatio enum"
```

---

## Task 2: PreferencesManager – PlayerSettings Persistence

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt`

- [ ] **Step 1: Add import and key constant**

In `PreferencesManager.kt`, add to the `companion object`:

```kotlin
private const val KEY_PLAYER_SETTINGS = "player_settings"
```

- [ ] **Step 2: Add getPlayerSettings() and savePlayerSettings()**

Add at the bottom of the class (before the closing `}`), after the channel order methods:

```kotlin
// ── Player Settings ───────────────────────────────────────

fun getPlayerSettings(): PlayerSettings {
    val json = prefs.getString(KEY_PLAYER_SETTINGS, null) ?: return PlayerSettings()
    return try {
        gson.fromJson(json, PlayerSettings::class.java) ?: PlayerSettings()
    } catch (e: Exception) {
        PlayerSettings()
    }
}

fun savePlayerSettings(settings: PlayerSettings) {
    val json = gson.toJson(settings)
    prefs.edit().putString(KEY_PLAYER_SETTINGS, json).apply()
}
```

Also add the import at the top of the file:

```kotlin
import com.nativeplayer.data.model.PlayerSettings
```

- [ ] **Step 3: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/data/repository/PreferencesManager.kt
git commit -m "feat: add getPlayerSettings/savePlayerSettings to PreferencesManager"
```

---

## Task 3: Navigation – Add PlayerSettings Route

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt`
- Modify: `app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt`

- [ ] **Step 1: Add Screen.PlayerSettings to Screen.kt**

In `Screen.kt`, add after `data object Search`:

```kotlin
data object PlayerSettings : Screen("player_settings")
```

- [ ] **Step 2: Register route in NavGraph.kt**

Add import at top of NavGraph.kt:

```kotlin
import com.nativeplayer.ui.settings.PlayerSettingsScreen
```

Add composable inside `NavHost` after the existing `Settings` route:

```kotlin
composable(Screen.PlayerSettings.route) {
    PlayerSettingsScreen(navController = navController)
}
```

- [ ] **Step 3: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL (PlayerSettingsScreen missing will cause error – that's expected at this step if you haven't created it yet; the route registration itself is correct)

- [ ] **Step 4: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/navigation/Screen.kt
git add app/src/main/java/com/nativeplayer/ui/navigation/NavGraph.kt
git commit -m "feat: add PlayerSettings navigation route"
```

---

## Task 4: Reusable Settings Composables

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/SectionHeader.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/SliderRow.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/SegmentedRow.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/SwitchRow.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/DropdownRow.kt`
- Create: `app/src/main/java/com/nativeplayer/ui/settings/components/TextFieldRow.kt`

- [ ] **Step 1: Create SectionHeader.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, bottom = 4.dp)
    ) {
        Text(
            text = title,
            color = WarmGold,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
        Divider(
            modifier = Modifier.padding(top = 4.dp),
            color = WarmTextSecondary.copy(alpha = 0.15f),
            thickness = 1.dp
        )
    }
}
```

- [ ] **Step 2: Create SliderRow.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * A row with a label, a Slider, and a value display.
 * D-Pad left/right adjusts value by [step].
 *
 * @param label Display label
 * @param value Current value (in valueRange units)
 * @param valueRange Valid range for the slider
 * @param step Step size for D-Pad navigation
 * @param valueText Formatted display string (e.g., "30 s")
 * @param onValueChange Called with new value while sliding or via D-Pad
 */
@Composable
fun SliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = WarmTextPrimary,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = valueText,
                color = WarmGold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.DirectionLeft -> {
                                val newVal = (value - step).coerceIn(valueRange.start, valueRange.endInclusive)
                                onValueChange(newVal)
                                true
                            }
                            Key.DirectionRight -> {
                                val newVal = (value + step).coerceIn(valueRange.start, valueRange.endInclusive)
                                onValueChange(newVal)
                                true
                            }
                            else -> false
                        }
                    } else false
                },
            colors = SliderDefaults.colors(
                thumbColor = WarmGold,
                activeTrackColor = WarmGold,
                inactiveTrackColor = WarmSurfaceElevated
            )
        )
    }
}

// Needed for remember inside a composable function file
import androidx.compose.runtime.remember
```

> **Note:** The `import androidx.compose.runtime.remember` at the bottom must be moved to the top imports block. Place all imports at the top of the file like so:

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun SliderRow(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    step: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                color = WarmTextPrimary,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = valueText,
                color = WarmGold,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable()
                .onKeyEvent { event ->
                    if (event.type == KeyEventType.KeyDown) {
                        when (event.key) {
                            Key.DirectionLeft -> {
                                val newVal = (value - step).coerceIn(
                                    valueRange.start, valueRange.endInclusive
                                )
                                onValueChange(newVal)
                                true
                            }
                            Key.DirectionRight -> {
                                val newVal = (value + step).coerceIn(
                                    valueRange.start, valueRange.endInclusive
                                )
                                onValueChange(newVal)
                                true
                            }
                            else -> false
                        }
                    } else false
                },
            colors = SliderDefaults.colors(
                thumbColor = WarmGold,
                activeTrackColor = WarmGold,
                inactiveTrackColor = WarmSurfaceElevated
            )
        )
    }
}
```

- [ ] **Step 3: Create SegmentedRow.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * A row with a label and a horizontal group of buttons for discrete values.
 *
 * @param label Display label
 * @param options List of (value, display text) pairs
 * @param selected Currently selected value
 * @param onSelect Called with newly selected value
 */
@Composable
fun <T> SegmentedRow(
    label: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            options.forEach { (value, text) ->
                val isSelected = value == selected
                Button(
                    onClick = { onSelect(value) },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) WarmGold else WarmSurfaceElevated,
                        contentColor = if (isSelected) Color.Black else WarmTextSecondary
                    )
                ) {
                    Text(
                        text = text,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}
```

- [ ] **Step 4: Create SwitchRow.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary

@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = WarmGold,
                checkedTrackColor = WarmGold.copy(alpha = 0.4f),
                uncheckedThumbColor = WarmSurfaceElevated,
                uncheckedTrackColor = WarmSurfaceElevated.copy(alpha = 0.5f)
            )
        )
    }
}
```

- [ ] **Step 5: Create DropdownRow.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

/**
 * A row with a label and an ExposedDropdownMenuBox.
 *
 * @param label Display label
 * @param options List of (value, display text) pairs
 * @param selected Currently selected value
 * @param onSelect Called with newly selected value
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownRow(
    label: String,
    options: List<Pair<T, String>>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedText = options.find { it.first == selected }?.second ?: ""

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = WarmTextPrimary,
                    unfocusedTextColor = WarmTextPrimary,
                    focusedBorderColor = WarmGold,
                    unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.3f),
                    focusedContainerColor = WarmSurfaceElevated,
                    unfocusedContainerColor = WarmSurfaceElevated
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.exposedDropdownSize()
            ) {
                options.forEach { (value, text) ->
                    DropdownMenuItem(
                        text = { Text(text, color = WarmTextPrimary) },
                        onClick = {
                            onSelect(value)
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
```

- [ ] **Step 6: Create TextFieldRow.kt**

```kotlin
package com.nativeplayer.ui.settings.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.ui.theme.WarmGold
import com.nativeplayer.ui.theme.WarmSurfaceElevated
import com.nativeplayer.ui.theme.WarmTextPrimary
import com.nativeplayer.ui.theme.WarmTextSecondary

@Composable
fun TextFieldRow(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = label,
            color = WarmTextPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = WarmTextSecondary.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = WarmTextPrimary,
                unfocusedTextColor = WarmTextPrimary,
                cursorColor = WarmGold,
                focusedBorderColor = WarmGold,
                unfocusedBorderColor = WarmTextSecondary.copy(alpha = 0.3f),
                focusedLabelColor = WarmGold,
                unfocusedLabelColor = WarmTextSecondary,
                focusedContainerColor = WarmSurfaceElevated,
                unfocusedContainerColor = WarmSurfaceElevated
            )
        )
    }
}
```

- [ ] **Step 7: Verify all components compile**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 8: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/components/
git commit -m "feat: add reusable settings UI components (SliderRow, SegmentedRow, SwitchRow, DropdownRow, TextFieldRow, SectionHeader)"
```

---

## Task 5: PlayerSettingsViewModel

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsViewModel.kt`

- [ ] **Step 1: Create the ViewModel**

```kotlin
package com.nativeplayer.ui.settings

import androidx.lifecycle.ViewModel
import com.nativeplayer.data.model.PlayerSettings
import com.nativeplayer.data.repository.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerSettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _settings = MutableStateFlow(preferencesManager.getPlayerSettings())
    val settings: StateFlow<PlayerSettings> = _settings.asStateFlow()

    fun update(settings: PlayerSettings) {
        _settings.value = settings
    }

    fun save() {
        val validated = _settings.value.copy(
            resumeOffsetMs = _settings.value.resumeOffsetMs.coerceIn(0L, 30_000L),
            defaultPlaybackSpeed = _settings.value.defaultPlaybackSpeed.coerceIn(0.5f, 2.0f),
            seekForwardMs = _settings.value.seekForwardMs.coerceIn(5_000L, 300_000L),
            seekBackwardMs = _settings.value.seekBackwardMs.coerceIn(5_000L, 300_000L),
            autoHideControlsMs = _settings.value.autoHideControlsMs.coerceIn(2_000L, 15_000L),
            bufferSizeSeconds = _settings.value.bufferSizeSeconds.coerceIn(10, 120),
            preferredAudioLanguage = _settings.value.preferredAudioLanguage
                .trim()
                .take(3)
                .lowercase()
        )
        _settings.value = validated
        preferencesManager.savePlayerSettings(validated)
    }
}
```

- [ ] **Step 2: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsViewModel.kt
git commit -m "feat: add PlayerSettingsViewModel with validation on save"
```

---

## Task 6: PlayerSettingsScreen

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt`

- [ ] **Step 1: Create the screen**

```kotlin
package com.nativeplayer.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nativeplayer.data.model.AspectRatio
import com.nativeplayer.ui.settings.components.*
import com.nativeplayer.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerSettingsScreen(
    navController: NavController,
    viewModel: PlayerSettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        containerColor = WarmBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Player-Einstellungen",
                        color = WarmTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, "Zurück", tint = WarmTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // ── Wiedergabe ─────────────────────────────────────────
            item { SectionHeader("Wiedergabe") }

            item {
                SliderRow(
                    label = "Rücksprung beim Fortsetzen",
                    value = settings.resumeOffsetMs.toFloat() / 1000f,
                    valueRange = 0f..30f,
                    step = 1f,
                    valueText = "${(settings.resumeOffsetMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(resumeOffsetMs = (it * 1000).toLong())) }
                )
            }

            item {
                SegmentedRow(
                    label = "Standard-Geschwindigkeit",
                    options = listOf(0.5f to "0.5×", 1.0f to "1×", 1.5f to "1.5×", 2.0f to "2×"),
                    selected = settings.defaultPlaybackSpeed,
                    onSelect = { viewModel.update(settings.copy(defaultPlaybackSpeed = it)) }
                )
            }

            // ── Seek-Buttons ───────────────────────────────────────
            item { SectionHeader("Seek-Buttons") }

            item {
                SliderRow(
                    label = "Vorspulen",
                    value = settings.seekForwardMs.toFloat() / 1000f,
                    valueRange = 5f..300f,
                    step = 5f,
                    valueText = "${(settings.seekForwardMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(seekForwardMs = (it * 1000).toLong())) }
                )
            }

            item {
                SliderRow(
                    label = "Zurückspulen",
                    value = settings.seekBackwardMs.toFloat() / 1000f,
                    valueRange = 5f..300f,
                    step = 5f,
                    valueText = "${(settings.seekBackwardMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(seekBackwardMs = (it * 1000).toLong())) }
                )
            }

            // ── Verhalten ──────────────────────────────────────────
            item { SectionHeader("Verhalten") }

            item {
                SliderRow(
                    label = "Controls ausblenden nach",
                    value = settings.autoHideControlsMs.toFloat() / 1000f,
                    valueRange = 2f..15f,
                    step = 1f,
                    valueText = "${(settings.autoHideControlsMs / 1000).toInt()} s",
                    onValueChange = { viewModel.update(settings.copy(autoHideControlsMs = (it * 1000).toLong())) }
                )
            }

            item {
                SwitchRow(
                    label = "Nächste Episode automatisch abspielen",
                    checked = settings.autoPlayNext,
                    onCheckedChange = { viewModel.update(settings.copy(autoPlayNext = it)) }
                )
            }

            item {
                SliderRow(
                    label = "Puffer-Größe",
                    value = settings.bufferSizeSeconds.toFloat(),
                    valueRange = 10f..120f,
                    step = 5f,
                    valueText = "${settings.bufferSizeSeconds} s",
                    onValueChange = { viewModel.update(settings.copy(bufferSizeSeconds = it.toInt())) }
                )
            }

            // ── Bild ───────────────────────────────────────────────
            item { SectionHeader("Bild") }

            item {
                DropdownRow(
                    label = "Standard-Seitenverhältnis",
                    options = AspectRatio.entries.map { it to it.displayName() },
                    selected = settings.defaultAspectRatio,
                    onSelect = { viewModel.update(settings.copy(defaultAspectRatio = it)) }
                )
            }

            item {
                SwitchRow(
                    label = "Bildschirm anlassen",
                    checked = settings.keepScreenOn,
                    onCheckedChange = { viewModel.update(settings.copy(keepScreenOn = it)) }
                )
            }

            // ── Audio ──────────────────────────────────────────────
            item { SectionHeader("Audio") }

            item {
                TextFieldRow(
                    label = "Bevorzugte Audiosprache",
                    value = settings.preferredAudioLanguage,
                    placeholder = "z.B. de, en, fr",
                    onValueChange = { viewModel.update(settings.copy(preferredAudioLanguage = it)) }
                )
            }

            item {
                SwitchRow(
                    label = "Lautstärke normalisieren",
                    checked = settings.loudnessNormalization,
                    onCheckedChange = { viewModel.update(settings.copy(loudnessNormalization = it)) }
                )
            }

            // ── Speichern ──────────────────────────────────────────
            item {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.save()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = WarmGold,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        "Einstellungen speichern",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}
```

- [ ] **Step 2: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/PlayerSettingsScreen.kt
git commit -m "feat: implement PlayerSettingsScreen with all settings groups"
```

---

## Task 7: SettingsScreen – Add Player Card

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt`

- [ ] **Step 1: Add "Player" section card to SettingsScreen**

In `SettingsScreen.kt`, after the "Daten" section (after the `showClearCacheDialog` card item and the closing `}`), add a new item block before the final `}` of `LazyColumn`. Find this block:

```kotlin
            // ── Daten-Bereich ─────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Text("Daten", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
```

After the entire "Daten" item block ends (after the `}`), add:

```kotlin
            // ── Player-Bereich ────────────────────────────────────────
            item {
                Spacer(Modifier.height(16.dp))
                Text("Player", color = WarmGold, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.PlayerSettings.route) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = WarmSurface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null,
                            tint = WarmTextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "Wiedergabe-Einstellungen",
                                color = WarmTextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Seek, Geschwindigkeit, Puffer und mehr",
                                color = WarmTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            tint = WarmTextSecondary.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
```

- [ ] **Step 2: Verify it compiles**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 3: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/settings/SettingsScreen.kt
git commit -m "feat: add Player settings card to SettingsScreen"
```

---

## Task 8: Apply Settings in PlayerScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt`

In `PlayerScreen.kt`, the following hardcoded values need to be replaced. The `PreferencesManager` is not currently injected in PlayerScreen – we will read settings directly from `PreferencesManager` via the existing `PlayerViewModel` or by adding a `LaunchedEffect(Unit)`.

The cleanest approach: add `preferencesManager: PreferencesManager` to `PlayerViewModel` and expose settings. But since `PlayerViewModel` may not have `PreferencesManager` yet, we'll add a `playerSettings` state to `PlayerScreen` loaded via `LaunchedEffect`.

- [ ] **Step 1: Check PlayerViewModel for existing PreferencesManager injection**

Read the file:
```bash
cat /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/ui/player/PlayerViewModel.kt
```

If `PreferencesManager` is NOT already injected, add it there. If it IS injected, expose `getPlayerSettings()`.

- [ ] **Step 2: Add playerSettings state in PlayerScreen.kt**

Find the existing state declarations (around line 120) and add:

```kotlin
var playerSettings by remember { mutableStateOf(PlayerSettings()) }
```

Also add the import at the top:
```kotlin
import com.nativeplayer.data.model.PlayerSettings
```

- [ ] **Step 3: Load settings on startup**

Find the `LaunchedEffect(Unit)` or add it right after the state declarations (before the `LaunchedEffect(surfaceView, ...)` block):

```kotlin
// ── Load player settings once on startup ────────────────
LaunchedEffect(Unit) {
    playerSettings = viewModel.getPlayerSettings()
}
```

For this to work, add `getPlayerSettings()` to `PlayerViewModel.kt`:

```kotlin
fun getPlayerSettings(): PlayerSettings = preferencesManager.getPlayerSettings()
```

And inject `PreferencesManager` in `PlayerViewModel` if not already done:

```kotlin
@HiltViewModel
class PlayerViewModel @Inject constructor(
    // ... existing params ...
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    // ...
    fun getPlayerSettings(): PlayerSettings = preferencesManager.getPlayerSettings()
}
```

- [ ] **Step 4: Replace hardcoded autoHide timer (line ~247)**

Find:
```kotlin
delay(5000)
```
in the `resetHideTimer()` function and replace with:
```kotlin
delay(playerSettings.autoHideControlsMs)
```

- [ ] **Step 5: Replace hardcoded seek values**

In `PlayerScreen.kt`, find all uses of `180_000` (or the seek button onClick handlers) and replace seek amounts:

Search for seek buttons (the `SeekButton` composables with `seekTo` calls). Replace the hardcoded seek amount with:
- Forward: `playerSettings.seekForwardMs`
- Backward: `playerSettings.seekBackwardMs`

- [ ] **Step 6: Apply resumeOffsetMs to resume position**

Find the resume seek (around line 322):
```kotlin
if (currentResumePos > 0) {
    pm.seekTo(currentResumePos)
}
```

Replace with:
```kotlin
if (currentResumePos > 0) {
    val offsetPos = maxOf(0L, currentResumePos - playerSettings.resumeOffsetMs)
    pm.seekTo(offsetPos)
}
```

- [ ] **Step 7: Apply defaultPlaybackSpeed after player initializes**

Directly after `pm.play()` (around line 325), add:
```kotlin
if (playerSettings.defaultPlaybackSpeed != 1.0f) {
    pm.setPlaybackSpeed(playerSettings.defaultPlaybackSpeed)
    playbackSpeed = playerSettings.defaultPlaybackSpeed
}
```

- [ ] **Step 8: Apply keepScreenOn from settings**

Find the existing `keepScreenOn` logic (around line 191):
```kotlin
LaunchedEffect(isPlaying, playerState) {
    currentView.keepScreenOn = isPlaying || playerState == PlayerState.BUFFERING
}
```

Replace with:
```kotlin
LaunchedEffect(isPlaying, playerState, playerSettings) {
    currentView.keepScreenOn = playerSettings.keepScreenOn &&
        (isPlaying || playerState == PlayerState.BUFFERING)
}
```

- [ ] **Step 9: Apply preferredAudioLanguage after tracks load**

Find the `LaunchedEffect(playerManager, playerState)` block that loads tracks (around line 360). After `tracksLoaded = true`, add:

```kotlin
// Auto-select preferred audio language from settings
if (playerSettings.preferredAudioLanguage.isNotBlank()) {
    val preferred = audioTracks.find {
        it.language.startsWith(playerSettings.preferredAudioLanguage, ignoreCase = true)
    }
    preferred?.let {
        pm.selectAudioTrack(it.id)
        currentAudioTrack = it
    }
}
```

- [ ] **Step 10: Verify the full build**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -30
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 11: Commit**

```bash
git add app/src/main/java/com/nativeplayer/ui/player/PlayerScreen.kt
git add app/src/main/java/com/nativeplayer/ui/player/PlayerViewModel.kt
git commit -m "feat: apply PlayerSettings in PlayerScreen (seek, autoHide, resume offset, speed, keepScreen, audioLang)"
```

---

## Task 9: Buffer Size – ExoPlayerEngine

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/player/ExoPlayerEngine.kt`

ExoPlayer's buffer size is configured via `DefaultLoadControl` at player creation time, not via a setter method. This needs to be passed during initialization.

- [ ] **Step 1: Check how ExoPlayer is created (in PlayerManager or di/AppModule.kt)**

```bash
cat /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/player/PlayerManager.kt
cat /home/osi61/nativeplayer/app/src/main/java/com/nativeplayer/di/AppModule.kt
```

- [ ] **Step 2: Pass bufferSizeSeconds to PlayerManager**

In `PlayerManager.kt` (or wherever `ExoPlayer.Builder` is called), update the constructor/`initialize` method to accept `bufferSizeSeconds: Int`.

Typical ExoPlayer buffer setup:
```kotlin
import androidx.media3.exoplayer.DefaultLoadControl

val loadControl = DefaultLoadControl.Builder()
    .setBufferDurationsMs(
        /* minBufferMs */ bufferSizeSeconds * 1000 / 2,
        /* maxBufferMs */ bufferSizeSeconds * 1000,
        /* bufferForPlaybackMs */ DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_MS,
        /* bufferForPlaybackAfterRebufferMs */ DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS
    )
    .build()

val exoPlayer = ExoPlayer.Builder(context)
    .setLoadControl(loadControl)
    .build()
```

- [ ] **Step 3: Pass bufferSizeSeconds from PlayerScreen**

In `PlayerScreen.kt`, when creating `PlayerManager`, pass `playerSettings.bufferSizeSeconds`:
```kotlin
playerManager = PlayerManager(context, bufferSizeSeconds = playerSettings.bufferSizeSeconds).also { pm ->
```

Update `PlayerManager` constructor accordingly.

- [ ] **Step 4: Verify the build**

```bash
cd /home/osi61/nativeplayer
./gradlew :app:compileDebugKotlin 2>&1 | tail -20
```
Expected: BUILD SUCCESSFUL

- [ ] **Step 5: Commit**

```bash
git add app/src/main/java/com/nativeplayer/player/
git commit -m "feat: configure ExoPlayer buffer size from PlayerSettings"
```

---

## Task 10: Final Build & Smoke Test

- [ ] **Step 1: Full debug build**

```bash
cd /home/osi61/nativeplayer
./gradlew assembleDebug 2>&1 | tail -30
```
Expected: BUILD SUCCESSFUL, APK generated

- [ ] **Step 2: Verify APK exists**

```bash
ls -la app/build/outputs/apk/debug/
```
Expected: `app-debug.apk` present

- [ ] **Step 3: Manual smoke test checklist**
  - [ ] Open Settings → "Player" card visible
  - [ ] Tap "Player" → PlayerSettingsScreen opens
  - [ ] Adjust seek forward to 60s → tap Save → go back
  - [ ] Re-open PlayerSettingsScreen → value still 60s
  - [ ] Play a video with a saved resume position → verify it resumes 5s before saved position
  - [ ] Seek buttons in player use new duration

- [ ] **Step 4: Final commit**

```bash
git add -A
git commit -m "feat: player settings screen complete – seek, resume offset, speed, buffer, aspect ratio, audio language"
```

---

## Spec Reference

Full design: `docs/superpowers/specs/2026-05-15-player-settings-design.md`
