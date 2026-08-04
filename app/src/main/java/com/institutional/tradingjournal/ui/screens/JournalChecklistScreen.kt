package com.institutional.tradingjournal.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.data.repository.DefaultStrategies
import com.institutional.tradingjournal.ui.theme.*

@Composable
fun JournalChecklistScreen() {
    var selectedStrategyIndex by remember { mutableStateOf(0) }
    var selectedPair by remember { mutableStateOf("XAUUSD") }
    var selectedSession by remember { mutableStateOf("Asian") }
    var selectedResult by remember { mutableStateOf("No Trade") }
    var mistakeText by remember { mutableStateOf("") }
    var learningText by remember { mutableStateOf("") }

    val currentStrategy = DefaultStrategies.list.getOrElse(selectedStrategyIndex) { DefaultStrategies.list[0] }
    val checklistItems = remember(selectedStrategyIndex) { currentStrategy.checklistItems.split("|").filter { it.isNotBlank() } }
    val checkedStates = remember(selectedStrategyIndex) { mutableStateListOf(*Array(checklistItems.size) { false }) }

    val checkedCount = checkedStates.count { it }
    val progressFraction = if (checklistItems.isNotEmpty()) checkedCount.toFloat() / checklistItems.size.toFloat() else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "progress")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Dark Blue-Black Institutional Tone
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- HEADER BANNER ---
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📊 Institutional",
                color = GoldPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Trading Journal PRO",
                color = GoldPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "⚡ Liquidity • Orderflow • Footprint • Heatmap • Volume Profile",
                color = TextMuted,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = GoldPrimary.copy(alpha = 0.5f), thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))

        // --- TOP SELECTORS CARD ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Date Card
                    SelectorBox(label = "📅 Date", value = "Today", modifier = Modifier.weight(1f))
                    // Pair Card
                    SelectorBox(label = "💱 Pair", value = selectedPair, modifier = Modifier.weight(1f)) {
                        selectedPair = if (selectedPair == "XAUUSD") "EURUSD" else "XAUUSD"
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Session Card
                    SelectorBox(label = "🕒 Session", value = selectedSession, modifier = Modifier.weight(1f)) {
                        selectedSession = when (selectedSession) {
                            "Asian" -> "London"
                            "London" -> "New York"
                            else -> "Asian"
                        }
                    }
                    // Result Card
                    SelectorBox(label = "🎯 Result", value = selectedResult, modifier = Modifier.weight(1f)) {
                        selectedResult = when (selectedResult) {
                            "No Trade" -> "WIN (+3R)"
                            "WIN (+3R)" -> "LOSS (-1R)"
                            else -> "No Trade"
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- STRATEGY TABS (2x2 GRID) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StrategyTabButton(
                title = "⭐ Strategy 1",
                isSelected = selectedStrategyIndex == 0,
                selectedColor = Color(0xFF22C55E),
                modifier = Modifier.weight(1f)
            ) { selectedStrategyIndex = 0 }

            StrategyTabButton(
                title = "🎯 Strategy 2",
                isSelected = selectedStrategyIndex == 1,
                selectedColor = Color(0xFFEF4444),
                modifier = Modifier.weight(1f)
            ) { selectedStrategyIndex = 1 }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StrategyTabButton(
                title = "🌏 Strategy 3",
                isSelected = selectedStrategyIndex == 2,
                selectedColor = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            ) { selectedStrategyIndex = 2 }

            StrategyTabButton(
                title = "🚀 Strategy 4",
                isSelected = selectedStrategyIndex == 3,
                selectedColor = Color(0xFFEC4899),
                modifier = Modifier.weight(1f)
            ) { selectedStrategyIndex = 3 }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- CHECKLIST CARD WITH EMOJIS & PROGRESS BAR ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = currentStrategy.name,
                    color = GoldPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Checkbox List
                checklistItems.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { checkedStates[index] = !checkedStates[index] },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = checkedStates.getOrElse(index) { false },
                            onCheckedChange = { checkedStates[index] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = GoldPrimary,
                                uncheckedColor = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = GoldPrimary,
                    trackColor = Color(0xFF334155)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${(progressFraction * 100).toInt()}% Completed",
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- TRADE REVIEW SECTION (MISTAKE & LEARNING) ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderGlass, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 Trade Review",
                        color = GoldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "After Trade",
                            color = TextMuted,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("❌ Mistake", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = mistakeText,
                    onValueChange = { mistakeText = it },
                    placeholder = { Text("What mistake was made?", color = TextMuted, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = BorderGlass,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("💡 Learning", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = learningText,
                    onValueChange = { learningText = it },
                    placeholder = { Text("Key takeaways from this trade", color = TextMuted, fontSize = 12.sp) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary,
                        unfocusedBorderColor = BorderGlass,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BOTTOM MOTIVATION BANNER ---
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🏆 Discipline > Prediction • Follow the Checklist, Not Emotions.",
                color = TextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun SelectorBox(label: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Box(
        modifier = modifier
            .background(Color(0xFF0F172A), RoundedCornerShape(8.dp))
            .border(1.dp, BorderGlass, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(10.dp)
    ) {
        Column {
            Text(label, color = TextMuted, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun StrategyTabButton(
    title: String,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedColor else Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else TextMuted,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

