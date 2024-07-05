package com.example.playgroundyandexschool.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://hive.mrdekk.ru/todo/"

private val client = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY })
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

private val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

/**
 * Объект TodoApi предоставляет доступ к API сервиса задач.
 */
object TodoApi {
    val retrofitService: TodoApiService by lazy {
        retrofit.create(TodoApiService::class.java)
    }
}