package com.institutional.tradingjournal

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.institutional.tradingjournal.model.TradeEntry

object TradeStorage {
    private const val PREF_NAME = "trade_journal_prefs"
    private const val KEY_TRADES = "saved_trades"

    fun saveTrades(context: Context, trades: List<TradeEntry>) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(trades)
        prefs.edit().putString(KEY_TRADES, json).apply()
    }

    fun loadTrades(context: Context): MutableList<TradeEntry> {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TRADES, null) ?: return mutableListOf()
        val type = object : TypeToken<List<TradeEntry>>() {}.type
        return try {
            Gson().fromJson(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }
}

