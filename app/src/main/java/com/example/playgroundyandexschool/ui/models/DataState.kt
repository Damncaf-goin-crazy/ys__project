package com.example.playgroundyandexschool.ui.models

/**
 * sealed interface DataState представляет информацию про состояние передачи данных
 */
sealed interface DataState {
    data class Error(val error: String): DataState
    data object Ok: DataState
}