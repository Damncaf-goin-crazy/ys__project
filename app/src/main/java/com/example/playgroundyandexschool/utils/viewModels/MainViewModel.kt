package com.example.playgroundyandexschool.utils.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.playgroundyandexschool.network.connection.MyConnectivityManager
import com.example.playgroundyandexschool.utils.TodoItemsRepository
import com.example.playgroundyandexschool.utils.classes.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiStateMainScreen = MutableStateFlow(UiState(0, emptyList(), true))
    val uiStateMainScreen: StateFlow<UiState> = _uiStateMainScreen.asStateFlow()
    private val repository = TodoItemsRepository
    private val myConnectivityManager = MyConnectivityManager(application, viewModelScope)
    private val isConnected: AtomicBoolean = AtomicBoolean(true)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodoItems().collect { todoList ->
                _uiStateMainScreen.update {
                    _uiStateMainScreen.value.copy(
                        numDone = todoList.count { it.isCompleted },
                        currentList = todoList
                    )
                }
                updateFilteredList()
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.subscribeToInternet(myConnectivityManager, isConnected)
        }
    }

    fun toggleVisibility() {
        val newVisibility = !_uiStateMainScreen.value.todosVisible
        _uiStateMainScreen.update {
            _uiStateMainScreen.value.copy(todosVisible = newVisibility)
        }
        updateFilteredList()
    }

    fun isNewListBigger(): Boolean {
        return repository.getItemCount() > repository.previousListSize
    }

    fun updateFilteredList() {
        val currentList = _uiStateMainScreen.value.currentList
        val filteredList = if (_uiStateMainScreen.value.todosVisible) {
            currentList
        } else {
            currentList.filter { !it.isCompleted }
        }
        val numDone = currentList.count { it.isCompleted }
        _uiStateMainScreen.update {
            _uiStateMainScreen.value.copy(filteredList = filteredList, numDone = numDone)
        }
    }

    fun deleteItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removeTodoItem(item.id)
        }
    }

    fun changeDone(item: TodoItem, doneState: Boolean) {
        item.isCompleted = doneState
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveTodoItem(item)
            updateFilteredList()
        }
    }

}

data class UiState(
    val numDone: Int,
    val currentList: List<TodoItem>,
    val todosVisible: Boolean,
    val filteredList: List<TodoItem> = emptyList()
)
