package com.example.playgroundyandexschool.data

import android.content.Context
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.data.network.TodoApi
import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.example.playgroundyandexschool.data.network.models.requests.PostItemRequest
import com.example.playgroundyandexschool.data.network.models.toTodoItem
import com.example.playgroundyandexschool.ui.models.DataState
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.example.playgroundyandexschool.utils.MyConnectivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Репозиторий для управления списком задач, включая загрузку, сохранение и удаление задач.
 */
class TodoItemsRepository private constructor(context: Context) {

    private val todoItemsList: MutableStateFlow<MutableList<TodoItem>> =
        MutableStateFlow(mutableListOf())

    private val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(context)

    private var revision: Int = 0
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var reloads = 0
    private val MAX_RELOADS = 4

    init {
        repositoryScope.launch {
            loadItems()
        }
    }

    suspend fun subscribeToInternet(
        myConnectivityManager: MyConnectivityManager,
        isConnected: AtomicBoolean
    ) {
        myConnectivityManager.connectionAsStateFlow.collect { connected ->
            isConnected.set(connected)
            if (connected) {
                loadItems()
            }
        }
    }

    suspend fun loadItems(): DataState {
        return try {
            val response = TodoApi.retrofitService.getTodos(sharedPreferencesHelper.getHeader())
            if (response.isSuccessful) {
                response.body()?.let { getListResponse ->
                    val items = getListResponse.list.map { it.toTodoItem() }.toMutableList()
                    todoItemsList.value = items
                    revision = getListResponse.revision
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
            DataState.Error("Loading items failed after $MAX_RELOADS attempts")
        }
    }


    fun getTodoItems(): StateFlow<List<TodoItem>> {
        return todoItemsList.asStateFlow()
    }

    fun getTodoItem(id: String): TodoItem? {
        return todoItemsList.value.find { it.id == id }
    }

    suspend fun saveTodoItem(todoItem: TodoItem?): DataState {
        if (todoItem == null) return DataState.Error("No such item")
        val currentList = todoItemsList.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == todoItem.id }

        return try {
            if (index == -1) {
                handleAddTodoItem(todoItem, currentList)
            } else {
                handleUpdateTodoItem(todoItem, currentList, index)
            }
            todoItemsList.value = currentList
            reloads = 0
            DataState.Ok
        } catch (e: Exception) {
            handleSaveError(todoItem)
        }
    }

    private suspend fun handleSaveError(todoItem: TodoItem?): DataState {
        return if (reloads < MAX_RELOADS) {
            reloads++
            saveTodoItem(todoItem)
        } else {
            reloads = 0
            DataState.Error("Work with items failed after $MAX_RELOADS attempts. Changes will be lost")
        }
    }

    private suspend fun handleAddTodoItem(todoItem: TodoItem, currentList: MutableList<TodoItem>) {
        val response = TodoApi.retrofitService.addTodoItem(
            sharedPreferencesHelper.getHeader(),
            revision,
            PostItemRequest("ok", TodoItemDto.fromItem(todoItem))
        )
        if (response.isSuccessful) {
            response.body()?.let { getListResponse ->
                revision = getListResponse.revision
            }
            response.body()?.element?.let {
                val newItem = it.toTodoItem()
                currentList.add(newItem)
            }
        } else {
            response.errorBody()?.close()
        }
    }

    private suspend fun handleUpdateTodoItem(
        todoItem: TodoItem,
        currentList: MutableList<TodoItem>,
        index: Int
    ) {
        val response = TodoApi.retrofitService.updateTodoItem(
            sharedPreferencesHelper.getHeader(),
            revision,
            todoItem.id,
            PostItemRequest("ok", TodoItemDto.fromItem(todoItem))
        )
        if (response.isSuccessful) {
            response.body()?.let { getListResponse ->
                revision = getListResponse.revision
            }
            response.body()?.element?.let {
                val updatedItem = it.toTodoItem()
                currentList[index] = updatedItem
            }
        } else {
            response.errorBody()?.close()
        }
    }


    suspend fun removeTodoItem(id: String): DataState {
        val currentList = todoItemsList.value.toMutableList()
        val itemToRemove = currentList.find { it.id == id }
        if (itemToRemove != null) {
            return try {
                val response = TodoApi.retrofitService.deleteTodoItem(
                    sharedPreferencesHelper.getHeader(),
                    revision,
                    id
                )
                if (response.isSuccessful) {
                    response.body()?.let { getListResponse ->
                        revision = getListResponse.revision
                    }
                    currentList.remove(itemToRemove)
                    todoItemsList.value = currentList
                    reloads = 0
                    DataState.Ok
                } else {
                    response.errorBody()?.close()
                    handleRemoveError(id)
                }
            } catch (e: HttpException) {
                handleRemoveError(id)
            } catch (e: Exception) {
                handleRemoveError(id)
            }
        } else {
            return DataState.Error("No such item")
        }
    }

    private suspend fun handleRemoveError(id: String): DataState {
        return if (reloads < MAX_RELOADS) {
            reloads++
            removeTodoItem(id)
        } else {
            reloads = 0
            DataState.Error("Work with items deletion failed after $MAX_RELOADS attempts.")
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
