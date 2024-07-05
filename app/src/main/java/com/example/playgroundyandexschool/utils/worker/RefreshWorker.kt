package com.example.playgroundyandexschool.utils.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.data.network.TodoApi

/**
 * Класс RefreshWorker представляет рабочего для периодического обновления данных через сетевой доступ.
 */
class RefreshWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            TodoApi.retrofitService.getTodos(
                SharedPreferencesHelper.getInstance(appContext).getHeader()
            )
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}