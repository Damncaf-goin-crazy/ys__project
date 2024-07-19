package com.example.playgroundyandexschool.di

import android.content.Context
import com.example.playgroundyandexschool.utils.MyConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ConnectivityModule {
    @Provides
    @Singleton
    fun provideMyConnectivityManager(
        @ApplicationContext app: Context,
        externalScope: CoroutineScope
    ): MyConnectivityManager {
        return MyConnectivityManager(app, externalScope)
    }
}