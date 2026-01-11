package com.example.dactylife.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dactylife.data.entity.TaskEntity
import com.example.dactylife.data.model.TaskType
import com.example.dactylife.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate


class TodayViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    val tasks = repository.tasks
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


    fun addTask(title: String, type: TaskType, date: LocalDate?) {
        viewModelScope.launch {
            repository.addTask(
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
            repository.deleteTask(task)
        }
    }

    fun undoDelete() {
        recentlyDeletedTask?.let { task ->
            viewModelScope.launch {
                repository.addTask(task)
                recentlyDeletedTask = null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleTask(id: Int) {
        val task = tasks.value.find { it.id == id } ?: return
        val today = LocalDate.now()

        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    isCompleted = !task.isCompleted,
                    completedAt = if (!task.isCompleted) today else null
                )
            )
        }
    }
}
