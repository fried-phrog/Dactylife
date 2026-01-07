package com.example.dactylife.data.repository

import com.example.dactylife.data.dao.TaskDao
import com.example.dactylife.data.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val dao: TaskDao
) {
    val tasks: Flow<List<TaskEntity>> = dao.getAllTasks()

    suspend fun addTask(task: TaskEntity) {
        dao.insertTask(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        dao.updateTask(task)
    }
}