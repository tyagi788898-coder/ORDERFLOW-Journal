package com.institutional.tradingjournal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "strategies")
data class StrategyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String,
    val colorHex: String,
    val iconName: String,
    val checklistItems: String, // Pipe or JSON string
    val scoreWeight: Int = 100,
    val isDefault: Boolean = false,
    val isHidden: Boolean = false
)

