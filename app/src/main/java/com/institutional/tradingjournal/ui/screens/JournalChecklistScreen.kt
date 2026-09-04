package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.data.UserDataStore
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.data.repository.DefaultStrategies
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalChecklistScreen(
    isDark: Boolean = true,
    tradeViewModel: TradeViewModel
) {
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""

    // Theme Palette
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val secondaryCard = if (isDark) Color(0xFF1A1E29) else Color(0xFFEAEFF5)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF6B7280)
    val goldColor = Color(0xFFFFC107)
    val greenColor = Color(0xFF2E7D32)
    val redColor = Color(0xFFC62828)
    val accentBlue = Color(0xFF1976D2)

    // Strategy & Data Sources
    val strategies = remember { DefaultStrategies.getList() }
    var selectedStrategyIndex by remember { mutableStateOf(0) }
    var expandedStrategyDropdown by remember { mutableStateOf(false) }

    // Market Context Selectors
    val sessions = listOf("London Killzone", "NY AM Session", "NY PM Session", "Asian Range")
    var selectedSession by remember { mutableStateOf(sessions[0]) }

    val htfBiases = listOf("BULLISH (HTF)", "BEARISH (HTF)", "CONSOLIDATION")
    var selectedHtfBias by remember { mutableStateOf(htfBiases[0]) }

    // Trade Execution Input Parameters
    var symbol by remember { mutableStateOf("XAUUSD") }
    var direction by remember { mutableStateOf("BUY") }
    var lotSize by remember { mutableStateOf("1.00") }
    var entryPrice by remember { mutableStateOf("") }
    var stopLoss by remember { mutableStateOf("") }
    var takeProfit by remember { mutableStateOf("") }
    var pnlInput by remember { mutableStateOf("") }
    var executionNotes by remember { mutableStateOf("") }

    // Strategy-specific Checklist state
    val currentStrategy = strategies.getOrNull(selectedStrategyIndex)
    val ruleItems = remember(selectedStrategyIndex) {
        val rawItems = currentStrategy?.checklistItems?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() }
        if (!rawItems.isNullOrEmpty()) {
            rawItems
        } else {
            listOf(
                "4H / 1H Higher Timeframe Market Structure Aligned",
                "Key Liquidity Pool Swept (BSL / SSL Purged)",
                "Fair Value Gap (FVG) / Orderflow Imbalance Confirmed",
                "Lower Timeframe (15M/5M) Market Structure Shift (MSS)",
                "Displacement Candle with High Institutional Volume",
                "Strict 1:2 Minimum Risk-to-Reward Verified",
                "Stop Loss anchored behind Structural Invalidation Point",
                "Capital Risk Capped at Maximum 1% per Trade"
            )
        }
    }

    val checkedStates = remember(selectedStrategyIndex) {
        mutableStateMapOf<Int, Boolean>().apply {
            ruleItems.indices.forEach { put(it, false) }
        }
    }

    val totalChecklistCount = ruleItems.size
    val checkedCount = checkedStates.values.count { it }
    val isFullyQualified = totalChecklistCount > 0 && checkedCount == totalChecklistCount

    // Dynamic RR Calculation
    val calculatedRR = remember(entryPrice, stopLoss, takeProfit, direction) {
        val entry = entryPrice.toDoubleOrNull()
        val sl = stopLoss.toDoubleOrNull()
        val tp = takeProfit.toDoubleOrNull()
        if (entry != null && sl != null && tp != null) {
            val risk = if (direction == "BUY") entry - sl else sl - entry
            val reward = if (direction == "BUY") tp - entry else entry - tp
            if (risk > 0 && reward > 0) {
                String.format(Locale.US, "1:%.2f", reward / risk)
            } else "Invalid SL/TP"
        } else "0.00"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        // Screen Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "⚡ Institutional Execution Engine",
                    color = goldColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Orderflow Verification & Pre-Flight Journal",
                    color = subTextColor,
                    fontSize = 12.sp
                )
            }

            Surface(
                color = if (isFullyQualified) greenColor.copy(alpha = 0.2f) else redColor.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isFullyQualified) "READY" else "LOCKED",
                    color = if (isFullyQualified) Color(0xFF4CAF50) else Color(0xFFEF5350),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Strategy Model Selection Dropdown
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Institutional Strategy Model",
                            color = textColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        ExposedDropdownMenuBox(
                            expanded = expandedStrategyDropdown,
                            onExpandedChange = { expandedStrategyDropdown = !expandedStrategyDropdown }
                        ) {
                            OutlinedTextField(
                                value = currentStrategy?.name ?: "Select Strategy",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStrategyDropdown) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                            ExposedDropdownMenu(
                                expanded = expandedStrategyDropdown,
                                onDismissRequest = { expandedStrategyDropdown = false },
                                modifier = Modifier.background(cardBg)
                            ) {
                                strategies.forEachIndexed { index, strat ->
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(strat.name, color = textColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                Text(strat.description, color = subTextColor, fontSize = 11.sp)
                                            }
                                        },
                                        onClick = {
                                            selectedStrategyIndex = index
                                            expandedStrategyDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Card 2: Session & Higher Timeframe Bias Matrix
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Session Timing & Killzone", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            sessions.forEach { s ->
                                val sel = selectedSession == s
                                Surface(
                                    color = if (sel) accentBlue else secondaryCard,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedSession = s }
                                ) {
                                    Text(
                                        text = s.replace(" Killzone", "").replace(" Session", ""),
                                        color = if (sel) Color.White else subTextColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 7.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Higher Timeframe (4H/1D) Structural Bias", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            htfBiases.forEach { b ->
                                val sel = selectedHtfBias == b
                                val chipColor = if (b.startsWith("BULL")) greenColor else if (b.startsWith("BEAR")) redColor else goldColor
                                Surface(
                                    color = if (sel) chipColor else secondaryCard,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedHtfBias = b }
                                ) {
                                    Text(
                                        text = b.split(" ")[0],
                                        color = if (sel) Color.White else subTextColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 7.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Card 3: Execution Trade Parameters (Pair, Direction, Lots, SL, TP, PnL)
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Order Parameters & Risk Plan", color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = symbol,
                                onValueChange = { symbol = it },
                                label = { Text("Pair / Symbol") },
                                modifier = Modifier.weight(1f),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = lotSize,
                                onValueChange = { lotSize = it },
                                label = { Text("Lot Size") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // BUY / SELL Selectors
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { direction = "BUY" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (direction == "BUY") greenColor else secondaryCard
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("BUY / LONG", color = Color.White, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = { direction = "SELL" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (direction == "SELL") redColor else secondaryCard
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("SELL / SHORT", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = entryPrice,
                                onValueChange = { entryPrice = it },
                                label = { Text("Entry") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = stopLoss,
                                onValueChange = { stopLoss = it },
                                label = { Text("SL") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )

                            OutlinedTextField(
                                value = takeProfit,
                                onValueChange = { takeProfit = it },
                                label = { Text("TP") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Calculated RR Target: $calculatedRR", color = goldColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            OutlinedTextField(
                                value = pnlInput,
                                onValueChange = { pnlInput = it },
                                label = { Text("Realized PnL ($)") },
                                modifier = Modifier.width(150.dp),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = executionNotes,
                            onValueChange = { executionNotes = it },
                            label = { Text("Discipline & Execution Mindset Remarks") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Card 4: Strategy Rules Execution Checklist
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pre-Execution Rules Verification",
                                color = textColor,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$checkedCount / $totalChecklistCount Verified",
                                color = if (isFullyQualified) Color(0xFF4CAF50) else goldColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = if (totalChecklistCount > 0) checkedCount.toFloat() / totalChecklistCount.toFloat() else 0f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isFullyQualified) Color(0xFF4CAF50) else goldColor,
                            trackColor = secondaryCard
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ruleItems.forEachIndexed { index, ruleText ->
                            val isChecked = checkedStates[index] ?: false
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChecked) secondaryCard else Color.Transparent)
                                    .clickable { checkedStates[index] = !isChecked }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { checkedStates[index] = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF4CAF50),
                                        uncheckedColor = subTextColor
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = ruleText,
                                    color = if (isChecked) textColor else subTextColor,
                                    fontSize = 12.sp,
                                    fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Direct Action Buttons: Reset & Direct Commit to Room DB
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(
                onClick = {
                    ruleItems.indices.forEach { checkedStates[it] = false }
                    pnlInput = ""
                    executionNotes = ""
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(0.35f)
                    .height(50.dp)
            ) {
                Text("Reset", color = textColor, fontSize = 13.sp)
            }

            Button(
                onClick = {
                    if (!isFullyQualified) {
                        Toast.makeText(
                            context,
                            "⚠️ Pre-execution protocol incomplete: Verify all institutional rules!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val finalPnl = pnlInput.toDoubleOrNull() ?: 0.0
                    val finalLot = lotSize.toDoubleOrNull() ?: 1.0
                    val finalEntry = entryPrice.toDoubleOrNull() ?: 0.0
                    val finalExit = takeProfit.toDoubleOrNull() ?: stopLoss.toDoubleOrNull() ?: 0.0
                    val dateFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

                    val tradeRecord = TradeEntity(
                        email = currentEmail,
                        tradeIdString = "ORD_${System.currentTimeMillis()}",
                        timestamp = System.currentTimeMillis(),
                        pair = symbol.uppercase().trim(),
                        symbol = symbol.uppercase().trim(),
                        direction = direction,
                        lotSize = finalLot,
                        entryPrice = finalEntry,
                        exitPrice = finalExit,
                        pnl = finalPnl,
                        date = dateFormatted,
                        strategyName = currentStrategy?.name ?: "Institutional Orderflow",
                        session = selectedSession,
                        emotion = executionNotes.ifBlank { "Executed with $selectedHtfBias bias under strict rule qualification." }
                    )

                    // Direct SQLite Insertion & State Sync
                    tradeViewModel.insertTrade(tradeRecord)
                    Toast.makeText(context, "✅ Trade Recorded in Journal Database!", Toast.LENGTH_SHORT).show()

                    // Reset form fields
                    pnlInput = ""
                    executionNotes = ""
                    ruleItems.indices.forEach { checkedStates[it] = false }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFullyQualified) greenColor else accentBlue
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(0.65f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Log Trade to Journal History ➔",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
