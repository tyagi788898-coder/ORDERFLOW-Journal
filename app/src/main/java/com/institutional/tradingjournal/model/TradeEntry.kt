package com.institutional.tradingjournal.model

import java.util.UUID

data class TradeEntry(
    val id: String = UUID.randomUUID().toString(),
    val date: String = "",
    val pair: String = "XAUUSD",
    val session: String = "London",
    val strategy: String = "Strategy 1",
    val result: String = "PENDING",
    val pnlAmount: Double = 0.0,
    val scorePercentage: Int = 0,
    val mistake: String = "",
    val learning: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
