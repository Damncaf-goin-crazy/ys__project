package com.example.playgroundyandexschool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playgroundyandexschool.data.local.sharedPreferences.SharedPreferencesHelper
import com.example.playgroundyandexschool.databinding.FragmentSettingsBinding
import com.example.playgroundyandexschool.ui.models.Mode
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPreferencesHelper: SharedPreferencesHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        when(sharedPreferencesHelper.getMode()){
            Mode.NIGHT -> binding.themeName.text = "Тёмная"
            Mode.LIGHT -> binding.themeName.text = "Светлая"
            Mode.SYSTEM -> binding.themeName.text = "Из настроек системы"
        }

        binding.theme.setOnClickListener {
            BottomSheetFragment(object : ThemeInterface{
                override fun changeTheme() {
                    when(sharedPreferencesHelper.getMode()){
                        Mode.NIGHT -> binding.themeName.text = "Тёмная"
                        Mode.LIGHT -> binding.themeName.text = "Светлая"
                        Mode.SYSTEM -> binding.themeName.text = "Из настроек системы"
                    }
                }

            }).show(parentFragmentManager, "change_task")
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}