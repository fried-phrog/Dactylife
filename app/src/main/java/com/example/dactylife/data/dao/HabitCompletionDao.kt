package com.example.dactylife.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dactylife.data.entity.HabitCompletionEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitCompletionDao {

    @Query("""
        SELECT EXISTS(
            SELECT 1 FROM habit_completions
            WHERE habitId = :habitId AND date = :date
        ) 
    """)
    suspend fun isCompletedOnDate(
        habitId: Int,
        date: LocalDate
    ): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: HabitCompletionEntity)

    @Query("""
        DELETE FROM habit_completions
        WHERE habitId = :habitId and date = :date
    """)
    suspend fun deleteCompletionForDate(
        habitId: Int,
        date: LocalDate
    )

    @Query("""
        SELECT date FROM habit_completions
        WHERE habitId = :habitId
    """)
    suspend fun getCompletionDates(
        habitId: Int
    ): List<LocalDate>

    @Query("""
        SELECT date FROM habit_completions
        WHERE habitId = :habitId
    """)
    fun observeCompletionDates(
        habitId: Int
    ): Flow<List<LocalDate>>

    @Query("""
        SELECT habitId FROM habit_completions
        WHERE date = :date
    """)
    fun observeCompletedHabitsForDate(
        date: LocalDate
    ): Flow<List<Int>>
}