package com.institutional.tradingjournal.model

data class TradeEntry(
    val id: String = System.currentTimeMillis().toString(),
    val date: String,
    val pair: String,
    val session: String,
    val strategy: String,
    val result: String, // WIN, LOSS, BREAKEVEN, PENDING
    val scorePercentage: Int,
    val mistake: String,
    val learning: String,
    val timestamp: Long = System.currentTimeMillis()
)

