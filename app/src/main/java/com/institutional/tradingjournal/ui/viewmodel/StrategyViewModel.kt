package com.institutional.tradingjournal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.institutional.tradingjournal.data.entity.StrategyEntity
import com.institutional.tradingjournal.data.repository.StrategyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StrategyViewModel @Inject constructor(
    private val strategyRepository: StrategyRepository
) : ViewModel() {

    val activeStrategies: StateFlow<List<StrategyEntity>> = strategyRepository.activeStrategies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            strategyRepository.checkAndSeedDefaultStrategies()
        }
    }

    fun createCustomStrategy(name: String, description: String, colorHex: String, checklistItems: List<String>) {
        viewModelScope.launch {
            val formattedChecklist = checklistItems.joinToString("|")
            val newStrategy = StrategyEntity(
                name = name,
                description = description,
                colorHex = colorHex,
                iconName = "Bookmark",
                checklistItems = formattedChecklist,
                scoreWeight = 100,
                isDefault = false
            )
            strategyRepository.insertStrategy(newStrategy)
        }
    }

    fun deleteStrategy(strategy: StrategyEntity) {
        viewModelScope.launch {
            strategyRepository.deleteStrategy(strategy)
        }
    }
}

