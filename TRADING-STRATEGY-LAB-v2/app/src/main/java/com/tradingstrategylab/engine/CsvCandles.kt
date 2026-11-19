package com.tradingstrategylab.engine

import com.tradingstrategylab.model.Candle
import java.io.BufferedReader

object CsvCandles {
    fun parse(reader: BufferedReader): List<Candle> {
        val result = ArrayList<Candle>()
        var first = true
        reader.forEachLine { line ->
            val s = line.trim()
            if (s.isNotEmpty()) {
                if (first && s.lowercase().startsWith("timestamp")) {
                    first = false
                } else {
                    first = false
                    val p = s.split(',')
                    require(p.size == 6) { "Expected six CSV columns" }
                    val rawTs = p[0].trim().toLong()
                    val ts = if (rawTs.toString().length == 10) rawTs * 1000 else rawTs
                    val candle = Candle(ts, p[1].toDouble(), p[2].toDouble(),
                        p[3].toDouble(), p[4].toDouble(), p[5].toDouble())
                    require(candle.isValid()) { "Invalid OHLCV row: $s" }
                    result.add(candle)
                }
            }
        }
        require(result.isNotEmpty()) { "CSV contains no candle rows" }
        return result.distinctBy { it.timestamp }.sortedBy { it.timestamp }
    }
}
