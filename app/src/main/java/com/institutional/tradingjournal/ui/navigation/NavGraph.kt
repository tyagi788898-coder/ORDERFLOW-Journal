package com.institutional.tradingjournal.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Journal : Screen("journal")
    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF0D0E12)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Orderflow Dashboard Ready", color = Color.White, fontSize = 18.sp)
            }
        }
        composable(Screen.Journal.route) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF0D0E12)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Journal & Checklist", color = Color.White, fontSize = 18.sp)
            }
        }
        composable(Screen.Analytics.route) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF0D0E12)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Analytics Screen", color = Color.White, fontSize = 18.sp)
            }
        }
        composable(Screen.Settings.route) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF0D0E12)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Settings & Backup", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}
