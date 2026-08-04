package com.institutional.tradingjournal.utils

object ScoreCalculator {

    /**
     * Calculates dynamic trade score (0 to 100) based on checklist items completion,
     * emotion discipline, and risk-reward alignment.
     */
    fun calculateScore(
        checkedItemsCount: Int,
        totalChecklistItems: Int,
        emotion: String,
        riskRewardRatio: Double
    ): Pair<Int, String> {
        if (totalChecklistItems == 0) return Pair(50, "C")

        // Checklist completion weight = 70%
        val checklistPercentage = (checkedItemsCount.toDouble() / totalChecklistItems) * 70.0

        // Emotion discipline weight = 15%
        val emotionScore = when (emotion.lowercase()) {
            "calm", "discipline", "confidence" -> 15.0
            "stress" -> 8.0
            "fear", "greed" -> 5.0
            "fomo", "revenge" -> 0.0
            else -> 10.0
        }

        // RR Alignment weight = 15%
        val rrScore = when {
            riskRewardRatio >= 3.0 -> 15.0
            riskRewardRatio >= 2.0 -> 12.0
            riskRewardRatio >= 1.5 -> 8.0
            riskRewardRatio >= 1.0 -> 5.0
            else -> 0.0
        }

        val totalScore = (checklistPercentage + emotionScore + rrScore).toInt().coerceIn(0, 100)

        val grade = when {
            totalScore >= 90 -> "A+"
            totalScore >= 80 -> "A"
            totalScore >= 70 -> "B"
            totalScore >= 50 -> "C"
            else -> "D"
        }

        return Pair(totalScore, grade)
    }
}

