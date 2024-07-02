package com.example.playgroundyandexschool.network.connection

import com.example.playgroundyandexschool.utils.classes.TodoItem


sealed interface UiState {
    data class Error(val message: String) : UiState
    data object Loading : UiState
    data class Content(
        val numDone: Int,
        val currentList: List<TodoItem>,
        val todosVisible: Boolean,
        val filteredList: List<TodoItem> = emptyList()
    ) : UiState
}