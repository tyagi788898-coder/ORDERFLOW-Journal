package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.institutional.tradingjournal.data.UserDataStore
import com.institutional.tradingjournal.data.entity.TradeEntity
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewTradeDialog(
    tradeViewModel: TradeViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""

    var symbol by remember { mutableStateOf("EURUSD") }
    var direction by remember { mutableStateOf("BUY") }
    var lotSize by remember { mutableStateOf("1.0") }
    var entryPrice by remember { mutableStateOf("") }
    var exitPrice by remember { mutableStateOf("") }
    var pnl by remember { mutableStateOf("") }
    var strategyName by remember { mutableStateOf("Orderflow Imbalance") }
    var notes by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF12141C)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "📝 Log New Trade",
                    color = Color(0xFFFFC107),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = symbol,
                    onValueChange = { symbol = it },
                    label = { Text("Symbol / Pair (e.g. BTCUSD, XAUUSD)", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = { direction = "BUY" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (direction == "BUY") Color(0xFF2E7D32) else Color(0xFF1E2638)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("BUY", color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { direction = "SELL" },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (direction == "SELL") Color(0xFFC62828) else Color(0xFF1E2638)
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SELL", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = pnl,
                    onValueChange = { pnl = it },
                    label = { Text("PnL ($) [e.g. 150 or -50]", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = strategyName,
                    onValueChange = { strategyName = it },
                    label = { Text("Strategy Used", color = Color.Gray) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Remarks / Execution Notes", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val finalPnl = pnl.toDoubleOrNull() ?: 0.0
                        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

                        val trade = TradeEntity(
                            email = currentEmail,
                            tradeIdString = "TRD_${System.currentTimeMillis()}",
                            timestamp = System.currentTimeMillis(),
                            pair = symbol.uppercase().trim(),
                            symbol = symbol.uppercase().trim(),
                            direction = direction,
                            lotSize = lotSize.toDoubleOrNull() ?: 1.0,
                            entryPrice = entryPrice.toDoubleOrNull() ?: 0.0,
                            exitPrice = exitPrice.toDoubleOrNull() ?: 0.0,
                            pnl = finalPnl,
                            date = currentDate,
                            strategyName = strategyName,
                            emotion = notes
                        )

                        tradeViewModel.insertTrade(trade)
                        Toast.makeText(context, "Trade Logged Successfully!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("Log Trade to Journal History", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
