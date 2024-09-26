package com.example.weatherproject.view.settings

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentSettingsBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.utilitis
import com.example.weatherproject.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view_model.home.HomeFragmentViewModelFactory
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    lateinit var locatioGroup : RadioGroup
    lateinit var languageGroup : RadioGroup
    lateinit var tempGroup : RadioGroup
    lateinit var windGroup : RadioGroup

    lateinit var gpsButton : RadioButton
    lateinit var mapButton : RadioButton
    lateinit var arabicButton : RadioButton
    lateinit var englishButton : RadioButton
    lateinit var kelvinButton : RadioButton
    lateinit var celsiusButton : RadioButton
    lateinit var fahrenButton : RadioButton
    lateinit var msButton : RadioButton
    lateinit var mhButton : RadioButton

    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var allFactory: HomeFragmentViewModelFactory

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
        tempGroup = binding.tempRadioGroup
        windGroup = binding.windRadioGroup

        gpsButton = binding.gpsRadioButton
        mapButton = binding.mapRadioButton
        arabicButton = binding.arabicRadioButton
        englishButton = binding.englishRadioButton
        kelvinButton = binding.kelvinRadioButton
        fahrenButton = binding.fahrenRadioButton
        celsiusButton = binding.celsiusRadioButton
        msButton = binding.msRadioButton
        mhButton = binding.mhRadioButton
        allFactory = HomeFragmentViewModelFactory(
            WeatherRepository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.service), WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao()),
                SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
            ))
        viewModel = ViewModelProvider(this, allFactory).get(HomeFragmentViewModel::class.java)

        locatioGroup.setOnCheckedChangeListener{group, checkedId ->
            if(mapButton.id == checkedId){
                viewModel.saveData("location","map")
                findNavController().navigate(R.id.action_settingsFragment_to_mapFragment)

            }
            else if(gpsButton.id == checkedId){
                viewModel.saveData("location","gps")
                viewModel.removeFromSharedPref("lon")
            }
        }

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
                viewModel.saveData("lang","ar")
            }else if(englishButton.id == checkedId){
                u.setAppLocale("en",requireContext())
                viewModel.saveData("lang","en")
            }
            findNavController().navigate(R.id.action_settingsFragment_self)
        }

        tempGroup.setOnCheckedChangeListener{group, checkedId ->
            if(kelvinButton.id == checkedId){
                msButton.isChecked=true
                viewModel.saveData("units","standard")
            }else if(celsiusButton.id == checkedId){
                msButton.isChecked=true
                viewModel.saveData("units","metric")
            }else if(fahrenButton.id == checkedId){
                mhButton.isChecked=true
                viewModel.saveData("units","imperial")
            }
        }

        windGroup.setOnCheckedChangeListener{group, checkedId ->
            if(msButton.id == checkedId){
                    kelvinButton.isChecked=true
                if(kelvinButton.isChecked){
                    viewModel.saveData("units","standard")
                }else{
                    viewModel.saveData("units","metric")
                }
            }else if(mhButton.id == checkedId){
                fahrenButton.isChecked=true
                viewModel.saveData("units","imperial")
            }
        }
    }
fun showConfermationDialog(msg : String){

}
}