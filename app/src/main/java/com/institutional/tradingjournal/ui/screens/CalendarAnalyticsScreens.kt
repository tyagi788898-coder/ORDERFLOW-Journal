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
                        // Clickable Upper Trade Box Area (Red Box Area in Screenshot)
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

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Date: ${trade.date}", color = subTextColor, fontSize = 11.sp)
                                Text(text = "Status: ${trade.result}", color = Color(0xFFFFC107), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = Color(0xFF2A2E3D)
                        )

                        // Action Buttons: Edit (Mistake/Learning Review) & Delete
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

    // 1. Read-Only Detailed Trade View Modal
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
                    Text(
                        text = "📋 Full Trade Details",
                        color = Color(0xFFFFC107),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "✕",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.clickable { viewingTrade = null }
                    )
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text("Pair: ${trade.pair}", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("Session: ${trade.session}", color = textColor, fontSize = 13.sp)
                    Text("Date: ${trade.date}", color = subTextColor, fontSize = 12.sp)
                    Text("Strategy: ${trade.strategy}", color = Color(0xFFFFC107), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Checklist Completion: ${trade.scorePercentage}%", color = Color(0xFF00E676), fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFF2A2E3D))
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("❌ Mistake Note:", color = Color(0xFFFF5252), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (trade.mistake.isNotBlank()) trade.mistake else "No mistake noted.",
                        color = if (trade.mistake.isNotBlank()) textColor else subTextColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text("💡 Learning Note:", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = if (trade.learning.isNotBlank()) trade.learning else "No learning noted.",
                        color = if (trade.learning.isNotBlank()) textColor else subTextColor,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewingTrade = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("CLOSE", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // 2. Trade Review (Mistake & Learning Edit Modal)
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
                    Text(
                        text = "📝 Trade Review",
                        color = Color(0xFFFFC107),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = Color(0xFF1A1D28),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "After Trade",
                            color = Color(0xFF00E676),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = "❌ Mistake", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "💡 Learning", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                        Toast.makeText(context, "Trade Review Saved!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("SAVE REVIEW", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
