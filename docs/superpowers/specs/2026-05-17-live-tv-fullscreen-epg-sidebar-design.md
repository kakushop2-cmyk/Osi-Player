# Live TV Fullscreen with EPG Sidebar - Design Specification

**Date**: 2026-05-17  
**Author**: Claude Build Agent  
**Status**: Design Approved  
**Project**: NativePlayer IPTV

---

## 1. Overview

This design adds a professional **EPG (Electronic Program Guide) sidebar experience** to the Live TV fullscreen mode. When a stream starts, the UI displays:
- **Right Sidebar (20-25%)**: Current, previous, and next program logos with names
- **Bottom Bar (full width)**: Current program info with timeline progress and localized time
- **Auto-hide behavior**: Both bars disappear after 5 seconds, reappear on interaction
- **No playback controls**: Seekbar and control buttons are hidden during fullscreen

---

## 2. Functional Requirements

### 2.1 Right Sidebar - Program Logos

**Display Layout** (top to bottom):
1. **Previous Program** (if available)
   - Logo: 60x60 dp
   - Transparency: 50% alpha
   - Program name in full below logo
   
2. **Current Program** ⭐ (Active)
   - Logo: 60x60 dp with border
   - Border color: `#FF8C00` (dark orange-gold)
   - Border thickness: 4dp
   - Transparency: 100% alpha (fully opaque)
   - Program name in full below logo
   
3. **Next Program** (if available)
   - Logo: 60x60 dp
   - Transparency: 50% alpha
   - Program name in full below logo

**Styling Details**:
- Sidebar width: 20-25% of screen width
- Background: WarmSurface color with 85% alpha
- Logos are **NOT clickable** - for orientation only
- All three program names displayed in full below their respective logos
- Vertical spacing: Equal padding between elements for visual balance

### 2.2 Bottom Bar - EPG Timeline

**Display Layout** (left to right):
```
[Current Time] | [Program Name] | [████████░░░░░ 35%] | [Ends in: XX min]
```

**Components**:
1. **Current Time (left)**: 
   - Display time in the channel's category timezone (from backend)
   - Format: HH:MM
   - Updated in real-time

2. **Program Name**: 
   - Full program name
   - May scroll if too long

3. **Timeline Progress Bar**:
   - Visual representation: 0-100% fill
   - Color filled portion: AccentColor (or Gold `#FFD700`)
   - Color unfilled portion: Gray (40% alpha)
   - Shows how far through the current program we are

4. **Remaining Time (right)**:
   - Text: "Ends in: XX min"
   - Updated every minute

**Styling Details**:
- Bar height: ~48dp
- Full width across screen
- Background: WarmSurface with 85% alpha
- Text color: Warm text colors (existing theme)

### 2.3 Auto-Hide Behavior

| Event | Behavior |
|-------|----------|
| Stream starts | Both sidebars fade in (300ms animation) |
| After 5 seconds of inactivity | Both sidebars fade out (300ms animation) |
| User taps screen (video area) | Sidebars fade in, 5-second timer resets |
| User selects different program | New stream loads, sidebars rebuild with new EPG, timer resets |
| Channel changes | All EPG data refreshes based on new category timezone |

---

## 3. Technical Requirements

### 3.1 Data Model Extensions

**Channel & EPG Updates**:
- Backend provides timezone string with each category (e.g., "Europe/London", "Europe/Istanbul")
- EPG items must include start/end timestamps in UTC
- Client converts times to category timezone for display

### 3.2 ViewModel State Additions

**LiveViewModel extensions**:
```kotlin
// New StateFlows
val previousEpgItem: StateFlow<EpgItem?> // Program before current
val currentEpgItem: StateFlow<EpgItem?>   // Program currently playing
val nextEpgItem: StateFlow<EpgItem?>      // Program after current
val epgProgress: StateFlow<Float>         // Progress 0-100%
val categoryTimezone: StateFlow<String>   // Timezone for current category (from Backend)
val currentTimeInZone: StateFlow<Long>    // Current system time converted to category timezone

// New functions
fun calculateEpgProgress(): Float       // Calculate % progress of current program
fun getTimezoneForCategory(categoryId: String): String // Fetch from backend
fun convertTimeToZone(utcTime: Long, timezone: String): Long
fun getRemainingMinutes(): Int          // Calculate minutes until program ends
```

### 3.3 UI Component Architecture

**New Composable Components**:

1. **`EPGLogosSidebar`** (@Composable)
   - Input: previousEpgItem, currentEpgItem, nextEpgItem
   - Displays 3 stacked logos with proper styling and transparency
   - Read-only, no interactions

2. **`EPGTimelineBar`** (@Composable)
   - Input: currentEpgItem, epgProgress, currentTimeInZone, categoryTimezone
   - Displays time, program name, progress bar, remaining minutes
   - Updates progress bar continuously (every second)

3. **`FullscreenLiveOverlay`** (@Composable)
   - Controller for both sidebars
   - Manages auto-hide timer (5 seconds)
   - Detects user interaction (taps)
   - Coordinates fade in/out animations
   - Triggers UI rebuild on channel change

### 3.4 Data Flow

```
LiveScreen (fullscreen mode)
  ├─ selectedChannel changes
  │   └─ ViewModel fetches EPG items for channel
  │       ├─ Requests timezone from backend for channel's category
  │       └─ Calculates previous/current/next program
  │
  ├─ Every second (while playing)
  │   ├─ Calculate progress % (currentTime - programStart) / (programEnd - programStart)
  │   ├─ Convert system time to category timezone
  │   ├─ Calculate remaining minutes
  │   └─ Update all UI state
  │
  └─ On user interaction
      └─ Reset 5-second timer
```

---

## 4. Color & Styling Specification

### 4.1 Border & Logo Styling

| Element | Color | Thickness | Alpha | Font |
|---------|-------|-----------|-------|------|
| Active Logo Border | `#FF8C00` | 4dp | 100% | - |
| Active Logo Background | - | - | 100% | - |
| Inactive Logo (previous/next) | - | - | 50% | - |
| Program Name Text (active) | WarmText | - | 100% | Regular 14sp |
| Program Name Text (inactive) | WarmText | - | 50% | Regular 12sp |

### 4.2 Bottom Bar Colors

| Element | Color | Alpha | Font |
|---------|-------|-------|------|
| Background | WarmSurface | 85% | - |
| Current Time Text | WarmText | 100% | Bold 14sp |
| Program Name Text | WarmText | 100% | Bold 16sp |
| Timeline Fill (progress) | AccentColor or `#FFD700` | 100% | - |
| Timeline Background (unfilled) | Gray | 40% | - |
| Remaining Time Text | WarmText | 100% | Regular 12sp |

### 4.3 Animations

- Sidebar fade-in: 300ms, ease-in
- Sidebar fade-out: 300ms, ease-out
- No logo animations on interaction (read-only components)

---

## 5. Timezone Handling

### 5.1 Backend Integration

**Assumption**: Backend provides timezone with each category response:
```json
{
  "category": {
    "id": "category_1",
    "name": "UK",
    "timezone": "Europe/London"  // Server provides this
  }
}
```

### 5.2 Client-Side Time Conversion

**Algorithm**:
1. Receive EPG `startTime` and `endTime` as UTC timestamps from backend
2. Fetch category timezone (e.g., "Europe/Istanbul" → GMT+2)
3. Convert current system time to that timezone
4. Display converted time in bottom bar
5. Calculate progress: `(currentTimeInZone - startTime) / (endTime - startTime) * 100`

**Example**:
- Category: "Türkiye" → Timezone: "Europe/Istanbul" → GMT+2
- EPG startTime: 1716000000 (UTC)
- Current system time: 1716014400 (UTC)
- Current time in Istanbul: 1716014400 + (2 * 3600) = 1716021600
- Display: "16:30" (example)

### 5.3 Edge Cases

- **Missing timezone**: Fall back to system timezone
- **Invalid timezone string**: Log error, use UTC
- **EPG not loaded**: Show loading state or hide bottom bar temporarily

---

## 6. User Interactions

### 6.1 Valid Interactions

- **Tap anywhere on video (during fullscreen)**: Sidebars reappear, timer restarts
- **Select next program (via gesture/button outside fullscreen)**: Channel changes, new stream loads, sidebars rebuild

### 6.2 Invalid Interactions

- **Click on logo** ❌ → No action (read-only)
- **Drag/swipe logos** ❌ → No action
- **Long-press sidebar** ❌ → No action

---

## 7. Implementation Scope

### 7.1 In Scope

- New right sidebar with 3 program logos
- Bottom EPG bar with timeline
- Auto-hide timer (5 seconds)
- Timezone calculation and display
- Progress bar animation (real-time updates)
- Fade in/out animations
- Event handling (tap to show, channel switch to rebuild)

### 7.2 Out of Scope

- Seeking/scrubbing the timeline (read-only)
- Changing channels from the sidebar
- Storing sidebar preferences
- Custom timezone selection by user

---

## 8. Testing Strategy

### 8.1 Unit Tests

- `calculateEpgProgress()` with various start/end times
- `convertTimeToZone()` with multiple timezone examples
- `getRemainingMinutes()` calculation

### 8.2 Integration Tests

- Sidebar visibility state transitions
- Timer reset on user interaction
- EPG data refresh on channel change
- Timezone display for different categories

### 8.3 Manual Tests

- Visual alignment of logos (proper spacing, sizing)
- Border rendering on active logo
- Alpha transparency of inactive logos
- Text wrapping for long program names
- Bottom bar updates in real-time (every second)
- Auto-hide timing (5 seconds, no more, no less)

---

## 9. Success Criteria

✅ **Functional**:
- Sidebars display correctly on stream start
- Auto-hide after exactly 5 seconds
- Interaction resets timer and shows sidebars
- EPG progress updates in real-time
- Timezone conversion is accurate
- Program name changes correctly when channel changes

✅ **Visual**:
- Logo borders render with correct color and thickness
- Inactive logos appear at 50% alpha
- Active logo at 100% with orange-gold border
- Bottom bar is readable and well-formatted
- All text displays in full (no clipping)

✅ **Performance**:
- No lag during logo rendering (60 FPS)
- Timer doesn't drift (±100ms acceptable)
- EPG data loads within 500ms of channel change

---

## 10. File Structure (After Implementation)

```
app/src/main/java/com/nativeplayer/ui/live/
  ├── LiveScreen.kt (modified)
  ├── LiveViewModel.kt (modified)
  └── components/
      ├── EPGLogosSidebar.kt (NEW)
      ├── EPGTimelineBar.kt (NEW)
      └── FullscreenLiveOverlay.kt (NEW)

app/src/main/java/com/nativeplayer/data/model/
  └── EpgItem.kt (may add timezone field if not present)

app/src/test/java/com/nativeplayer/ui/live/
  └── EpgTimeCalculationTest.kt (NEW)
```

---

## 11. Design Decisions & Rationale

| Decision | Rationale |
|----------|-----------|
| 5-second auto-hide | Balances UX: enough time to read EPG, not intrusive | 
| 50% alpha inactive logos | Visual hierarchy: inactive items fade into background |
| `#FF8C00` (4dp border) | Stands out enough on dark backgrounds, professional look |
| Backend timezone source | Flexible, scalable for international content |
| Read-only sidebars | Focused UX: controls are elsewhere, sidebars are informational |
| 20-25% sidebar width | Doesn't overcrowd video content, enough space for 60x60 logos |

---

## 12. Future Enhancements (Out of Scope)

- Gesture to scroll through more programs (not just ±1)
- Channel selection from sidebar by voice command
- Custom EPG reminder notifications
- User preference for timezone override
- Sidebar animation on program progress (e.g., highlight next program when current ends)

---

## Appendix: Example State Snapshot

```kotlin
// Example state during Live TV fullscreen
val selectedChannel = Channel(
    id = "uk_bbc1",
    name = "BBC One",
    categoryId = "uk",
    logo = "https://...",
    url = "https://stream.url"
)

val categoryTimezone = "Europe/London"  // From backend

val previousEpgItem = EpgItem(
    title = "Morning Show",
    startTime = 1716007200,  // UTC
    endTime = 1716010800
)

val currentEpgItem = EpgItem(
    title = "Mid-Day News",
    startTime = 1716010800,  // UTC
    endTime = 1716014400,
    logo = "https://logo.url"
)

val nextEpgItem = EpgItem(
    title = "Afternoon Program",
    startTime = 1716014400,  // UTC
    endTime = 1716018000
)

val epgProgress = 45.5f  // 45.5% through current program

val currentTimeInZone = 1716014400L  // Converted to London timezone for display

// UI displays:
// Right Sidebar:
//   - Morning Show logo (50% alpha)
//   - Mid-Day News logo (100% alpha, #FF8C00 border)
//   - Afternoon Program logo (50% alpha)
//
// Bottom Bar:
//   - "14:00" (current time in London)
//   - "Mid-Day News"
//   - "████████░░░░░ 45%"
//   - "Ends in: 5 min"
```

---

**Document Status**: ✅ Complete and ready for implementation planning
