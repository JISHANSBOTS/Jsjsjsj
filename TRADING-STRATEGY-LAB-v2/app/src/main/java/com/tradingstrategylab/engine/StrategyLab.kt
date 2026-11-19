package com.tradingstrategylab.engine

import com.tradingstrategylab.model.Candle

object StrategyLab {
    fun generate(candles: List<Candle>): List<String> {
        if (candles.size < 60) return emptyList()
        val close = candles.map { it.close }
        val fast = Indicators.ema(close, 12)
        val slow = Indicators.ema(close, 26)
        val rsi = Indicators.rsi(close, 14)
        val last = close.lastIndex
        val f = fast[last] ?: return emptyList()
        val s = slow[last] ?: return emptyList()
        val r = rsi[last] ?: return emptyList()
        return listOf(
            "EMA crossover hypothesis: EMA12=${"%.4f".format(f)}, EMA26=${"%.4f".format(s)}; current bias=${if (f > s) "LONG" else "SHORT"}.",
            "RSI filter hypothesis: RSI14=${"%.2f".format(r)}; state=${when { r < 30 -> "oversold" ; r > 70 -> "overbought"; else -> "neutral" }}.",
            "Research note: compare these rules on chronological train/validation/out-of-sample periods before considering paper deployment."
        )
    }
}
