package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.institutional.tradingjournal.data.UserDataStore
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    isDark: Boolean = true,
    viewModel: TradeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""
    val allTrades by viewModel.allTrades.collectAsState(initial = emptyList())

    val userTrades = remember(allTrades, currentEmail) {
        if (currentEmail.isBlank()) allTrades
        else allTrades.filter { it.email.equals(currentEmail, ignoreCase = true) || it.email == "default_trader" }
    }

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF757575)

    var showAnalyticsDialog by remember { mutableStateOf(false) }

    val totalTrades = userTrades.size
    val winTrades = userTrades.count { it.pnl > 0 }
    val lossTrades = userTrades.count { it.pnl < 0 }
    val totalPnl = userTrades.sumOf { it.pnl }
    val winRate = if (totalTrades > 0) (winTrades * 100) / totalTrades else 0

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
                    fontSize = 12.sp
                )
            }

            Surface(
                color = Color(0xFF1F2433),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.clickable { showAnalyticsDialog = true }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📊 Analytics", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Top Metrics Row
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Total Trades", color = subTextColor, fontSize = 11.sp)
                    Text("$totalTrades", color = textColor, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Win Rate", color = subTextColor, fontSize = 11.sp)
                    Text("$winRate%", color = if (winRate >= 50) Color(0xFF00E676) else Color(0xFFFFC107), fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Total PnL Card
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Total PnL", color = subTextColor, fontSize = 11.sp)
                    val pnlPrefix = if (totalPnl > 0) "+$" else if (totalPnl < 0) "-$" else "$"
                    val pnlColor = if (totalPnl > 0) Color(0xFF00E676) else if (totalPnl < 0) Color(0xFFEF5350) else Color(0xFFFFC107)
                    Text(
                        text = "$pnlPrefix${kotlin.math.abs(totalPnl)}",
                        color = pnlColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🟢 $winTrades W", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("🔴 $lossTrades L", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cumulative Equity Growth Curve Card
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "📊 Cumulative Equity Growth Curve",
                    color = Color(0xFFFFC107),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "0-Line Baseline: Green Above (Profit) / Red Below (Loss)",
                    color = subTextColor,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                if (userTrades.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Log trades in Journal to view performance curve", color = subTextColor, fontSize = 12.sp)
                    }
                } else {
                    EquityCurveChart(trades = userTrades)
                }
            }
        }
    }

    if (showAnalyticsDialog) {
        AnalyticsCalendarDialog(
            trades = userTrades,
            cardBg = cardBg,
            textColor = textColor,
            subTextColor = subTextColor,
            onDismiss = { showAnalyticsDialog = false }
        )
    }
}

@Composable
fun EquityCurveChart(trades: List<TradeEntity>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(160.dp)) {
        val width = size.width
        val height = size.height
        val midY = height / 2f

        // Draw 0-line baseline
        drawLine(
            color = Color(0xFF2A2E3D),
            start = Offset(0f, midY),
            end = Offset(width, midY),
            strokeWidth = 2f
        )

        var cumulative = 0.0
        val points = mutableListOf(0.0)
        trades.reversed().forEach {
            cumulative += it.pnl
            points.add(cumulative)
        }

        val maxVal = points.maxOfOrNull { kotlin.math.abs(it) }?.coerceAtLeast(10.0) ?: 10.0
        val stepX = width / (points.size - 1).coerceAtLeast(1)

        val path = Path()
        points.forEachIndexed { i, value ->
            val x = i * stepX
            val y = midY - ((value / maxVal) * (height / 2f * 0.85f)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        val strokeColor = if (points.last() >= 0) Color(0xFF00E676) else Color(0xFFEF5350)
        drawPath(path = path, color = strokeColor, style = Stroke(width = 4f))
    }
}

@Composable
fun AnalyticsCalendarDialog(
    trades: List<TradeEntity>,
    cardBg: Color,
    textColor: Color,
    subTextColor: Color,
    onDismiss: () -> Unit
) {
    var calendarMonth by remember { mutableStateOf(Calendar.getInstance()) }
    val currentMonthYear = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendarMonth.time)

    // Calculate best win and worst loss
    val bestWin = trades.filter { it.pnl > 0 }.maxOfOrNull { it.pnl } ?: 0.0
    val worstLoss = trades.filter { it.pnl < 0 }.minOfOrNull { it.pnl } ?: 0.0

    // Group PnL by day for current month
    val dayPnlMap = remember(trades, calendarMonth) {
        val map = mutableMapOf<Int, Double>()
        val monthFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(calendarMonth.time)
        trades.forEach { trade ->
            if (trade.date.contains(monthFormat, ignoreCase = true)) {
                val dayStr = trade.date.trim().split(" ").firstOrNull()
                val dayInt = dayStr?.toIntOrNull()
                if (dayInt != null) {
                    map[dayInt] = (map[dayInt] ?: 0.0) + trade.pnl
                }
            }
        }
        map
    }

    val cal = (calendarMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
    val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0 for Sunday
    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = cardBg,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📊 Advanced Analytics", color = Color(0xFFFFC107), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("✕", color = subTextColor, modifier = Modifier.clickable { onDismiss() })
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Best Win & Worst Loss Boxes
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Best Win", color = subTextColor, fontSize = 10.sp)
                            Text(if (bestWin > 0) "+$$bestWin" else "N/A ($0.0)", color = Color(0xFF00E676), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1D28)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Worst Loss", color = subTextColor, fontSize = 10.sp)
                            Text(if (worstLoss < 0) "-$${kotlin.math.abs(worstLoss)}" else "N/A ($0.0)", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Month Navigation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "◀ Prev",
                        color = Color(0xFFFFC107),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            calendarMonth = (calendarMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }
                        }
                    )
                    Text(text = currentMonthYear, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Next ▶",
                        color = Color(0xFFFFC107),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            calendarMonth = (calendarMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Days of week header
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    listOf("S", "M", "T", "W", "T", "F", "S").forEach {
                        Text(it, color = subTextColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Calendar Grid
                val totalSlots = firstDayOfWeek + daysInMonth
                val rows = (totalSlots + 6) / 7

                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (c in 0 until 7) {
                            val slotIndex = r * 7 + c
                            val dayNum = slotIndex - firstDayOfWeek + 1

                            if (dayNum in 1..daysInMonth) {
                                val pnlForDay = dayPnlMap[dayNum]
                                val dayBg = when {
                                    pnlForDay != null && pnlForDay > 0 -> Color(0xFF1B5E20)
                                    pnlForDay != null && pnlForDay < 0 -> Color(0xFFB71C1C)
                                    pnlForDay != null && pnlForDay == 0.0 -> Color(0xFFF57F17)
                                    else -> Color(0xFF1E2230)
                                }

                                Surface(
                                    color = dayBg,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text("$dayNum", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        if (pnlForDay != null) {
                                            Text(
                                                text = if (pnlForDay > 0) "+${pnlForDay.toInt()}" else "${pnlForDay.toInt()}",
                                                color = Color.White,
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.size(36.dp))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("CLOSE", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    )
}
