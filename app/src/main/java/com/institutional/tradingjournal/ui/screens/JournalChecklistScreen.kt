package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@Composable
fun JournalChecklistScreen(
    isDark: Boolean = true,
    tradeViewModel: TradeViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    val checklistItems = remember {
        mutableStateListOf(
            "Higher Timeframe Bias & Market Structure confirmed" to false,
            "Liquidity Sweep / Fair Value Gap identified" to false,
            "Orderflow / Volume Imbalance confirmed" to false,
            "Defined Risk-to-Reward (Minimum 1:2)" to false,
            "Position size calculated according to risk limits" to false,
            "No emotional bias (Fear / FOMO check passed)" to false
        )
    }

    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF666666)

    if (showDialog) {
        NewTradeDialog(
            tradeViewModel = tradeViewModel,
            onDismiss = { showDialog = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Text(
            text = "📋 Pre-Trade Execution Checklist",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Verify all institutional conditions before placing order",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(checklistItems) { index, (rule, checked) ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = rule,
                            color = textColor,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                checklistItems[index] = rule to isChecked
                            },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF1976D2))
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Log Trade to Journal History ➔",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
