package com.example.playgroundyandexschool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.databinding.ActivityMainBinding
import com.example.playgroundyandexschool.utils.worker.RefreshWorker
import java.util.concurrent.TimeUnit

/**
 * Главная активность приложения, отображающая список задач и управляющая периодическим обновлением данных.
 */
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var controller: NavController

    private val sharedPreferencesHelper by lazy { SharedPreferencesHelper.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)

        controller = getRootNavController()

        if (savedInstanceState != null) {
            controller.restoreState(savedInstanceState.getBundle("state"))
        } else {
            val navHostFragment =
                supportFragmentManager.findFragmentById(binding.navHostFragment.id)
                        as NavHostFragment
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)

            controller = navHostFragment.navController
            val destination = when (sharedPreferencesHelper.isAuthorized()) {
                false -> R.id.registrationFragment
                true -> R.id.mainScreenFragment
            }

            navGraph.setStartDestination(destination)

            controller.graph = navGraph
        }

    }


    private fun getRootNavController(): NavController {
        val navHost =
            supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        return navHost.navController
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("state", controller.saveState())
    }

    override fun onDestroy() {
        _binding = null
        WorkManager.getInstance(this)
            .enqueue(
                PeriodicWorkRequestBuilder<RefreshWorker>(8, TimeUnit.HOURS)
                    .build()
            )
        super.onDestroy()
    }

}