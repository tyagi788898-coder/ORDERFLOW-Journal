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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CalendarAnalyticsScreens(
    viewModel: TradeViewModel,
    isCalendarView: Boolean
) {
    val totalPnL by viewModel.totalPnL.collectAsState()
    val winRate by viewModel.winRate.collectAsState()
    val totalTradesCount by viewModel.totalTradesCount.collectAsState()
    val allTrades by viewModel.allTrades.collectAsState()

    val currentMonthYear = remember {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
    }

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
            text = if (isCalendarView) "Real-time PnL performance & daily breakdown" else "Win/Loss distributions & risk analytics",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isCalendarView) {
            // Real Dynamic Calendar Heatmap Grid
            Text(currentMonthYear, color = TextWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            val calendar = Calendar.getInstance()
            val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            // Map trades by day of current month
            val dayTradeMap = remember(allTrades) {
                val map = mutableMapOf<Int, String>()
                val sdfDay = SimpleDateFormat("d", Locale.getDefault())
                val sdfMonth = SimpleDateFormat("M", Locale.getDefault())
                val currentM = calendar.get(Calendar.MONTH) + 1

                allTrades.forEach { trade ->
                    val tradeCal = Calendar.getInstance().apply { timeInMillis = trade.date }
                    val tradeM = tradeCal.get(Calendar.MONTH) + 1
                    if (tradeM == currentM) {
                        val dayNum = tradeCal.get(Calendar.DAY_OF_MONTH)
                        map[dayNum] = trade.result
                    }
                }
                map
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(daysInMonth) { index ->
                    val dayNumber = index + 1
                    val resultStatus = dayTradeMap[dayNumber]

                    val isProfitDay = resultStatus == "WIN"
                    val isLossDay = resultStatus == "LOSS"
                    val isBreakevenDay = resultStatus == "BREAKEVEN"

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .background(
                                when {
                                    isProfitDay -> ProfitGreen.copy(alpha = 0.2f)
                                    isLossDay -> LossRed.copy(alpha = 0.2f)
                                    isBreakevenDay -> GoldPrimary.copy(alpha = 0.2f)
                                    else -> SurfaceCard
                                },
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                when {
                                    isProfitDay -> ProfitGreen
                                    isLossDay -> LossRed
                                    isBreakevenDay -> GoldPrimary
                                    else -> BorderGlass
                                },
                                RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("$dayNumber", color = TextWhite, fontSize = 12.sp)
                            when {
                                isProfitDay -> Text("WIN", color = ProfitGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                isLossDay -> Text("LOSS", color = LossRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                isBreakevenDay -> Text("BE", color = GoldPrimary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
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
                    AnalyticsRow("Total Logged Trades", "$totalTradesCount")
                    AnalyticsRow("Win Rate", "${String.format("%.1f", winRate)}%")
                    AnalyticsRow("Net Cumulative PnL", "$${String.format("%.2f", totalPnL)}")
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
