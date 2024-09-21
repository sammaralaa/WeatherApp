package com.example.weatherproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentSettingsBinding

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

        languageGroup.setOnCheckedChangeListener{group, checkedId ->
            if(arabicButton.id == checkedId){

            }else if(englishButton.id == checkedId){

            }
        }
    }

}