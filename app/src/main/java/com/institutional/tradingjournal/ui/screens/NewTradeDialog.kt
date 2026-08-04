package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.ui.components.TapSelectGroup
import com.institutional.tradingjournal.ui.theme.*

@Composable
fun NewTradeDialog(
    onDismiss: () -> Unit,
    onSaveTrade: (TradeEntity) -> Unit
) {
    var pair by remember { mutableStateOf("XAUUSD") }
    var direction by remember { mutableStateOf("BUY") }
    var session by remember { mutableStateOf("London") }
    var emotion by remember { mutableStateOf("Calm") }
    var lotSize by remember { mutableStateOf("1.0") }
    var pnl by remember { mutableStateOf("150.0") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = SurfaceDark,
            modifier = Modifier.border(1.dp, BorderGlass, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "LOG INSTITUTIONAL TRADE",
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Direction Select
                Text("Direction", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                TapSelectGroup(
                    options = listOf("BUY", "SELL"),
                    selectedOption = direction,
                    onOptionSelected = { direction = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Session Select
                Text("Session", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                TapSelectGroup(
                    options = listOf("Asian", "London", "New York"),
                    selectedOption = session,
                    onOptionSelected = { session = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Emotion Select
                Text("Emotion State", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                TapSelectGroup(
                    options = listOf("Calm", "FOMO", "Revenge", "Discipline"),
                    selectedOption = emotion,
                    onOptionSelected = { emotion = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Quick PnL Input
                OutlinedTextField(
                    value = pnl,
                    onValueChange = { pnl = it },
                    label = { Text("Net PnL ($)", color = TextMuted) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = BorderGlass,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextMuted)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val newTrade = TradeEntity(
                                tradeIdString = "TRD-${System.currentTimeMillis() % 10000}",
                                timestamp = System.currentTimeMillis(),
                                pair = pair,
                                broker = "Institutional",
                                account = "Primary",
                                direction = direction,
                                lotSize = lotSize.toDoubleOrNull() ?: 1.0,
                                entryPrice = 0.0,
                                exitPrice = 0.0,
                                stopLoss = 0.0,
                                takeProfit = 0.0,
                                riskPercentage = 1.0,
                                rewardPercentage = 3.0,
                                riskRewardRatio = 3.0,
                                pnl = pnl.toDoubleOrNull() ?: 0.0,
                                session = session,
                                strategyId = 1,
                                strategyName = "Orderflow Cluster",
                                tradeScore = 95,
                                tradeGrade = "A+",
                                emotion = emotion,
                                screenshotUris = ""
                            )
                            onSaveTrade(newTrade)
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text("Save Trade", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

