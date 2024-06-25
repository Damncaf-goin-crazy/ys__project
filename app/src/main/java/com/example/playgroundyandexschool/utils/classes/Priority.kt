package com.example.playgroundyandexschool.utils.classes

import android.content.Context
import com.example.playgroundyandexschool.R

enum class Priority {
    LOW, NO, HIGH
}

fun Priority.getText(context: Context): String {
    return when (this) {
        Priority.HIGH -> context.getString(R.string.priority_high)
        Priority.LOW -> context.getString(R.string.priority_low)
        Priority.NO -> context.getString(R.string.priority_no)
    }
}
