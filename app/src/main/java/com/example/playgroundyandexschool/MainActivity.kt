package com.example.playgroundyandexschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playgroundyandexschool.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)
    }
}