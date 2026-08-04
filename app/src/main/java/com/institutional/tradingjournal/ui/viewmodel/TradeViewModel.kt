package com.institutional.tradingjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val tradeRepository: TradeRepository
) : ViewModel() {

    val allTrades: StateFlow<List<TradeEntity>> = tradeRepository.allTrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPnL: StateFlow<Double> = tradeRepository.totalPnL
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalTradesCount: StateFlow<Int> = tradeRepository.totalTradesCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val winRate: StateFlow<Double> = allTrades.map { list ->
        if (list.isEmpty()) 0.0
        else {
            val wins = list.count { it.pnl > 0 }
            (wins.toDouble() / list.size) * 100
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addTrade(trade: TradeEntity) {
        viewModelScope.launch {
            tradeRepository.saveTrade(trade)
        }
    }

    fun deleteTrade(trade: TradeEntity) {
        viewModelScope.launch {
            tradeRepository.deleteTrade(trade)
        }
    }
}

