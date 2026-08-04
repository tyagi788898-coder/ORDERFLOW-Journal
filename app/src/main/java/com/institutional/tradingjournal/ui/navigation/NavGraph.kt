package com.institutional.tradingjournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.institutional.tradingjournal.ui.screens.CalendarAnalyticsScreens
import com.institutional.tradingjournal.ui.screens.JournalChecklistScreen
import com.institutional.tradingjournal.ui.screens.MainDashboardScreen
import com.institutional.tradingjournal.ui.screens.SettingsScreen
import com.institutional.tradingjournal.ui.viewmodel.TradingViewModel

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Journal : Screen("journal")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: TradingViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            MainDashboardScreen(
                onNavigateToJournal = { navController.navigate(Screen.Journal.route) },
                onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) },
                viewModel = viewModel
            )
        }
        composable(Screen.Journal.route) {
            JournalChecklistScreen(
                viewModel = viewModel,
                onTradeSaved = { navController.navigate(Screen.Dashboard.route) }
            )
        }
        composable(Screen.Analytics.route) {
            CalendarAnalyticsScreens(
                viewModel = viewModel
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = viewModel
            )
        }
    }
}
