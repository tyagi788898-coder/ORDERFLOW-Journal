package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry

val AnalyticsDarkBg = Color(0xFF090A0F)
val AnalyticsCardBg = Color(0xFF12141C)
val AnalyticsYellow = Color(0xFFFFC107)
val AnalyticsRed = Color(0xFFD32F2F)
val AnalyticsGreen = Color(0xFF00E676)

@Composable
fun CalendarAnalyticsScreens(
    tradeList: List<TradeEntry>,
    onStatusUpdate: (TradeEntry, String) -> Unit
) {
    var selectedTradeForUpdate by remember { mutableStateOf<TradeEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AnalyticsDarkBg)
            .padding(16.dp)
    ) {
        Text(
            text = "📈 Orderflow Journal History",
            color = AnalyticsYellow,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Click any trade to update WIN / LOSS status",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Metrics Overview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = AnalyticsCardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total Logged Trades", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "${tradeList.size}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    val wins = tradeList.count { it.result == "WIN" }
                    val winRate = if (tradeList.isNotEmpty()) (wins * 100) / tradeList.size else 0
                    Text(text = "Win Rate", color = Color.Gray, fontSize = 11.sp)
                    Text(text = "$winRate%", color = AnalyticsGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (tradeList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No trades logged yet.\nLog trades from Journal Tab!",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tradeList) { trade ->
                    TradeHistoryCard(trade = trade, onClick = { selectedTradeForUpdate = trade })
                }
            }
        }
    }

    // Interactive Status Update Dialog / Popup
    selectedTradeForUpdate?.let { trade ->
        AlertDialog(
            onDismissRequest = { selectedTradeForUpdate = null },
            containerColor = AnalyticsCardBg,
            title = {
                Text(
                    text = "Update Result: ${trade.pair} (${trade.strategy})",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(text = "Select final outcome for this trade:", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                onStatusUpdate(trade, "WIN")
                                selectedTradeForUpdate = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AnalyticsGreen),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WIN", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onStatusUpdate(trade, "LOSS")
                                selectedTradeForUpdate = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AnalyticsRed),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("LOSS", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            onStatusUpdate(trade, "BREAKEVEN")
                            selectedTradeForUpdate = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("BREAKEVEN", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun TradeHistoryCard(trade: TradeEntry, onClick: () -> Unit) {
    val statusColor = when (trade.result.uppercase()) {
        "WIN" -> AnalyticsGreen
        "LOSS" -> AnalyticsRed
        "BREAKEVEN" -> Color.Gray
        else -> AnalyticsYellow
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = AnalyticsCardBg),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF2A2E3D), RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = trade.pair,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "•  ${trade.session}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = trade.result,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = trade.strategy, color = AnalyticsYellow, fontSize = 12.sp)
                Text(text = "Score: ${trade.scorePercentage}%", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }

            if (trade.mistake.isNotBlank() || trade.learning.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                if (trade.mistake.isNotBlank()) {
                    Text(text = "❌ ${trade.mistake}", color = AnalyticsRed, fontSize = 11.sp)
                }
                if (trade.learning.isNotBlank()) {
                    Text(text = "💡 ${trade.learning}", color = AnalyticsYellow, fontSize = 11.sp)
                }
            }
        }
    }
}
