package com.institutional.tradingjournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.institutional.tradingjournal.ui.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Dashboard : Screen("dashboard")
    object Journal : Screen("journal")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Calendar : Screen("calendar")
    object Analytics : Screen("analytics")
    object StrategyManager : Screen("strategy_manager")
}

@Composable
fun OrderflowNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDark: Boolean = true,
    onToggleTheme: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToWelcome = {
                    navController.navigate(Screen.Welcome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToSignup = { navController.navigate(Screen.Signup.route) }
            )
        }

        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                isDark = isDark,
                tradeList = emptyList()
            )
        }

        composable(Screen.Journal.route) {
            JournalChecklistScreen(isDark = isDark)
        }

        composable(Screen.History.route) {
            HistoryScreen(isDark = isDark)
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                isDark = isDark,
                onToggleTheme = onToggleTheme
            )
        }

        composable(Screen.Calendar.route) {
            CalendarScreen(isDark = isDark)
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(isDark = isDark)
        }

        composable(Screen.StrategyManager.route) {
            StrategyManagerScreen(isDark = isDark)
        }
    }
}
