package com.example.playgroundyandexschool.network.responses

import com.example.playgroundyandexschool.network.networkClass.TodoItemDto
import com.google.gson.annotations.SerializedName

data class GetListResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("list")
    val list: List<TodoItemDto>,


    @SerializedName("revision")
    val revision: Int

)