package com.example.dactylife.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dactylife.data.model.TaskType

@Entity(tableName = "tasks")
data class TaskEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val type: TaskType,
    val isCompleted: Boolean = false
)