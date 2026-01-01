package com.example.dactylife.ui.today

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dactylife.data.model.Task

@Composable
fun TaskRow(
    task: Task,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { onToggle() }
        )

        Text(
            text = task.title,
            modifier = Modifier.padding(start = 8.dp),
            style = if (task.isCompleted)
                MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
            else
                MaterialTheme.typography.bodyLarge
        )
    }
}
