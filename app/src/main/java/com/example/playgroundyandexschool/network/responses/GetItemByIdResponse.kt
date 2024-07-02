package com.example.playgroundyandexschool.network.responses

import com.example.playgroundyandexschool.network.networkClass.TodoItemDto
import com.google.gson.annotations.SerializedName

data class GetItemByIdResponse(

    @SerializedName("status")
    val status: String,

    @SerializedName("element")
    val element: TodoItemDto,

    @SerializedName("revision")
    val revision: Int
)