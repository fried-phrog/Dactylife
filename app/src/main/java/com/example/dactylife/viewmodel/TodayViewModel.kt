package com.example.dactylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dactylife.data.entity.TaskEntity
import com.example.dactylife.data.model.TaskType
import com.example.dactylife.data.repository.HabitCompletionRepository
import com.example.dactylife.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Today screen logic
class TodayViewModel(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitCompletionRepository
) : ViewModel() {

    private val today = LocalDate.now()

// Tasks stream
    val tasks = taskRepository.tasks
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

// Set of habit IDs completed today
    val completedHabitsToday =
        habitRepository
            .observeCompletedHabitsForDate(today)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )

// Adding tasks
    fun addTask(title: String, type: TaskType, date: LocalDate?) {
    // Events must have a date
        if (type == TaskType.EVENT && date == null) {
            return
        }

        viewModelScope.launch {
            taskRepository.addTask(
                TaskEntity(
                    title = title,
                    type = type,
                    date = date
                )
            )
        }
    }

// Task deletion - remember recently deleted task
    private var recentlyDeletedTask: TaskEntity? = null
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            recentlyDeletedTask = task
            taskRepository.deleteTask(task)
        }
    }

// Deletion undo - reinserts the recently deleted task back into the DB
    fun undoDelete() {
        recentlyDeletedTask?.let { task ->
            viewModelScope.launch {
                taskRepository.addTask(task)
                recentlyDeletedTask = null
            }
        }
    }

// Task completion - updates completion state in DB
    fun toggleTask(id: Int) {
        val task = tasks.value.find { it.id == id } ?: return

        viewModelScope.launch {
            when (task.type) {
            // Habit completion logic - stores completion dates in separate DB table
                TaskType.HABIT -> {
                    val completedToday = habitRepository.isCompletedOnDate(task.id, today)

                    if (completedToday) {
                        habitRepository.removeCompletion(task.id, today)
                    } else {
                        habitRepository.addCompletion(task.id, today)
                    }
                }

            // TO-DO/Event completion logic - stores completion date in the same DB table
                else ->
                    taskRepository.updateTask(
                        task.copy(
                            isCompleted = !task.isCompleted,
                            completedAt = if (!task.isCompleted) today else null
                        )
                    )
            }
        }
    }
}
