package com.example.playgroundyandexschool.utils

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Объект ViewUtils предоставляет методы для работы с интерфейсами пользователя.
 */
object ViewUtils {
    fun convertMillisToDateString(millis: Long?): String {
        if (millis != null) {
            val date = Date(millis)
            val format = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            return format.format(date)
        }
        return ""
    }

    fun resolveColorAttr(context: Context, attr: Int): Int {
        val typedValue = TypedValue()
        val theme = context.theme
        return if (theme.resolveAttribute(attr, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                ContextCompat.getColor(context, typedValue.resourceId)
            } else {
                typedValue.data
            }
        } else {
            throw IllegalArgumentException("Attribute not found")
        }
    }
}