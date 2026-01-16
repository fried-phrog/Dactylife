package com.example.dactylife.ui.today

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.TaskType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

// Add task UI
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, TaskType, LocalDate?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(TaskType.HABIT) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

// A task must have a title, events must also have a date
    val isValid = title.isNotBlank() && (selectedType != TaskType.EVENT || selectedDate != null)

    if (showDatePicker) {
        val picker = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    picker.selectedDateMillis?.let {
                        selectedDate = Instant.ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                    }

                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = picker)
        }
    }


    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    if (title.isNotBlank()) {
                        onAddTask(
                            title.trim(),
                            selectedType,
                            selectedDate
                        )
                    }
                }
            ) {
                Text("Add")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },

        title = {
            Text("New Dactyl")
        },

        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Dactyl title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                TaskTypeSelector(
                    selected = selectedType,
                    onSelected = {
                        selectedType = it
                        if (it != TaskType.EVENT) selectedDate = null
                    }
                )

                if (selectedType == TaskType.EVENT) {
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { showDatePicker = true }
                    ) {
                        Text(
                            text = selectedDate?.toString() ?: "Pick event date"
                        )
                    }
                }
            }
        }
    )
}