package com.example.playgroundyandexschool.network.requests

import com.example.playgroundyandexschool.network.networkClass.TodoItemDto
import com.google.gson.annotations.SerializedName

data class UpdateListRequest(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemDto>
)