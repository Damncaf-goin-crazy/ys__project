package com.example.playgroundyandexschool.data.local.sharedPreferences

import android.content.Context
import android.content.SharedPreferences

/**
 * Класс SharedPreferencesHelper предоставляет методы для сохранения и получения токена авторизации,
 * а также для проверки авторизованного состояния пользователя.
 */
class SharedPreferencesHelper(context: Context) {
    private object Keys {
        const val TOKEN_KEY = "auth_token"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("SharedPrefHelper", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(Keys.TOKEN_KEY, token).apply()
    }

    fun getHeader(): String {
        return "OAuth ${sharedPreferences.getString(Keys.TOKEN_KEY, null) ?: ""}"
    }

    fun isAuthorized(): Boolean {
        return sharedPreferences.getString(Keys.TOKEN_KEY, null) != null
    }

    companion object {
        private var instance: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesHelper(context).also { instance = it }
            }
        }
    }

}