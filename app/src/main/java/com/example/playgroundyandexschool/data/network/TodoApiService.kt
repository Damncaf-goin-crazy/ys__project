package com.example.playgroundyandexschool.data.network

import com.example.playgroundyandexschool.data.network.models.requests.PostItemRequest
import com.example.playgroundyandexschool.data.network.models.requests.UpdateListRequest
import com.example.playgroundyandexschool.data.network.models.responses.GetItemByIdResponse
import com.example.playgroundyandexschool.data.network.models.responses.GetListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Интерфейс TodoApiService определяет методы для взаимодействия с API сервиса задач.
 */
interface TodoApiService {
    @GET("list")
    suspend fun getTodos(
        @Header("Authorization") token: String
    ): Response<GetListResponse>

    @PATCH("list")
    suspend fun updateTodoList(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: UpdateListRequest
    ): Response<GetListResponse>


    @GET("list/{id}")
    suspend fun getTodoItemById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<GetItemByIdResponse> //TODO Обработать 404 если элемента нет


    @POST("list")
    suspend fun addTodoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body element: PostItemRequest
    ): Response<GetItemByIdResponse> //TODO Обработать 400 если ревизии не сходятся


    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") id: String,
        @Body todoItem: PostItemRequest
    ): Response<GetItemByIdResponse>//TODO Обработать 400 если ревизии не сходятся, Обработать 404 если элемента нет

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("Authorization") token: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<GetItemByIdResponse> //TODO Обработать 400 если ревизии не сходятся, Обработать 404 если элемента нет
}

/*
400 — неправильно сформирован запрос; не хватает частей урла или заголовков; не совпадают известная ревизия на сервере и то что передано (unsynchronizedData)
401 — неверная авторизация
404 — такой элемент на сервере не найден
500 — какая-то ошибка сервера - retry
*/