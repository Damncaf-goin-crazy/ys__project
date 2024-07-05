package com.example.playgroundyandexschool.data.network.models.requests

import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.google.gson.annotations.SerializedName

/**
 * Data-класс UpdateListRequest представляет собой запрос на обновление списка задач.
 */
data class UpdateListRequest(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemDto>
)