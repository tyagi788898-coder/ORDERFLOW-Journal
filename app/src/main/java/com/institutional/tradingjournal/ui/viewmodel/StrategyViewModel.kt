package com.institutional.tradingjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.institutional.tradingjournal.data.entity.StrategyEntity
import com.institutional.tradingjournal.data.repository.StrategyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StrategyViewModel @Inject constructor(
    private val repository: StrategyRepository
) : ViewModel() {

    val strategies: StateFlow<List<StrategyEntity>> = repository.getAllActiveStrategies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun seedDefaults(email: String = "") {
        viewModelScope.launch {
            repository.checkAndInsertDefaults(email)
        }
    }

    fun addStrategy(strategy: StrategyEntity) {
        viewModelScope.launch {
            repository.insertStrategy(strategy)
        }
    }

    fun updateStrategy(strategy: StrategyEntity) {
        viewModelScope.launch {
            repository.updateStrategy(strategy)
        }
    }

    fun deleteStrategy(strategy: StrategyEntity) {
        viewModelScope.launch {
            repository.deleteStrategy(strategy)
        }
    }
}
