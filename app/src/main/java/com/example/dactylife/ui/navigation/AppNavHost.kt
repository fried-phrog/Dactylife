package com.example.dactylife.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dactylife.ui.calendar.CalendarScreen
import com.example.dactylife.ui.history.HistoryScreen
import com.example.dactylife.ui.today.TodayScreen
import com.example.dactylife.viewmodel.TodayViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: TodayViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Today.route
    ) {
        composable(Screen.Today.route) {
            TodayScreen(viewModel)
        }
        composable(Screen.Calendar.route) {
            CalendarScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}