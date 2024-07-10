package com.example.playgroundyandexschool.utils.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.playgroundyandexschool.data.TodoItemsRepository
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.data.network.TodoApi
import com.example.playgroundyandexschool.data.network.models.toTodoItem

/**
 * Класс RefreshWorker представляет рабочего для периодического обновления данных через сетевой доступ.
 */
class RefreshWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val repository = TodoItemsRepository.getInstance(appContext)

            val response = TodoApi.retrofitService.getTodos(
                SharedPreferencesHelper.getInstance(appContext).getHeader()
            )
            if (response.isSuccessful) {
                response.body()?.let { getListResponse ->
                    val items = getListResponse.list.map { it.toTodoItem() }.toMutableList()
                    repository.saveItemsToLocalDatabase(items)
                    repository.updateRevision(getListResponse.revision)
                }
                Result.success()
            } else {
                response.errorBody()?.close()
                Result.failure()
            }
        } catch (e: Exception) {
            return Result.failure()
        }
        return Result.failure()
    }
}