package com.tradingstrategylab

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tradingstrategylab.databinding.ActivityMainBinding
import com.tradingstrategylab.engine.Backtester
import com.tradingstrategylab.engine.CsvCandles
import com.tradingstrategylab.engine.StrategyLab
import com.tradingstrategylab.model.Candle
import com.tradingstrategylab.worker.ResearchScheduler
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val candles = mutableListOf<Candle>()
    private var lastCsvName = "candles.csv"

    private val importCsv = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (uri != null) importFile(uri)
    }
    private val exportCsv = registerForActivityResult(ActivityResultContracts.CreateDocument("text/csv")) { uri ->
        if (uri != null) exportFile(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.marketSpinner.adapter = adapter(listOf("BTCUSDT", "XAUUSD"))
        b.timeframeSpinner.adapter = adapter(listOf("H1", "D1"))
        b.importButton.setOnClickListener { importCsv.launch(arrayOf("text/*", "application/vnd.ms-excel")) }
        b.exportButton.setOnClickListener {
            lastCsvName = "${market()}_${timeframe()}_candles.csv"
            exportCsv.launch(lastCsvName)
        }
        b.generateButton.setOnClickListener {
            val ideas = StrategyLab.generate(candles)
            b.resultsText.text = if (ideas.isEmpty()) "Need at least 60 valid candles to generate candidates."
            else ideas.joinToString("\n\n") { it }
        }
        b.backtestButton.setOnClickListener {
            val report = Backtester.run(candles)
            b.resultsText.text = report
        }
        b.scheduleButton.setOnClickListener {
            ResearchScheduler.schedule(this)
            b.statusText.text = "Daily data-readiness check scheduled (WorkManager; timing is not exact)."
        }
    }

    private fun adapter(items: List<String>) =
        ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

    private fun market() = b.marketSpinner.selectedItem?.toString() ?: "BTCUSDT"
    private fun timeframe() = b.timeframeSpinner.selectedItem?.toString() ?: "H1"

    private fun importFile(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { stream ->
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    val parsed = CsvCandles.parse(reader)
                    candles.clear()
                    candles.addAll(parsed)
                }
            }
            b.statusText.text = "Imported ${candles.size} candles for ${market()} / ${timeframe()}."
            b.resultsText.text = "CSV loaded in memory. This prototype does not yet persist imports across app restarts."
        } catch (e: Exception) {
            b.statusText.text = "CSV import failed: ${e.message ?: "invalid file"}"
        }
    }

    private fun exportFile(uri: Uri) {
        try {
            contentResolver.openOutputStream(uri)?.bufferedWriter()?.use { writer ->
                writer.appendLine("timestamp,open,high,low,close,volume")
                candles.forEach {
                    writer.appendLine("${it.timestamp},${it.open},${it.high},${it.low},${it.close},${it.volume}")
                }
            }
            Toast.makeText(this, "CSV exported", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            b.statusText.text = "Export failed: ${e.message}"
        }
    }
}
