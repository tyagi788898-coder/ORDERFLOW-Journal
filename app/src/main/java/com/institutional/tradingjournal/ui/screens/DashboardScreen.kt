package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainDashboardScreen(
    onNavigateToJournal: () -> Unit = {},
    onNavigateToAnalytics: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0E12))
            .padding(16.dp)
    ) {
        Text(
            text = "Institutional Orderflow",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Trading Performance & Metrics",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Metrics Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Win Rate", "68.5%", Color(0xFF00E676), Modifier.weight(1f))
            MetricCard("Profit Factor", "2.41", Color(0xFF2962FF), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard("Total Trades", "42", Color.White, Modifier.weight(1f))
            MetricCard("Net PnL", "+$4,850", Color(0xFF00E676), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions
        Text(
            text = "Quick Actions",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Button(
            onClick = onNavigateToJournal,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2962FF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("+ Log New Orderflow Trade", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MetricCard(title: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16181E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}
