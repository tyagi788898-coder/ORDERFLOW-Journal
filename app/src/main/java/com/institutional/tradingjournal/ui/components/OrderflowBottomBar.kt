package com.institutional.tradingjournal.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.institutional.tradingjournal.ui.theme.DarkBackground
import com.institutional.tradingjournal.ui.theme.GoldPrimary
import com.institutional.tradingjournal.ui.theme.TextMuted

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomNavItem("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Journal : BottomNavItem("journal", "Journal", Icons.Default.Book)
    object Calendar : BottomNavItem("calendar", "Calendar", Icons.Default.DateRange)
    object Analytics : BottomNavItem("analytics", "Analytics", Icons.Default.BarChart)
    object Settings : BottomNavItem("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun OrderflowBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Journal,
        BottomNavItem.Calendar,
        BottomNavItem.Analytics,
        BottomNavItem.Settings
    )

    NavigationBar(
        containerColor = DarkBackground,
        contentColor = GoldPrimary
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GoldPrimary,
                    selectedTextColor = GoldPrimary,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor = Color(0xFF222228)
                )
            )
        }
    }
}

