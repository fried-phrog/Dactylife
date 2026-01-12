package com.example.dactylife.ui.history

import androidx.compose.ui.graphics.Color

enum class Level(
    val color: Color
) {
    Zero(Color(0xFFEBEDF0)), // not completed
    Completed(Color(0xFF40C463)) // completed
}