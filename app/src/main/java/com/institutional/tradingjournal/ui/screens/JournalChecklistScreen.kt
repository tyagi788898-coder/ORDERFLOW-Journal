package com.institutional.tradingjournal.ui.screens

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun JournalChecklistScreen(
    isDark: Boolean,
    onTradeLogged: (TradeEntry) -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val inputBg = if (isDark) Color(0xFF1A1D28) else Color(0xFFEBEFF5)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    var selectedDateText by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())) }
    var pair by remember { mutableStateOf("XAUUSD") }
    var session by remember { mutableStateOf("London") }
    var resultStatus by remember { mutableStateOf("PENDING") }
    var pnlAmountText by remember { mutableStateOf("") }
    var selectedStrategyIndex by remember { mutableStateOf(0) }

    val strategyTitles = listOf(
        "⭐ STRATEGY 1 — ASIAN RANGE ACCUMULATION",
        "⚡ STRATEGY 2 — LONDON EXPANSION BREAKOUT",
        "🛡️ STRATEGY 3 — ABSORPTION REVERSAL",
        "👑 STRATEGY 4 — NY OVERLAP SOVEREIGN"
    )

    val strategySessions = listOf(
        "Session: 05:00 – 11:30 IST",
        "Session: 12:30 – 16:30 IST",
        "Session: London Extension / NY Open",
        "Session: 17:30 – 21:30 IST"
    )

    val checklistsPerStrategy = listOf(
        listOf(
            "📐 Range Boundary Setup : Asian VAH / VAL",
            "📏 Profile Confirmation : D-Shape / Balanced Profile",
            "🪤 Liquidity Trap : Fake Breakout of Range",
            "👣 Auction Completion : Zero Bid / Ask",
            "↩️ Re-Entry Confirmation : Candle Close Back Inside Range",
            "📥 Entry Executed",
            "🛡️ Stop Loss Placed"
        ),
        listOf(
            "💥 Breakout Trigger : Asian Range / SVP Breakout",
            "🚀 Momentum Confirmation : Strong Candle Velocity",
            "📊 Orderflow Confirmation : Delta + Stacked Imbalance",
            "✔️ Acceptance Confirmation : Breakout Hold Above/Below Level",
            "🔄 Entry Trigger : Micro Retest + Engulfing",
            "📥 Entry Executed",
            "🛡️ Stop Loss Placed"
        ),
        listOf(
            "🧱 Extreme Level : VAH / VAL / POC",
            "🧊 Market Absorption : Iceberg / Big Bubble",
            "👣 Auction Completion : Zero Bid / Ask",
            "📉 Delta Confirmation : Weakness / Divergence",
            "🔄 Reversal Trigger : Strong Reversal Candle Close",
            "📥 Entry Executed",
            "🛡️ Stop Loss Placed"
        ),
        listOf(
            "📍 Structure Confirmation : FRVP POC Shift + Key Level",
            "📊 Delta Alignment : Cumulative Delta Trend",
            "🌊 Liquidity Confirmation : Heatmap Liquidity Vacuum",
            "⚔️ Aggression Confirmation : 250%+ Diagonal Imbalance",
            "📥 Entry Executed",
            "🛡️ Stop Loss Placed"
        )
    )

    val slTpNotes = listOf(
        Pair("🛑 SL : Asian Range High/Low ±2 Pips", "🎯 TP : Opposite Range / POC / VAH-VAL"),
        Pair("🛑 SL : Swing High/Low ±3 Pips", "🎯 TP : Next LVN / Volume Gap"),
        Pair("🛑 SL : Bubble High/Low ±2 Pips", "🎯 TP : POC → Mid VA → Opposite VA"),
        Pair("🛑 SL : Bubble High/Low ±1.5 Pips", "🎯 TP : Next SVP Wall / HVN")
    )

    val currentChecklist = checklistsPerStrategy[selectedStrategyIndex]
    var checkedStates by remember(selectedStrategyIndex) { mutableStateOf(List(currentChecklist.size) { false }) }

    val completedCount = checkedStates.count { it }
    val progressPercentage = if (currentChecklist.isNotEmpty()) (completedCount * 100) / currentChecklist.size else 0

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDateText = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "📊 Institutional Trading Journal PRO",
            color = Color(0xFFFFC107),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "⚡ Liquidity • Orderflow • Footprint • Heatmap • Volume Profile",
            color = subTextColor,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "📅 Date", color = subTextColor, fontSize = 11.sp, modifier = Modifier.padding(bottom = 2.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(inputBg, RoundedCornerShape(6.dp))
                                .clickable { datePickerDialog.show() }
                                .padding(10.dp)
                        ) {
                            Text(text = selectedDateText, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    SelectorBox("💱 Pair", pair, listOf("XAUUSD", "EURUSD", "US30", "BTCUSD"), { pair = it }, cardBg, inputBg, textColor, subTextColor, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SelectorBox("⏰ Session", session, listOf("Asian", "London", "NY Open", "NY Close"), { session = it }, cardBg, inputBg, textColor, subTextColor, Modifier.weight(1f))
                    SelectorBox("🎯 Status", resultStatus, listOf("PENDING", "WIN", "LOSS", "BREAKEVEN"), { resultStatus = it }, cardBg, inputBg, textColor, subTextColor, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pnlAmountText,
                    onValueChange = { pnlAmountText = it },
                    label = { Text("💵 Profit/Loss Amount ($)", color = Color(0xFFFFC107)) },
                    placeholder = { Text("e.g. 250 or -100", color = subTextColor) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFC107),
                        unfocusedBorderColor = inputBg,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Strategy Selector Tabs
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Strategy 1", "Strategy 2", "Strategy 3", "Strategy 4").forEachIndexed { index, title ->
                val isSelected = selectedStrategyIndex == index
                Button(
                    onClick = { selectedStrategyIndex = index },
                    modifier = Modifier.weight(1f).height(38.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFD32F2F) else inputBg
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else subTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Strategy Checklist Card
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strategyTitles[selectedStrategyIndex],
                    color = Color(0xFFFFC107),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${strategySessions[selectedStrategyIndex]})",
                    color = subTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                currentChecklist.forEachIndexed { idx, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                checkedStates = checkedStates.toMutableList().also { it[idx] = !it[idx] }
                            }
                            .padding(vertical = 5.dp)
                    ) {
                        Checkbox(
                            checked = checkedStates[idx],
                            onCheckedChange = { checkedVal ->
                                checkedStates = checkedStates.toMutableList().also { it[idx] = checkedVal }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFFFFC107),
                                checkmarkColor = Color.Black,
                                uncheckedColor = subTextColor
                            )
                        )
                        Text(
                            text = item,
                            color = textColor,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progressPercentage / 100f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFFFFC107),
                    trackColor = inputBg,
                )
                Text(
                    text = "$progressPercentage% Completed",
                    color = Color(0xFFFFC107),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                // Static SL & TP Parameters Note Box
                val currentSlTp = slTpNotes[selectedStrategyIndex]
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(inputBg, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = currentSlTp.first,
                            color = Color(0xFFFF5252),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentSlTp.second,
                            color = Color(0xFF00E676),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                val parsedPnl = pnlAmountText.toDoubleOrNull() ?: 0.0
                val finalPnl = if (resultStatus.uppercase() == "LOSS") -kotlin.math.abs(parsedPnl) else kotlin.math.abs(parsedPnl)
                val newEntry = TradeEntry(
                    date = selectedDateText,
                    pair = pair,
                    session = session,
                    strategy = "Strategy ${selectedStrategyIndex + 1}",
                    result = resultStatus,
                    pnlAmount = finalPnl,
                    scorePercentage = progressPercentage
                )
                onTradeLogged(newEntry)
                Toast.makeText(context, "Trade Logged Successfully!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "⚡ LOG TRADE TO JOURNAL HISTORY",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun SelectorBox(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    cardBg: Color,
    inputBg: Color,
    textColor: Color,
    subTextColor: Color,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(text = label, color = subTextColor, fontSize = 11.sp, modifier = Modifier.padding(bottom = 2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(inputBg, RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(10.dp)
        ) {
            Text(text = value, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(cardBg)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, color = textColor) },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
