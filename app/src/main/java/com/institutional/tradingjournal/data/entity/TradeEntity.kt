package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String = "",
    val tradeIdString: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val pair: String = "",
    val symbol: String = "",
    val broker: String = "",
    val account: String = "",
    val direction: String = "BUY",
    val lotSize: Double = 0.0,
    val entryPrice: Double = 0.0,
    val exitPrice: Double = 0.0,
    val stopLoss: Double = 0.0,
    val takeProfit: Double = 0.0,
    val pnl: Double = 0.0,
    val riskPercentage: Double = 0.0,
    val rewardPercentage: Double = 0.0,
    val riskRewardRatio: Double = 0.0,
    val date: String = "",
    val session: String = "",
    val strategyId: Int = 0,
    val strategyName: String = "",
    val tradeScore: Int = 0,
    val tradeGrade: String = "A",
    val emotion: String = "",
    val screenshotUris: String = ""
)
