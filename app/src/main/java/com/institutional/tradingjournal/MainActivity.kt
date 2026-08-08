package com.institutional.tradingjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.institutional.tradingjournal.model.TradeEntry
import com.institutional.tradingjournal.ui.components.OrderflowBottomBar
import com.institutional.tradingjournal.ui.navigation.OrderflowNavGraph
import com.institutional.tradingjournal.ui.navigation.Screen

@Composable
fun OrderflowJournalTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkColors = darkColorScheme(
        primary = Color(0xFFFFC107),
        background = Color(0xFF090A0F),
        surface = Color(0xFF12141C)
    )
    val lightColors = lightColorScheme(
        primary = Color(0xFFFFC107),
        background = Color(0xFFF4F6F9),
        surface = Color.White
    )

    MaterialTheme(
        colorScheme = if (darkTheme) darkColors else lightColors,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDark by remember { mutableStateOf(true) }
            val tradeList = remember { mutableStateListOf<TradeEntry>() }

            OrderflowJournalTheme(darkTheme = isDark) {
                val navController = rememberNavController()
                var currentRoute by remember { mutableStateOf(Screen.Splash.route) }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    currentRoute = destination.route ?: Screen.Splash.route
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != Screen.Splash.route) {
                            OrderflowBottomBar(
                                currentRoute = currentRoute,
                                onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo(Screen.Dashboard.route) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                ) { innerPadding ->
                    OrderflowNavGraph(
                        navController = navController,
                        isDark = isDark,
                        onToggleTheme = { isDark = it },
                        tradeList = tradeList,
                        onTradeLogged = { newTrade ->
                            tradeList.add(0, newTrade)
                        },
                        onDeleteTrade = { tradeToDelete ->
                            tradeList.remove(tradeToDelete)
                        },
                        onStatusUpdate = { trade, status, pnl, pair, session ->
                            val index = tradeList.indexOf(trade)
                            if (index != -1) {
                                tradeList[index] = trade.copy(
                                    result = status,
                                    pnlAmount = pnl,
                                    pair = pair,
                                    session = session
                                )
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

