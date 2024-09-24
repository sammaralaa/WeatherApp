package com.example.weatherproject.view_model.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherRepository

class FavFragmentViewModel(var repository: WeatherRepository) {
    private var _weather : MutableLiveData<List<WeatherModel>> = MutableLiveData<List<WeatherModel>>()
    val weather  : LiveData<List<WeatherModel>> = _weather

    fun getAllLocalWeather(){
       // viewModelScope
    }
}