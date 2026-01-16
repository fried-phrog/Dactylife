package com.example.dactylife.viewmodel

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

// Habit history logic
class HistoryViewModel(
    taskRepository: TaskRepository,
    private val habitRepository: HabitCompletionRepository
) : ViewModel() {

// Filter habits from all tasks
    val habits =
        taskRepository.tasks
            .map { tasks -> tasks.filter { it.type == TaskType.HABIT } }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

// Emits all dates a habit was completed
    fun habitCompletions(
        habitId: Int
    ): Flow<Set<LocalDate>> = habitRepository.observeCompletionDates(habitId)

// Streak calculation
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