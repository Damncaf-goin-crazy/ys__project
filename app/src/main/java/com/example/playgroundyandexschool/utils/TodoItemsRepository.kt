package com.example.playgroundyandexschool.utils

import android.util.Log
import com.example.playgroundyandexschool.network.connection.MyConnectivityManager
import com.example.playgroundyandexschool.network.networkAccess.TodoApi
import com.example.playgroundyandexschool.network.networkClass.TodoItemDto
import com.example.playgroundyandexschool.network.networkClass.toTodoItem
import com.example.playgroundyandexschool.network.requests.PostItemRequest
import com.example.playgroundyandexschool.utils.classes.TodoItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicBoolean

object TodoItemsRepository {

    private val todoItemsList: MutableStateFlow<MutableList<TodoItem>> =
        MutableStateFlow(mutableListOf())

    var previousListSize = 0
    private var revision: Int = 0
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())


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

    suspend fun loadItems() {
        try {
            val response = TodoApi.retrofitService.getTodos()
            Log.d("121212 - loadItems", "${response.body()}")
            if (response.isSuccessful) {
                response.body()?.let { getListResponse ->
                    val items = getListResponse.list.map { it.toTodoItem() }.toMutableList()
                    todoItemsList.value = items
                    revision = getListResponse.revision
                }
            } else {
                Log.e(
                    "TodoItemsRepository1",
                    "Error fetching todo list: ${response.code()} - ${response.message()}"
                )
            }
        } catch (e: HttpException) {
            Log.e("TodoItemsRepository1", "HTTP Exception: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.e("TodoItemsRepository1", "Exception: ${e.message}")
        }
    }


    fun getTodoItems(): StateFlow<List<TodoItem>> {
        return todoItemsList.asStateFlow()
    }

    suspend fun getTodoItem(id: String): TodoItem? {
        val localItem = todoItemsList.value.find { it.id == id }
        if (localItem != null) {
            return localItem
        }

        return try {
            val response = TodoApi.retrofitService.getTodoItemById(id)
            if (response.isSuccessful) {
                response.body()?.element?.toTodoItem()
            } else {
                Log.e(
                    "TodoItemsRepository2",
                    "Error fetching todo item: ${response.code()} - ${response.message()}"
                )
                null
            }
        } catch (e: HttpException) {
            Log.e("TodoItemsRepository2", "HTTP Exception: ${e.code()} - ${e.message()}")
            null
        } catch (e: Exception) {
            Log.e("TodoItemsRepository2", "Exception: ${e.message}")
            null
        }
    }

    fun getItemCount(): Int {
        val currentList = todoItemsList.value.toMutableList()
        return currentList.size
    }

    suspend fun saveTodoItem(todoItem: TodoItem?) {
        if (todoItem == null) return
        val currentList = todoItemsList.value.toMutableList()
        previousListSize = currentList.size
        val index = currentList.indexOfFirst { it.id == todoItem.id }

        try {
            if (index == -1) {
                val response = TodoApi.retrofitService.addTodoItem(
                    revision,
                    PostItemRequest("ok", TodoItemDto.fromItem(todoItem))
                )
                if (response.isSuccessful) {
                    revision += 1
                    response.body()?.element?.let {
                        val newItem = it.toTodoItem()
                        currentList.add(0, newItem)
                    }
                } else {
                    Log.e(
                        "TodoItemsRepository3",
                        "Error adding todo item: ${response.code()} - ${response.message()}"
                    )
                }
            } else {
                val response = TodoApi.retrofitService.updateTodoItem(
                    revision,
                    todoItem.id,
                    PostItemRequest("ok", TodoItemDto.fromItem(todoItem))
                )
                if (response.isSuccessful) {
                    revision += 1
                    response.body()?.element?.let {
                        val updatedItem = it.toTodoItem()
                        currentList[index] = updatedItem
                    }
                } else {
                    Log.e(
                        "TodoItemsRepository3",
                        "Error updating todo item: ${response.code()} - ${response.message()}"
                    )
                }
            }
            todoItemsList.value = currentList
        } catch (e: HttpException) {
            Log.e("TodoItemsRepository3", "HTTP Exception: ${e.code()} - ${e.message()}")
        } catch (e: Exception) {
            Log.e("TodoItemsRepository3", "Exception: ${e.message}")
        }
    }

    suspend fun removeTodoItem(id: String) {
        val currentList = todoItemsList.value.toMutableList()
        previousListSize = currentList.size
        val itemToRemove = currentList.find { it.id == id }
        if (itemToRemove != null) {
            try {
                val response = TodoApi.retrofitService.deleteTodoItem(revision, id)
                if (response.isSuccessful) {
                    revision += 1
                    currentList.remove(itemToRemove)
                    todoItemsList.value = currentList
                } else {
                    Log.e(
                        "TodoItemsRepository4",
                        "Error deleting todo item: ${response.code()} - ${response.message()}"
                    )
                }
            } catch (e: HttpException) {
                Log.e("TodoItemsRepository4", "HTTP Exception: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.e("TodoItemsRepository4", "Exception: ${e.message}")
            }
        }
    }
}
