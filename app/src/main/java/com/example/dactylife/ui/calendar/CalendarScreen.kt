package com.example.dactylife.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dactylife.viewmodel.CalendarViewModel
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.flow.filterNotNull
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.seedTestEventsIfEmpty()
    }

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.MONDAY)

    val selectedDate by viewModel.selectedDate.collectAsState()
    val events by viewModel.eventsForSelectedDate.collectAsState()

    Column (
        modifier = Modifier.fillMaxSize()
    ) {

        val calendarState = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first()
        )

        val visibleMonth = rememberFirstMostVisibleMonth(calendarState, viewportPercent = 90f)

        LaunchedEffect(visibleMonth.yearMonth) {
            viewModel.onMonthChanged(visibleMonth.yearMonth)
        }

        val eventDates by viewModel.eventDatesForMonth.collectAsState()

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = visibleMonth.yearMonth.month.toString(),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = visibleMonth.yearMonth.year.toString(),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        HorizontalCalendar(
            state = calendarState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            dayContent = {
                Day(
                    day = it,
                    isSelected = it.date == selectedDate,
                    hasEvent = eventDates.contains(it.date),
                    onClick = {
                        viewModel.onDateSelected(it.date)
                    }
                )
            },
            monthHeader = {
                DaysOfWeekTitle(daysOfWeek = daysOfWeek)
            }
        )

        if (events.isEmpty()) {
            Text(
                text = "No events for this day",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(0.5f),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .weight(0.5f)
            ) {
                events.forEach { event ->
                    Text(
                        text = "• ${event.title}",
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    hasEvent: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .clickable(
                enabled = day.position == DayPosition.MonthDate
            ) {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),

            color = when {
                isSelected -> Color.White
                day.date == LocalDate.now() -> Color.Blue
                day.position == DayPosition.MonthDate -> Color.Black
                else -> Color.LightGray
            },

            modifier = Modifier
                .background(
                    if (isSelected) Color.Blue else Color.Transparent,
                    shape = CircleShape
                )
                .padding(8.dp)
        )

        if (hasEvent) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(Color.Cyan, CircleShape)
                    .align(alignment = Alignment.BottomCenter)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekTitle(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            )
        }
    }
}

@Composable
fun rememberFirstMostVisibleMonth(
    state: CalendarState,
    viewportPercent: Float = 50f,
): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.firstMostVisibleMonth(viewportPercent) }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}


private fun CalendarLayoutInfo.firstMostVisibleMonth(viewportPercent: Float = 50f): CalendarMonth? {
    return if (visibleMonthsInfo.isEmpty()) {
        null
    } else {
        val viewportSize = (viewportEndOffset + viewportStartOffset) * viewportPercent / 100f
        visibleMonthsInfo.firstOrNull { itemInfo ->
            if (itemInfo.offset < 0) {
                itemInfo.offset + itemInfo.size >= viewportSize
            } else {
                itemInfo.size - itemInfo.offset >= viewportSize
            }
        }?.month
    }
}