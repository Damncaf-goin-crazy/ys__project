package com.example.playgroundyandexschool.data.network.models.responses

import com.example.playgroundyandexschool.data.network.models.TodoItemDto
import com.google.gson.annotations.SerializedName

/**
 * Data-класс GetListResponse представляет собой ответ сервера на запрос получения списка задач.
 */
data class GetListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemDto>,


    @SerializedName("revision")
    val revision: Int

)