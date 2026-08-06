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
import com.institutional.tradingjournal.ui.screens.SplashScreen

sealed class Screen(val route: String, val title: String) {
    object Splash : Screen("splash", "Splash")
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
    var isDarkTheme by remember { mutableStateOf(TradeStorage.isDarkMode(context)) }

    LaunchedEffect(Unit) {
        val loaded = TradeStorage.loadTrades(context)
        masterTradeList.clear()
        masterTradeList.addAll(loaded)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != Screen.Splash.route

    val items = listOf(
        Screen.Dashboard,
        Screen.Journal,
        Screen.Analytics,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = if (isDarkTheme) Color(0xFF12141C) else Color.White,
                    contentColor = if (isDarkTheme) Color.White else Color.Black
                ) {
                    items.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = {
                                if (currentRoute != screen.route) {
                                    navController.navigate(screen.route) {
                                        popUpTo(0) { inclusive = true }
                                        launchSingleTop = true
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
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onLoadingComplete = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Dashboard.route) {
                MainDashboardScreen(
                    tradeList = masterTradeList,
                    isDark = isDarkTheme,
                    onNavigateToJournal = {
                        navController.navigate(Screen.Journal.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Journal.route) {
                JournalChecklistScreen(
                    isDark = isDarkTheme,
                    onTradeLogged = { newTrade ->
                        masterTradeList.add(0, newTrade)
                        TradeStorage.saveTrades(context, masterTradeList)
                        navController.navigate(Screen.Analytics.route) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Analytics.route) {
                CalendarAnalyticsScreens(
                    tradeList = masterTradeList,
                    isDark = isDarkTheme,
                    onStatusUpdate = { tradeToUpdate, newStatus, pnlAmount, mistake, learning ->
                        val index = masterTradeList.indexOfFirst { it.id == tradeToUpdate.id }
                        if (index != -1) {
                            masterTradeList[index] = masterTradeList[index].copy(
                                result = newStatus,
                                pnlAmount = pnlAmount,
                                mistake = mistake,
                                learning = learning
                            )
                            TradeStorage.saveTrades(context, masterTradeList)
                        }
                    },
                    onDeleteTrade = { tradeToDelete ->
                        masterTradeList.removeIf { it.id == tradeToDelete.id }
                        TradeStorage.saveTrades(context, masterTradeList)
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    isDark = isDarkTheme,
                    tradeList = masterTradeList,
                    onThemeToggle = { newTheme ->
                        isDarkTheme = newTheme
                        TradeStorage.saveTheme(context, newTheme)
                    }
                )
            }
        }
    }
}

