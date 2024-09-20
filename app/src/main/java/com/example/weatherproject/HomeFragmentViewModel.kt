package com.example.weatherproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val repo : WeatherRepository) : ViewModel() {

    fun getCurrentWeather(lat: Double, lon: Double){
//       return viewModelScope.launch(Dispatchers.IO){
//            repo.getCurrentWeather(lat,lon)
//        }
    }
}