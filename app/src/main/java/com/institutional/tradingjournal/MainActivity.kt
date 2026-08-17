package com.institutional.tradingjournal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.institutional.tradingjournal.ui.components.OrderflowBottomBar
import com.institutional.tradingjournal.ui.navigation.OrderflowNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: ""

            val colorScheme = if (isDarkTheme) {
                darkColorScheme(
                    background = Color(0xFF090A0F),
                    surface = Color(0xFF12141C),
                    primary = Color(0xFFFFC107)
                )
            } else {
                lightColorScheme(
                    background = Color(0xFFF4F6F9),
                    surface = Color.White,
                    primary = Color(0xFF1976D2)
                )
            }

            MaterialTheme(colorScheme = colorScheme) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        OrderflowBottomBar(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    OrderflowNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        isDark = isDarkTheme,
                        onToggleTheme = { isDarkTheme = it }
                    )
                }
            }
        }
    }
}
