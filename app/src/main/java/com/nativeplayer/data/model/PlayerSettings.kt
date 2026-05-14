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
