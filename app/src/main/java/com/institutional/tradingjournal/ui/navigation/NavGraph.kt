package com.institutional.tradingjournal.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.institutional.tradingjournal.ui.components.BottomNavItem
import com.institutional.tradingjournal.ui.components.OrderflowBottomBar
import com.institutional.tradingjournal.ui.screens.*
import com.institutional.tradingjournal.ui.viewmodel.StrategyViewModel
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Dashboard.route

    val tradeViewModel: TradeViewModel = hiltViewModel()
    val strategyViewModel: StrategyViewModel = hiltViewModel()
    var showNewTradeDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            OrderflowBottomBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen(
                    viewModel = tradeViewModel,
                    onOpenNewTrade = { showNewTradeDialog = true }
                )
            }
            composable(BottomNavItem.Journal.route) {
                JournalChecklistScreen()
            }
            composable(BottomNavItem.Calendar.route) {
                CalendarAnalyticsScreens(viewModel = tradeViewModel, isCalendarView = true)
            }
            composable(BottomNavItem.Analytics.route) {
                CalendarAnalyticsScreens(viewModel = tradeViewModel, isCalendarView = false)
            }
            composable(BottomNavItem.Settings.route) {
                SettingsScreen()
            }
        }

        if (showNewTradeDialog) {
            NewTradeDialog(
                onDismiss = { showNewTradeDialog = false },
                onSaveTrade = { trade ->
                    tradeViewModel.addTrade(trade)
                }
            )
        }
    }
}
