package com.example.playgroundyandexschool.utils.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playgroundyandexschool.utils.TodoItemsRepository
import com.example.playgroundyandexschool.utils.classes.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState(0, emptyList(), true))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val repository = TodoItemsRepository

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTodoItems().collect { todoList ->
                _uiState.update {
                    _uiState.value.copy(
                        numDone = todoList.count { it.isCompleted },
                        currentList = todoList
                    )
                }
                updateFilteredList()
            }
        }
    }

    fun toggleVisibility() {
        val newVisibility = !_uiState.value.todosVisible
        _uiState.update {
            _uiState.value.copy(todosVisible = newVisibility)
        }
        updateFilteredList()
    }

    fun isNewListBigger(): Boolean {
        return repository.getItemCount() > repository.previousListSize
    }

    fun updateFilteredList() {
        val currentList = _uiState.value.currentList
        val filteredList = if (_uiState.value.todosVisible) {
            currentList
        } else {
            currentList.filter { !it.isCompleted }
        }
        val numDone = currentList.count { it.isCompleted }
        _uiState.value = _uiState.value.copy(filteredList = filteredList, numDone = numDone)
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
