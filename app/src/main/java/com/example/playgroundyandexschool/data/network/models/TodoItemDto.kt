package com.example.playgroundyandexschool.data.network.models

import com.example.playgroundyandexschool.ui.models.Priority
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.google.gson.annotations.SerializedName


/**
 * Data-класс TodoItemDto представляет собой DTO для элемента задачи.
 */

data class TodoItemDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("importance")
    var priority: String,
    @SerializedName("deadline")
    var deadline: Long?,
    @SerializedName("done")
    var isCompleted: Boolean,
    @SerializedName("created_at")
    val creationDate: Long,
    @SerializedName("changed_at")
    val modificationDate: Long,
    @SerializedName("last_updated_by")
    var updatedBy: String
) {
    companion object {
        /**
         * Преобразует объект TodoItem в объект TodoItemDto.
         */
        fun fromItem(toDoItem: TodoItem, id: String): TodoItemDto {
            return TodoItemDto(
                id = toDoItem.id,
                text = toDoItem.text,
                priority = when (toDoItem.priority) {
                    Priority.LOW -> "low"
                    Priority.NO -> "basic"
                    Priority.HIGH -> "important"
                },
                deadline = toDoItem.deadline,
                isCompleted = toDoItem.isCompleted,
                creationDate = toDoItem.creationDate,
                modificationDate = when (toDoItem.modificationDate) {
                    null -> toDoItem.creationDate
                    else -> {
                        toDoItem.modificationDate
                    }
                },
                updatedBy = id
            )
        }
    }
}

/**
 * Расширение функции для преобразования объекта TodoItemDto в объект TodoItem.
 */
fun TodoItemDto.toTodoItem(): TodoItem {
    return TodoItem(
        id,
        text,
        when (priority) {
            "low" -> Priority.LOW
            "basic" -> Priority.NO
            "important" -> Priority.HIGH
            else -> {
                Priority.NO
            }
        },
        deadline,
        isCompleted,
        creationDate,
        modificationDate
    )
}
