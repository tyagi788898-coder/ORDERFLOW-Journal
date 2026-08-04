package com.institutional.tradingjournal.data.repository

import com.institutional.tradingjournal.data.dao.TradeDao
import com.institutional.tradingjournal.data.entity.TradeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor(
    private val tradeDao: TradeDao
) {
    val allTrades: Flow<List<TradeEntity>> = tradeDao.getAllTrades()
    val totalPnL: Flow<Double?> = tradeDao.getTotalPnL()
    val totalTradesCount: Flow<Int> = tradeDao.getTotalTradesCount()

    suspend fun saveTrade(trade: TradeEntity): Long {
        return tradeDao.insertTrade(trade)
    }

    suspend fun updateTrade(trade: TradeEntity) {
        tradeDao.updateTrade(trade)
    }

    suspend fun deleteTrade(trade: TradeEntity) {
        tradeDao.deleteTrade(trade)
    }

    // Auto calculate Grade based on Score (0 - 100)
    fun calculateGrade(score: Int): String {
        return when {
            score >= 90 -> "A+"
            score >= 80 -> "A"
            score >= 70 -> "B"
            score >= 50 -> "C"
            else -> "D"
        }
    }
}

