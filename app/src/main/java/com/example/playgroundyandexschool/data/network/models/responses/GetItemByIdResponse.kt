package com.example.playgroundyandexschool.data.network.models.responses

import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.google.gson.annotations.SerializedName

/**
 * Data-класс GetItemByIdResponse представляет собой ответ сервера на запрос получения элемента задачи по ID.
 */
data class GetItemByIdResponse(

    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val element: TodoItemDto,

    @SerializedName("revision")
    val revision: Int
)