package com.institutional.tradingjournal.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.institutional.tradingjournal.ui.screens.CalendarAnalyticsScreens
import com.institutional.tradingjournal.ui.screens.JournalChecklistScreen
import com.institutional.tradingjournal.ui.screens.MainDashboardScreen
import com.institutional.tradingjournal.ui.screens.SettingsScreen

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object Journal : Screen("journal", "Journal")
    object Analytics : Screen("analytics", "Analytics")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Journal,
        Screen.Analytics,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF16181E),
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
                                color = if (currentRoute == screen.route) Color(0xFF2962FF) else Color.Gray,
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
                    onNavigateToJournal = { navController.navigate(Screen.Journal.route) },
                    onNavigateToAnalytics = { navController.navigate(Screen.Analytics.route) }
                )
            }
            composable(Screen.Journal.route) {
                JournalChecklistScreen(
                    onTradeSaved = { navController.navigate(Screen.Dashboard.route) }
                )
            }
            composable(Screen.Analytics.route) {
                CalendarAnalyticsScreens()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}
