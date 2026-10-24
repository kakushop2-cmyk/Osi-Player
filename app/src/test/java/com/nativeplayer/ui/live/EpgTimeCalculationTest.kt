package com.nativeplayer.ui.live

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class EpgTimeCalculationTest {

    @Test
    fun testConvertUtcToLondonTime() {
        // UTC time: 2024-05-17 14:00:00
        val utcTimeMs = 1715954400000L
        val londonZone = TimeZone.getTimeZone("Europe/London")

        val calendar = Calendar.getInstance(londonZone).apply {
            timeInMillis = utcTimeMs
        }

        // London ist im Mai auf BST (UTC+1)
        assertEquals(15, calendar.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun testConvertUtcToIstanbulTime() {
        // UTC time: 2024-05-17 14:00:00
        val utcTimeMs = 1715954400000L
        val istanbulZone = TimeZone.getTimeZone("Europe/Istanbul")

        val calendar = Calendar.getInstance(istanbulZone).apply {
            timeInMillis = utcTimeMs
        }

        // Istanbul ist UTC+3
        assertEquals(17, calendar.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun testCalculateEpgProgress() {
        val programStartMs = 1715954400000L  // 14:00 UTC
        val programEndMs = 1715958000000L    // 15:00 UTC (1 hour duration)
        val currentTimeMs = 1715956200000L   // 14:30 UTC (30 minutes in)
        
        val progress = ((currentTimeMs - programStartMs).toFloat() / 
                       (programEndMs - programStartMs)) * 100
        
        assertEquals(50f, progress)
    }

    @Test
    fun testCalculateRemainingMinutes() {
        val programEndMs = 1715958000000L    // 15:00 UTC
        val currentTimeMs = 1715956200000L   // 14:30 UTC
        
        val remainingMs = programEndMs - currentTimeMs
        val remainingMinutes = remainingMs / (1000 * 60)
        
        assertEquals(30L, remainingMinutes)
    }
}
