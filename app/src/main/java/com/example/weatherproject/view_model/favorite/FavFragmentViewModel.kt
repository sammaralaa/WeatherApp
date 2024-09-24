package com.example.weatherproject.view_model.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherRepository
import kotlinx.coroutines.launch

class FavFragmentViewModel(var repository: WeatherRepository) : ViewModel(){

    private var _weather : MutableLiveData<List<WeatherModel>> = MutableLiveData<List<WeatherModel>>()
    val weather  : LiveData<List<WeatherModel>> = _weather

    init {
        getAllLocalWeather()
    }
    fun getAllLocalWeather(){
        viewModelScope.launch {
            repository.getLocalWeathers().collect{weathers->
                _weather.postValue(weathers)
            }
        }
    }
    fun insertWeather(weatherModel: WeatherModel){
        viewModelScope.launch {
            repository.insertWeather(weatherModel)
        }
    }
    fun deleteWeather(weatherModel: WeatherModel){
        viewModelScope.launch {
            repository.deleteWeather(weatherModel)
            getAllLocalWeather()
        }
    }
}