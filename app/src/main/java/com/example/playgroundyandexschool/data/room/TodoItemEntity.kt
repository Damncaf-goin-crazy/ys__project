package com.example.playgroundyandexschool.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playgroundyandexschool.ui.models.Priority
import com.example.playgroundyandexschool.ui.models.TodoItem

@Entity(tableName = "todoList")
data class ToDoItemEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "importance") var importance: Priority,
    @ColumnInfo(name = "deadline") var deadline: Long?,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "createdAt") val createdAt: Long,
    @ColumnInfo(name = "changedAt") var changedAt: Long?
) {

    companion object {
        fun fromItem(toDoItem: TodoItem): ToDoItemEntity {
            return ToDoItemEntity(
                id = toDoItem.id,
                description = toDoItem.text,
                importance = toDoItem.priority,
                deadline = toDoItem.deadline,
                done = toDoItem.isCompleted,
                createdAt = toDoItem.creationDate,
                changedAt = toDoItem.modificationDate
            )
        }
    }
}

fun ToDoItemEntity.toItem(): TodoItem {
    return TodoItem(
        id,
        description,
        importance,
        deadline,
        done,
        createdAt,
        changedAt
    )

}
