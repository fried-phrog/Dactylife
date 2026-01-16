package com.example.dactylife.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dactylife.data.dao.TaskDao
import com.example.dactylife.data.entity.TaskEntity
import com.example.dactylife.data.model.TaskType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

// Tracks, loads and displays events
class CalendarViewModel(
    private val taskDao: TaskDao
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

// Gets events for currently selected date
    @OptIn(ExperimentalCoroutinesApi::class)
    val eventsForSelectedDate = _selectedDate
        .flatMapLatest { date ->
            taskDao.getEventsForDate(date)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList()
        )

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    private val _visibleMonth = MutableStateFlow(YearMonth.now())

// Set of dates in currently visible month that have at least one date
    @OptIn(ExperimentalCoroutinesApi::class)
    val eventDatesForMonth = _visibleMonth
        .flatMapLatest { month ->
            flow {
                emit(
                    taskDao.getEventDatesBetween(
                        month.atDay(1),
                        month.atEndOfMonth()
                    ).toSet()
                )
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptySet()
        )

    fun onMonthChanged(month: YearMonth) {
        _visibleMonth.value = month
    }

// Function to add testing event data
    fun seedTestEventsIfEmpty() {
        /*
        viewModelScope.launch {
            val eventCount = taskDao.countTasksOfType(TaskType.EVENT)
            if (eventCount == 0) {
                val today = LocalDate.now()

                taskDao.insertTask(
                    TaskEntity(
                        title = "Doctor Appointment",
                        type = TaskType.EVENT,
                        date = today
                    )
                )

                taskDao.insertTask(
                    TaskEntity(
                        title = "Team meeting",
                        type = TaskType.EVENT,
                        date = today.plusDays(4)
                    )
                )

                taskDao.insertTask(
                    TaskEntity(
                        title = "Dinner with friends",
                        type = TaskType.EVENT,
                        date = today.minusDays(1)
                    )
                )

                taskDao.insertTask(
                    TaskEntity(
                        title = "Cat",
                        type = TaskType.EVENT,
                        date = today.plusDays(4)
                    )
                )
            }
        }
         */
    }
}
