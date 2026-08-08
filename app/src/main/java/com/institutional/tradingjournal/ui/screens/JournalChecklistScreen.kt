package com.institutional.tradingjournal.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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

data class StrategyConfig(
    var title: String,
    var sessionDesc: String,
    var items: MutableList<String>,
    var slNote: String,
    var tpNote: String
)

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
    var pnlAmountText by remember { mutableStateOf("0.0") }
    var selectedStrategyIndex by remember { mutableStateOf(0) }

    // Dialog state for WIN/LOSS PnL Input
    var showPnlDialog by remember { mutableStateOf(false) }
    var tempPnlInput by remember { mutableStateOf("") }
    var targetStatusForDialog by remember { mutableStateOf("WIN") }

    // Editable Strategies Memory
    val strategies = remember {
        mutableStateListOf(
            StrategyConfig(
                title = "⭐ STRATEGY 1 — ASIAN RANGE ACCUMULATION",
                sessionDesc = "Session: 05:00 – 11:30 IST",
                items = mutableStateListOf(
                    "📐 Range Boundary Setup : Asian VAH / VAL",
                    "📏 Profile Confirmation : D-Shape / Balanced Profile",
                    "🪤 Liquidity Trap : Fake Breakout of Range",
                    "👣 Auction Completion : Zero Bid / Ask",
                    "↩️ Re-Entry Confirmation : Candle Close Back Inside Range",
                    "📥 Entry Executed",
                    "🛡️ Stop Loss Placed"
                ),
                slNote = "🛑 SL : Asian Range High/Low ±2 Pips",
                tpNote = "🎯 TP : Opposite Range / POC / VAH-VAL"
            ),
            StrategyConfig(
                title = "⚡ STRATEGY 2 — LONDON EXPANSION BREAKOUT",
                sessionDesc = "Session: 12:30 – 16:30 IST",
                items = mutableStateListOf(
                    "💥 Breakout Trigger : Asian Range / SVP Breakout",
                    "🚀 Momentum Confirmation : Strong Candle Velocity",
                    "📊 Orderflow Confirmation : Delta + Stacked Imbalance",
                    "✔️ Acceptance Confirmation : Breakout Hold Above/Below Level",
                    "🔄 Entry Trigger : Micro Retest + Engulfing",
                    "📥 Entry Executed",
                    "🛡️ Stop Loss Placed"
                ),
                slNote = "🛑 SL : Swing High/Low ±3 Pips",
                tpNote = "🎯 TP : Next LVN / Volume Gap"
            ),
            StrategyConfig(
                title = "🛡️ STRATEGY 3 — ABSORPTION REVERSAL",
                sessionDesc = "Session: London Extension / NY Open",
                items = mutableStateListOf(
                    "🧱 Extreme Level : VAH / VAL / POC",
                    "🧊 Market Absorption : Iceberg / Big Bubble",
                    "👣 Auction Completion : Zero Bid / Ask",
                    "📉 Delta Confirmation : Weakness / Divergence",
                    "🔄 Reversal Trigger : Strong Reversal Candle Close",
                    "📥 Entry Executed",
                    "🛡️ Stop Loss Placed"
                ),
                slNote = "🛑 SL : Bubble High/Low ±2 Pips",
                tpNote = "🎯 TP : POC → Mid VA → Opposite VA"
            ),
            StrategyConfig(
                title = "👑 STRATEGY 4 — NY OVERLAP SOVEREIGN",
                sessionDesc = "Session: 17:30 – 21:30 IST",
                items = mutableStateListOf(
                    "📍 Structure Confirmation : FRVP POC Shift + Key Level",
                    "📊 Delta Alignment : Cumulative Delta Trend",
                    "🌊 Liquidity Confirmation : Heatmap Liquidity Vacuum",
                    "⚔️ Aggression Confirmation : 250%+ Diagonal Imbalance",
                    "📥 Entry Executed",
                    "🛡️ Stop Loss Placed"
                ),
                slNote = "🛑 SL : Bubble High/Low ±1.5 Pips",
                tpNote = "🎯 TP : Next SVP Wall / HVN"
            )
        )
    }

    var showEditMenuForStrategy by remember { mutableStateOf<Int?>(null) }
    var editingStrategyIndex by remember { mutableStateOf<Int?>(null) }

    val currentStrategy = strategies[selectedStrategyIndex]
    var checkedStates by remember(selectedStrategyIndex, currentStrategy.items.size) {
        mutableStateOf(List(currentStrategy.items.size) { false })
    }

    val completedCount = checkedStates.count { it }
    val progressPercentage = if (currentStrategy.items.isNotEmpty()) (completedCount * 100) / currentStrategy.items.size else 0

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
                    // STEP 1: Sydney/Asian, London, New York
                    SelectorBox("⏰ Session", session, listOf("Asian (Sydney)", "London", "New York"), { session = it }, cardBg, inputBg, textColor, subTextColor, Modifier.weight(1f))

                    // STEP 2: Interactive Status trigger
                    SelectorBox("🎯 Status", resultStatus, listOf("PENDING", "WIN", "LOSS", "BREAKEVEN"), { newStatus ->
                        resultStatus = newStatus
                        if (newStatus == "WIN" || newStatus == "LOSS") {
                            targetStatusForDialog = newStatus
                            tempPnlInput = ""
                            showPnlDialog = true
                        } else if (newStatus == "BREAKEVEN") {
                            pnlAmountText = "0.0"
                        }
                    }, cardBg, inputBg, textColor, subTextColor, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(inputBg, RoundedCornerShape(6.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💵 Profit/Loss Amount:", color = subTextColor, fontSize = 12.sp)
                        val pnlColor = when {
                            pnlAmountText.startsWith("+") -> Color(0xFF00E676)
                            pnlAmountText.startsWith("-") -> Color(0xFFD32F2F)
                            else -> Color(0xFFFFC107)
                        }
                        Text(text = pnlAmountText, color = pnlColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // STEP 3: Strategy Tabs with Double Click Edit Popup
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Strategy 1", "Strategy 2", "Strategy 3", "Strategy 4").forEachIndexed { index, title ->
                val isSelected = selectedStrategyIndex == index
                Box(modifier = Modifier.weight(1f)) {
                    Button(
                        onClick = {
                            if (selectedStrategyIndex == index) {
                                // Double click detected -> trigger edit option popup
                                showEditMenuForStrategy = if (showEditMenuForStrategy == index) null else index
                            } else {
                                selectedStrategyIndex = index
                                showEditMenuForStrategy = null
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(38.dp),
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

                    // Edit popup trigger overlay
                    if (showEditMenuForStrategy == index) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E2230),
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-45).dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✏️ Edit",
                                    color = Color(0xFFFFC107),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable {
                                            editingStrategyIndex = index
                                            showEditMenuForStrategy = null
                                        }
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "❌",
                                    fontSize = 10.sp,
                                    modifier = Modifier.clickable {
                                        showEditMenuForStrategy = null
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Strategy Checklist Display
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = currentStrategy.title,
                    color = Color(0xFFFFC107),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${currentStrategy.sessionDesc})",
                    color = subTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                currentStrategy.items.forEachIndexed { idx, item ->
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
                            checked = if (idx < checkedStates.size) checkedStates[idx] else false,
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(inputBg, RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            text = currentStrategy.slNote,
                            color = Color(0xFFFF5252),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentStrategy.tpNote,
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
                val numericVal = pnlAmountText.replace("+", "").replace("-", "").replace("$", "").toDoubleOrNull() ?: 0.0
                val finalPnl = if (pnlAmountText.startsWith("-")) -kotlin.math.abs(numericVal) else kotlin.math.abs(numericVal)
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

    // STEP 2 DIALOG: WIN/LOSS Amount Entry Dialog
    if (showPnlDialog) {
        AlertDialog(
            onDismissRequest = { showPnlDialog = false },
            containerColor = cardBg,
            title = {
                Text(
                    text = if (targetStatusForDialog == "WIN") "🎉 Log WIN Amount" else "📉 Log LOSS Amount",
                    color = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Enter $targetStatusForDialog Amount ($):",
                        color = subTextColor,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = tempPnlInput,
                        onValueChange = { tempPnlInput = it },
                        placeholder = { Text("e.g. 250") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F),
                            focusedTextColor = textColor,
                            unfocusedTextColor = textColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val absAmount = tempPnlInput.toDoubleOrNull() ?: 0.0
                        pnlAmountText = if (targetStatusForDialog == "WIN") "+$$absAmount" else "-$$absAmount"
                        showPnlDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFD32F2F)
                    )
                ) {
                    Text("SAVE AMOUNT", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // STEP 3 DIALOG: Strategy Customizer & Checklist Editor Window
    editingStrategyIndex?.let { stratIdx ->
        val stratToEdit = strategies[stratIdx]
        var editTitle by remember { mutableStateOf(stratToEdit.title) }
        var editSessionDesc by remember { mutableStateOf(stratToEdit.sessionDesc) }
        var editSlNote by remember { mutableStateOf(stratToEdit.slNote) }
        var editTpNote by remember { mutableStateOf(stratToEdit.tpNote) }
        val editItems = remember { mutableStateListOf<String>().apply { addAll(stratToEdit.items) } }
        val editScrollState = rememberScrollState()

        AlertDialog(
            onDismissRequest = { editingStrategyIndex = null },
            containerColor = cardBg,
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "✏️ Edit Strategy ${stratIdx + 1}",
                        color = Color(0xFFFFC107),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = Color(0xFF2A0808),
                        shape = CircleShape,
                        modifier = Modifier.clickable { editingStrategyIndex = null }
                    ) {
                        Text(
                            text = " ✕ ",
                            color = Color.Red,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                        .verticalScroll(editScrollState)
                ) {
                    Text(text = "Strategy Title:", color = subTextColor, fontSize = 11.sp)
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Session Description:", color = subTextColor, fontSize = 11.sp)
                    OutlinedTextField(
                        value = editSessionDesc,
                        onValueChange = { editSessionDesc = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "📋 Checklist Items:", color = Color(0xFFFFC107), fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    editItems.forEachIndexed { itemIdx, currentText ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            OutlinedTextField(
                                value = currentText,
                                onValueChange = { editItems[itemIdx] = it },
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { editItems.removeAt(itemIdx) }) {
                                Text("🗑️", fontSize = 14.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Button(
                        onClick = { editItems.add("New Checklist Point") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1D28)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("➕ Add New Checklist Point", color = Color(0xFFFFC107), fontSize = 11.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "🛑 Stop Loss Note:", color = Color(0xFFFF5252), fontSize = 11.sp)
                    OutlinedTextField(
                        value = editSlNote,
                        onValueChange = { editSlNote = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "🎯 Target (TP) Note:", color = Color(0xFF00E676), fontSize = 11.sp)
                    OutlinedTextField(
                        value = editTpNote,
                        onValueChange = { editTpNote = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        stratToEdit.title = editTitle
                        stratToEdit.sessionDesc = editSessionDesc
                        stratToEdit.slNote = editSlNote
                        stratToEdit.tpNote = editTpNote
                        stratToEdit.items.clear()
                        stratToEdit.items.addAll(editItems)
                        editingStrategyIndex = null
                        Toast.makeText(context, "Strategy Updated Successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text("SAVE CHANGES", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        )
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
