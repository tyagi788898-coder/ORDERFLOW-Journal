package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.theme.*
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@Composable
fun DashboardScreen(
    viewModel: TradeViewModel,
    onOpenNewTrade: () -> Unit
) {
    val totalPnL by viewModel.totalPnL.collectAsState()
    val totalTrades by viewModel.totalTradesCount.collectAsState()
    val winRate by viewModel.winRate.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenNewTrade,
                containerColor = GoldPrimary,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Quick Trade")
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "ORDERFLOW",
                color = GoldPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Institutional Trading Journal V7",
                color = TextMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main PnL Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Total Net PnL", color = TextMuted, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (totalPnL >= 0) "+$${String.format("%.2f", totalPnL)}" else "-$${String.format("%.2f", kotlin.math.abs(totalPnL))}",
                        color = if (totalPnL >= 0) ProfitGreen else LossRed,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Total Trades",
                    value = "$totalTrades",
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Win Rate",
                    value = "${String.format("%.1f", winRate)}%",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.border(1.dp, BorderGlass, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(title, color = TextMuted, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = TextWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

