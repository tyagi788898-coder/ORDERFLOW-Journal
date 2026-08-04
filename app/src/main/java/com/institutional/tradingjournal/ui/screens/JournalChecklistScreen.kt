package com.institutional.tradingjournal.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.data.repository.DefaultStrategies
import com.institutional.tradingjournal.ui.theme.*
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalChecklistScreen(
    viewModel: TradeViewModel
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // State Variables
    var selectedDateText by remember { mutableStateOf(SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())) }
    var selectedPair by remember { mutableStateOf("XAUUSD") }
    var selectedSession by remember { mutableStateOf("Asian") }
    var selectedResult by remember { mutableStateOf("WIN") }
    var mistakeText by remember { mutableStateOf("") }
    var learningText by remember { mutableStateOf("") }

    var showPairDialog by remember { mutableStateOf(false) }
    var showHistorySheet by remember { mutableStateOf(false) }

    var selectedStrategyIndex by remember { mutableStateOf(0) }
    val currentStrategy = DefaultStrategies.list.getOrElse(selectedStrategyIndex) { DefaultStrategies.list[0] }
    val checklistItems = remember(selectedStrategyIndex) { currentStrategy.checklistItems.split("|").filter { it.isNotBlank() } }
    val checkedStates = remember(selectedStrategyIndex) { mutableStateListOf(*Array(checklistItems.size) { false }) }

    val checkedCount = checkedStates.count { it }
    val progressFraction = if (checklistItems.isNotEmpty()) checkedCount.toFloat() / checklistItems.size.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "progress")

    val tradesList by viewModel.allTrades.collectAsState()

    // Pairs List
    val majorPairs = listOf("XAUUSD", "EURUSD", "GBPUSD", "USDJPY", "USDCAD", "AUDUSD", "BTCUSD", "ETHUSD", "US30", "NAS100")

    // DatePicker Dialog setup
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selCal = Calendar.getInstance()
            selCal.set(year, month, dayOfMonth)
            selectedDateText = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selCal.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- HEADER BANNER ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("📊 Institutional", color = GoldPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Trading Journal PRO", color = GoldPrimary, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text("⚡ Orderflow • Footprint • Heatmap • Volume Profile", color = TextMuted, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = GoldPrimary.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // --- SELECTORS CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Date Picker Box
                    SelectorBox(label = "📅 Date", value = selectedDateText, modifier = Modifier.weight(1f)) {
                        datePickerDialog.show()
                    }
                    // Pair Box with Dialog trigger
                    SelectorBox(label = "💱 Pair", value = selectedPair, modifier = Modifier.weight(1f)) {
                        showPairDialog = true
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Session Box
                    SelectorBox(label = "🕒 Session", value = selectedSession, modifier = Modifier.weight(1f)) {
                        selectedSession = when (selectedSession) {
                            "Asian" -> "London"
                            "London" -> "New York"
                            else -> "Asian"
                        }
                    }
                    // Clean Result Box: WIN, LOSS, BREAKEVEN
                    SelectorBox(label = "🎯 Result", value = selectedResult, modifier = Modifier.weight(1f)) {
                        selectedResult = when (selectedResult) {
                            "WIN" -> "LOSS"
                            "LOSS" -> "BREAKEVEN"
                            else -> "WIN"
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- STRATEGY TABS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StrategyTabButton("⭐ Strategy 1", selectedStrategyIndex == 0, Color(0xFF22C55E), Modifier.weight(1f)) { selectedStrategyIndex = 0 }
            StrategyTabButton("🎯 Strategy 2", selectedStrategyIndex == 1, Color(0xFFEF4444), Modifier.weight(1f)) { selectedStrategyIndex = 1 }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StrategyTabButton("🌏 Strategy 3", selectedStrategyIndex == 2, Color(0xFF3B82F6), Modifier.weight(1f)) { selectedStrategyIndex = 2 }
            StrategyTabButton("🚀 Strategy 4", selectedStrategyIndex == 3, Color(0xFFEC4899), Modifier.weight(1f)) { selectedStrategyIndex = 3 }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CHECKLIST & PROGRESS BAR ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(currentStrategy.name, color = GoldPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(14.dp))

                checklistItems.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { checkedStates[index] = !checkedStates[index] },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedStates.getOrElse(index) { false },
                            onCheckedChange = { checkedStates[index] = it },
                            colors = CheckboxDefaults.colors(checkedColor = GoldPrimary, uncheckedColor = Color.White)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(item, color = TextWhite, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = GoldPrimary,
                    trackColor = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text("${(progressFraction * 100).toInt()}% Confluences Matched", color = TextMuted, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TRADE REVIEW SECTION ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📝 Trade Review", color = GoldPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Text("❌ Mistake", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = mistakeText,
                    onValueChange = { mistakeText = it },
                    placeholder = { Text("What mistake was made?", color = TextMuted, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary, unfocusedBorderColor = BorderGlass,
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("💡 Learning", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = learningText,
                    onValueChange = { learningText = it },
                    placeholder = { Text("Key takeaways from this trade", color = TextMuted, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary, unfocusedBorderColor = BorderGlass,
                        focusedTextColor = TextWhite, unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- ACTION BUTTONS: SAVE ENTRY & HISTORY ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val pnlValue = when(selectedResult) {
                        "WIN" -> 300.0
                        "LOSS" -> -100.0
                        else -> 0.0
                    }
                    val newTrade = TradeEntity(
                        date = System.currentTimeMillis(),
                        pair = selectedPair,
                        type = "BUY",
                        session = selectedSession,
                        strategyName = currentStrategy.name,
                        result = selectedResult,
                        pnl = pnlValue,
                        riskReward = 3.0,
                        score = (progressFraction * 100).toInt(),
                        grade = if (progressFraction > 0.8) "A+" else "B",
                        notes = "Mistake: $mistakeText | Learning: $learningText",
                        emotion = "Calm",
                        timeframe = "15m"
                    )
                    viewModel.addTrade(newTrade)
                    mistakeText = ""
                    learningText = ""
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("✅ LOG TRADE ENTRY", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            IconButton(
                onClick = { showHistorySheet = true },
                modifier = Modifier
                    .size(50.dp)
                    .background(SurfaceCard, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderGlass, RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.History, contentDescription = "History", tint = GoldPrimary)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // --- PAIR SELECTION DIALOG ---
    if (showPairDialog) {
        AlertDialog(
            onDismissRequest = { showPairDialog = false },
            title = { Text("Select Instrument / Pair", color = GoldPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    majorPairs.forEach { pair ->
                        Text(
                            text = pair,
                            color = TextWhite,
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedPair = pair
                                    showPairDialog = false
                                }
                                .padding(vertical = 10.dp)
                        )
                        Divider(color = BorderGlass)
                    }
                }
            },
            confirmButton = {},
            containerColor = SurfaceDark
        )
    }

    // --- HISTORY BOTTOM SHEET ---
    if (showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showHistorySheet = false },
            containerColor = SurfaceDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("📜 TRADE HISTORY LOGS", color = GoldPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (tradesList.isEmpty()) {
                    Text("No trades logged yet.", color = TextMuted, fontSize = 14.sp)
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.heightIn(max = 400.dp)
                    ) {
                        items(tradesList) { trade ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("${trade.pair} • ${trade.session}", color = TextWhite, fontWeight = FontWeight.Bold)
                                        Text(trade.strategyName, color = TextMuted, fontSize = 11.sp)
                                    }
                                    Text(
                                        trade.result,
                                        color = when(trade.result) {
                                            "WIN" -> ProfitGreen
                                            "LOSS" -> LossRed
                                            else -> GoldPrimary
                                        },
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
