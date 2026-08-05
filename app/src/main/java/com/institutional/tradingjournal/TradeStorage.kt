package com.institutional.tradingjournal

import android.content.Context
import android.os.Environment
import com.institutional.tradingjournal.model.TradeEntry
import java.io.File

object TradeStorage {
    private const val PREF_NAME = "trade_journal_prefs"
    private const val KEY_TRADES = "saved_trades_raw_v2"
    private const val KEY_THEME = "app_theme_dark"

    fun saveTheme(context: Context, isDark: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_THEME, isDark).apply()
    }

    fun isDarkMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_THEME, true)
    }

    fun saveTrades(context: Context, trades: List<TradeEntry>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val serializedList = trades.joinToString(separator = "|||") { trade ->
            "${trade.id}:::${trade.date}:::${trade.pair}:::${trade.session}:::${trade.strategy}:::${trade.result}:::${trade.pnlAmount}:::${trade.scorePercentage}:::${trade.mistake.replace("\n", " ")}:::${trade.learning.replace("\n", " ")}:::${trade.timestamp}"
        }
        prefs.edit().putString(KEY_TRADES, serializedList).apply()
    }

    fun loadTrades(context: Context): MutableList<TradeEntry> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val rawString = prefs.getString(KEY_TRADES, null) ?: return mutableListOf()
        if (rawString.isBlank()) return mutableListOf()

        val list = mutableListOf<TradeEntry>()
        try {
            val items = rawString.split("|||")
            for (item in items) {
                if (item.isBlank()) continue
                val parts = item.split(":::")
                if (parts.size >= 10) {
                    list.add(
                        TradeEntry(
                            id = parts[0],
                            date = parts[1],
                            pair = parts[2],
                            session = parts[3],
                            strategy = parts[4],
                            result = parts[5],
                            pnlAmount = parts[6].toDoubleOrNull() ?: 0.0,
                            scorePercentage = parts[7].toIntOrNull() ?: 0,
                            mistake = parts[8],
                            learning = parts[9],
                            timestamp = parts[10].toLongOrNull() ?: System.currentTimeMillis()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun exportToCSV(context: Context, trades: List<TradeEntry>): String {
        return try {
            val csvHeader = "ID,Date,Pair,Session,Strategy,Result,PnL_Amount,Score_Percent,Mistake,Learning\n"
            val csvBody = trades.joinToString(separator = "\n") { t ->
                "\"${t.id}\",\"${t.date}\",\"${t.pair}\",\"${t.session}\",\"${t.strategy}\",\"${t.result}\",${t.pnlAmount},${t.scorePercentage},\"${t.mistake}\",\"${t.learning}\""
            }
            val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsFolder, "Orderflow_Trading_Journal_Backup.csv")
            file.writeText(csvHeader + csvBody)
            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }
}
