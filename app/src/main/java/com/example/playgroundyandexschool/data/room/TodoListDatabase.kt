package com.example.playgroundyandexschool.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoItemEntity::class],
    version = 1
)
abstract class TodoListDatabase : RoomDatabase() {
    abstract val dao: TodoListDao
}