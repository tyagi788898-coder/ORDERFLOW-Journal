package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel
import java.text.SimpleDateFormat
import java.util.*

data class StrategyData(
    val id: Int,
    val title: String,
    val sessionTime: String,
    val items: List<String>
)

@Composable
fun JournalChecklistScreen(
    isDark: Boolean = true,
    tradeViewModel: TradeViewModel
) {
    val context = LocalContext.current
    val currentEmail = UserDataStore.getCurrentSession(context) ?: ""

    // Strategy Definitions
    val strategyList = remember {
        listOf(
            StrategyData(
                id = 1,
                title = "STRATEGY 1 – ASIAN RANGE ACCUMULATION",
                sessionTime = "(Session: 05:00 – 11:30 IST)",
                items = listOf(
                    "📐 Range Boundary Setup : Asian VAH / VAL",
                    "🧪 Profile Confirmation : D-Shape / Balanced Profile",
                    "📦 Liquidity Trap : Fake Breakout of Range",
                    "👣 Auction Completion : Zero Bid / Ask",
                    "↪️ Re-Entry Confirmation : Candle Close Back Inside Range",
                    "📥 Entry Executed"
                )
            ),
            StrategyData(
                id = 2,
                title = "STRATEGY 2 – LONDON OPEN BREAKOUT & SWEEP",
                sessionTime = "(Session: 12:30 – 16:30 IST)",
                items = listOf(
                    "🎯 Previous Session High / Low Marked",
                    "⚡ Aggressive Sweep into Key Orderblock / FVG",
                    "📊 Volume Delta Absorption Confirmed",
                    "📉 Lower Timeframe Market Structure Shift",
                    "📥 Entry Executed on Retest"
                )
            ),
            StrategyData(
                id = 3,
                title = "STRATEGY 3 – NY AM SILVER BULLET / IMBALANCE",
                sessionTime = "(Session: 19:30 – 22:30 IST)",
                items = listOf(
                    "⏱️ 10:00 AM NY Killzone Window Active",
                    "🌊 Liquidity Pool Purged (BSL / SSL)",
                    "🕯️ Energetic FVG Displacement Formed",
                    "🎯 Invalidation Anchor Defined",
                    "📥 Limit Order Filled"
                )
            ),
            StrategyData(
                id = 4,
                title = "STRATEGY 4 – LATE SESSION POI CONTINUATION",
                sessionTime = "(Session: 22:30 – 01:30 IST)",
                items = listOf(
                    "📈 Institutional Daily Trend Continuation",
                    "🧱 High-Volume Node (HVN) Re-test",
                    "🛑 Clean Invalidation Defined",
                    "📥 Target Previous Extremes"
                )
            )
        )
    }

    var selectedStrategyIndex by remember { mutableStateOf(0) }
    var pnlInput by remember { mutableStateOf("") }
    var symbolInput by remember { mutableStateOf("EURUSD") }
    var direction by remember { mutableStateOf("BUY") }

    val activeStrategy = strategyList[selectedStrategyIndex]

    // Track checked status for items of current strategy
    val checkedMap = remember { mutableStateMapOf<String, Boolean>() }

    val bgColor = Color(0xFF090A10)
    val cardBg = Color(0xFF10121A)
    val selectedTabColor = Color(0xFFE53935)
    val unselectedTabColor = Color(0xFF1E2230)
    val goldColor = Color(0xFFFFC107)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Profit/Loss Card
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💵 Profit/Loss Amount:",
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                OutlinedTextField(
                    value = pnlInput,
                    onValueChange = { pnlInput = it },
                    placeholder = { Text("0.0", color = goldColor) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.width(110.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = goldColor,
                        unfocusedTextColor = goldColor,
                        focusedBorderColor = Color(0xFF333A4D),
                        unfocusedBorderColor = Color(0xFF222736)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Strategy Selector Row (Strategy 1, Strategy 2, Strategy 3, Strategy 4)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            strategyList.forEachIndexed { index, _ ->
                val isSelected = selectedStrategyIndex == index
                Surface(
                    color = if (isSelected) selectedTabColor else unselectedTabColor,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clickable { selectedStrategyIndex = index }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Strategy ${index + 1}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Checklist Box
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title
                Text(
                    text = "⭐ ${activeStrategy.title}",
                    color = goldColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = activeStrategy.sessionTime,
                    color = Color.Gray,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                // Items list
                activeStrategy.items.forEach { rule ->
                    val key = "${activeStrategy.id}_$rule"
                    val isChecked = checkedMap[key] ?: false

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 7.dp)
                            .clickable { checkedMap[key] = !isChecked },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .border(1.5.dp, if (isChecked) Color(0xFF1E88E5) else Color.Gray, RoundedCornerShape(4.dp))
                                .background(if (isChecked) Color(0xFF1E88E5) else Color.Transparent),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isChecked) {
                                Text("✓", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = rule,
                            color = if (isChecked) Color.White else Color(0xFFC0C4D0),
                            fontSize = 13.sp,
                            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fast Symbol & Direction Selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = symbolInput,
                onValueChange = { symbolInput = it },
                label = { Text("Pair", color = Color.Gray, fontSize = 11.sp) },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { direction = if (direction == "BUY") "SELL" else "BUY" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (direction == "BUY") Color(0xFF2E7D32) else Color(0xFFC62828)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(52.dp)
            ) {
                Text(direction, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Log Trade Button
        Button(
            onClick = {
                val pnlVal = pnlInput.toDoubleOrNull() ?: 0.0
                val dateFormatted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

                val trade = TradeEntity(
                    email = currentEmail,
                    tradeIdString = "TRD_${System.currentTimeMillis()}",
                    timestamp = System.currentTimeMillis(),
                    pair = symbolInput.uppercase().trim(),
                    symbol = symbolInput.uppercase().trim(),
                    direction = direction,
                    lotSize = 1.0,
                    entryPrice = 0.0,
                    exitPrice = 0.0,
                    pnl = pnlVal,
                    date = dateFormatted,
                    strategyName = activeStrategy.title.substringBefore(" ("),
                    session = activeStrategy.sessionTime,
                    emotion = "Executed via Strategy ${selectedStrategyIndex + 1}"
                )

                tradeViewModel.insertTrade(trade)
                Toast.makeText(context, "Trade Saved to History!", Toast.LENGTH_SHORT).show()
                pnlInput = ""
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                text = "Log Trade to Journal History ➔",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
