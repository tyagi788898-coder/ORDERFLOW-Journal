package com.institutional.tradingjournal.data.repository

import com.institutional.tradingjournal.data.entity.StrategyEntity

object DefaultStrategies {
    fun getList(email: String = ""): List<StrategyEntity> = listOf(
        StrategyEntity(
            id = 1,
            email = email,
            name = "Orderflow Imbalance",
            description = "Trading stacked imbalances at key support/resistance zones.",
            colorHex = "#1976D2",
            iconName = "trending_up",
            checklistItems = "Delta Confirmation,Volume Spike,Key Level Rejection",
            scoreWeight = 100,
            isDefault = true,
            isActive = true
        ),
        StrategyEntity(
            id = 2,
            email = email,
            name = "Absorption Reversal",
            description = "High volume with little price movement showing institutional absorption.",
            colorHex = "#388E3C",
            iconName = "swap_horiz",
            checklistItems = "Delta Divergence,Exhaustion Volume,Pin Bar at Level",
            scoreWeight = 100,
            isDefault = true,
            isActive = true
        ),
        StrategyEntity(
            id = 3,
            email = email,
            name = "Liquidity Sweep",
            description = "False breakout above/below session highs/lows trapping retail breakout traders.",
            colorHex = "#F57C00",
            iconName = "flash_on",
            checklistItems = "Previous High/Low Swept,Quick Rejection Close,Volume Climax",
            scoreWeight = 100,
            isDefault = true,
            isActive = true
        ),
        StrategyEntity(
            id = 4,
            email = email,
            name = "POC Retest (Point of Control)",
            description = "Pullback to high volume node of previous profile for trend continuation.",
            colorHex = "#7B1FA2",
            iconName = "radar",
            checklistItems = "Value Area Acceptance,POC Bounce,Trend Alignment",
            scoreWeight = 100,
            isDefault = true,
            isActive = true
        )
    )
}
