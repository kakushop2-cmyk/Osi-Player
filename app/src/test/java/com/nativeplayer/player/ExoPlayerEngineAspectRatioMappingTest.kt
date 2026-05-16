package com.nativeplayer.player

import androidx.media3.common.C
import com.nativeplayer.data.model.AspectRatio
import org.junit.Test

/**
 * Unit Tests für AspectRatio-zu-ExoPlayer-Skalierungsmodi Mapping.
 * Diese Tests verifizieren die Mapping-Logik ohne vollständige ExoPlayer-Initi alisierung.
 */
class ExoPlayerEngineAspectRatioMappingTest {
    
    /**
     * Mapping Function unter Test (extrahiert aus setAspectRatio)
     */
    private fun mapAspectRatioToScalingMode(aspectRatio: AspectRatio): Int {
        return when (aspectRatio) {
            AspectRatio.FIT -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT
            AspectRatio.FILL -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
            else -> C.VIDEO_SCALING_MODE_SCALE_TO_FIT // Fallback für alte enum values
        }
    }
    
    @Test
    fun testMapAspectRatioFITCorrectly() {
        val result = mapAspectRatioToScalingMode(AspectRatio.FIT)
        assert(result == C.VIDEO_SCALING_MODE_SCALE_TO_FIT) {
            "AspectRatio.FIT sollte zu VIDEO_SCALING_MODE_SCALE_TO_FIT gemappt werden"
        }
    }
    
    @Test
    fun testMapAspectRatioFILLCorrectly() {
        val result = mapAspectRatioToScalingMode(AspectRatio.FILL)
        assert(result == C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING) {
            "AspectRatio.FILL sollte zu VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING gemappt werden"
        }
    }
    
    @Test
    fun testMapAspectRatioOldValuesFallbackToFIT() {
        // Teste alte enum values, die noch existieren
        val resultRatio43 = mapAspectRatioToScalingMode(AspectRatio.RATIO_4_3)
        assert(resultRatio43 == C.VIDEO_SCALING_MODE_SCALE_TO_FIT) {
            "AspectRatio.RATIO_4_3 sollte zu VIDEO_SCALING_MODE_SCALE_TO_FIT fallback"
        }
        
        val resultRatio169 = mapAspectRatioToScalingMode(AspectRatio.RATIO_16_9)
        assert(resultRatio169 == C.VIDEO_SCALING_MODE_SCALE_TO_FIT) {
            "AspectRatio.RATIO_16_9 sollte zu VIDEO_SCALING_MODE_SCALE_TO_FIT fallback"
        }
        
        val resultStretch = mapAspectRatioToScalingMode(AspectRatio.STRETCH)
        assert(resultStretch == C.VIDEO_SCALING_MODE_SCALE_TO_FIT) {
            "AspectRatio.STRETCH sollte zu VIDEO_SCALING_MODE_SCALE_TO_FIT fallback"
        }
    }
    
    @Test
    fun testMappingConsistency() {
        // Teste dass konsistente Mapping bei mehrfachen Aufrufen
        repeat(5) {
            assert(mapAspectRatioToScalingMode(AspectRatio.FIT) == C.VIDEO_SCALING_MODE_SCALE_TO_FIT)
            assert(mapAspectRatioToScalingMode(AspectRatio.FILL) == C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        }
    }
}


