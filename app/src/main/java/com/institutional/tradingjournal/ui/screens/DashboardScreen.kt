package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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

    // WIN/LOSS Logic: result status based or PnL based
    val totalTrades = tradeList.size
    val wins = tradeList.count { it.result.equals("WIN", true) || it.pnlAmount > 0 }
    val losses = tradeList.count { it.result.equals("LOSS", true) || it.pnlAmount < 0 }
    val winRate = if (totalTrades > 0) (wins * 100) / totalTrades else 0
    val totalPnl = tradeList.sumOf { it.pnlAmount }

    val bestWin = tradeList.maxOfOrNull { it.pnlAmount } ?: 0.0
    val worstLoss = tradeList.minOfOrNull { it.pnlAmount } ?: 0.0

    var showAnalyticsModal by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(bgColor).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text("📈 Performance Dashboard", color = Color(0xFFFFC107), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Real-time Orderflow Metrics", color = subTextColor, fontSize = 11.sp)
            }
            Button(onClick = { showAnalyticsModal = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1D28)), shape = RoundedCornerShape(8.dp)) {
                Text("📅 Analytics", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = cardBg), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total Trades", color = subTextColor, fontSize = 11.sp)
                    Text("$totalTrades", color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = cardBg), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Win Rate", color = subTextColor, fontSize = 11.sp)
                    Text("$winRate%", color = Color(0xFF00E676), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Card(colors = CardDefaults.cardColors(containerColor = cardBg), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Total PnL", color = subTextColor, fontSize = 12.sp)
                    Text(if (totalPnl >= 0) "+$$totalPnl" else "-$$${kotlin.math.abs(totalPnl)}", color = if (totalPnl >= 0) Color(0xFF00E676) else Color(0xFFD32F2F), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🟢 $wins W", color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
                    Text("🔴 $losses L", color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Equity Curve Graph
        Card(colors = CardDefaults.cardColors(containerColor = cardBg), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(260.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Cumulative Equity Growth", color = Color(0xFFFFC107), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                if (tradeList.isEmpty()) Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("No data yet", color = subTextColor) }
                else {
                    val points = mutableListOf<Double>(0.0)
                    var running = 0.0
                    tradeList.reversed().forEach { running += it.pnlAmount; points.add(running) }
                    val min = points.minOrNull() ?: 0.0
                    val max = points.maxOrNull() ?: 1.0
                    val range = if (max == min) 1.0 else max - min
                    Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                        val path = Path()
                        points.forEachIndexed { i, v ->
                            val x = (i.toFloat() / (points.size - 1)) * size.width
                            val y = size.height - (((v - min) / range).toFloat() * size.height)
                            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path, Color(0xFF00E676), style = Stroke(width = 4.dp.toPx()))
                    }
                }
            }
        }
    }

    if (showAnalyticsModal) {
        AlertDialog(onDismissRequest = { showAnalyticsModal = false }, containerColor = cardBg, title = { Text("📅 Analytics", color = Color(0xFFFFC107)) }, text = {
            Column {
                Text("Best Win: +$$bestWin", color = Color(0xFF00E676))
                Text("Worst Loss: $${worstLoss}", color = Color(0xFFD32F2F))
                Spacer(modifier = Modifier.height(10.dp))
                tradeList.groupBy { it.date }.forEach { (date, trades) ->
                    val daily = trades.sumOf { it.pnlAmount }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(date, color = textColor)
                        Text(if (daily >= 0) "+$$daily" else "-$$${kotlin.math.abs(daily)}", color = if (daily >= 0) Color(0xFF00E676) else Color(0xFFD32F2F))
                    }
                }
            }
        }, confirmButton = { Button(onClick = { showAnalyticsModal = false }) { Text("Close") } })
    }
}
