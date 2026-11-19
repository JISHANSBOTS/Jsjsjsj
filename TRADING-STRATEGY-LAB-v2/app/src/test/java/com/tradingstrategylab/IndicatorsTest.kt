package com.tradingstrategylab

import com.tradingstrategylab.engine.Indicators
import org.junit.Assert.*
import org.junit.Test

class IndicatorsTest {
    @Test fun smaReturnsExpectedMean() {
        assertEquals(2.0, Indicators.sma(listOf(1.0,2.0,3.0), 2)[1]!!, 0.00001)
    }
    @Test fun emaHasWarmupNulls() {
        val x = Indicators.ema(listOf(1.0,2.0,3.0), 2)
        assertNull(x[0]); assertNotNull(x[1])
    }
}
