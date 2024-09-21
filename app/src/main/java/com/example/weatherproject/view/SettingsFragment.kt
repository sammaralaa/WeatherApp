package com.example.weatherproject.view

import android.app.LocaleManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentSettingsBinding
import com.example.weatherproject.utilitis
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    lateinit var locatioGroup : RadioGroup
    lateinit var languageGroup : RadioGroup

    lateinit var gpsButton : RadioButton
    lateinit var mapButton : RadioButton
    lateinit var arabicButton : RadioButton
    lateinit var englishButton : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locatioGroup = binding.locationRadioGroup
        languageGroup = binding.languageRadioGroup

        gpsButton = binding.gpsRadioButton
        mapButton = binding.mapRadioButton
        arabicButton = binding.arabicRadioButton
        englishButton = binding.englishRadioButton

        locatioGroup.setOnCheckedChangeListener{group, checkedId ->
            if(mapButton.id == checkedId){
                findNavController().navigate(R.id.action_settingsFragment_to_mapFragment)
            }
            else if(gpsButton.id == checkedId){

            }
        }
//        val currentAppLocales: LocaleList = requireContext().getSystemService(LocaleManager::class.java).getApplicationLocales("com.example.weatherproject")
//        Log.i("TAG", "onViewCreated: $currentAppLocales")
        val currentLanguage = Locale.getDefault().language
        val currentCountry = Locale.getDefault().country
        Log.i("TAG", "Current language: $currentLanguage, Current country: $currentCountry")
        if(currentLanguage=="en"){
            englishButton.isChecked = true
        }else{
            arabicButton.isChecked = true
        }
        languageGroup.setOnCheckedChangeListener{group, checkedId ->
            var u = utilitis()
            if(arabicButton.id == checkedId){
                u.setAppLocale("ar",requireContext())
            }else if(englishButton.id == checkedId){
                u.setAppLocale("en",requireContext())
            }
        }
    }

}