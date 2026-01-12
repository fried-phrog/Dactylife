package com.example.dactylife.data.repository

import com.example.dactylife.data.dao.HabitCompletionDao
import com.example.dactylife.data.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class HabitCompletionRepository(
    private val dao: HabitCompletionDao
) {

    fun observeCompletedHabitsForDate(
        date: LocalDate
    ): Flow<Set<Int>> = dao.observeCompletedHabitsForDate(date).map { it.toSet() }

    fun observeCompletionDates(
        habitId: Int
    ): Flow<Set<LocalDate>> = dao.observeCompletionDates(habitId).map { it.toSet() }

    suspend fun isCompletedOnDate(
        habitId: Int,
        date: LocalDate
    ): Boolean {
        return dao.isCompletedOnDate(habitId, date)
    }

    suspend fun addCompletion(
        habitId: Int,
        date: LocalDate
    ) {
        dao.insertCompletion(
            HabitCompletionEntity(
                habitId = habitId,
                date = date
            )
        )
    }

    suspend fun removeCompletion(
        habitId: Int,
        date: LocalDate
    ) {
        dao.deleteCompletionForDate(habitId, date)
    }

    suspend fun getCompletionDates(
        habitId: Int
    ): List<LocalDate> {
        return dao.getCompletionDates(habitId)
    }
}