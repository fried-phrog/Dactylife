package com.example.dactylife.ui.navigation

sealed class Screen(val route: String) {
    object Today : Screen("today")
    object Calendar : Screen("calendar")
    object History : Screen("history")
}