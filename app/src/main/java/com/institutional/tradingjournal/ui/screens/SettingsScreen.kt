package com.institutional.tradingjournal.ui.screens

import android.widget.Toast
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
import com.institutional.tradingjournal.UserPreferences

@Composable
fun SettingsScreen(isDark: Boolean, onToggleTheme: (Boolean) -> Unit) {
    val context = LocalContext.current
    var userName by remember { mutableStateOf(UserPreferences.getUserName(context)) }
    val userEmail = UserPreferences.getUserEmail(context)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF090A0F)).padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(text = "Settings & Profile", color = Color(0xFFFFC107), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        
        // Profile Section
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF12141C)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Trader Profile", color = Color(0xFFFFC107), fontWeight = FontWeight.Bold)
                OutlinedTextField(value = userName, onValueChange = { userName = it; UserPreferences.setUserName(context, it) }, label = { Text("Username", color = Color.Gray) }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Email: $userEmail", color = Color.Gray, fontSize = 12.sp)
            }
        }

        // Dark Mode
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF12141C)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = if(isDark) "Dark Mode (ON)" else "Dark Mode (OFF)", color = Color.White, modifier = Modifier.weight(1f))
                Switch(checked = isDark, onCheckedChange = onToggleTheme)
            }
        }

        // Import/Export
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF12141C)), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Data Backup", color = Color.White, fontWeight = FontWeight.Bold)
                Button(onClick = { Toast.makeText(context, "Exported JSON", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) { Text("Export Trade Data") }
                OutlinedButton(onClick = { Toast.makeText(context, "Imported JSON", Toast.LENGTH_SHORT).show() }, modifier = Modifier.fillMaxWidth()) { Text("Import Data") }
            }
        }
    }
}
