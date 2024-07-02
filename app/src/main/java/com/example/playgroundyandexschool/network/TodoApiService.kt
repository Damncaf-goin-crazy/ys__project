package com.example.playgroundyandexschool.network

import com.example.playgroundyandexschool.network.requests.PostItemRequest
import com.example.playgroundyandexschool.network.requests.UpdateListRequest
import com.example.playgroundyandexschool.network.responses.GetItemByIdResponse
import com.example.playgroundyandexschool.network.responses.GetListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface TodoApiService {
    @GET("list")
    suspend fun getTodos(): Response<GetListResponse>

    @PATCH("list")
    suspend fun updateTodoList(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body list: UpdateListRequest
    ): Response<GetListResponse>


    @GET("list/{id}")
    suspend fun getTodoItemById(
        @Path("id") id: String
    ): Response<GetItemByIdResponse> //TODO Обработать 404 если элемента нет


    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Body element: PostItemRequest
    ): Response<GetItemByIdResponse> //TODO Обработать 400 если ревизии не сходятся


    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Header("X-Last-Known-Revision") lastKnownRevision: Int,
        @Path("id") id: String,
        @Body todoItem: PostItemRequest
    ): Response<GetItemByIdResponse>//TODO Обработать 400 если ревизии не сходятся, Обработать 404 если элемента нет

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Path("id") id: String
    ): Response<GetItemByIdResponse> //TODO Обработать 400 если ревизии не сходятся, Обработать 404 если элемента нет
}

/*
400 — неправильно сформирован запрос; не хватает частей урла или заголовков; не совпадают известная ревизия на сервере и то что передано (unsynchronizedData)
401 — неверная авторизация
404 — такой элемент на сервере не найден
500 — какая-то ошибка сервера
*/