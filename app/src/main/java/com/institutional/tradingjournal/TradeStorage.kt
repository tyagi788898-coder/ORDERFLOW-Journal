package com.institutional.tradingjournal

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.institutional.tradingjournal.model.TradeEntry

object TradeStorage {
    private const val PREFS_NAME = "trade_journal_prefs"
    private const val KEY_TRADES = "saved_trades"
    private val gson = Gson()

    fun saveTrades(context: Context, trades: List<TradeEntry>) {
        try {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val jsonString = gson.toJson(trades)
            prefs.edit().putString(KEY_TRADES, jsonString).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadTrades(context: Context): List<TradeEntry> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_TRADES, "[]") ?: "[]"
        val type = object : TypeToken<List<TradeEntry>>() {}.type
        return try {
            gson.fromJson(jsonString, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
