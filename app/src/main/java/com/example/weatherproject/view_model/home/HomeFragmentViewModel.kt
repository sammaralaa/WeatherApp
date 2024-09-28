package com.example.weatherproject.view_model.home


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.areSameDay
import com.example.weatherproject.getCurrentDateTime
import com.example.weatherproject.isMidnight
import com.example.weatherproject.model.repo.IWeatherRepository
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.ApiStateForcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeFragmentViewModel(private val repo : IWeatherRepository) : ViewModel() {
//    private val _weather = MutableLiveData<WeatherResponse?>()
//    val weather: LiveData<WeatherResponse?> = _weather
    private val _weatherStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherStateFlow: StateFlow<ApiState> = _weatherStateFlow

    private val _weatherHourlyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherHourlyStateFlow: StateFlow<ApiStateForcast> = _weatherHourlyStateFlow

    private val _weatherDailyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherDaileStateFlow: StateFlow<ApiStateForcast> = _weatherDailyStateFlow

    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String) {
           viewModelScope.launch {
               repo.getCurrentWeather(lat,lon,lang,unit)
                   .onStart {
                       _weatherStateFlow.value = ApiState.Loading
                   }

                   .catch { e ->
                       _weatherStateFlow.value = ApiState.Failure(e)
                   }

                   .collect { weather ->
                       _weatherStateFlow.value = ApiState.Success(weather)

                       Log.i("TAG", "getCurrentWeather: viewModel success ${weather.toString()}")
                   }
           }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForcastWeather(lat: Double, lon: Double, lang : String, unit:String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getForcastWeather(lat,lon,lang,unit)
                .onStart { _weatherHourlyStateFlow.value = ApiStateForcast.Loading
                            _weatherDailyStateFlow.value = ApiStateForcast.Loading
                }
                .catch { e-> _weatherHourlyStateFlow.value = ApiStateForcast.Failure(e) }

                .collect{ weather ->
                    val filteredWeatherList = weather.filter { weatherItem ->
                        areSameDay(weatherItem.dt_txt, getCurrentDateTime())
                    }
                    val filterDailyWeatherList = weather.filter { weatherItem->
                        isMidnight(weatherItem.dt_txt)
                    }
                    Log.i("TAG", "getForcastWeather: hourly ${filteredWeatherList.toString()}")
                    Log.i("TAG", "getForcastWeather: Daily ${filterDailyWeatherList.toString()}")
                    // Emit the filtered weather data
                    _weatherHourlyStateFlow.value = ApiStateForcast.Success(filteredWeatherList)
                    _weatherDailyStateFlow.value = ApiStateForcast.Success(filterDailyWeatherList)
                }
        }
    }

    fun isSharedPreferencesContains(key : String,activity: Activity) : Boolean{
        val sharedPreferences = activity.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }
    fun getDataFromSharedPref() : Pair<Double,Double>{
        return repo.getCoordFromSharedPref()
    }
    @SuppressLint("SuspiciousIndentation")
    fun addSelected(activity: Activity){
        val sharedPreferences = activity.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
            editor.putBoolean("selected", true)
            editor.apply()
    }
     fun saveData(key: String, value: Double,activity: Activity) {
        val sharedPreferences =activity.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value.toFloat())
        editor.apply() // or editor.commit() for synchronous saving
        Log.i("TAG", "saveData: $key == $value ")
    }
    fun saveData(key: String, value: String) {
        repo.setStringFromSharedPref(key,value)
    }
    fun getStringFromSharedPref(key : String) : String?{
        return repo.getStringFromSharedPref(key)
    }
    fun removeFromSharedPref(key : String){
        repo.removeFromSharedPref(key)
    }

}