package com.example.playgroundyandexschool.data.local.sharedPreferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import com.example.playgroundyandexschool.ui.models.Mode

/**
 * Класс SharedPreferencesHelper предоставляет методы для сохранения и получения токена авторизации,
 * а также для проверки авторизованного состояния пользователя.
 */
@SuppressLint("HardwareIds")
class SharedPreferencesHelper(context: Context) {
    private object Keys {
        const val TOKEN_KEY = "auth_token"
    }
    private val sharedPreferences: SharedPreferences


    init{
        sharedPreferences =
            context.getSharedPreferences("SharedPrefHelper", Context.MODE_PRIVATE)

        if(!sharedPreferences.contains("mode")){
            sharedPreferences.edit().putString("mode", "system").apply()
        }
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(Keys.TOKEN_KEY, token).apply()
    }

    fun getHeader(): String {
        return "OAuth ${sharedPreferences.getString(Keys.TOKEN_KEY, null) ?: ""}"
    }

    fun isAuthorized(): Boolean {
        return sharedPreferences.getString(Keys.TOKEN_KEY, null) != null
    }

    val userId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)



    fun getMode(): Mode {
        return when(sharedPreferences.getString("mode", "system")){
            "dark" -> Mode.NIGHT
            "light" -> Mode.LIGHT
            else -> Mode.SYSTEM
        }
    }

    fun setMode(mode:Mode) {
        when(mode){
            Mode.NIGHT -> {
                sharedPreferences.edit().putString("mode", "dark").apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Mode.LIGHT -> {
                sharedPreferences.edit().putString("mode", "light").apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            Mode.SYSTEM -> {
                sharedPreferences.edit().putString("mode", "system").apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}