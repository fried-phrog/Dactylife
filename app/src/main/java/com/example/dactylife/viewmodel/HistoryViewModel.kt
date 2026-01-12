package com.example.dactylife.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dactylife.data.model.TaskType
import com.example.dactylife.data.repository.HabitCompletionRepository
import com.example.dactylife.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

class HistoryViewModel(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitCompletionRepository
): ViewModel() {

    val habits =
        taskRepository.tasks
            .map { tasks -> tasks.filter { it.type == TaskType.HABIT } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun habitCompletions(
        habitId: Int
    ): Flow<Set<LocalDate>> = habitRepository.observeCompletionDates(habitId)

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateStreak(
        completions: Set<LocalDate>
    ): Int {
        var streak = 0
        var date = LocalDate.now()

        while (completions.contains(date)) {
            streak++
            date = date.minusDays(1)
        }

        return streak
    }
}