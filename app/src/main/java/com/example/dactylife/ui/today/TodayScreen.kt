package com.example.dactylife.ui.today

import android.icu.text.DateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.TaskType
import com.example.dactylife.ui.components.SectionTitle
import com.example.dactylife.viewmodel.TodayViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(viewModel: TodayViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    val tasks by viewModel.tasks.collectAsState()
    val completedHabits by viewModel.completedHabitsToday.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val today = LocalDate.now()
    val habits = tasks.filter { it.type == TaskType.HABIT }
    val todos = tasks.filter { it.type == TaskType.TODO && (it.completedAt == null || it.completedAt == today) }
    val events = tasks.filter { it.type == TaskType.EVENT && it.date == today }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Today")
                        Text(
                            text = DateFormat.getDateInstance(DateFormat.LONG).format(today),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Task") },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                onClick = { showDialog = true }
            )
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = innerPadding
        ) {

            item { SectionTitle("Habits") }
            items(
                habits,
                key = { it.id }
            ) { task ->
                TaskRow(
                    task = task,
                    checked = completedHabits.contains(task.id),
                    onToggle = { viewModel.toggleTask(task.id) },
                    onDelete = {
                        viewModel.deleteTask(task)

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Task deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.undoDelete()
                            }
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { SectionTitle("To-Do") }
            items(
                todos,
                key = { it.id }
            ) { task ->
                TaskRow(
                    task = task,
                    checked = task.isCompleted,
                    onToggle = { viewModel.toggleTask(task.id) },
                    onDelete = {
                        viewModel.deleteTask(task)

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Task deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.undoDelete()
                            }
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
            item { SectionTitle("Events") }
            items(
                events,
                key = { it.id }
            ) { task ->
                TaskRow(
                    task = task,
                    checked = task.isCompleted,
                    onToggle = { viewModel.toggleTask(task.id) },
                    onDelete = {
                        viewModel.deleteTask(task)

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Task deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Short
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.undoDelete()
                            }
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(80.dp)) }
        }


    }

    if (showDialog) {
        AddTaskDialog(
            onDismiss = { showDialog = false },
            onAddTask = { title, type, date ->
                viewModel.addTask(title, type, date)
                showDialog = false
            }
        )
    }
}
