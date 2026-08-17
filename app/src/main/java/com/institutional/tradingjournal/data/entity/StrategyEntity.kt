package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strategies")
data class StrategyEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String = "",
    val name: String = "",
    val description: String = "",
    val colorHex: String = "#1976D2",
    val iconName: String = "trending_up",
    val checklistItems: String = "",
    val scoreWeight: Int = 100,
    val isDefault: Boolean = false,
    val isActive: Boolean = true
)
