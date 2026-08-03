package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tradeIdString: String,
    val timestamp: Long,
    val pair: String,
    val broker: String,
    val account: String,
    val direction: String, // BUY / SELL
    val lotSize: Double,
    val entryPrice: Double,
    val exitPrice: Double,
    val stopLoss: Double,
    val takeProfit: Double,
    val riskPercentage: Double,
    val rewardPercentage: Double,
    val riskRewardRatio: Double,
    val pnl: Double,
    val commission: Double = 0.0,
    val swap: Double = 0.0,
    val session: String, // Asian, London, New York
    val strategyId: Long,
    val strategyName: String,
    val tradeScore: Int, // 0-100
    val tradeGrade: String, // A+, A, B, C, D
    val emotion: String, // Calm, Fear, Greed, FOMO, Revenge, Confidence, Stress, Discipline
    val screenshotUris: String, // Comma separated URIs
    val mistakesNotes: String = "",
    val learningNotes: String = "",
    val contextNotes: String = "",
    val isFavorite: Boolean = false
)

