package com.example.playgroundyandexschool.data

import android.content.Context
import androidx.room.Room
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.data.network.TodoApi
import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.example.playgroundyandexschool.data.network.models.requests.UpdateListRequest
import com.example.playgroundyandexschool.data.network.models.toTodoItem
import com.example.playgroundyandexschool.data.room.ToDoItemEntity
import com.example.playgroundyandexschool.data.room.TodoListDao
import com.example.playgroundyandexschool.data.room.TodoListDatabase
import com.example.playgroundyandexschool.data.room.toItem
import com.example.playgroundyandexschool.ui.models.DataState
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.example.playgroundyandexschool.utils.MyConnectivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Репозиторий для управления списком задач, включая загрузку, сохранение и удаление задач.
 */
class TodoItemsRepository private constructor(context: Context) {

    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)

    private val db = Room.databaseBuilder(
        context.applicationContext, TodoListDatabase::class.java, "todo_list_database"
    ).build()
    private val todoListDao: TodoListDao = db.dao

    private var revision: Int = 0
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var reloads = 0
    private val MAX_RELOADS = 4

    private val _todoItemsFlow: Flow<List<TodoItem>> =
        todoListDao.getListAsFlow().map { entityList -> entityList.map { it.toItem() } }
    private val todoItemsFlow: StateFlow<List<TodoItem>> = _todoItemsFlow.stateIn(
        scope = repositoryScope, started = SharingStarted.Eagerly, initialValue = emptyList()
    )

    init {
        repositoryScope.launch {
            loadItems()
            syncLocalChangesWithBackend()
        }
    }


    suspend fun subscribeToInternet(
        myConnectivityManager: MyConnectivityManager, isConnected: AtomicBoolean
    ) {

        myConnectivityManager.connectionAsStateFlow.collect { connected ->
            isConnected.set(connected)
            if (connected) {
                loadItems()
                syncLocalChangesWithBackend()
            }
        }
    }

    suspend fun loadItems(): DataState {
        return try {
            val response = TodoApi.retrofitService.getTodos(sharedPreferencesHelper.getHeader())
            if (response.isSuccessful) {
                response.body()?.let { getListResponse ->
                    val serverItems = getListResponse.list.map { it.toTodoItem() }
                    val localItems = todoListDao.getList().map { it.toItem() }

                    val mergedItems = mergeItems(localItems, serverItems)

                    revision = getListResponse.revision
                    todoListDao.insertList(mergedItems.map { ToDoItemEntity.fromItem(it) })
                }
                reloads = 0
                DataState.Ok
            } else {
                response.errorBody()?.close()
                handleLoadError()
            }
        } catch (e: Exception) {
            handleLoadError()
        }
    }

    private suspend fun handleLoadError(): DataState {
        return if (reloads < MAX_RELOADS) {
            reloads++
            loadItems()
        } else {
            reloads = 0
            DataState.Error("Loading items failed after $MAX_RELOADS attempts. All changes will be saved locally")
        }
    }

    private fun mergeItems(
        localItems: List<TodoItem>, serverItems: List<TodoItem>
    ): List<TodoItem> {
        val mergedItems = mutableListOf<TodoItem>()
        val serverItemMap = serverItems.associateBy { it.id }
        val localItemMap = localItems.associateBy { it.id }

        serverItems.forEach { serverItem ->
            val localItem = localItemMap[serverItem.id]
            val localDate: Long = if (localItem != null) {
                localItem.modificationDate ?: localItem.creationDate
            } else {
                0
            }
            val serverDate: Long = serverItem.modificationDate ?: serverItem.creationDate
            if (localItem == null || serverDate > localDate) {
                mergedItems.add(serverItem)
            }
        }

        localItems.forEach { localItem ->
            if (!serverItemMap.containsKey(localItem.id)) {
                mergedItems.add(localItem)
            }
        }

        return mergedItems
    }

    suspend fun syncLocalChangesWithBackend(): DataState {
        try {
            val userId = sharedPreferencesHelper.userId
            val localItems = todoListDao.getList()
            val todoItems = localItems.map { it.toItem() }
            val todoDtoItems = todoItems.map { TodoItemDto.fromItem(it, userId) }

            val response = TodoApi.retrofitService.updateTodoList(
                sharedPreferencesHelper.getHeader(), revision, UpdateListRequest("ok", todoDtoItems)

            )
            if (response.isSuccessful) {
                response.body()?.let { getListResponse ->
                    revision = getListResponse.revision
                }
            } else {
                response.errorBody()?.close()
            }
            return DataState.Ok
        } catch (e: Exception) {
            return DataState.Error("Synchronising items failed after $MAX_RELOADS attempts. All changes will be saved locally")
        }
    }


    //Функция для обновления раз в 8 часов.
    suspend fun saveItemsToLocalDatabase(items: List<TodoItem>) {
        todoListDao.insertList(items.map { ToDoItemEntity.fromItem(it) })
    }

    //Функция для обновления раз в 8 часов.
    fun updateRevision(newRevision: Int) {
        revision = newRevision
    }


    fun getTodoItems(): StateFlow<List<TodoItem>> {
        return todoItemsFlow
    }

    fun getTodoItem(id: String): TodoItem? {
        return todoItemsFlow.value.find { it.id == id }
    }

    suspend fun saveTodoItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        val currentList = todoItemsFlow.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == todoItem.id }

        if (index == -1) {
            handleAddTodoItem(todoItem)
        } else {
            handleUpdateTodoItem(todoItem)
        }
    }

    private suspend fun handleAddTodoItem(todoItem: TodoItem) {
        todoListDao.insertItem(ToDoItemEntity.fromItem(todoItem))
    }

    private suspend fun handleUpdateTodoItem(todoItem: TodoItem) {
        todoListDao.updateItem(ToDoItemEntity.fromItem(todoItem))
    }

    suspend fun removeTodoItem(id: String) {
        val currentList = todoItemsFlow.value.toMutableList()
        val itemToRemove = currentList.find { it.id == id }
        if (itemToRemove != null) {
            todoListDao.deleteItem(ToDoItemEntity.fromItem(itemToRemove))
        }
    }

    companion object {
        private var instance: TodoItemsRepository? = null

        fun getInstance(context: Context): TodoItemsRepository {
            return instance ?: synchronized(this) {
                instance ?: TodoItemsRepository(context).also { instance = it }
            }
        }
    }


}