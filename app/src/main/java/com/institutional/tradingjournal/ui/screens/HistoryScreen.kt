package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.institutional.tradingjournal.data.UserDataStore
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

fun extractStatusFromTrade(trade: TradeEntity): String {
    val meta = trade.emotion
    return if (meta.contains("STATUS:")) {
        meta.substringAfter("STATUS:").substringBefore("|").trim()
    } else if (trade.pnl > 0) "WIN"
    else if (trade.pnl < 0) "LOSS"
    else "PENDING"
}

fun extractScoreFromTrade(trade: TradeEntity): String {
    val meta = trade.emotion
    return if (meta.contains("SCORE:")) {
        meta.substringAfter("SCORE:").substringBefore("|").trim()
    } else {
        "0%"
    }
}

fun extractMistake(trade: TradeEntity): String {
    val meta = trade.emotion
    return if (meta.contains("MISTAKE:")) {
        meta.substringAfter("MISTAKE:").substringBefore("|").trim()
    } else {
        ""
    }
}

fun extractLearning(trade: TradeEntity): String {
    val meta = trade.emotion
    return if (meta.contains("LEARNING:")) {
        meta.substringAfter("LEARNING:").trim()
    } else {
        ""
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    isDark: Boolean = true,
    viewModel: TradeViewModel = hiltViewModel(),
    tradeViewModel: TradeViewModel = viewModel
) {
    val actualViewModel = tradeViewModel
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""
    val allTradesList by actualViewModel.allTrades.collectAsState(initial = emptyList())

    val trades = remember(allTradesList, currentEmail) {
        if (currentEmail.isBlank()) allTradesList
        else allTradesList.filter { 
            it.email.equals(currentEmail, ignoreCase = true) || it.email == "default_trader" 
        }
    }

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val inputBg = if (isDark) Color(0xFF1A1D28) else Color(0xFFEBEFF5)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF757575)

    var searchQuery by remember { mutableStateOf("") }
    var filterTab by remember { mutableStateOf("ALL") }

    var tradeToEdit by remember { mutableStateOf<TradeEntity?>(null) }
    var tradeToDelete by remember { mutableStateOf<TradeEntity?>(null) }
    var viewPopupTitle by remember { mutableStateOf<String?>(null) }
    var viewPopupContent by remember { mutableStateOf<String?>(null) }

    val filteredTrades = trades.filter { trade ->
        val currentStatus = extractStatusFromTrade(trade)
        val matchesSearch = trade.symbol.contains(searchQuery, ignoreCase = true) ||
                trade.pair.contains(searchQuery, ignoreCase = true) ||
                trade.strategyName.contains(searchQuery, ignoreCase = true)

        val matchesTab = when (filterTab) {
            "WIN" -> currentStatus == "WIN" || trade.pnl > 0
            "LOSS" -> currentStatus == "LOSS" || trade.pnl < 0
            else -> true
        }
        matchesSearch && matchesTab
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Text(
            text = "📜 Trade Execution History",
            color = Color(0xFFFFC107),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Total Entries: ${filteredTrades.size}",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by pair, symbol or strategy...", color = subTextColor, fontSize = 12.sp) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFC107),
                unfocusedBorderColor = Color(0xFF2A2E3D)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "WIN", "LOSS").forEach { tab ->
                val isSelected = filterTab == tab
                Button(
                    onClick = { filterTab = tab },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFFFFC107) else inputBg
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = tab,
                        color = if (isSelected) Color.Black else textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        if (filteredTrades.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No trade records found.", color = subTextColor, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTrades, key = { it.id }) { trade ->
                    val statusStr = extractStatusFromTrade(trade)
                    val scoreStr = extractScoreFromTrade(trade)
                    val mistakeStr = extractMistake(trade)
                    val learningStr = extractLearning(trade)

                    val statusColor = when (statusStr) {
                        "WIN" -> Color(0xFF00E676)
                        "LOSS" -> Color(0xFFEF5350)
                        "BREAKEVEN" -> Color(0xFF9E9E9E)
                        else -> Color(0xFFFFC107)
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Top Row: Pair Name + Status Badge + Score + PnL
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = trade.symbol.ifBlank { trade.pair },
                                        color = textColor,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Surface(
                                        color = statusColor.copy(alpha = 0.18f),
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor)
                                    ) {
                                        Text(
                                            text = statusStr,
                                            color = statusColor,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        color = Color(0xFF1E2638),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Score: $scoreStr",
                                            color = Color(0xFFFFC107),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                val pnlColor = when {
                                    trade.pnl > 0 -> Color(0xFF00E676)
                                    trade.pnl < 0 -> Color(0xFFEF5350)
                                    else -> Color(0xFFFFC107)
                                }
                                val pnlPrefix = if (trade.pnl > 0) "+$" else if (trade.pnl < 0) "-$" else "$"
                                Text(
                                    text = "$pnlPrefix${kotlin.math.abs(trade.pnl)}",
                                    color = pnlColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "${trade.date}  •  ${trade.session}  •  ${trade.strategyName}",
                                color = subTextColor,
                                fontSize = 11.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Bottom Buttons: Equal Size, High Brightness Yellow / Red Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mistake Button
                                Surface(
                                    color = Color(0xFF261818),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF5C2525)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clickable {
                                            viewPopupTitle = "❌ Mistake Note"
                                            viewPopupContent = mistakeStr.ifBlank { "No mistake notes recorded for this trade." }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("❌ Mistake", color = Color(0xFFFF8A80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Learning Button
                                Surface(
                                    color = Color(0xFF13231B),
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B4D36)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clickable {
                                            viewPopupTitle = "📖 Learning Note"
                                            viewPopupContent = learningStr.ifBlank { "No learning notes recorded for this trade." }
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("📖 Learning", color = Color(0xFFB9F6CA), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // High Brightness Yellow EDIT Button
                                Surface(
                                    color = Color(0xFFFFC107), // Full Brightness Yellow
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clickable { tradeToEdit = trade }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("✏️ Edit", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // High Brightness Red DELETE Button
                                Surface(
                                    color = Color(0xFFD32F2F), // Full Brightness Red
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(36.dp)
                                        .clickable { tradeToDelete = trade }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🗑️ Delete", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Popup for viewing Mistake / Learning Content
    if (viewPopupTitle != null && viewPopupContent != null) {
        AlertDialog(
            onDismissRequest = {
                viewPopupTitle = null
                viewPopupContent = null
            },
            containerColor = cardBg,
            title = {
                Text(
                    text = viewPopupTitle ?: "",
                    color = Color(0xFFFFC107),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = viewPopupContent ?: "",
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewPopupTitle = null
                        viewPopupContent = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Close", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Edit Trade Dialog
    tradeToEdit?.let { currentTrade ->
        val currentScore = extractScoreFromTrade(currentTrade)
        var editStatus by remember { mutableStateOf(extractStatusFromTrade(currentTrade)) }
        var pnlAmountText by remember {
            mutableStateOf(
                if (currentTrade.pnl > 0) "+$${currentTrade.pnl}"
                else if (currentTrade.pnl < 0) "-$${kotlin.math.abs(currentTrade.pnl)}"
                else "0.0"
            )
        }

        var mistakeText by remember { mutableStateOf(extractMistake(currentTrade)) }
        var learningText by remember { mutableStateOf(extractLearning(currentTrade)) }

        var showStatusDropdown by remember { mutableStateOf(false) }
        var showPnlDialog by remember { mutableStateOf(false) }
        var tempPnlInput by remember { mutableStateOf("") }
        var targetStatusForDialog by remember { mutableStateOf("WIN") }

        AlertDialog(
            onDismissRequest = { tradeToEdit = null },
            containerColor = cardBg,
            title = {
                Text(
                    text = "Edit Trade: ${currentTrade.symbol.ifBlank { currentTrade.pair }}",
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text("🎯 Status", color = subTextColor, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(inputBg, RoundedCornerShape(8.dp))
                            .clickable { showStatusDropdown = true }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = editStatus,
                            color = textColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        DropdownMenu(
                            expanded = showStatusDropdown,
                            onDismissRequest = { showStatusDropdown = false },
                            modifier = Modifier.background(cardBg)
                        ) {
                            listOf("PENDING", "WIN", "LOSS", "BREAKEVEN").forEach { opt ->
                                DropdownMenuItem(
                                    text = { Text(opt, color = textColor, fontWeight = FontWeight.Bold) },
                                    onClick = {
                                        editStatus = opt
                                        showStatusDropdown = false
                                        if (opt == "WIN" || opt == "LOSS") {
                                            targetStatusForDialog = opt
                                            tempPnlInput = ""
                                            showPnlDialog = true
                                        } else if (opt == "BREAKEVEN") {
                                            pnlAmountText = "0.0"
                                        }
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(inputBg, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("💵 Profit/Loss Amount:", color = subTextColor, fontSize = 11.sp)
                            val pColor = when {
                                pnlAmountText.startsWith("+") -> Color(0xFF00E676)
                                pnlAmountText.startsWith("-") -> Color(0xFFEF5350)
                                else -> Color(0xFFFFC107)
                            }
                            Text(text = pnlAmountText, color = pColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("⚠️ Mistake (Trade Error / Psychology)", color = subTextColor, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = mistakeText,
                        onValueChange = { mistakeText = it },
                        placeholder = { Text("Type your mistake...", color = subTextColor, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFC107),
                            unfocusedBorderColor = Color(0xFF2A2E3D)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("💡 Learning (Rule for Next Time)", color = subTextColor, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = learningText,
                        onValueChange = { learningText = it },
                        placeholder = { Text("Type your learning...", color = subTextColor, fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFC107),
                            unfocusedBorderColor = Color(0xFF2A2E3D)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val numericVal = pnlAmountText.replace("+", "").replace("-", "").replace("$", "").toDoubleOrNull() ?: 0.0
                        val finalPnl = if (pnlAmountText.startsWith("-")) -kotlin.math.abs(numericVal) else kotlin.math.abs(numericVal)

                        val compiledMeta = buildString {
                            append("STATUS:$editStatus | SCORE:$currentScore")
                            if (mistakeText.isNotBlank()) append(" | MISTAKE:${mistakeText.trim()}")
                            if (learningText.isNotBlank()) append(" | LEARNING:${learningText.trim()}")
                        }

                        val updated = currentTrade.copy(
                            pnl = finalPnl,
                            emotion = compiledMeta
                        )
                        actualViewModel.updateTrade(updated)
                        Toast.makeText(context, "Trade updated successfully!", Toast.LENGTH_SHORT).show()
                        tradeToEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { tradeToEdit = null }) {
                    Text("Cancel", color = subTextColor)
                }
            }
        )

        if (showPnlDialog) {
            AlertDialog(
                onDismissRequest = { showPnlDialog = false },
                containerColor = cardBg,
                title = {
                    Text(
                        text = if (targetStatusForDialog == "WIN") "🎉 Log WIN Amount" else "📉 Log LOSS Amount",
                        color = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFEF5350),
                        fontSize = 15.sp,
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
                            placeholder = { Text("e.g. 150") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFEF5350),
                                unfocusedBorderColor = Color(0xFF2A2E3D)
                            ),
                            shape = RoundedCornerShape(8.dp)
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
                            containerColor = if (targetStatusForDialog == "WIN") Color(0xFF00E676) else Color(0xFFEF5350)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Amount", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPnlDialog = false }) {
                        Text("Cancel", color = subTextColor)
                    }
                }
            )
        }
    }

    tradeToDelete?.let { trade ->
        AlertDialog(
            onDismissRequest = { tradeToDelete = null },
            containerColor = cardBg,
            title = { Text("Delete Trade", color = Color(0xFFEF5350), fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete trade ${trade.symbol.ifBlank { trade.pair }}?", color = textColor) },
            confirmButton = {
                Button(
                    onClick = {
                        actualViewModel.deleteTrade(trade)
                        Toast.makeText(context, "Trade deleted", Toast.LENGTH_SHORT).show()
                        tradeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350))
                ) {
                    Text("Delete", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { tradeToDelete = null }) {
                    Text("Cancel", color = subTextColor)
                }
            }
        )
    }
}
