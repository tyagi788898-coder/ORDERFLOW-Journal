package com.institutional.tradingjournal.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.institutional.tradingjournal.ui.components.BottomNavItem
import com.institutional.tradingjournal.ui.components.OrderflowBottomBar
import com.institutional.tradingjournal.ui.screens.DashboardScreen
import com.institutional.tradingjournal.ui.screens.NewTradeDialog
import com.institutional.tradingjournal.ui.theme.DarkBackground
import com.institutional.tradingjournal.ui.theme.GoldPrimary
import com.institutional.tradingjournal.ui.viewmodel.TradeViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: BottomNavItem.Dashboard.route

    val tradeViewModel: TradeViewModel = hiltViewModel()
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
                PlaceholderScreen("JOURNAL HISTORY")
            }
            composable(BottomNavItem.Calendar.route) {
                PlaceholderScreen("TRADING CALENDAR")
            }
            composable(BottomNavItem.Analytics.route) {
                PlaceholderScreen("INSTITUTIONAL ANALYTICS")
            }
            composable(BottomNavItem.Settings.route) {
                PlaceholderScreen("SYSTEM SETTINGS")
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

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = GoldPrimary, fontSize = 18.sp)
    }
}

