package com.example.dactylife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.dactylife.data.db.DactylifeDatabase
import com.example.dactylife.data.repository.TaskRepository
import com.example.dactylife.ui.navigation.AppNavHost
import com.example.dactylife.ui.navigation.BottomBar
import com.example.dactylife.viewmodel.TodayViewModel
import com.example.dactylife.viewmodel.TodayViewModelFactory


class MainActivity : ComponentActivity() {

    private lateinit var todayViewModel: TodayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            DactylifeDatabase::class.java,
            "dactylife.db"
        ).build()

        val repository = TaskRepository(db.taskDao())
        val factory = TodayViewModelFactory(repository)

        todayViewModel =
            ViewModelProvider(this, factory)[TodayViewModel::class.java]

        setContent {
            MaterialTheme {
                val navController = rememberNavController()

                Scaffold(
                    bottomBar = {
                        BottomBar(navController)
                    }
                ) { padding ->
                    Box(
                        Modifier.padding(padding)
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
