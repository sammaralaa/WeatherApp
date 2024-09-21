package com.example.weatherproject.view.view_model.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
const val  REQUEST_LOCATION_CODE = 2001
class HomeFragmentViewModel(private val repo : WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather
     var w :WeatherResponse? = null
    var wind : Wind? = null
    init {
       // getCurrentWeather(10.99,44.34)
    }
    fun getCurrentWeather(lat: Double, lon: Double) {
       viewModelScope.launch(Dispatchers.IO){
            w = repo.getCurrentWeather(lat,lon)
           withContext(Dispatchers.Main){
               _weather.postValue(w)
           }
           Log.i("TAG", "viewModel: ${w?.wind?.deg}")
        }
    }


}