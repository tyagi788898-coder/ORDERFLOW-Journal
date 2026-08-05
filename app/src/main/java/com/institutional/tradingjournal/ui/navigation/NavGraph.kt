package com.institutional.tradingjournal.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.institutional.tradingjournal.TradeStorage
import com.institutional.tradingjournal.model.TradeEntry
import com.institutional.tradingjournal.ui.screens.CalendarAnalyticsScreens
import com.institutional.tradingjournal.ui.screens.JournalChecklistScreen
import com.institutional.tradingjournal.ui.screens.MainDashboardScreen
import com.institutional.tradingjournal.ui.screens.SettingsScreen

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Journal : Screen("journal", "Journal")
    object Analytics : Screen("analytics", "History")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val masterTradeList = remember { mutableStateListOf<TradeEntry>() }

    LaunchedEffect(Unit) {
        val loaded = TradeStorage.loadTrades(context)
        masterTradeList.clear()
        masterTradeList.addAll(loaded)
    }

    val items = listOf(
        Screen.Dashboard,
        Screen.Journal,
        Screen.Analytics,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF12141C),
                contentColor = Color.White
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(Screen.Dashboard.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        label = {
                            Text(
                                text = screen.title,
                                color = if (currentRoute == screen.route) Color(0xFFFFC107) else Color.Gray,
                                fontWeight = if (currentRoute == screen.route) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = {}
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                MainDashboardScreen(
                    tradeList = masterTradeList,
                    onNavigateToJournal = { navController.navigate(Screen.Journal.route) }
                )
            }
            composable(Screen.Journal.route) {
                JournalChecklistScreen(
                    onTradeLogged = { newTrade ->
                        masterTradeList.add(0, newTrade)
                        TradeStorage.saveTrades(context, masterTradeList)
                        navController.navigate(Screen.Analytics.route)
                    }
                )
            }
            composable(Screen.Analytics.route) {
                CalendarAnalyticsScreens(
                    tradeList = masterTradeList,
                    onStatusUpdate = { tradeToUpdate, newStatus ->
                        val index = masterTradeList.indexOfFirst { it.id == tradeToUpdate.id }
                        if (index != -1) {
                            masterTradeList[index] = masterTradeList[index].copy(result = newStatus)
                            TradeStorage.saveTrades(context, masterTradeList)
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
