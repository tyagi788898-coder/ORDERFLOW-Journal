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
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    isDark: Boolean = true,
    viewModel: TradeViewModel = hiltViewModel(),
    tradeViewModel: TradeViewModel = viewModel
) {
    val actualViewModel = tradeViewModel
    val context = LocalContext.current
    val trades by actualViewModel.allTrades.collectAsState(initial = emptyList())

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val inputBg = if (isDark) Color(0xFF1A1D28) else Color(0xFFEBEFF5)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF757575)

    var searchQuery by remember { mutableStateOf("") }
    var filterTab by remember { mutableStateOf("ALL") }

    var tradeToEdit by remember { mutableStateOf<TradeEntity?>(null) }
    var tradeToDelete by remember { mutableStateOf<TradeEntity?>(null) }

    val filteredTrades = trades.filter { trade ->
        val matchesSearch = trade.symbol.contains(searchQuery, ignoreCase = true) ||
                trade.pair.contains(searchQuery, ignoreCase = true) ||
                trade.strategyName.contains(searchQuery, ignoreCase = true)

        val matchesTab = when (filterTab) {
            "WIN" -> trade.pnl > 0
            "LOSS" -> trade.pnl < 0
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
            text = "Total Entries: ${trades.size}",
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrades, key = { it.id }) { trade ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = trade.symbol.ifBlank { trade.pair },
                                    color = textColor,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )

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

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${trade.date}  •  ${trade.session}  •  ${trade.strategyName}",
                                color = subTextColor,
                                fontSize = 11.sp
                            )

                            if (trade.emotion.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = trade.emotion,
                                    color = Color(0xFFB0B7C3),
                                    fontSize = 12.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✏️ Edit",
                                    color = Color(0xFF1976D2),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { tradeToEdit = trade }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "🗑️ Delete",
                                    color = Color(0xFFEF5350),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { tradeToDelete = trade }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Comprehensive Edit Dialog
    tradeToEdit?.let { currentTrade ->
        var editStatus by remember {
            mutableStateOf(
                when {
                    currentTrade.pnl > 0 -> "WIN"
                    currentTrade.pnl < 0 -> "LOSS"
                    else -> "PENDING"
                }
            )
        }
        var pnlAmountText by remember {
            mutableStateOf(
                if (currentTrade.pnl > 0) "+$${currentTrade.pnl}"
                else if (currentTrade.pnl < 0) "-$${kotlin.math.abs(currentTrade.pnl)}"
                else "0.0"
            )
        }

        val existingNotes = currentTrade.emotion
        var mistakeText by remember {
            mutableStateOf(
                if (existingNotes.contains("Mistake:"))
                    existingNotes.substringAfter("Mistake:").substringBefore("|").trim()
                else ""
            )
        }
        var learningText by remember {
            mutableStateOf(
                if (existingNotes.contains("Learning:"))
                    existingNotes.substringAfter("Learning:").trim()
                else if (!existingNotes.contains("Mistake:")) existingNotes
                else ""
            )
        }

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
                        placeholder = { Text("e.g. FOMO entry, didn't wait for candle close", color = subTextColor, fontSize = 12.sp) },
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
                        placeholder = { Text("e.g. Strict wait for FVG re-test confirmation", color = subTextColor, fontSize = 12.sp) },
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

                        val compiledNotes = buildString {
                            if (mistakeText.isNotBlank()) append("Mistake: $mistakeText ")
                            if (learningText.isNotBlank()) {
                                if (isNotEmpty()) append("| ")
                                append("Learning: $learningText")
                            }
                        }

                        val updated = currentTrade.copy(
                            pnl = finalPnl,
                            emotion = compiledNotes
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
