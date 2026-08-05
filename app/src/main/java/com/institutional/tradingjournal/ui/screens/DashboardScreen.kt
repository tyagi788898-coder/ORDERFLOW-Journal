package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry

@Composable
fun MainDashboardScreen(
    tradeList: List<TradeEntry>,
    onNavigateToJournal: () -> Unit = {}
) {
    val totalTrades = tradeList.size
    val wins = tradeList.count { it.result.uppercase() == "WIN" }
    val losses = tradeList.count { it.result.uppercase() == "LOSS" }
    
    val winRate = if (totalTrades > 0) (wins * 100) / totalTrades else 0
    val profitFactor = if (losses > 0) String.format("%.2f", wins.toDouble() / losses) else if (wins > 0) "MAX" else "0.0"
    
    // Calculated net metric estimation
    val netPnL = (wins * 300) - (losses * 100)
    val pnlString = if (netPnL >= 0) "+$$netPnL" else "-$${kotlin.math.abs(netPnL)}"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090A0F))
            .padding(16.dp)
    ) {
        Text(
            text = "Institutional Orderflow",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Live Trading Performance & Metrics",
            color = Color.Gray,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Win Rate", "$winRate%", Color(0xFF00E676), Modifier.weight(1f))
            MetricCard("Profit Factor", profitFactor, Color(0xFFFFC107), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Total Trades", "$totalTrades", Color.White, Modifier.weight(1f))
            MetricCard("Net PnL", pnlString, if (netPnL >= 0) Color(0xFF00E676) else Color(0xFFD32F2F), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Quick Actions",
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Button(
            onClick = onNavigateToJournal,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("+ Log New Orderflow Trade", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12141C)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
