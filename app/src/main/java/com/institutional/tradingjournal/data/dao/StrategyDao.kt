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

    @Query("SELECT * FROM strategies WHERE email = :email ORDER BY id DESC")
    fun getStrategiesByEmail(email: String): Flow<List<StrategyEntity>>

    @Query("SELECT * FROM strategies WHERE isActive = 1 ORDER BY id DESC")
    fun getAllActiveStrategies(): Flow<List<StrategyEntity>>

    @Query("SELECT * FROM strategies ORDER BY id DESC")
    fun getAllStrategies(): Flow<List<StrategyEntity>>

    @Query("SELECT COUNT(*) FROM strategies")
    suspend fun getStrategyCount(): Int
}
