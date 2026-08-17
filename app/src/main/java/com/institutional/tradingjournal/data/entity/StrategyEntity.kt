package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strategies")
data class StrategyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String, 
    val strategyName: String,
    val description: String
)
