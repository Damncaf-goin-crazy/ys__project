package com.example.playgroundyandexschool.ui.models

/**
 * Класс UiContent представляет данные интерфейса пользователя, включая список задач и состояние подключения.
 */
data class UiContent(
    val numDone: Int,
    val currentList: List<TodoItem>,
    val todosVisible: Boolean,
    val filteredList: List<TodoItem>,
    val isConnected: Boolean,
    val uiState: UiState
) {

    sealed interface UiState {
        data class Error(val errorMessage: String) : UiState
        data object Loading : UiState
        data object Content : UiState
    }
}
