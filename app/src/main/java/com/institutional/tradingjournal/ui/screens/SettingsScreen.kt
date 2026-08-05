package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.institutional.tradingjournal.TradeStorage
import com.institutional.tradingjournal.model.TradeEntry

@Composable
fun SettingsScreen(
    isDark: Boolean,
    tradeList: List<TradeEntry>,
    onThemeToggle: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
    ) {
        Text(
            text = "⚙️ Journal Settings & Preferences",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "🎨 Appearance Theme", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isDark) "Dark Pro Mode (Active)" else "Light Theme Mode (Active)", color = subTextColor, fontSize = 13.sp)
                    Switch(
                        checked = isDark,
                        onCheckedChange = { onThemeToggle(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFFFFC107))
                    )
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "📊 Excel / CSV Backup Export", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val filePath = TradeStorage.exportToCSV(context, tradeList)
                        if (filePath.isNotBlank()) {
                            Toast.makeText(context, "Excel File Exported to Downloads!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Export Saved to Storage!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("📥 Export Trade History to Excel (.CSV)", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "ℹ️ Institutional Engine Details", color = textColor, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Orderflow Pro Trading Journal v3.0 Final", color = subTextColor, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Storage: Offline Local Storage (Auto-Persistent)", color = subTextColor, fontSize = 13.sp)
            }
        }
    }
}
