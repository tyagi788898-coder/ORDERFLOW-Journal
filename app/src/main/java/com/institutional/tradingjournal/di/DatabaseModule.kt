package com.institutional.tradingjournal.di

import android.content.Context
import androidx.room.Room
import com.institutional.tradingjournal.data.dao.StrategyDao
import com.institutional.tradingjournal.data.dao.TradeDao
import com.institutional.tradingjournal.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "orderflow_v7_db"
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideTradeDao(database: AppDatabase): TradeDao {
        return database.tradeDao()
    }

    @Provides
    fun provideStrategyDao(database: AppDatabase): StrategyDao {
        return database.strategyDao()
    }
}

