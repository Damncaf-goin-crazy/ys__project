package com.example.playgroundyandexschool.utils.classes

import java.util.UUID

data class TodoItem(
    val id: String,
    var text: String,
    var priority: Priority,
    var deadline: Long?,
    var isCompleted: Boolean,
    val creationDate: Long,
    val modificationDate: Long
) {
    companion object {
        fun empty(): TodoItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = "",
            priority = Priority.NO,
            deadline = null,
            isCompleted = false,
            creationDate = System.currentTimeMillis(),
            modificationDate = System.currentTimeMillis()
        )
    }
}
