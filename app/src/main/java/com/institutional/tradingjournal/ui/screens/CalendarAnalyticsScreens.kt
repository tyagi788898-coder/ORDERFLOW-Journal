package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
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

@Composable
fun CalendarAnalyticsScreens(
    tradeList: List<TradeEntry>,
    isDark: Boolean,
    onStatusUpdate: (TradeEntry, String, Double, String, String) -> Unit
) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    var selectedTradeForUpdate by remember { mutableStateOf<TradeEntry?>(null) }
    var inputPnlText by remember { mutableStateOf("") }
    var inputMistake by remember { mutableStateOf("") }
    var inputLearning by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Text(
            text = "📈 Orderflow Journal History",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Click any trade to update WIN / LOSS, PnL & Review",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total Logged Trades", color = subTextColor, fontSize = 11.sp)
                    Text(text = "${tradeList.size}", color = textColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    val wins = tradeList.count { it.result.uppercase() == "WIN" }
                    val winRate = if (tradeList.isNotEmpty()) (wins * 100) / tradeList.size else 0
                    Text(text = "Win Rate", color = subTextColor, fontSize = 11.sp)
                    Text(text = "$winRate%", color = Color(0xFF00E676), fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
                    color = subTextColor,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tradeList) { trade ->
                    TradeHistoryCard(trade = trade, cardBg = cardBg, textColor = textColor, subTextColor = subTextColor, onClick = {
                        selectedTradeForUpdate = trade
                        inputPnlText = kotlin.math.abs(trade.pnlAmount).toString()
                        inputMistake = trade.mistake
                        inputLearning = trade.learning
                    })
                }
            }
        }
    }

    selectedTradeForUpdate?.let { trade ->
        AlertDialog(
            onDismissRequest = { selectedTradeForUpdate = null },
            containerColor = cardBg,
            title = {
                Text(
                    text = "Update Result: ${trade.pair} (${trade.strategy})",
                    color = textColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(text = "Enter PnL Amount ($):", color = subTextColor, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = inputPnlText,
                        onValueChange = { inputPnlText = it },
                        placeholder = { Text("e.g. 100") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFC107),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Trade Review inside Dialog
                    Text(text = "📝 Trade Review", color = Color(0xFFFFC107), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = inputMistake,
                        onValueChange = { inputMistake = it },
                        label = { Text("❌ Mistake", color = Color(0xFFD32F2F)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD32F2F),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = inputLearning,
                        onValueChange = { inputLearning = it },
                        label = { Text("💡 Learning", color = Color(0xFFFFC107)) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFC107),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val amount = kotlin.math.abs(inputPnlText.toDoubleOrNull() ?: 0.0)
                                onStatusUpdate(trade, "WIN", amount, inputMistake, inputLearning)
                                selectedTradeForUpdate = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("WIN", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                val amount = kotlin.math.abs(inputPnlText.toDoubleOrNull() ?: 0.0)
                                onStatusUpdate(trade, "LOSS", -amount, inputMistake, inputLearning)
                                selectedTradeForUpdate = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("LOSS", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}
