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
