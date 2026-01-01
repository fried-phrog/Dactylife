package com.example.dactylife.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.dactylife.data.model.Task
import com.example.dactylife.data.model.TaskType
import java.time.LocalDate
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


class TodayViewModel : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _tasks: SnapshotStateList<Task> = mutableStateListOf(
        Task(1, "Drink water", TaskType.HABIT),
        Task(2, "Stretch", TaskType.HABIT),
        Task(3, "Buy groceries", TaskType.TODO),
        Task(4, "Change air filter", TaskType.EVENT, scheduledDate = LocalDate.now())
    )

    @RequiresApi(Build.VERSION_CODES.O)
    val tasks: List<Task> = _tasks

    @RequiresApi(Build.VERSION_CODES.O)
    fun toggleTask(taskId: Long) {
        val index = _tasks.indexOfFirst { it.id == taskId }
        if (index == -1) return

        val task = _tasks[index]
        _tasks[index] = task.copy(isCompleted = !task.isCompleted)
    }

    private var nextId = 5L
    fun addTask(
        title: String,
        type: TaskType
    ) {
        _tasks.add(
            Task(
                id = nextId++,
                title = title,
                type = type
            )
        )
    }
}
