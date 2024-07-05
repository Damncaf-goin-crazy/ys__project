package com.example.playgroundyandexschool.data.network.models.requests

import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.google.gson.annotations.SerializedName

/**
 * Data-класс PostItemRequest представляет собой запрос на добавление элемента задачи.
 */
data class PostItemRequest(
    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val item: TodoItemDto,
)
