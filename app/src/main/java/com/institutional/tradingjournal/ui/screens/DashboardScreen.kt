package com.institutional.tradingjournal.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry

@Composable
fun DashboardScreen(isDark: Boolean, tradeList: List<TradeEntry>) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    // Counters properly initialized based on tradeList
    val totalTrades = tradeList.size
    val wins = tradeList.count { it.result.equals("WIN", true) || it.pnlAmount > 0 }
    val losses = tradeList.count { it.result.equals("LOSS", true) || it.pnlAmount < 0 }
    val totalPnl = tradeList.sumOf { it.pnlAmount }
    
    var showAnalytics by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(bgColor).padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("📈 Dashboard", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Button(onClick = { showAnalytics = true }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1D28))) {
                Text("📊 Analytics", color = Color(0xFFFFC107), fontSize = 12.sp)
            }
        }
        
        // Summary Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Total Trades", fontSize = 10.sp, color = subTextColor)
                    Text("$totalTrades", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Wins / Losses", fontSize = 10.sp, color = subTextColor)
                    Text("🟢 $wins / 🔴 $losses", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Total PnL: ${if(totalPnl >= 0) "+" else ""}$${totalPnl}", color = if(totalPnl >= 0) Color(0xFF00E676) else Color(0xFFD32F2F), fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)

        if (showAnalytics) {
            AlertDialog(onDismissRequest = { showAnalytics = false }, containerColor = cardBg, title = { Text("📊 Analytics Calendar", color = Color(0xFFFFC107)) }, text = {
                Column(modifier = Modifier.heightIn(max = 400.dp).verticalScroll(rememberScrollState())) {
                    val best = tradeList.maxByOrNull { it.pnlAmount }
                    val worst = tradeList.minByOrNull { it.pnlAmount }
                    Text("Best Trade: ${best?.pair ?: "-"} ($${best?.pnlAmount ?: 0})", color = Color(0xFF00E676), fontSize = 12.sp)
                    Text("Worst Trade: ${worst?.pair ?: "-"} ($${worst?.pnlAmount ?: 0})", color = Color(0xFFD32F2F), fontSize = 12.sp)
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    tradeList.groupBy { it.date }.forEach { (date, trades) ->
                        Text("$date: ${trades.sumOf { it.pnlAmount }}$", color = textColor)
                    }
                }
            }, confirmButton = { Button(onClick = { showAnalytics = false }) { Text("Close") } })
        }
    }
}
