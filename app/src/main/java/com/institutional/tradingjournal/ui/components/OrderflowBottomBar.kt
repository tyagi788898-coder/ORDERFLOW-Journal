package com.institutional.tradingjournal.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.navigation.Screen

@Composable
fun OrderflowBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Triple(Screen.Dashboard.route, "Dashboard", "📊"),
        Triple(Screen.Journal.route, "Journal", "📖"),
        Triple(Screen.History.route, "History", "📜"),
        Triple(Screen.Settings.route, "Settings", "⚙️")
    )

    // Login/Signup/Splash par bottom bar nahi dikhana
    val hideOnRoutes = listOf(Screen.Splash.route, Screen.Welcome.route, Screen.Login.route, Screen.Signup.route)
    if (currentRoute in hideOnRoutes) return

    NavigationBar(
        containerColor = Color(0xFF12141C),
        contentColor = Color.White
    ) {
        items.forEach { (route, title, iconText) ->
            val selected = currentRoute == route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(route) },
                icon = { Text(iconText, fontSize = 18.sp) },
                label = {
                    Text(
                        text = title,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) Color(0xFFFFC107) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF1E2638)
                )
            )
        }
    }
}
