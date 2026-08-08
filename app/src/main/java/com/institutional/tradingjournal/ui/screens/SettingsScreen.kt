package com.institutional.tradingjournal.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import java.io.InputStream
import java.io.OutputStream

@Composable
fun SettingsScreen(
    isDark: Boolean,
    onToggleTheme: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val bgColor = if (isDark) Color(0xFF090A0F) else Color(0xFFF4F6F9)
    val cardBg = if (isDark) Color(0xFF12141C) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF12141C)
    val subTextColor = if (isDark) Color.Gray else Color(0xFF555555)

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? ->
        uri?.let { exportDataToUri(context, it) }
    }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let { importDataFromUri(context, it) }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "⚙️ App Settings",
            color = Color(0xFFFFC107),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Configure Theme & Data Security",
            color = subTextColor,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🌙 Dark Theme Mode",
                        color = textColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Switch between Dark and Light background",
                        color = subTextColor,
                        fontSize = 11.sp
                    )
                }
                Switch(
                    checked = isDark,
                    onCheckedChange = { onToggleTheme(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFFFC107),
                        checkedTrackColor = Color(0xFF2A2E3D)
                    )
                )
            }
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "💾 Trade History Backup & Restore",
                    color = Color(0xFFFFC107),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Export your trades to JSON or Restore them after reinstalling.",
                    color = subTextColor,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )

                Button(
                    onClick = { exportLauncher.launch("Orderflow_Backup.json") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00E676)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "📤 Export Backup (Save File)",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { importLauncher.launch(arrayOf("application/json")) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "📥 Import Backup (Restore File)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

private fun exportDataToUri(context: Context, uri: Uri) {
    try {
        val prefs = context.getSharedPreferences("trade_journal_prefs", Context.MODE_PRIVATE)
        val jsonString = prefs.getString("saved_trades", "[]") ?: "[]"
        context.contentResolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
            outputStream.write(jsonString.toByteArray())
        }
        Toast.makeText(context, "Backup Saved Successfully!", Toast.LENGTH_LONG).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Export Failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

private fun importDataFromUri(context: Context, uri: Uri) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val jsonString = inputStream?.bufferedReader()?.use { it.readText() } ?: ""
        if (jsonString.isNotBlank()) {
            val prefs = context.getSharedPreferences("trade_journal_prefs", Context.MODE_PRIVATE)
            prefs.edit().putString("saved_trades", jsonString).apply()
            Toast.makeText(context, "Data Restored Successfully!", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Import Failed: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

