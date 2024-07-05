package com.example.playgroundyandexschool.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken

/**
 * Фрагмент RegistrationFragment представляет собой экран регистрации пользователя.
 */
class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sdk: YandexAuthSdk
    private lateinit var launcher: ActivityResultLauncher<YandexAuthLoginOptions>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sdk = YandexAuthSdk.create(YandexAuthOptions(requireContext()))
        launcher = registerForActivityResult(sdk.contract) { result -> handleResult(result) }
        registerEvents()
    }

    private fun registerEvents(): Unit = with(binding) {
        loginWithYandexButton.setOnClickListener {
            val loginOptions = YandexAuthLoginOptions()
            launcher.launch(loginOptions)
        }
    }

    private fun handleResult(result: YandexAuthResult) {
        when (result) {
            is YandexAuthResult.Success -> onSuccessAuth(result.token)
            is YandexAuthResult.Failure -> onProccessError(result.exception)
            YandexAuthResult.Cancelled -> onCancelled()
        }
    }

    private fun onSuccessAuth(token: YandexAuthToken) {
        val tokenValue = token.value
        val sharedPreferencesHelper = SharedPreferencesHelper.getInstance(requireContext())
        sharedPreferencesHelper.saveToken(tokenValue)
        Snackbar.make(binding.root, "Login successful", Snackbar.LENGTH_LONG).show()
        findNavController().navigate(RegistrationFragmentDirections.loginFragmentToMain())

    }

    private fun onProccessError(exception: Exception) {
        Snackbar.make(binding.root, "Login failed: ${exception.message}", Snackbar.LENGTH_LONG)
            .show()
    }

    private fun onCancelled() {
        Snackbar.make(binding.root, "Login cancelled", Snackbar.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}