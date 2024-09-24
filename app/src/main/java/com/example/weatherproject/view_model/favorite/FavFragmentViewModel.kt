package com.example.weatherproject.view_model.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    fun insertWeather(lat: Double, lon: Double,lang : String,unit:String){
        viewModelScope.launch(Dispatchers.IO){
            var w = repository.getCurrentWeather(lat,lon,lang,unit)
            var wm = WeatherModel(w?.name?:"",w?.coord?.lat?:0.0,w?.coord?.lon?:0.0,w?.main?.temp?:0.0,w?.main?.pressure?:0,w?.main?.humidity?:0,w?.weather?.get(0)?.description?:"")
            repository.insertWeather(wm)
            getAllLocalWeather()
        }
    }
    fun deleteWeather(weatherModel: WeatherModel){
        viewModelScope.launch {
            repository.deleteWeather(weatherModel)
            getAllLocalWeather()
        }
    }
}