package com.example.playgroundyandexschool.network.requests

import com.example.playgroundyandexschool.network.networkClass.TodoItemDto
import com.google.gson.annotations.SerializedName

data class PostItemRequest(
    @SerializedName("status")
    val status: String,

    @SerializedName("element")
        val item: TodoItemDto,
)
