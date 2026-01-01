package com.example.dactylife.data.model

import java.time.LocalDate

data class Task (
    val id: Long,
    val title: String,
    val type: TaskType,
    val isCompleted: Boolean = false,
    val scheduledDate: LocalDate? = null
)

