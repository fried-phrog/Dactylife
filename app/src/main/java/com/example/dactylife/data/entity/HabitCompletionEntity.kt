package com.example.dactylife.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "habit_completions",
    indices = [Index(value = ["habitId", "date"], unique = true)]
)
data class HabitCompletionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val habitId: Int,
    val date: LocalDate
)