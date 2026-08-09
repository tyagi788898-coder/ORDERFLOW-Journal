package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.model.TradeEntry

@Composable
fun CalendarAnalyticsScreens(
    isDark: Boolean,
    tradeList: List<TradeEntry>,
    onDeleteTrade: (TradeEntry) -> Unit,
    onStatusUpdate: (TradeEntry, String, Double, String, String) -> Unit
) {
    val context = LocalContext.current
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    var editingTrade by remember { mutableStateOf<TradeEntry?>(null) }
    var newStatus by remember { mutableStateOf("WIN") }
    var newPnlText by remember { mutableStateOf("") }
    var newPair by remember { mutableStateOf("") }
    var newSession by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "📅 Trade History & Analytics",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "View and manage logged institutional trades",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (tradeList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(cardBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No trades logged yet.",
                    color = subTextColor,
                    fontSize = 14.sp
                )
            }
        } else {
            tradeList.forEach { trade ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${trade.pair} • ${trade.session}",
                                color = textColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            val pnlColor = when {
                                trade.pnlAmount > 0 -> Color(0xFF00E676)
                                trade.pnlAmount < 0 -> Color(0xFFD32F2F)
                                else -> Color(0xFFFFC107)
                            }
                            Text(
                                text = if (trade.pnlAmount >= 0) "+$${trade.pnlAmount}" else "-$${kotlin.math.abs(trade.pnlAmount)}",
                                color = pnlColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Date: ${trade.date}", color = subTextColor, fontSize = 11.sp)
                            Text(text = "Status: ${trade.result}", color = Color(0xFFFFC107), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = Color(0xFF2A2E3D)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚙️ Edit",
                                color = Color(0xFFFFC107),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        editingTrade = trade
                                        newStatus = trade.result
                                        newPnlText = trade.pnlAmount.toString()
                                        newPair = trade.pair
                                        newSession = trade.session
                                    }
                                    .padding(end = 16.dp)
                            )
                            Text(
                                text = "🗑️ Delete",
                                color = Color(0xFFFF5252),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    onDeleteTrade(trade)
                                    Toast.makeText(context, "Trade Deleted", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Scrollable Edit Trade Dialog with Soft Keyboard Adjustments
    editingTrade?.let { trade ->
        var statusExpanded by remember { mutableStateOf(false) }
        val dialogScrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { editingTrade = null },
            containerColor = cardBg,
            title = {
                Text(
                    text = "⚙️ Edit Trade Log",
                    color = Color(0xFFFFC107),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(dialogScrollState)
                ) {
                    Text(text = "Pair Symbol:", color = subTextColor, fontSize = 11.sp)
                    OutlinedTextField(
                        value = newPair,
                        onValueChange = { newPair = it },
                        textStyle = TextStyle(color = textColor, fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedBorderColor = Color(0xFFFFC107)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Session:", color = subTextColor, fontSize = 11.sp)
                    OutlinedTextField(
                        value = newSession,
                        onValueChange = { newSession = it },
                        textStyle = TextStyle(color = textColor, fontSize = 14.sp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedBorderColor = Color(0xFFFFC107)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Status:", color = subTextColor, fontSize = 11.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF1A1D28), RoundedCornerShape(6.dp))
                            .clickable { statusExpanded = true }
                            .padding(12.dp)
                    ) {
                        Text(text = newStatus, color = Color(0xFFFFC107), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        DropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            listOf("WIN", "LOSS", "BREAKEVEN", "PENDING").forEach { statusOption ->
                                DropdownMenuItem(
                                    text = { Text(text = statusOption) },
                                    onClick = {
                                        newStatus = statusOption
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "PnL Amount ($):", color = subTextColor, fontSize = 11.sp)
                    OutlinedTextField(
                        value = newPnlText,
                        onValueChange = { newPnlText = it },
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFFFFC107),
                            unfocusedBorderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF1A1D28),
                            unfocusedContainerColor = Color(0xFF1A1D28)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        var parsedPnl = newPnlText.toDoubleOrNull() ?: trade.pnlAmount
                        if (newStatus == "LOSS" && parsedPnl > 0) {
                            parsedPnl = -parsedPnl
                        } else if (newStatus == "WIN" && parsedPnl < 0) {
                            parsedPnl = kotlin.math.abs(parsedPnl)
                        } else if (newStatus == "BREAKEVEN") {
                            parsedPnl = 0.0
                        }

                        onStatusUpdate(trade, newStatus, parsedPnl, newPair, newSession)
                        editingTrade = null
                        Toast.makeText(context, "Trade Updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("SAVE", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
