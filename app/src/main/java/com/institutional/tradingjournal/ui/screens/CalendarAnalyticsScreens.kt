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

    var viewingTrade by remember { mutableStateOf<TradeEntry?>(null) }
    var reviewingTrade by remember { mutableStateOf<TradeEntry?>(null) }

    var statusChangingTrade by remember { mutableStateOf<TradeEntry?>(null) }
    var showPnlInputDialog by remember { mutableStateOf(false) }
    var targetStatus by remember { mutableStateOf("WIN") }
    var tempPnlText by remember { mutableStateOf("") }

    var mistakeText by remember { mutableStateOf("") }
    var learningText by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "📅 Trade History",
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
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        // Clickable Trade Details Header
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewingTrade = trade }
                        ) {
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

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "Date: ${trade.date}", color = subTextColor, fontSize = 11.sp)

                                // Status Clickable Button (Red Box Target)
                                Surface(
                                    color = Color(0xFF1A1D28),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.clickable { statusChangingTrade = trade }
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Status: ",
                                            color = subTextColor,
                                            fontSize = 11.sp
                                        )
                                        Text(
                                            text = trade.result.uppercase(),
                                            color = Color(0xFFFFC107),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = Color(0xFF2A2E3D)
                        )

                        // Bottom Actions: Edit Notes & Delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✏️ Edit",
                                color = Color(0xFFFFC107),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        reviewingTrade = trade
                                        mistakeText = trade.mistake
                                        learningText = trade.learning
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

    // Status Change Pop-Up Selector Dialog
    statusChangingTrade?.let { trade ->
        AlertDialog(
            onDismissRequest = { statusChangingTrade = null },
            containerColor = cardBg,
            title = {
                Text(
                    text = "🎯 Update Trade Status",
                    color = Color(0xFFFFC107),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("PENDING", "WIN", "LOSS", "BREAKEVEN").forEach { statusOption ->
                        Surface(
                            color = if (trade.result.equals(statusOption, true)) Color(0xFF2A2E3D) else Color(0xFF1A1D28),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (statusOption == "WIN" || statusOption == "LOSS") {
                                        targetStatus = statusOption
                                        tempPnlText = ""
                                        showPnlInputDialog = true
                                    } else if (statusOption == "BREAKEVEN") {
                                        onStatusUpdate(trade, "BREAKEVEN", 0.0, trade.pair, trade.session)
                                        statusChangingTrade = null
                                        Toast.makeText(context, "Status Updated to BREAKEVEN", Toast.LENGTH_SHORT).show()
                                    } else {
                                        onStatusUpdate(trade, "PENDING", 0.0, trade.pair, trade.session)
                                        statusChangingTrade = null
                                        Toast.makeText(context, "Status Updated to PENDING", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        ) {
                            Text(
                                text = statusOption,
                                color = if (statusOption == "WIN") Color(0xFF00E676) else if (statusOption == "LOSS") Color(0xFFD32F2F) else Color(0xFFFFC107),
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { statusChangingTrade = null }) {
                    Text("CANCEL", color = Color.Gray)
                }
            }
        )
    }

    // PnL Amount Dialog after WIN/LOSS Selection
    if (showPnlInputDialog && statusChangingTrade != null) {
        val activeTrade = statusChangingTrade!!
        AlertDialog(
            onDismissRequest = { showPnlInputDialog = false },
            containerColor = cardBg,
            title = {
                Text(
                    text = if (targetStatus == "WIN") "🎉 Log WIN Amount" else "📉 Log LOSS Amount",
                    color = if (targetStatus == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Enter $targetStatus Amount ($):", color = subTextColor, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = tempPnlText,
                        onValueChange = { tempPnlText = it },
                        placeholder = { Text("e.g. 250") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedBorderColor = if (targetStatus == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val absAmount = tempPnlText.toDoubleOrNull() ?: 0.0
                        val finalPnl = if (targetStatus == "WIN") absAmount else -absAmount
                        onStatusUpdate(activeTrade, targetStatus, finalPnl, activeTrade.pair, activeTrade.session)
                        showPnlInputDialog = false
                        statusChangingTrade = null
                        Toast.makeText(context, "Status & PnL Updated!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (targetStatus == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F)
                    )
                ) {
                    Text("SAVE AMOUNT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Read-Only Trade Details View
    viewingTrade?.let { trade ->
        AlertDialog(
            onDismissRequest = { viewingTrade = null },
            containerColor = cardBg,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📋 Full Trade Details", color = Color(0xFFFFC107), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("✕", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.clickable { viewingTrade = null })
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Pair: ${trade.pair}", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Session: ${trade.session}", color = textColor, fontSize = 13.sp)
                    Text("Date: ${trade.date}", color = subTextColor, fontSize = 12.sp)
                    Text("Strategy: ${trade.strategy}", color = Color(0xFFFFC107), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Checklist Score: ${trade.scorePercentage}%", color = Color(0xFF00E676), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color(0xFF2A2E3D))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("❌ Mistake Note:", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(if (trade.mistake.isNotBlank()) trade.mistake else "No mistake noted.", color = textColor, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("💡 Learning Note:", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(if (trade.learning.isNotBlank()) trade.learning else "No learning noted.", color = textColor, fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(onClick = { viewingTrade = null }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))) {
                    Text("CLOSE", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Edit Review Modal (Mistake & Learning)
    reviewingTrade?.let { trade ->
        AlertDialog(
            onDismissRequest = { reviewingTrade = null },
            containerColor = cardBg,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📝 Trade Review", color = Color(0xFFFFC107), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Surface(color = Color(0xFF1A1D28), shape = RoundedCornerShape(12.dp)) {
                        Text("After Trade", color = Color(0xFF00E676), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("❌ Mistake", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = mistakeText,
                        onValueChange = { mistakeText = it },
                        placeholder = { Text("What went wrong during execution?", color = subTextColor, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedBorderColor = Color(0xFFFF5252),
                            focusedContainerColor = Color(0xFF1A1D28),
                            unfocusedContainerColor = Color(0xFF1A1D28)
                        ),
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("💡 Learning", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = learningText,
                        onValueChange = { learningText = it },
                        placeholder = { Text("Key takeaway or lesson learned...", color = subTextColor, fontSize = 11.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor,
                            focusedBorderColor = Color(0xFFFFC107),
                            focusedContainerColor = Color(0xFF1A1D28),
                            unfocusedContainerColor = Color(0xFF1A1D28)
                        ),
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        trade.mistake = mistakeText
                        trade.learning = learningText
                        onStatusUpdate(trade, trade.result, trade.pnlAmount, trade.pair, trade.session)
                        reviewingTrade = null
                        Toast.makeText(context, "Review Saved!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("SAVE REVIEW", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
