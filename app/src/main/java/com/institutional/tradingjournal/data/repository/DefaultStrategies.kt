package com.institutional.tradingjournal.data.repository

import com.institutional.tradingjournal.data.entity.StrategyEntity

object DefaultStrategies {
    val list = listOf(
        StrategyEntity(
            id = 1,
            name = "Strategy 1 – Liquidity Cluster Counter Attack",
            description = "High probability reversal trade off VAL/VAH clusters and POC virgin levels.",
            colorHex = "#22C55E", // Green Accent
            iconName = "Star",
            checklistItems = "📍 Price at VAL / VAH or Virgin POC|🟨 Strong Heatmap Limit Order Wall|🫧 Huge Bubble + Price Freeze|🦵 Extreme BID/ASK = 0|⚖️ 300%+ Diagonal Imbalance|📈 Delta Reversal Confirmed|🎯 Entry Executed|🛡️ SL Correct|🏁 TP (RR ≥ 1:3)",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 2,
            name = "Strategy 2 – All Weather Sniper",
            description = "Multi-confluence institutional setup using Footprint & Heatmap dynamics.",
            colorHex = "#EF4444", // Red Accent
            iconName = "Target",
            checklistItems = "📊 SVP Value Area Confluence|🟨 Heatmap Liquidity Absorption|🫧 Footprint Delta Cluster|🎯 Entry Executed|🛡️ SL Correct|🏁 TP (RR ≥ 1:3)",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 3,
            name = "Strategy 3 – Asian Range Rejection",
            description = "Liquidity sweep and trap inside Asian Session VAH/VAL limits.",
            colorHex = "#3B82F6", // Blue Accent
            iconName = "Globe",
            checklistItems = "🌏 Asian Session Liquidity Sweep|🪤 Fakeout & Return Inside Range|🦵 Extreme Volume Divergence|🎯 Entry Executed|🛡️ SL Correct|🏁 TP (RR ≥ 1:3)",
            scoreWeight = 100,
            isDefault = true
        ),
        StrategyEntity(
            id = 4,
            name = "Strategy 4 – London Over Speed Expansion",
            description = "Momentum expansion strategy following London open range breakouts.",
            colorHex = "#EC4899", // Rocket/Pink Accent
            iconName = "Rocket",
            checklistItems = "🚀 London Open Volume Surge|📈 High Delta Expansion|🔄 Retest of Value Area|🎯 Entry Executed|🛡️ SL Correct|🏁 TP (RR ≥ 1:3)",
            scoreWeight = 100,
            isDefault = true
        )
    )
}

