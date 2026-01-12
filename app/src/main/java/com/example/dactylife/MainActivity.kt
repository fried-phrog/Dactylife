package com.example.dactylife

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.dactylife.data.db.DactylifeDatabase
import com.example.dactylife.data.repository.HabitCompletionRepository
import com.example.dactylife.data.repository.TaskRepository
import com.example.dactylife.ui.navigation.AppNavHost
import com.example.dactylife.ui.navigation.BottomBar
import com.example.dactylife.viewmodel.CalendarViewModel
import com.example.dactylife.viewmodel.CalendarViewModelFactory
import com.example.dactylife.viewmodel.HistoryViewModel
import com.example.dactylife.viewmodel.HistoryViewModelFactory
import com.example.dactylife.viewmodel.TodayViewModel
import com.example.dactylife.viewmodel.TodayViewModelFactory


class MainActivity : ComponentActivity() {

    private lateinit var todayViewModel: TodayViewModel
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var historyViewModel: HistoryViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            DactylifeDatabase::class.java,
            "dactylife.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()

        val taskRepository = TaskRepository(db.taskDao())
        val habitCompletionRepository = HabitCompletionRepository(db.habitCompletionDao())

        val todayFactory = TodayViewModelFactory(
            taskRepository,
            habitCompletionRepository
        )
        val calendarFactory = CalendarViewModelFactory(db.taskDao())
        val historyFactory = HistoryViewModelFactory(taskRepository, habitCompletionRepository)

        todayViewModel = ViewModelProvider(this, todayFactory)[TodayViewModel::class.java]
        calendarViewModel = ViewModelProvider(this, calendarFactory)[CalendarViewModel::class.java]
        historyViewModel =  ViewModelProvider(this, historyFactory)[HistoryViewModel::class.java]


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
                            todayVM = todayViewModel,
                            calendarVM = calendarViewModel,
                            historyVM = historyViewModel
                        )
                    }
                }
            }
        }
    }
}
