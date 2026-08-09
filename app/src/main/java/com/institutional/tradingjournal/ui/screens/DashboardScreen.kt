package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry

@Composable
fun DashboardScreen(
    isDark: Boolean,
    tradeList: List<TradeEntry>
) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    val totalTrades = tradeList.size
    val wins = tradeList.count { it.result == "WIN" || it.pnlAmount > 0 }
    val losses = tradeList.count { it.result == "LOSS" || it.pnlAmount < 0 }
    val winRate = if (totalTrades > 0) (wins * 100) / totalTrades else 0
    val totalPnl = tradeList.sumOf { it.pnlAmount }

    val bestWin = tradeList.maxOfOrNull { it.pnlAmount } ?: 0.0
    val worstLoss = tradeList.minOfOrNull { it.pnlAmount } ?: 0.0

    var showAnalyticsModal by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "📈 Performance Dashboard",
                    color = Color(0xFFFFC107),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Real-time Orderflow Metrics",
                    color = subTextColor,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = { showAnalyticsModal = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1D28)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("📅 Analytics", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Total Trades", color = subTextColor, fontSize = 11.sp)
                    Text(text = "$totalTrades", color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(text = "Win Rate", color = subTextColor, fontSize = 11.sp)
                    Text(text = "$winRate%", color = Color(0xFF00E676), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total PnL", color = subTextColor, fontSize = 12.sp)
                    val pnlColor = when {
                        totalPnl > 0 -> Color(0xFF00E676)
                        totalPnl < 0 -> Color(0xFFD32F2F)
                        else -> Color(0xFFFFC107)
                    }
                    Text(
                        text = if (totalPnl >= 0) "+$$totalPnl" else "-$$${kotlin.math.abs(totalPnl)}",
                        color = pnlColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "🟢 $wins W", color = Color(0xFF00E676), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "🔴 $losses L", color = Color(0xFFD32F2F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cumulative Equity Growth Curve
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📊 Cumulative Equity Growth Curve",
                    color = Color(0xFFFFC107),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Performance trajectory from Trade 1 to Latest",
                    color = subTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (tradeList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Log trades in Journal to see Equity Graph", color = subTextColor, fontSize = 12.sp)
                    }
                } else {
                    val chronologicalTrades = tradeList.reversed()
                    val cumulativePoints = mutableListOf<Double>()
                    var runningSum = 0.0
                    cumulativePoints.add(0.0)
                    chronologicalTrades.forEach {
                        runningSum += it.pnlAmount
                        cumulativePoints.add(runningSum)
                    }

                    val minVal = cumulativePoints.minOrNull() ?: 0.0
                    val maxVal = cumulativePoints.maxOrNull() ?: 1.0
                    val range = if (maxVal == minVal) 1.0 else maxVal - minVal

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
                    ) {
                        val width = size.width
                        val height = size.height
                        val path = Path()

                        cumulativePoints.forEachIndexed { index, value ->
                            val x = (index.toFloat() / (cumulativePoints.size - 1)) * width
                            val y = height - (((value - minVal) / range).toFloat() * height)
                            if (index == 0) {
                                path.moveTo(x, y)
                            } else {
                                path.lineTo(x, y)
                            }
                        }

                        drawPath(
                            path = path,
                            color = Color(0xFF00E676),
                            style = Stroke(width = 4.dp.toPx())
                        )
                    }
                }
            }
        }
    }

    if (showAnalyticsModal) {
        val groupedByDate = tradeList.groupBy { it.date }

        AlertDialog(
            onDismissRequest = { showAnalyticsModal = false },
            containerColor = cardBg,
            title = {
                Text("📅 Calendar & Analytics Overview", color = Color(0xFFFFC107), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)), modifier = Modifier.weight(1f).padding(end = 4.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Best Win", color = Color.Gray, fontSize = 10.sp)
                                Text("+$${bestWin}", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)), modifier = Modifier.weight(1f).padding(start = 4.dp)) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Worst Loss", color = Color.Gray, fontSize = 10.sp)
                                Text("$${worstLoss}", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    Text("Daily Breakdown:", color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))

                    if (groupedByDate.isEmpty()) {
                        Text("No daily trade history available.", color = subTextColor, fontSize = 11.sp)
                    } else {
                        groupedByDate.forEach { (date, trades) ->
                            val dailyPnl = trades.sumOf { it.pnlAmount }
                            val dailyColor = if (dailyPnl >= 0) Color(0xFF00E676) else Color(0xFFD32F2F)

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("$date (${trades.size} trades)", color = textColor, fontSize = 12.sp)
                                Text(if (dailyPnl >= 0) "+$$dailyPnl" else "-$$${kotlin.math.abs(dailyPnl)}", color = dailyColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showAnalyticsModal = false }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))) {
                    Text("CLOSE", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

