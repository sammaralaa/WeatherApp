package com.example.weatherproject.view_model.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
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
        viewModelScope.launch(Dispatchers.IO) {
            repository.getLocalWeathers().collect{weathers->
                withContext(Dispatchers.Main){
                    _weather.postValue(weathers)
                }
                Log.i("TAG", "getAllLocalWeather: ")
            }
        }
    }
    fun insertWeather(lat: Double, lon: Double,name : String){
        viewModelScope.launch(Dispatchers.IO){
            val formattedNumber = String.format("%.2f", lat) // Formats to 2 decimal places
            val lat = formattedNumber.toDouble()

            val formattedNumber2 = String.format("%.2f", lon) // Formats to 2 decimal places
            val lon = formattedNumber2.toDouble()

            var lang = repository.getStringFromSharedPref("lang")
            var unite = repository.getStringFromSharedPref("unite")
            var wm = WeatherModel(name,lat,lon,lang?:"", unite?:"")
            repository.insertWeather(wm)
            getAllLocalWeather()
        }
    }
    fun deleteWeather(weatherModel: WeatherModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWeather(weatherModel)
            getAllLocalWeather()
        }
    }

}