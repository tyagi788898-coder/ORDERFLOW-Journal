package com.institutional.tradingjournal.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.institutional.tradingjournal.data.dao.TradeDao
import com.institutional.tradingjournal.data.entity.StrategyEntity
import com.institutional.tradingjournal.data.entity.TradeEntity

@Database(
    entities = [TradeEntity::class, StrategyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tradeDao(): TradeDao
}

