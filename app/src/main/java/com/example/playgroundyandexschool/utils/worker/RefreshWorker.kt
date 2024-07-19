package com.example.playgroundyandexschool.utils.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.playgroundyandexschool.data.TodoItemsRepository
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.data.network.TodoApiService
import com.example.playgroundyandexschool.data.network.models.toTodoItem
import javax.inject.Inject

/**
 * Класс RefreshWorker представляет рабочего для периодического обновления данных через сетевой доступ.
 */
class RefreshWorker @Inject constructor(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repository: TodoItemsRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper,
    private val service: TodoApiService
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = service.getTodos(sharedPreferencesHelper.getHeader())
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
            Result.failure()
        }
    }
}