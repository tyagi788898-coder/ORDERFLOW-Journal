package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.theme.*
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@Composable
fun CalendarAnalyticsScreens(
    viewModel: TradeViewModel,
    isCalendarView: Boolean
) {
    val totalPnL by viewModel.totalPnL.collectAsState()
    val winRate by viewModel.winRate.collectAsState()
    val totalTrades by viewModel.totalTradesCount.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = if (isCalendarView) "TRADING CALENDAR HEATMAP" else "INSTITUTIONAL ANALYTICS",
            color = GoldPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (isCalendarView) "Monthly PnL performance & daily breakdown" else "Win/Loss distributions & risk analytics",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isCalendarView) {
            // Mock Calendar Heatmap Grid
            Text("August 2026", color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(31) { day ->
                    val isProfitDay = day % 3 == 0
                    val isLossDay = day % 5 == 0 && !isProfitDay

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                when {
                                    isProfitDay -> ProfitGreen.copy(alpha = 0.2f)
                                    isLossDay -> LossRed.copy(alpha = 0.2f)
                                    else -> SurfaceCard
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                when {
                                    isProfitDay -> ProfitGreen
                                    isLossDay -> LossRed
                                    else -> BorderGlass
                                },
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("${day + 1}", color = TextWhite, fontSize = 12.sp)
                            if (isProfitDay) Text("+$250", color = ProfitGreen, fontSize = 9.sp)
                            if (isLossDay) Text("-$100", color = LossRed, fontSize = 9.sp)
                        }
                    }
                }
            }
        } else {
            // Detailed Analytics Metrics
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Performance Summary", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    AnalyticsRow("Win Rate", "${String.format("%.1f", winRate)}%")
                    AnalyticsRow("Profit Factor", "2.45")
                    AnalyticsRow("Average Risk Reward", "1:3.2")
                    AnalyticsRow("Max Drawdown", "2.8%")
                    AnalyticsRow("Best Session", "London")
                    AnalyticsRow("Most Profitable Pair", "XAUUSD")
                }
            }
        }
    }
}

@Composable
fun AnalyticsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, fontSize = 13.sp)
        Text(value, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

