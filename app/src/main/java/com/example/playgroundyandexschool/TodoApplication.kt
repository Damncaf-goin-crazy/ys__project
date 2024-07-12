package com.example.playgroundyandexschool

import android.app.Application
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.playgroundyandexschool.utils.worker.RefreshWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        WorkManager.getInstance(this)
            .enqueue(
                PeriodicWorkRequestBuilder<RefreshWorker>(8, TimeUnit.HOURS)
                    .build()
            )
    }
}