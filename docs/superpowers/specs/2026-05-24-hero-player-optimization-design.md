# Hero Player Optimization — Single-Player Recycling

**Date:** 2026-05-24
**Status:** Draft
**Author:** AI (Build Agent)

## Problem

The HeroVideoPlayer on the HomeScreen uses excessive resources:
- Up to 3 cached ExoPlayer instances (HW codecs, buffer pools, network connections)
- Video streaming with full audio for 15-second previews
- 3 retry attempts with 2s delay on failure
- Poster image loaded simultaneously with video (double network usage)
- Inconsistent clip duration constants (15s vs 60s)
- Pulse animation performing 2 spring iterations per focus

## Approach

Replace the multi-player cache (`Map<String, ExoPlayer>`) with a **single ExoPlayer** that is recycled across all hero cards. The player is reconfigured (new URL, prepare, play) each time focus moves to a different card.

## Design

### 1. HomeViewModel — Single Player

**Current:**
```kotlin
private val heroPlayers = mutableMapOf<String, ExoPlayer>()
private val maxCachedPlayers = 3

fun getOrCreateHeroPlayer(streamUrl: String): ExoPlayer { ... }
fun prepareAndPlayHero(streamUrl: String) { ... }
fun pauseHero(streamUrl: String) { ... }
fun preprepareHero(streamUrl: String) { ... }
```

**New:**
```kotlin
private var heroPlayer: ExoPlayer? = null

fun getOrCreateHeroPlayer(): ExoPlayer       // No URL param — returns single instance
fun prepareAndPlayHero(streamUrl: String)     // Reconfigures single player if URL changed
fun pauseHero()                                // No URL param — pauses single player
fun preprepareHero(streamUrl: String)          // Sets MediaItem on single player (no prepare)
fun stopAllHeroPlayers()                       // Stops + clears single player
```

**Key behaviors:**
- Volume: Keep **0.7f** (as originally configured)
- Buffer: Reduce to **2 seconds** (minimal for preview)
- Timeout: Reduce to **5 seconds** (from 15s)
- On `pauseHero()`: `stop()` (frees codec, keeps MediaItem) — *unchanged*
- On `prepareAndPlayHero(url)`: if URL differs from current MediaItem, stop + clear + set new item; then prepare + play
- On `preprepareHero(url)`: only set MediaItem if URL differs — **no prepare()** (saves buffering until actual focus)
- Override `onCleared()`: release single player

### 2. HeroVideoPlayer — Behavioral Changes

**Retry logic:**
- Reduce `MAX_HERO_RETRIES` from **3 → 1**
- Reduce `RETRY_DELAY_MS` from **2000 → 1000**

**Poster loading:**
- Currently: `AsyncImage` always loads poster, even when video is playing
- New: Only load poster when `exoPlayer == null` (i.e., when no video is active)
- When video becomes active, poster stays as-is from previous render (still visible behind TextureView)

**Pulse animation:**
- Currently: `animateTo(1.08f)` + `animateTo(1f)` (two spring animations)
- New: Single `animateTo(1.08f)` then snap back (reduces CPU by ~50% on animation)

**CLIP_DURATION:**
- Unify: Both HeroVideoPlayer and the seek logic use **15_000L** (15 seconds)
- Remove the 60_000L constant from HomeViewModel that is never used

**TextureView handling:**
- When focus is gained: call `player.setVideoTextureView(textureView)` in `AndroidView.factory`
- When focus is lost: call `player.clearVideoTextureView()` via listener callback
- This ensures the video output is always redirected to the correct card

### 3. HomeScreen — Interface Changes

- `HeroVideoPlayer` no longer needs `getOrCreatePlayer` lambda (single global player)
- Instead, pass a simpler `getPlayer: () -> ExoPlayer` lambda that returns the singleton
- OR: HeroVideoPlayer calls ViewModel directly (not ideal — keeps separation)

**Recommended approach:** Keep lambda interface but simplify:

```kotlin
// Current:
getOrCreatePlayer = { url -> viewModel.getOrCreateHeroPlayer(url) }

// New — simpler, no URL needed:
getPlayer = { viewModel.getOrCreateHeroPlayer() }
```

## Files Changed

| File | Changes |
|------|---------|
| `HomeViewModel.kt` | Map → single ExoPlayer; simplified API (many `streamUrl` params removed); 5s timeout; 2s buffer |
| `HeroVideoPlayer.kt` | MAX_HERO_RETRIES 3→1, RETRY_DELAY 2s→1s, poster conditional on video, pulse simplified, TextureView re-attachment |
| `HomeScreen.kt` | Adapt `getOrCreatePlayer` to single-player signature |

## Expected Resource Savings

| Resource | Before | After | Reduction |
|----------|--------|-------|-----------|
| ExoPlayer instances | 3 | 1 | **-66%** |
| HW codecs allocated | 3 | 1 | **-66%** |
| Network connections (peak) | 3 | 1 | **-66%** |
| Failed stream retries | 3 attempts × 2s | 1 attempt × 1s | **-83%** |
| Poster network requests per card | 2 (poster + video) | 1 (video only when playing) | **-50%** |
| Animation CPU per focus | 2 spring iterations | 1 spring + snap | **-50%** |
| Memory (ExoPlayer buffers) | 3× buffer pool | 1× buffer pool | **-66%** |

## Risk Assessment

- **Minor:** Slight delay between focus and video start due to player reconfiguration (~200-500ms vs instant for preloaded player)
- **Mitigation:** `preprepareHero()` still sets MediaItem when next card URL is known — only `prepare()` is deferred
- **Edge case:** Rapid D-pad scrolling through cards — each focus change triggers a player reset
- **Mitigation:** `stop()` → `setMediaItem()` → `prepare()` → `play()` sequence is <100ms for prepared sources
