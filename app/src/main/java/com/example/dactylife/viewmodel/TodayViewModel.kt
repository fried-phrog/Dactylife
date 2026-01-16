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


class TodayViewModel(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitCompletionRepository
) : ViewModel() {

    private val today = LocalDate.now()

    val tasks = taskRepository.tasks
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    val completedHabitsToday =
        habitRepository
            .observeCompletedHabitsForDate(today)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptySet()
            )


    fun addTask(title: String, type: TaskType, date: LocalDate?) {
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

    private var recentlyDeletedTask: TaskEntity? = null
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            recentlyDeletedTask = task
            taskRepository.deleteTask(task)
        }
    }

    fun undoDelete() {
        recentlyDeletedTask?.let { task ->
            viewModelScope.launch {
                taskRepository.addTask(task)
                recentlyDeletedTask = null
            }
        }
    }

    fun toggleTask(id: Int) {
        val task = tasks.value.find { it.id == id } ?: return

        viewModelScope.launch {
            when (task.type) {
                TaskType.HABIT -> {
                    val completedToday = habitRepository.isCompletedOnDate(task.id, today)

                    if (completedToday) {
                        habitRepository.removeCompletion(task.id, today)
                    } else {
                        habitRepository.addCompletion(task.id, today)
                    }
                }

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
