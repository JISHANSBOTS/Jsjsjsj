package com.tradingstrategylab.model

data class Candle(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
) {
    fun isValid(): Boolean = timestamp > 0 &&
        listOf(open, high, low, close, volume).all { it.isFinite() } &&
        open > 0 && high > 0 && low > 0 && close > 0 && volume >= 0 &&
        high >= maxOf(open, close, low) && low <= minOf(open, close, high)
}
