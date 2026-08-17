package com.institutional.tradingjournal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.institutional.tradingjournal.ui.navigation.Screen

@Composable
fun OrderflowBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    // Hide bottom bar on auth screens
    if (currentRoute == "splash" || currentRoute == "welcome" || currentRoute == "login" || currentRoute == "signup") {
        return
    }

    NavigationBar(
        containerColor = Color(0xFF12141C),
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(Screen.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
            Triple(Screen.Journal.route, "Journal", Icons.Default.Book),
            Triple(Screen.History.route, "History", Icons.Default.History),
            Triple(Screen.Settings.route, "Settings", Icons.Default.Settings)
        )

        items.forEach { (route, label, icon) ->
            val isSelected = currentRoute == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(text = label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFFFC107),
                    selectedTextColor = Color(0xFFFFC107),
                    indicatorColor = Color(0xFF1A1D28),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}
