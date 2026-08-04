package com.institutional.tradingjournal.data.repository

import com.institutional.tradingjournal.data.entity.StrategyEntity

object DefaultStrategies {
    val list = listOf(
        StrategyEntity(
            id = 1,
            name = "Liquidity Cluster Counter Attack",
            description = "High probability reversal trade off VAL/VAH clusters and POC virgin levels.",
            colorHex = "#FFD700", // Gold
            iconName = "FlashOn",
            checklistItems = "Price at VAL/VAH|Virgin POC|Heatmap Wall|Bubble Freeze|Extreme 0|300% Imbalance|Delta Shift|Entry|SL|TP|RR 1:3+",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 2,
            name = "All Weather Sniper",
            description = "Multi-confluence institutional setup using Footprint & Heatmap dynamics.",
            colorHex = "#00FF7F", // Spring Green
            iconName = "TrackChanges",
            checklistItems = "SVP|Heatmap|Bubble|Footprint|4 Confirmation|Entry|SL|TP",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 3,
            name = "Asian Range Rejection",
            description = "Liquidity sweep and trap inside Asian Session VAH/VAL limits.",
            colorHex = "#00E5FF", // Cyan
            iconName = "Map",
            checklistItems = "FRVP|Asian VAH|Asian VAL|Trap|Return Inside|Extreme 0|Entry|SL|TP",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 4,
            name = "London Over Speed Expansion",
            description = "Momentum expansion strategy following London open range breakouts.",
            colorHex = "#FF3D00", // Bright Red-Orange
            iconName = "TrendingUp",
            checklistItems = "Range Break|Velocity|Pullback|Retest|Engulfing|Entry|SL|TP",
            scoreWeight = 100,
            isDefault = true
        )
    )
}

