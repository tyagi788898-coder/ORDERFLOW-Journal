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
    fun getAllTrades(): Flow<List<TradeEntity>> = tradeDao.getAllTrades()

    fun getTradesByEmail(email: String): Flow<List<TradeEntity>> = tradeDao.getTradesByEmail(email)

    suspend fun insertTrade(trade: TradeEntity): Long = tradeDao.insertTrade(trade)

    suspend fun updateTrade(trade: TradeEntity) = tradeDao.updateTrade(trade)

    suspend fun deleteTrade(trade: TradeEntity) = tradeDao.deleteTrade(trade)

    fun getTotalPnL(): Flow<Double?> = tradeDao.getTotalPnL()

    fun getTotalTradesCount(): Flow<Int> = tradeDao.getTotalTradesCount()
}
