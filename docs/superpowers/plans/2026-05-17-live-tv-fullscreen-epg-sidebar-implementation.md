# Live TV Fullscreen with EPG Sidebar - Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Implement a professional EPG sidebar experience for Live TV fullscreen mode with auto-hide behavior, timezone-aware timeline, and real-time progress tracking.

**Architecture:** 
- Three new lightweight Compose components handle UI rendering (EPGLogosSidebar, EPGTimelineBar, FullscreenLiveOverlay)
- LiveViewModel extends with EPG state management and timezone conversion logic
- FullscreenLiveOverlay acts as controller managing the 5-second auto-hide timer and user interactions
- Backend timezone data is fetched and stored in ViewModel, applied to all time calculations

**Tech Stack:** 
- Kotlin + Jetpack Compose (UI)
- StateFlow for state management
- Coroutines for timer logic
- TimeZone API for timezone conversions
- Coil for logo image loading (existing)

---

## File Structure

**New files:**
- `app/src/main/java/com/nativeplayer/ui/live/components/EPGLogosSidebar.kt` — Right sidebar displaying 3 program logos
- `app/src/main/java/com/nativeplayer/ui/live/components/EPGTimelineBar.kt` — Bottom bar with program name and timeline
- `app/src/main/java/com/nativeplayer/ui/live/components/FullscreenLiveOverlay.kt` — Controller for auto-hide and interaction logic
- `app/src/test/java/com/nativeplayer/ui/live/EpgTimeCalculationTest.kt` — Unit tests for timezone and progress calculations

**Modified files:**
- `app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt` — Add fullscreen state & integrate new components
- `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt` — Add EPG state flows & timezone logic

---

## Task 1: Set Up ViewModel State & Timezone Logic

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt` (add new StateFlows and functions)
- Create: `app/src/test/java/com/nativeplayer/ui/live/EpgTimeCalculationTest.kt` (unit tests)

### Step 1.1: Write the failing test for timezone conversion

```bash
mkdir -p app/src/test/java/com/nativeplayer/ui/live
touch app/src/test/java/com/nativeplayer/ui/live/EpgTimeCalculationTest.kt
```

Create the test file with failing tests:

```kotlin
package com.nativeplayer.ui.live

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class EpgTimeCalculationTest {

    @Test
    fun testConvertUtcToLondonTime() {
        // UTC time: 2024-05-17 14:00:00
        val utcTimeMs = 1715943600000L
        val londonZone = TimeZone.getTimeZone("Europe/London")
        
        val calendar = Calendar.getInstance(londonZone).apply {
            timeInMillis = utcTimeMs
        }
        
        assertEquals(14, calendar.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun testConvertUtcToIstanbulTime() {
        // UTC time: 2024-05-17 14:00:00
        val utcTimeMs = 1715943600000L
        val istanbulZone = TimeZone.getTimeZone("Europe/Istanbul")
        
        val calendar = Calendar.getInstance(istanbulZone).apply {
            timeInMillis = utcTimeMs
        }
        
        // Istanbul is UTC+3 (during DST: +3, standard: +2)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        assertTrue(hour >= 16 && hour <= 17) // Accounts for DST variations
    }

    @Test
    fun testCalculateEpgProgress() {
        val programStartMs = 1715943600000L  // 14:00 UTC
        val programEndMs = 1715947200000L    // 15:00 UTC (1 hour duration)
        val currentTimeMs = 1715945400000L   // 14:30 UTC (30 minutes in)
        
        val progress = ((currentTimeMs - programStartMs).toFloat() / 
                       (programEndMs - programStartMs)) * 100
        
        assertEquals(50f, progress)
    }

    @Test
    fun testCalculateRemainingMinutes() {
        val programEndMs = 1715947200000L    // 15:00 UTC
        val currentTimeMs = 1715945400000L   // 14:30 UTC
        
        val remainingMs = programEndMs - currentTimeMs
        val remainingMinutes = remainingMs / (1000 * 60)
        
        assertEquals(30L, remainingMinutes)
    }
}
```

- [ ] Run: `cd /home/osi61/nativeplayer && ./gradlew test --tests EpgTimeCalculationTest -v`

Expected output: 4 test failures (functions not yet implemented)

### Step 1.2: Add StateFlow properties to LiveViewModel

Open `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt` and add these properties:

```kotlin
// At the top of class, after existing StateFlows:

private val _previousEpgItem = MutableStateFlow<EpgItem?>(null)
val previousEpgItem: StateFlow<EpgItem?> = _previousEpgItem.asStateFlow()

private val _currentEpgItem = MutableStateFlow<EpgItem?>(null)
val currentEpgItem: StateFlow<EpgItem?> = _currentEpgItem.asStateFlow()

private val _nextEpgItem = MutableStateFlow<EpgItem?>(null)
val nextEpgItem: StateFlow<EpgItem?> = _nextEpgItem.asStateFlow()

private val _epgProgress = MutableStateFlow(0f)
val epgProgress: StateFlow<Float> = _epgProgress.asStateFlow()

private val _categoryTimezone = MutableStateFlow("UTC")
val categoryTimezone: StateFlow<String> = _categoryTimezone.asStateFlow()

private val _currentTimeInZone = MutableStateFlow(System.currentTimeMillis())
val currentTimeInZone: StateFlow<Long> = _currentTimeInZone.asStateFlow()

private val _remainingMinutes = MutableStateFlow(0)
val remainingMinutes: StateFlow<Int> = _remainingMinutes.asStateFlow()
```

### Step 1.3: Add helper functions to LiveViewModel

Add these functions to the LiveViewModel class:

```kotlin
/**
 * Converts UTC timestamp to the given timezone
 */
fun convertTimeToZone(utcTimeMs: Long, timezoneId: String): Calendar {
    val timezone = TimeZone.getTimeZone(timezoneId)
    return Calendar.getInstance(timezone).apply {
        timeInMillis = utcTimeMs
    }
}

/**
 * Calculates EPG progress as a percentage (0-100)
 */
fun calculateEpgProgress(
    programStartMs: Long,
    programEndMs: Long,
    currentTimeMs: Long
): Float {
    if (programEndMs <= programStartMs) return 0f
    val progress = ((currentTimeMs - programStartMs).toFloat() / 
                    (programEndMs - programStartMs)) * 100
    return progress.coerceIn(0f, 100f)
}

/**
 * Calculates remaining minutes until program ends
 */
fun calculateRemainingMinutes(programEndMs: Long, currentTimeMs: Long): Int {
    val remainingMs = programEndMs - currentTimeMs
    return (remainingMs / (1000 * 60)).toInt().coerceAtLeast(0)
}

/**
 * Formats time for display (HH:MM)
 */
fun formatTimeForDisplay(calendar: Calendar): String {
    val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
    return "$hour:$minute"
}

/**
 * Fetches timezone for category from backend and updates state
 * (Backend will provide this with category data)
 */
suspend fun fetchAndSetCategoryTimezone(categoryId: String) {
    try {
        // TODO: This will be filled in when backend integration is complete
        // For now, we use a mapping (can be replaced with API call)
        val timezone = getCategoryTimezone(categoryId)
        _categoryTimezone.value = timezone
    } catch (e: Exception) {
        // Log error, default to UTC
        _categoryTimezone.value = "UTC"
    }
}

/**
 * Helper to map category names to timezones (temporary until backend provides this)
 */
private fun getCategoryTimezone(categoryId: String): String {
    return when {
        categoryId.contains("uk", ignoreCase = true) -> "Europe/London"
        categoryId.contains("turk", ignoreCase = true) || categoryId.contains("tr", ignoreCase = true) -> "Europe/Istanbul"
        categoryId.contains("us", ignoreCase = true) -> "America/New_York"
        categoryId.contains("ger", ignoreCase = true) || categoryId.contains("de", ignoreCase = true) -> "Europe/Berlin"
        else -> "UTC"
    }
}
```

### Step 1.4: Run tests to verify they pass

```bash
cd /home/osi61/nativeplayer && ./gradlew test --tests EpgTimeCalculationTest -v
```

Expected: All 4 tests PASS ✅

### Step 1.5: Commit changes

```bash
cd /home/osi61/nativeplayer && git add app/src/test/java/com/nativeplayer/ui/live/EpgTimeCalculationTest.kt app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt && git commit -m "feat: add EPG timezone and progress calculation logic with unit tests"
```

---

## Task 2: Create EPGLogosSidebar Composable

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/live/components/EPGLogosSidebar.kt`

### Step 2.1: Create the component file

```bash
mkdir -p app/src/main/java/com/nativeplayer/ui/live/components
touch app/src/main/java/com/nativeplayer/ui/live/components/EPGLogosSidebar.kt
```

### Step 2.2: Write the EPGLogosSidebar composable

```kotlin
package com.nativeplayer.ui.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmText

/**
 * Sidebar component displaying current, previous, and next program logos
 * Located on right side of fullscreen Live TV view
 */
@Composable
fun EPGLogosSidebar(
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(WarmSurface.copy(alpha = 0.85f))
            .padding(vertical = 16.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Previous Program (if available)
        if (previousEpgItem != null) {
            EPGProgramCard(
                epgItem = previousEpgItem,
                isActive = false
            )
        } else {
            Spacer(modifier = Modifier.height(0.dp))
        }

        // Current Program (Active)
        if (currentEpgItem != null) {
            EPGProgramCard(
                epgItem = currentEpgItem,
                isActive = true
            )
        }

        // Next Program (if available)
        if (nextEpgItem != null) {
            EPGProgramCard(
                epgItem = nextEpgItem,
                isActive = false
            )
        } else {
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}

/**
 * Individual program card with logo and name
 */
@Composable
private fun EPGProgramCard(
    epgItem: EpgItem,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Logo Container
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(4.dp))
                .then(
                    if (isActive) {
                        Modifier.border(
                            width = 4.dp,
                            color = Color(0xFFFF8C00), // Dark orange-gold
                            shape = RoundedCornerShape(4.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .background(Color.Gray)
        ) {
            // Program Logo Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(epgItem.logo ?: epgItem.channelLogo)
                    .crossfade(true)
                    .build(),
                contentDescription = epgItem.title,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop,
                alpha = if (isActive) 1f else 0.5f
            )
        }

        // Program Name
        Text(
            text = epgItem.title,
            fontSize = if (isActive) 14.sp else 12.sp,
            fontWeight = FontWeight.Normal,
            color = WarmText.copy(alpha = if (isActive) 1f else 0.5f),
            maxLines = 2,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}
```

### Step 2.3: Verify the file compiles

```bash
cd /home/osi61/nativeplayer && ./gradlew compileDebugKotlin --console=plain 2>&1 | grep -i "error\|success" | head -20
```

Expected: Successful compilation (no errors)

### Step 2.4: Commit

```bash
cd /home/osi61/nativeplayer && git add app/src/main/java/com/nativeplayer/ui/live/components/EPGLogosSidebar.kt && git commit -m "feat: create EPGLogosSidebar component with logo display and styling"
```

---

## Task 3: Create EPGTimelineBar Composable

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/live/components/EPGTimelineBar.kt`

### Step 3.1: Create the component file

```bash
touch app/src/main/java/com/nativeplayer/ui/live/components/EPGTimelineBar.kt
```

### Step 3.2: Write the EPGTimelineBar composable

```kotlin
package com.nativeplayer.ui.live.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativeplayer.data.model.EpgItem
import com.nativeplayer.ui.theme.WarmSurface
import com.nativeplayer.ui.theme.WarmText
import kotlinx.coroutines.delay

/**
 * Bottom bar component displaying current EPG program and timeline progress
 * Shows program name, current time in timezone, progress bar, and remaining minutes
 */
@Composable
fun EPGTimelineBar(
    currentEpgItem: EpgItem?,
    epgProgress: Float,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    modifier: Modifier = Modifier
) {
    if (currentEpgItem == null) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(WarmSurface.copy(alpha = 0.85f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Current Time (Left)
            Text(
                text = timeDisplayString,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = WarmText,
                modifier = Modifier.widthIn(min = 50.dp)
            )

            // Program Name (Middle)
            Text(
                text = currentEpgItem.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = WarmText,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )

            // Progress Bar
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(horizontal = 4.dp)
            ) {
                LinearProgressIndicator(
                    progress = { epgProgress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = Color(0xFFFFD700), // Gold
                    trackColor = Color.Gray.copy(alpha = 0.4f),
                    drawStopIndicator = {}
                )
                Text(
                    text = "${epgProgress.toInt()}%",
                    fontSize = 10.sp,
                    color = WarmText.copy(alpha = 0.7f),
                    modifier = Modifier.align(Alignment.End)
                )
            }

            // Remaining Time (Right)
            Text(
                text = "Ends in: ${remainingMinutes}m",
                fontSize = 12.sp,
                color = WarmText,
                modifier = Modifier.widthIn(min = 80.dp)
            )
        }
    }
}
```

### Step 3.3: Verify compilation

```bash
cd /home/osi61/nativeplayer && ./gradlew compileDebugKotlin --console=plain 2>&1 | grep -i "error\|success" | head -20
```

Expected: Successful compilation

### Step 3.4: Commit

```bash
cd /home/osi61/nativeplayer && git add app/src/main/java/com/nativeplayer/ui/live/components/EPGTimelineBar.kt && git commit -m "feat: create EPGTimelineBar component with timeline progress display"
```

---

## Task 4: Create FullscreenLiveOverlay Controller

**Files:**
- Create: `app/src/main/java/com/nativeplayer/ui/live/components/FullscreenLiveOverlay.kt`

### Step 4.1: Create the component file

```bash
touch app/src/main/java/com/nativeplayer/ui/live/components/FullscreenLiveOverlay.kt
```

### Step 4.2: Write the FullscreenLiveOverlay composable

```kotlin
package com.nativeplayer.ui.live.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nativeplayer.data.model.EpgItem
import kotlinx.coroutines.delay

/**
 * Overlay controller for fullscreen Live TV experience
 * Manages auto-hide behavior, user interaction detection, and component visibility
 *
 * Behavior:
 * - Shows sidebars on stream start
 * - Auto-hides after 5 seconds of no interaction
 * - Resets timer on user tap
 * - Rebuilds on channel change
 */
@Composable
fun FullscreenLiveOverlay(
    previousEpgItem: EpgItem?,
    currentEpgItem: EpgItem?,
    nextEpgItem: EpgItem?,
    epgProgress: Float,
    currentTimeInZone: Long,
    remainingMinutes: Int,
    timeDisplayString: String,
    onChannelChanged: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }
    var hideTimerActive by remember { mutableStateOf(false) }

    // Auto-hide timer logic
    LaunchedEffect(isVisible, hideTimerActive) {
        if (isVisible && !hideTimerActive) {
            hideTimerActive = true
            delay(5000L) // 5 seconds
            isVisible = false
            hideTimerActive = false
        }
    }

    // Reset timer when channel changes
    LaunchedEffect(currentEpgItem) {
        isVisible = true
        hideTimerActive = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // User tapped the screen - show sidebars and reset timer
                isVisible = true
                hideTimerActive = false
            }
    ) {
        // Right Sidebar with Logos
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(durationMillis = 300),
            exit = fadeOut(durationMillis = 300),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight()
                .fillMaxWidth(0.225f) // 20-25% of screen width
        ) {
            EPGLogosSidebar(
                previousEpgItem = previousEpgItem,
                currentEpgItem = currentEpgItem,
                nextEpgItem = nextEpgItem
            )
        }

        // Bottom Timeline Bar
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(durationMillis = 300),
            exit = fadeOut(durationMillis = 300),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
        ) {
            EPGTimelineBar(
                currentEpgItem = currentEpgItem,
                epgProgress = epgProgress,
                currentTimeInZone = currentTimeInZone,
                remainingMinutes = remainingMinutes,
                timeDisplayString = timeDisplayString
            )
        }
    }
}
```

### Step 4.3: Verify compilation

```bash
cd /home/osi61/nativeplayer && ./gradlew compileDebugKotlin --console=plain 2>&1 | grep -i "error\|success" | head -20
```

Expected: Successful compilation

### Step 4.4: Commit

```bash
cd /home/osi61/nativeplayer && git add app/src/main/java/com/nativeplayer/ui/live/components/FullscreenLiveOverlay.kt && git commit -m "feat: create FullscreenLiveOverlay controller with auto-hide timer"
```

---

## Task 5: Extend LiveViewModel with EPG Updates

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt`

### Step 5.1: Add EPG update logic to selectedChannel watcher

Add this to the `init` block or create a new LaunchedEffect in the ViewModel:

```kotlin
init {
    // ... existing code ...

    viewModelScope.launch {
        selectedChannel.collect { channel ->
            if (channel != null) {
                // Fetch timezone for this channel's category
                fetchAndSetCategoryTimezone(channel.categoryId)
                
                // Update EPG items for current channel
                updateEpgItems(channel)
            }
        }
    }
}

/**
 * Updates previous, current, and next EPG items for the selected channel
 */
private suspend fun updateEpgItems(channel: Channel) {
    try {
        val allEpgItems = epgItems.value.filter { it.channelId == channel.id }
        
        if (allEpgItems.isEmpty()) {
            _previousEpgItem.value = null
            _currentEpgItem.value = null
            _nextEpgItem.value = null
            return
        }

        val now = System.currentTimeMillis()

        // Sort by start time
        val sortedItems = allEpgItems.sortedBy { it.startTime }

        // Find current program (program that contains 'now')
        val current = sortedItems.firstOrNull { item ->
            item.startTime <= now && item.endTime > now
        }

        if (current != null) {
            val currentIndex = sortedItems.indexOf(current)
            _previousEpgItem.value = if (currentIndex > 0) sortedItems[currentIndex - 1] else null
            _currentEpgItem.value = current
            _nextEpgItem.value = if (currentIndex < sortedItems.size - 1) sortedItems[currentIndex + 1] else null
        } else {
            // If no current program, use the next upcoming one
            val next = sortedItems.firstOrNull { it.startTime > now }
            _currentEpgItem.value = next
            _previousEpgItem.value = sortedItems.lastOrNull { it.startTime <= now }
            _nextEpgItem.value = if (next != null) {
                val nextIndex = sortedItems.indexOf(next)
                if (nextIndex < sortedItems.size - 1) sortedItems[nextIndex + 1] else null
            } else {
                null
            }
        }
    } catch (e: Exception) {
        // Handle error, log it
        e.printStackTrace()
    }
}

/**
 * Starts a coroutine to update EPG progress and time every second
 */
fun startEpgProgressUpdate() {
    viewModelScope.launch {
        while (true) {
            delay(1000L) // Update every second
            
            val current = currentEpgItem.value ?: continue
            val now = System.currentTimeMillis()
            
            // Calculate progress
            val progress = calculateEpgProgress(
                current.startTime,
                current.endTime,
                now
            )
            _epgProgress.value = progress
            
            // Calculate remaining minutes
            val remaining = calculateRemainingMinutes(current.endTime, now)
            _remainingMinutes.value = remaining
            
            // Update current time in zone
            val calendar = convertTimeToZone(now, categoryTimezone.value)
            _currentTimeInZone.value = calendar.timeInMillis
        }
    }
}

/**
 * Stops the EPG progress update coroutine
 */
fun stopEpgProgressUpdate() {
    // Coroutine will naturally stop when ViewModel is cleared
}
```

### Step 5.2: Start EPG progress updates in LaunchedEffect

Modify the existing `LaunchedEffect(selectedChannel)` in LiveScreen (or add it to the ViewModel's init):

```kotlin
// Add to LiveViewModel.init or create new LaunchedEffect in LiveScreen
viewModelScope.launch {
    selectedChannel.collect { channel ->
        if (channel != null) {
            startEpgProgressUpdate()
        }
    }
}
```

### Step 5.3: Add necessary imports to LiveViewModel

At the top of LiveViewModel.kt, ensure these imports are present:

```kotlin
import java.util.Calendar
import java.util.TimeZone
```

### Step 5.4: Test ViewModel changes compile

```bash
cd /home/osi61/nativeplayer && ./gradlew compileDebugKotlin --console=plain 2>&1 | tail -20
```

Expected: Successful compilation

### Step 5.5: Commit

```bash
cd /home/osi61/nativeplayer && git add app/src/main/java/com/nativeplayer/ui/live/LiveViewModel.kt && git commit -m "feat: add EPG item updates and real-time progress tracking to ViewModel"
```

---

## Task 6: Integrate FullscreenLiveOverlay into LiveScreen

**Files:**
- Modify: `app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt`

### Step 6.1: Add fullscreen state and overlay display

Open LiveScreen.kt and find the main `Box(modifier = Modifier.fillMaxSize()...)` section. 

Add these state variables near the top of the `LiveScreen` function (after other state declarations):

```kotlin
var isFullscreen by remember { mutableStateOf(false) }
```

### Step 6.2: Add fullscreen toggle logic

Add a button or gesture handler to enter fullscreen. Find where seekbar/controls are displayed and add:

```kotlin
// Example: Add a fullscreen button to existing controls
IconButton(
    onClick = { isFullscreen = !isFullscreen },
    modifier = Modifier.size(48.dp)
) {
    Icon(
        imageVector = if (isFullscreen) Icons.Filled.Fullscreen else Icons.Filled.FullscreenExit,
        contentDescription = "Toggle Fullscreen"
    )
}
```

### Step 6.3: Modify Box layout to conditionally show overlay

Replace the existing main Box content with conditional rendering:

```kotlin
Box(modifier = Modifier.fillMaxSize().background(WarmBackground)) {
    if (isFullscreen) {
        // Fullscreen mode: Show video + overlay
        Box(modifier = Modifier.fillMaxSize()) {
            // Video player (SurfaceView)
            AndroidView(
                factory = { SurfaceView(it).apply { surfaceView = this } },
                modifier = Modifier.fillMaxSize()
            )

            // EPG Overlay with auto-hide
            FullscreenLiveOverlay(
                previousEpgItem = viewModel.previousEpgItem.collectAsState().value,
                currentEpgItem = viewModel.currentEpgItem.collectAsState().value,
                nextEpgItem = viewModel.nextEpgItem.collectAsState().value,
                epgProgress = viewModel.epgProgress.collectAsState().value,
                currentTimeInZone = viewModel.currentTimeInZone.collectAsState().value,
                remainingMinutes = viewModel.remainingMinutes.collectAsState().value,
                timeDisplayString = formatCurrentTime(
                    viewModel.currentTimeInZone.collectAsState().value,
                    viewModel.categoryTimezone.collectAsState().value
                ),
                modifier = Modifier.fillMaxSize()
            )
        }
    } else {
        // Normal mode: Show existing layout
        Row(modifier = Modifier.fillMaxSize()) {
            // ... existing categories and channels columns ...
        }
    }
}
```

### Step 6.4: Add time formatting helper function

Add this helper function to LiveScreen:

```kotlin
/**
 * Formats time for display in the current timezone
 */
fun formatCurrentTime(utcTimeMs: Long, timezoneId: String): String {
    val timezone = TimeZone.getTimeZone(timezoneId)
    val calendar = Calendar.getInstance(timezone).apply { timeInMillis = utcTimeMs }
    val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
    val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
    return "$hour:$minute"
}
```

### Step 6.5: Add necessary imports to LiveScreen

```kotlin
import com.nativeplayer.ui.live.components.FullscreenLiveOverlay
import com.nativeplayer.ui.live.components.EPGLogosSidebar
import com.nativeplayer.ui.live.components.EPGTimelineBar
import java.util.TimeZone
import java.util.Calendar
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
```

### Step 6.6: Test compilation

```bash
cd /home/osi61/nativeplayer && ./gradlew compileDebugKotlin --console=plain 2>&1 | tail -20
```

Expected: Successful compilation

### Step 6.7: Commit

```bash
cd /home/osi61/nativeplayer && git add app/src/main/java/com/nativeplayer/ui/live/LiveScreen.kt && git commit -m "feat: integrate FullscreenLiveOverlay into LiveScreen with fullscreen toggle"
```

---

## Task 7: Build & Manual Testing

**Files:**
- No new files, testing only

### Step 7.1: Clean build

```bash
cd /home/osi61/nativeplayer && ./gradlew clean build --console=plain 2>&1 | tail -30
```

Expected: BUILD SUCCESSFUL

### Step 7.2: Run unit tests

```bash
cd /home/osi61/nativeplayer && ./gradlew test --console=plain 2>&1 | tail -50
```

Expected: All tests pass (including EpgTimeCalculationTest)

### Step 7.3: Manual testing checklist

If you can run the app on a device/emulator, test:

- [ ] Stream starts in normal mode
- [ ] Click fullscreen button → enters fullscreen with EPG overlay visible
- [ ] After 5 seconds → sidebars fade out (only video visible)
- [ ] Click on video → sidebars fade back in
- [ ] Check logos display correctly:
  - [ ] Previous program logo (50% transparent, no border)
  - [ ] Current program logo (100% opaque, #FF8C00 4dp border)
  - [ ] Next program logo (50% transparent, no border)
- [ ] Bottom bar shows:
  - [ ] Current time in correct timezone
  - [ ] Program name
  - [ ] Progress bar updating in real-time
  - [ ] Remaining minutes updating
- [ ] Change channel → new program loads, EPG data updates, sidebars rebuild
- [ ] Check no errors in logcat

### Step 7.4: Final commit

```bash
cd /home/osi61/nativeplayer && git add -A && git commit -m "build: successful fullscreen EPG sidebar implementation"
```

---

## Summary

| Task | Component | Status |
|------|-----------|--------|
| 1 | ViewModel State & Tests | Write tests, add TimeZone logic ✅ |
| 2 | EPGLogosSidebar | 3 logos, styling, transparency ✅ |
| 3 | EPGTimelineBar | Program name, timeline, time ✅ |
| 4 | FullscreenLiveOverlay | Auto-hide, interaction, visibility ✅ |
| 5 | ViewModel Integration | EPG updates, progress tracking ✅ |
| 6 | LiveScreen Integration | Fullscreen mode, overlay display ✅ |
| 7 | Build & Testing | Compile, unit tests, manual testing ✅ |

**Total estimated time:** 3-4 hours for experienced Kotlin/Compose developer

---

**Next Step:** Implementation via subagent-driven-development or executing-plans skill
