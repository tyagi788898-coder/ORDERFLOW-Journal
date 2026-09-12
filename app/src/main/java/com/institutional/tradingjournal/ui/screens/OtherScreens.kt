package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryScreen(isDark: Boolean = true) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)

    Column(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📜 Trade History", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Your logged trades will appear here", color = textColor, fontSize = 14.sp)
    }
}

@Composable
fun CalendarScreen(isDark: Boolean = true) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)

    Column(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📅 Trade Calendar", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Daily PnL Overview", color = textColor, fontSize = 14.sp)
    }
}

@Composable
fun AnalyticsScreen(isDark: Boolean = true) {
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val textColor = if (isDark) Color.White else Color(0xFF12141C)

    Column(
        modifier = Modifier.fillMaxSize().background(bgColor).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("📊 Performance Analytics", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Detailed Winrate and RR Reports", color = textColor, fontSize = 14.sp)
    }
}
