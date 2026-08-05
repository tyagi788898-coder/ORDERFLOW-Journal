package com.institutional.tradingjournal

import android.content.Context
import com.institutional.tradingjournal.model.TradeEntry

object TradeStorage {
    private const val PREF_NAME = "trade_journal_prefs"
    private const val KEY_TRADES = "saved_trades_raw"

    fun saveTrades(context: Context, trades: List<TradeEntry>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val serializedList = trades.joinToString(separator = "|||") { trade ->
            "${trade.id}:::${trade.date}:::${trade.pair}:::${trade.session}:::${trade.strategy}:::${trade.result}:::${trade.scorePercentage}:::${trade.mistake.replace("\n", " ")}:::${trade.learning.replace("\n", " ")}:::${trade.timestamp}"
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
                            scorePercentage = parts[6].toIntOrNull() ?: 0,
                            mistake = parts[7],
                            learning = parts[8],
                            timestamp = parts[9].toLongOrNull() ?: System.currentTimeMillis()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
