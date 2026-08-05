package com.institutional.tradingjournal.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

val DarkBg = Color(0xFF090A0F)
val CardBg = Color(0xFF12141C)
val InputBg = Color(0xFF1A1D28)
val PrimaryYellow = Color(0xFFFFC107)
val AccentRed = Color(0xFFD32F2F)

@Composable
fun JournalChecklistScreen(
    onTradeLogged: (TradeEntry) -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    var selectedDateText by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())) }
    var pair by remember { mutableStateOf("XAUUSD") }
    var session by remember { mutableStateOf("London") }
    var resultStatus by remember { mutableStateOf("PENDING") }
    var selectedStrategyIndex by remember { mutableStateOf(0) }

    var mistakeText by remember { mutableStateOf("") }
    var learningText by remember { mutableStateOf("") }

    val strategyNames = listOf(
        "STRATEGY 1: THE LIQUIDITY CLUSTER COUNTER-ATTACK (5-STAR SOVEREIGN)",
        "STRATEGY 2: THE ALL-WEATHER SNIPER FRAMEWORK (HIGH CONFIRMATION SCALPER)",
        "STRATEGY 3: THE ASIAN RANGE REJECTION (4-STAR REVERSAL)",
        "STRATEGY 4: THE LONDON OVER-SPEED EXPANSION (4-STAR MOMENTUM)"
    )

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

    val checklistItems = remember {
        mutableStateListOf(
            "📍 Price at VAL / VAH or Virgin POC",
            "🟨 Strong Heatmap Limit Order Wall",
            "🫧 Huge Bubble + Price Freeze",
            "🍌 Extreme BID/ASK = 0",
            "⚖️ 300%+ Diagonal Imbalance",
            "📈 Delta Reversal Confirmed",
            "🎯 Entry Executed",
            "🛡️ SL Correct",
            "🏁 TP (RR ≥ 1:3)"
        )
    }
    val checkedStates = remember { mutableStateListOf(*Array(checklistItems.size) { false }) }

    val completedCount = checkedStates.count { it }
    val progressPercentage = if (checklistItems.isNotEmpty()) (completedCount * 100) / checklistItems.size else 0

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "📊 Institutional Trading Journal PRO",
            color = PrimaryYellow,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "⚡ Liquidity • Orderflow • Footprint • Heatmap • Volume Profile",
            color = Color.Gray,
            fontSize = 11.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Date Box with Calendar Trigger
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "📅 Date", color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 2.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(InputBg, RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFF2A2E3D), RoundedCornerShape(6.dp))
                                .clickable { datePickerDialog.show() }
                                .padding(10.dp)
                        ) {
                            Text(text = selectedDateText, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    SelectorBox("💱 Pair", pair, listOf("XAUUSD", "EURUSD", "US30", "BTCUSD"), { pair = it }, Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SelectorBox("⏰ Session", session, listOf("Asian", "London", "NY Open", "NY Close"), { session = it }, Modifier.weight(1f))
                    SelectorBox("🎯 Status", resultStatus, listOf("PENDING", "WIN", "LOSS", "BREAKEVEN"), { resultStatus = it }, Modifier.weight(1f))
                }
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
                        containerColor = if (isSelected) AccentRed else InputBg
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Active Strategy Title Header
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = strategyNames[selectedStrategyIndex],
                    color = PrimaryYellow,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                checklistItems.forEachIndexed { idx, item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { checkedStates[idx] = !checkedStates[idx] }
                            .padding(vertical = 5.dp)
                    ) {
                        Checkbox(
                            checked = checkedStates[idx],
                            onCheckedChange = { checkedStates[idx] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = PrimaryYellow,
                                checkmarkColor = Color.Black,
                                uncheckedColor = Color.Gray
                            )
                        )
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progressPercentage / 100f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = PrimaryYellow,
                    trackColor = InputBg,
                )
                Text(
                    text = "$progressPercentage% Completed",
                    color = PrimaryYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // Review Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "📝 Trade Review", color = PrimaryYellow, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = mistakeText,
                    onValueChange = { mistakeText = it },
                    label = { Text("❌ Mistake", color = AccentRed) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentRed,
                        unfocusedBorderColor = InputBg,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = learningText,
                    onValueChange = { learningText = it },
                    label = { Text("💡 Learning", color = PrimaryYellow) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryYellow,
                        unfocusedBorderColor = InputBg,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Button(
            onClick = {
                val newEntry = TradeEntry(
                    date = selectedDateText,
                    pair = pair,
                    session = session,
                    strategy = "Strat ${selectedStrategyIndex + 1}",
                    result = resultStatus,
                    scorePercentage = progressPercentage,
                    mistake = mistakeText,
                    learning = learningText
                )
                onTradeLogged(newEntry)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryYellow),
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
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(text = label, color = Color.Gray, fontSize = 11.sp, modifier = Modifier.padding(bottom = 2.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(InputBg, RoundedCornerShape(6.dp))
                .border(1.dp, Color(0xFF2A2E3D), RoundedCornerShape(6.dp))
                .clickable { expanded = true }
                .padding(10.dp)
        ) {
            Text(text = value, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(CardBg)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option, color = Color.White) },
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
