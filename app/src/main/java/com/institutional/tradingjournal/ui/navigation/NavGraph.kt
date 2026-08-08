package com.institutional.tradingjournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.institutional.tradingjournal.model.TradeEntry
import com.institutional.tradingjournal.ui.screens.CalendarAnalyticsScreens
import com.institutional.tradingjournal.ui.screens.DashboardScreen
import com.institutional.tradingjournal.ui.screens.JournalChecklistScreen
import com.institutional.tradingjournal.ui.screens.SettingsScreen
import com.institutional.tradingjournal.ui.screens.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Dashboard : Screen("dashboard")
    object Journal : Screen("journal")
    object History : Screen("history")
    object Settings : Screen("settings")
}

@Composable
fun OrderflowNavGraph(
    navController: NavHostController,
    isDark: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    tradeList: List<TradeEntry>,
    onTradeLogged: (TradeEntry) -> Unit,
    onDeleteTrade: (TradeEntry) -> Unit,
    onStatusUpdate: (TradeEntry, String, Double, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
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
            DashboardScreen(
                isDark = isDark,
                tradeList = tradeList
            )
        }

        composable(Screen.Journal.route) {
            JournalChecklistScreen(
                isDark = isDark,
                onTradeLogged = onTradeLogged
            )
        }

        composable(Screen.History.route) {
            CalendarAnalyticsScreens(
                isDark = isDark,
                tradeList = tradeList,
                onDeleteTrade = onDeleteTrade,
                onStatusUpdate = onStatusUpdate
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                isDark = isDark,
                onToggleTheme = onToggleTheme
            )
        }
    }
}

