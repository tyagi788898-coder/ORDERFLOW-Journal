package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun HistoryScreen(
    isDark: Boolean = true,
    viewModel: TradeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""
    val allTrades by viewModel.allTrades.collectAsState(initial = emptyList())

    // Logged in user ki trades filter karein
    val userTrades = remember(allTrades, currentEmail) {
        if (currentEmail.isBlank()) allTrades else allTrades.filter { it.email.equals(currentEmail, ignoreCase = true) || it.email.isBlank() }
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // ALL, WIN, LOSS
    var tradeToEdit by remember { mutableStateOf<TradeEntity?>(null) }
    var tradeToDelete by remember { mutableStateOf<TradeEntity?>(null) }

    val filteredList = userTrades.filter { trade ->
        val matchesSearch = trade.symbol.contains(searchQuery, ignoreCase = true) ||
                trade.pair.contains(searchQuery, ignoreCase = true) ||
                trade.strategyName.contains(searchQuery, ignoreCase = true)
        val matchesStatus = when (selectedFilter) {
            "WIN" -> trade.pnl > 0
            "LOSS" -> trade.pnl < 0
            else -> true
        }
        matchesSearch && matchesStatus
    }

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF666666)

    // Edit Trade Dialog
    tradeToEdit?.let { trade ->
        var editPnl by remember { mutableStateOf(trade.pnl.toString()) }
        var editNotes by remember { mutableStateOf(trade.emotion) }

        AlertDialog(
            onDismissRequest = { tradeToEdit = null },
            containerColor = cardBg,
            title = { Text("Edit Trade: ${trade.symbol.ifBlank { trade.pair }}", color = textColor, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = editPnl,
                        onValueChange = { editPnl = it },
                        label = { Text("PnL ($)", color = subTextColor) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editNotes,
                        onValueChange = { editNotes = it },
                        label = { Text("Emotion / Remarks", color = subTextColor) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newPnl = editPnl.toDoubleOrNull() ?: trade.pnl
                        viewModel.updateTrade(trade.copy(pnl = newPnl, emotion = editNotes))
                        tradeToEdit = null
                        Toast.makeText(context, "Trade updated", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { tradeToEdit = null }) {
                    Text("Cancel", color = subTextColor)
                }
            }
        )
    }

    // Delete Trade Dialog
    tradeToDelete?.let { trade ->
        AlertDialog(
            onDismissRequest = { tradeToDelete = null },
            containerColor = cardBg,
            title = { Text("Delete Trade", color = Color(0xFFE53935), fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete this trade log?", color = textColor) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteTrade(trade)
                        tradeToDelete = null
                        Toast.makeText(context, "Trade deleted", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Text(
            text = "📜 Trade Execution History",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Total Entries: ${userTrades.size}",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by pair, symbol or strategy...", color = subTextColor) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
        )

        // Filters Row
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "WIN", "LOSS").forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    color = if (isSelected) Color(0xFFFFC107) else cardBg,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { selectedFilter = filter }
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) Color.Black else textColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (userTrades.isEmpty()) "No trades logged yet for this account." else "No matching records found.",
                    color = subTextColor,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList) { item ->
                    val isProfit = item.pnl >= 0
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.symbol.ifBlank { item.pair.ifBlank { "TRADE" } },
                                        color = textColor,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = if (item.direction.equals("BUY", true)) Color(0xFF2E7D32).copy(alpha = 0.2f) else Color(0xFFC62828).copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = item.direction.uppercase(),
                                            color = if (item.direction.equals("BUY", true)) Color(0xFF4CAF50) else Color(0xFFEF5350),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = "${if (isProfit) "+" else ""}$${String.format("%.2f", item.pnl)}",
                                    color = if (isProfit) Color(0xFF4CAF50) else Color(0xFFEF5350),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (item.strategyName.isNotBlank()) "Strategy: ${item.strategyName}" else "Session: ${item.session.ifBlank { "Regular" }}",
                                    color = subTextColor,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = item.date.ifBlank { "Logged" },
                                    color = subTextColor,
                                    fontSize = 11.sp
                                )
                            }

                            if (item.emotion.isNotBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Notes: ${item.emotion}",
                                    color = subTextColor,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "✏️ Edit",
                                    color = Color(0xFF1976D2),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { tradeToEdit = item }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = "🗑️ Delete",
                                    color = Color(0xFFE53935),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { tradeToDelete = item }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
