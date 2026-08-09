package com.institutional.tradingjournal.model

data class TradeEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val date: String,
    val pair: String,
    val session: String,
    val strategy: String,
    val result: String,
    val pnlAmount: Double,
    val scorePercentage: Int = 0,
    var mistake: String = "",
    var learning: String = ""
)
