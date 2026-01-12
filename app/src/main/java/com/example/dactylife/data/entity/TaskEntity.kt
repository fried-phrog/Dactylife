package com.example.dactylife.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.dactylife.data.model.TaskType
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String? = null,

    val type: TaskType,

    val date: LocalDate? = null,

    val isCompleted: Boolean = false,
    val completedAt: LocalDate? = null
)