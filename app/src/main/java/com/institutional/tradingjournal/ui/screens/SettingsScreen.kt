package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.theme.*

@Composable
fun SettingsScreen() {
    var isBiometricEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "SYSTEM SETTINGS",
            color = GoldPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Security, Offline Database Backup & Preferences",
            color = TextMuted,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Security Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Security & Privacy", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Fingerprint / PIN Lock", color = TextWhite, fontSize = 14.sp)
                        Text("Require authentication on app launch", color = TextMuted, fontSize = 11.sp)
                    }
                    Switch(
                        checked = isBiometricEnabled,
                        onCheckedChange = { isBiometricEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = GoldPrimary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Data Backup Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Offline Database Management", color = GoldPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Export Backup */ },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderGlass, RoundedCornerShape(8.dp))
                ) {
                    Text("📥 Backup Database (JSON / SQLite)", color = TextWhite)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { /* Restore Backup */ },
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BorderGlass, RoundedCornerShape(8.dp))
                ) {
                    Text("📤 Restore Database Backup", color = TextWhite)
                }
            }
        }
    }
}

