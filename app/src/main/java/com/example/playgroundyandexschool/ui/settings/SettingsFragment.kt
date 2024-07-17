package com.example.playgroundyandexschool.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playgroundyandexschool.R
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
            Mode.NIGHT -> binding.darkButton.isChecked = true
            Mode.LIGHT -> binding.lightButton.isChecked = true
            Mode.SYSTEM -> binding.systemButton.isChecked = true
        }

        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            when (i) {
                R.id.light_button -> {
                    sharedPreferencesHelper.setMode(Mode.LIGHT)
                }
                R.id.dark_button -> {
                    sharedPreferencesHelper.setMode(Mode.NIGHT)
                }
                R.id.system_button -> {
                    sharedPreferencesHelper.setMode(Mode.SYSTEM)
                }
            }
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