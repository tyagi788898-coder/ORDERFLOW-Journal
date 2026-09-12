package com.institutional.tradingjournal.data.repository

import com.institutional.tradingjournal.data.dao.StrategyDao
import com.institutional.tradingjournal.data.entity.StrategyEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StrategyRepository @Inject constructor(
    private val strategyDao: StrategyDao
) {
    fun getAllActiveStrategies(): Flow<List<StrategyEntity>> = strategyDao.getAllActiveStrategies()

    suspend fun checkAndInsertDefaults(email: String = "") {
        if (strategyDao.getStrategyCount() == 0) {
            DefaultStrategies.getList(email).forEach { strategy ->
                strategyDao.insertStrategy(strategy)
            }
        }
    }

    suspend fun insertStrategy(strategy: StrategyEntity): Long = strategyDao.insertStrategy(strategy)

    suspend fun updateStrategy(strategy: StrategyEntity) = strategyDao.updateStrategy(strategy)

    suspend fun deleteStrategy(strategy: StrategyEntity) = strategyDao.deleteStrategy(strategy)
}
