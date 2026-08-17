package com.institutional.tradingjournal.data.dao

import androidx.room.*
import com.institutional.tradingjournal.data.entity.TradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrade(trade: TradeEntity): Long

    @Update
    suspend fun updateTrade(trade: TradeEntity)

    @Delete
    suspend fun deleteTrade(trade: TradeEntity)

    @Query("SELECT * FROM trades WHERE email = :email ORDER BY id DESC")
    fun getTradesByEmail(email: String): Flow<List<TradeEntity>>

    @Query("SELECT * FROM trades ORDER BY id DESC")
    fun getAllTrades(): Flow<List<TradeEntity>>

    @Query("SELECT SUM(pnl) FROM trades")
    fun getTotalPnL(): Flow<Double?>

    @Query("SELECT COUNT(*) FROM trades")
    fun getTotalTradesCount(): Flow<Int>
}
