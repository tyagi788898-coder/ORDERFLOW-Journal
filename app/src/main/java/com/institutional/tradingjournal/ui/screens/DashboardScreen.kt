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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    val wins = tradeList.count { it.result.equals("WIN", true) || it.pnlAmount > 0 }
    val losses = tradeList.count { it.result.equals("LOSS", true) || it.pnlAmount < 0 }
    val winRate = if (totalTrades > 0) (wins * 100) / totalTrades else 0
    val totalPnl = tradeList.sumOf { it.pnlAmount }

    val winTrades = tradeList.filter { it.pnlAmount > 0 }
    val lossTrades = tradeList.filter { it.pnlAmount < 0 }

    val bestWinTrade = winTrades.maxByOrNull { it.pnlAmount }
    val worstLossTrade = lossTrades.minByOrNull { it.pnlAmount }

    var showAnalyticsModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
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
                Text("📊 Analytics", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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

        // Advanced Cumulative Growth Graph with Zero Line Baseline
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
                    text = "0-Line Baseline: Green Above (Profit) / Red Below (Loss)",
                    color = subTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (tradeList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Log trades in Journal to view performance curve", color = subTextColor, fontSize = 12.sp)
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

                    val rawMin = cumulativePoints.minOrNull() ?: 0.0
                    val rawMax = cumulativePoints.maxOrNull() ?: 0.0
                    val minVal = kotlin.math.min(0.0, rawMin)
                    val maxVal = kotlin.math.max(0.0, rawMax)
                    val range = if (maxVal == minVal) 1.0 else maxVal - minVal

                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 8.dp)
                    ) {
                        val width = size.width
                        val height = size.height
                        val zeroY = height - (((0.0 - minVal) / range).toFloat() * height)

                        // Draw Baseline 0 Line
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.4f),
                            start = Offset(0f, zeroY),
                            end = Offset(width, zeroY),
                            strokeWidth = 2.dp.toPx()
                        )

                        for (i in 0 until cumulativePoints.size - 1) {
                            val startVal = cumulativePoints[i]
                            val endVal = cumulativePoints[i + 1]

                            val x1 = (i.toFloat() / (cumulativePoints.size - 1)) * width
                            val y1 = height - (((startVal - minVal) / range).toFloat() * height)

                            val x2 = ((i + 1).toFloat() / (cumulativePoints.size - 1)) * width
                            val y2 = height - (((endVal - minVal) / range).toFloat() * height)

                            val lineColor = if (endVal >= 0) Color(0xFF00E676) else Color(0xFFD32F2F)

                            drawLine(
                                color = lineColor,
                                start = Offset(x1, y1),
                                end = Offset(x2, y2),
                                strokeWidth = 4.dp.toPx()
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAnalyticsModal) {
        var currentCalendar by remember { mutableStateOf(Calendar.getInstance()) }
        var selectedDateString by remember { mutableStateOf<String?>(null) }

        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val dateFormatKey = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        val daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val tempCal = currentCalendar.clone() as Calendar
        tempCal.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1

        val tradesByDate = tradeList.groupBy { it.date }

        AlertDialog(
            onDismissRequest = { showAnalyticsModal = false },
            containerColor = cardBg,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📊 Advanced Analytics", color = Color(0xFFFFC107), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("✕", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.clickable { showAnalyticsModal = false })
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 480.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Best/Worst Trade Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Best Win", color = Color.Gray, fontSize = 10.sp)
                                if (bestWinTrade != null) {
                                    Text("${bestWinTrade.pair} (+$${bestWinTrade.pnlAmount})", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                } else {
                                    Text("N/A ($0.0)", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Worst Loss", color = Color.Gray, fontSize = 10.sp)
                                if (worstLossTrade != null) {
                                    Text("${worstLossTrade.pair} (-$${kotlin.math.abs(worstLossTrade.pnlAmount)})", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                } else {
                                    Text("N/A ($0.0)", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Month Switching Header
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "◀ Prev",
                            color = Color(0xFFFFC107),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                val c = currentCalendar.clone() as Calendar
                                c.add(Calendar.MONTH, -1)
                                currentCalendar = c
                                selectedDateString = null
                            }
                        )
                        Text(monthFormat.format(currentCalendar.time), color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Next ▶",
                            color = Color(0xFFFFC107),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                val c = currentCalendar.clone() as Calendar
                                c.add(Calendar.MONTH, 1)
                                currentCalendar = c
                                selectedDateString = null
                            }
                        )
                    }

                    // Days Grid Header
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                            Text(it, color = subTextColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Calendar Month Grid
                    val totalSlots = firstDayOfWeek + daysInMonth
                    val rows = (totalSlots + 6) / 7

                    for (r in 0 until rows) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            for (c in 0 until 7) {
                                val dayNum = (r * 7 + c) - firstDayOfWeek + 1
                                if (dayNum in 1..daysInMonth) {
                                    val dateCal = currentCalendar.clone() as Calendar
                                    dateCal.set(Calendar.DAY_OF_MONTH, dayNum)
                                    val dateStr = dateFormatKey.format(dateCal.time)
                                    val dayTrades = tradesByDate[dateStr] ?: emptyList()
                                    val dayPnl = dayTrades.sumOf { it.pnlAmount }

                                    val isSelected = selectedDateString == dateStr

                                    val bgBoxColor = when {
                                        isSelected -> Color(0xFFFFC107)
                                        dayTrades.isNotEmpty() && dayPnl >= 0 -> Color(0xFF1B5E20)
                                        dayTrades.isNotEmpty() && dayPnl < 0 -> Color(0xFFB71C1C)
                                        else -> Color(0xFF1A1D28)
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .padding(2.dp)
                                            .background(bgBoxColor, RoundedCornerShape(6.dp))
                                            .clickable { selectedDateString = dateStr },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$dayNum",
                                            color = if (isSelected) Color.Black else textColor,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(36.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date Specific Trades List Display
                    selectedDateString?.let { selDate ->
                        val selectedTrades = tradesByDate[selDate] ?: emptyList()
                        Text("📅 Trades on $selDate (${selectedTrades.size}):", color = Color(0xFFFFC107), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        if (selectedTrades.isEmpty()) {
                            Text("No trades logged on this day.", color = subTextColor, fontSize = 11.sp)
                        } else {
                            selectedTrades.forEach { trade ->
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)),
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("${trade.pair} • ${trade.session}", color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text("Status: ${trade.result}", color = subTextColor, fontSize = 10.sp)
                                        }
                                        val pnlColor = if (trade.pnlAmount >= 0) Color(0xFF00E676) else Color(0xFFD32F2F)
                                        Text(
                                            if (trade.pnlAmount >= 0) "+$$${trade.pnlAmount}" else "-$$${kotlin.math.abs(trade.pnlAmount)}",
                                            color = pnlColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
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
