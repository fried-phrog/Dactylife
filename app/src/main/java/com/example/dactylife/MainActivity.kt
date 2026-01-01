package com.example.dactylife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.dactylife.ui.navigation.AppNavHost
import com.example.dactylife.ui.navigation.BottomBar
import com.example.dactylife.ui.today.TodayScreen
import com.example.dactylife.viewmodel.TodayViewModel


class MainActivity : ComponentActivity() {

    private val todayViewModel: TodayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier.padding(padding)
                    ) {
                        AppNavHost(
                            navController = navController,
                            viewModel = todayViewModel
                        )
                    }
                }
            }
        }
    }
}