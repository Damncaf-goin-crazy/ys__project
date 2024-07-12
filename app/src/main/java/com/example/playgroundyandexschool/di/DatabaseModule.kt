package com.example.playgroundyandexschool.di

import android.content.Context
import androidx.room.Room
import com.example.playgroundyandexschool.data.room.TodoListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideTodoListDatabase(
        @ApplicationContext app: Context
    ): TodoListDatabase {
        return Room.databaseBuilder(
            app,
            TodoListDatabase::class.java,
            "todo_list_database"
        ).build()
    }
}