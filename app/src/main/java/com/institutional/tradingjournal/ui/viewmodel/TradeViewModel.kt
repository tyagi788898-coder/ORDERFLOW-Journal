package com.institutional.tradingjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.data.repository.TradeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val repository: TradeRepository
) : ViewModel() {

    val allTrades: StateFlow<List<TradeEntity>> = repository.getAllTrades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertTrade(trade: TradeEntity) {
        viewModelScope.launch {
            repository.insertTrade(trade)
        }
    }

    fun updateTrade(trade: TradeEntity) {
        viewModelScope.launch {
            repository.updateTrade(trade)
        }
    }

    fun deleteTrade(trade: TradeEntity) {
        viewModelScope.launch {
            repository.deleteTrade(trade)
        }
    }
}
