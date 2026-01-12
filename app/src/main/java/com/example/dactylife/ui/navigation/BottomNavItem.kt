package com.example.dactylife.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Today, "Today", Icons.Default.CheckCircle),
    BottomNavItem(Screen.Calendar, "Calendar", Icons.Default.DateRange),
    BottomNavItem(Screen.History, "History", Icons.Default.Favorite)
)