package com.example.playgroundyandexschool.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(todoItem: ToDoItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(newItems: List<ToDoItemEntity>)

    @Update
    suspend fun updateItem(toDoItemEntity: ToDoItemEntity)

    @Query("UPDATE todolist SET done= :done, changedAt=:time WHERE id = :id")
    suspend fun updateDone(id: String, done: Boolean, time: Long)

    @Query("SELECT * FROM todolist")
    fun getListAsFlow(): Flow<List<ToDoItemEntity>>

    @Query("SELECT * FROM todolist")
    fun getList(): List<ToDoItemEntity>

    @Query("SELECT * FROM todolist WHERE id=:itemId")
    fun getItem(itemId: String): ToDoItemEntity

    @Delete
    suspend fun deleteItem(entity: ToDoItemEntity)

    @Query("DELETE FROM todoList")
    suspend fun deleteAll()

}