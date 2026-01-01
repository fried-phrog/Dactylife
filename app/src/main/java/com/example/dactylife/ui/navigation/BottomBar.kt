package com.example.dactylife.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(
    navController: NavController
) {
    NavigationBar {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.route == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(Screen.Today.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(item.icon, contentDescription = item.label)
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}