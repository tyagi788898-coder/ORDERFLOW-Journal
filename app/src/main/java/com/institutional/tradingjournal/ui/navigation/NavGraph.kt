package com.institutional.tradingjournal.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.institutional.tradingjournal.UserPreferences
import com.institutional.tradingjournal.model.TradeEntry
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
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onLoadingComplete = {
                    val isLoggedIn = UserPreferences.isLoggedIn(context)
                    val targetRoute = if (isLoggedIn) Screen.Dashboard.route else Screen.Welcome.route
                    navController.navigate(targetRoute) {
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
