package com.institutional.tradingjournal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.data.entity.StrategyEntity
import com.institutional.tradingjournal.ui.theme.*
import com.institutional.tradingjournal.ui.viewmodel.StrategyViewModel

@Composable
fun StrategyManagerScreen(
    viewModel: StrategyViewModel
) {
    val strategies by viewModel.activeStrategies.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = GoldPrimary,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Strategy")
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
            Text(
                text = "INSTITUTIONAL STRATEGIES",
                color = GoldPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Default & Dynamic Custom Strategy Builder",
                color = TextMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(strategies) { strategy ->
                    StrategyCard(
                        strategy = strategy,
                        onDelete = { viewModel.deleteStrategy(strategy) }
                    )
                }
            }
        }

        if (showCreateDialog) {
            CreateStrategyDialog(
                onDismiss = { showCreateDialog = false },
                onCreateStrategy = { name, desc, colorHex, checklist ->
                    viewModel.createCustomStrategy(name, desc, colorHex, checklist)
                }
            )
        }
    }
}

@Composable
fun StrategyCard(
    strategy: StrategyEntity,
    onDelete: () -> Unit
) {
    val items = strategy.checklistItems.split("|").filter { it.isNotBlank() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strategy.name,
                    color = GoldPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (!strategy.isDefault) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = LossRed)
                    }
                }
            }

            if (strategy.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(strategy.description, color = TextMuted, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Checklist Confluences (${items.size})", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF222228), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "• $item", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

