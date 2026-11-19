package com.tradingstrategylab.engine

import kotlin.math.abs

object Indicators {
    fun sma(v: List<Double>, period: Int): List<Double?> {
        require(period > 0)
        return v.indices.map { i -> if (i + 1 < period) null else v.subList(i + 1 - period, i + 1).average() }
    }
    fun ema(v: List<Double>, period: Int): List<Double?> {
        require(period > 0)
        if (v.size < period) return List(v.size) { null }
        val out = MutableList<Double?>(v.size) { null }
        var e = v.take(period).average()
        out[period - 1] = e
        val k = 2.0 / (period + 1)
        for (i in period until v.size) { e = v[i] * k + e * (1 - k); out[i] = e }
        return out
    }
    fun rsi(v: List<Double>, period: Int = 14): List<Double?> {
        val out = MutableList<Double?>(v.size) { null }
        if (v.size <= period) return out
        var gain = 0.0; var loss = 0.0
        for (i in 1..period) {
            val d = v[i] - v[i - 1]
            if (d >= 0) gain += d else loss -= d
        }
        var avgG = gain / period; var avgL = loss / period
        fun value(): Double = if (avgL == 0.0) 100.0 else 100.0 - 100.0 / (1 + avgG / avgL)
        out[period] = value()
        for (i in period + 1 until v.size) {
            val d = v[i] - v[i - 1]
            avgG = (avgG * (period - 1) + maxOf(d, 0.0)) / period
            avgL = (avgL * (period - 1) + maxOf(-d, 0.0)) / period
            out[i] = value()
        }
        return out
    }
    fun atr(high: List<Double>, low: List<Double>, close: List<Double>, period: Int = 14): List<Double?> {
        val tr = high.indices.map { i ->
            if (i == 0) high[i] - low[i]
            else maxOf(high[i] - low[i], abs(high[i] - close[i-1]), abs(low[i] - close[i-1]))
        }
        return sma(tr, period)
    }
}
