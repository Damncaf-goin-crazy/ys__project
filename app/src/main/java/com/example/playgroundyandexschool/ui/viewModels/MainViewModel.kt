package com.example.playgroundyandexschool.ui.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.playgroundyandexschool.data.TodoItemsRepository
import com.example.playgroundyandexschool.ui.models.DataState
import com.example.playgroundyandexschool.ui.models.TodoItem
import com.example.playgroundyandexschool.ui.models.UiContent
import com.example.playgroundyandexschool.ui.models.UiContent.UiState
import com.example.playgroundyandexschool.utils.MyConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Класс MainViewModel представляет основную ViewModel для управления состоянием экрана.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val isConnected: AtomicBoolean = AtomicBoolean(true)

    private val _uiStateMainScreen = MutableStateFlow(
        UiContent(
            numDone = 0,
            currentList = emptyList(),
            todosVisible = true,
            isConnected = isConnected.get(),
            uiState = UiState.Loading,
            filteredList = emptyList()
        )
    )
    val uiStateMainScreen: StateFlow<UiContent> = _uiStateMainScreen.asStateFlow()

    private val repository = TodoItemsRepository.getInstance(application)
    private val myConnectivityManager = MyConnectivityManager(application, viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodoItems().collect { todoList ->
                _uiStateMainScreen.update {
                    UiContent(
                        numDone = todoList.count { it.isCompleted },
                        currentList = todoList,
                        todosVisible = true,
                        isConnected = isConnected.get(),
                        uiState = UiState.Content,
                        filteredList = emptyList()
                    )
                }
                updateFilteredList()
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            repository.subscribeToInternet(myConnectivityManager, isConnected)
        }

        viewModelScope.launch {
            myConnectivityManager.connectionAsStateFlow.collect { connected ->
                _uiStateMainScreen.update {
                    it.copy(isConnected = connected, uiState = UiState.Content)
                }
            }
        }
    }

    private fun handleDataState(dataState: DataState) {
        _uiStateMainScreen.update {
            when (dataState) {
                is DataState.Ok -> it.copy(uiState = UiState.Content)
                is DataState.Error -> it.copy(uiState = UiState.Error(dataState.error))
            }
        }
    }

    fun toggleVisibility() {
        _uiStateMainScreen.update {
            it.copy(todosVisible = !it.todosVisible)
        }
        updateFilteredList()
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
            val dataState = repository.removeTodoItem(item.id)
            handleDataState(dataState)
        }
    }

    fun changeDone(item: TodoItem, doneState: Boolean) {
        item.isCompleted = doneState
        viewModelScope.launch(Dispatchers.IO) {
            val dataState = repository.saveTodoItem(item)
            handleDataState(dataState)
            updateFilteredList()
        }
    }

    fun updateItemsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataState = repository.loadItems()
            handleDataState(dataState)
        }
    }

}

