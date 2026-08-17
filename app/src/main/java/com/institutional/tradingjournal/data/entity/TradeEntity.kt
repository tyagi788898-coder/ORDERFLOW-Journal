package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trades")
data class TradeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String, // Ye column humne add kiya
    val symbol: String,
    val pnl: Double,
    val date: String
)
