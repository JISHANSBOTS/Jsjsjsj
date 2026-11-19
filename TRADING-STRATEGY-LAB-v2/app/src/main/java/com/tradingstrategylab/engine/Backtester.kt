package com.tradingstrategylab.engine

import com.tradingstrategylab.model.Candle

object Backtester {
    fun run(c: List<Candle>): String {
        if (c.size < 60) return "At least 60 candles are required."
        val close = c.map { it.close }
        val fast = Indicators.ema(close, 12)
        val slow = Indicators.ema(close, 26)
        var position = 0
        var entry = 0.0
        var equity = 10_000.0
        var trades = 0
        var wins = 0
        var peak = equity
        var maxDd = 0.0
        for (i in 1 until c.size) {
            val f = fast[i] ?: continue
            val s = slow[i] ?: continue
            val prevF = fast[i-1] ?: continue
            val prevS = slow[i-1] ?: continue
            val crossUp = prevF <= prevS && f > s
            val crossDown = prevF >= prevS && f < s
            if (position != 0 && ((position == 1 && crossDown) || (position == -1 && crossUp))) {
                val pnlPct = (c[i].close / entry - 1) * position
                val pnl = equity * 0.1 * pnlPct - equity * 0.1 * 0.001
                equity += pnl; trades++; if (pnl > 0) wins++
                position = 0
            }
            if (position == 0 && (crossUp || crossDown)) {
                position = if (crossUp) 1 else -1
                entry = c[i].close
            }
            peak = maxOf(peak, equity)
            maxDd = maxOf(maxDd, (peak - equity) / peak)
        }
        val ret = (equity / 10_000.0 - 1) * 100
        return "EMA 12/26 crossover backtest\nCandles: ${c.size}\nClosed trades: $trades\nWin rate: ${if (trades == 0) 0 else wins * 100 / trades}%\nEnding equity: ${"%.2f".format(equity)}\nReturn: ${"%.2f".format(ret)}%\nMax drawdown: ${"%.2f".format(maxDd * 100)}%\n\nSimplified close-price model; excludes realistic intrabar fills, funding, spread and slippage. Not predictive."
    }
}
