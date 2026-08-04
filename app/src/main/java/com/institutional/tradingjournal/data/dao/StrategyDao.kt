package com.institutional.tradingjournal.data.dao

import androidx.room.*
import com.institutional.tradingjournal.data.entity.StrategyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StrategyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStrategy(strategy: StrategyEntity): Long

    @Update
    suspend fun updateStrategy(strategy: StrategyEntity)

    @Delete
    suspend fun deleteStrategy(strategy: StrategyEntity)

    @Query("SELECT * FROM strategies WHERE isHidden = 0 ORDER BY id ASC")
    fun getAllActiveStrategies(): Flow<List<StrategyEntity>>

    @Query("SELECT * FROM strategies ORDER BY id ASC")
    fun getAllStrategiesIncludingHidden(): Flow<List<StrategyEntity>>

    @Query("SELECT * FROM strategies WHERE id = :id")
    suspend fun getStrategyById(id: Long): StrategyEntity?

    @Query("SELECT COUNT(*) FROM strategies")
    suspend fun getStrategyCount(): Int
}

