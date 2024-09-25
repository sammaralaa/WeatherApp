package com.example.weatherproject.view_model.home


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.ApiStateForcast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val  REQUEST_LOCATION_CODE = 2001
class HomeFragmentViewModel(private val repo : WeatherRepository) : ViewModel() {
//    private val _weather = MutableLiveData<WeatherResponse?>()
//    val weather: LiveData<WeatherResponse?> = _weather
    private val _weatherStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherStateFlow: StateFlow<ApiState> = _weatherStateFlow

    private val _weatherHourlyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherHourlyStateFlow: StateFlow<ApiStateForcast> = _weatherHourlyStateFlow

    private val _weatherDailyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherDaileStateFlow: StateFlow<ApiStateForcast> = _weatherDailyStateFlow

     var w :WeatherResponse? = null
    var wind : Wind? = null

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
                .onStart { _weatherHourlyStateFlow.value = ApiStateForcast.Loading }
                .catch { e-> _weatherHourlyStateFlow.value = ApiStateForcast.Failure(e) }

                .collect{weather ->
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyWeather(lat: Double, lon: Double, lang : String, unit:String) {
        viewModelScope.launch(Dispatchers.IO){
            repo.getForcastWeather(lat,lon,lang,unit)
                .onStart { _weatherDailyStateFlow.value = ApiStateForcast.Loading }
                .catch { e-> _weatherDailyStateFlow.value = ApiStateForcast.Failure(e) }

                .collect{weather ->
                    val filterDailyWeatherList = weather.filter { weatherItem->
                        isMidnight(weatherItem.dt_txt)
                    }
                    //Log.i("TAG", "getForcastWeather: ${filteredWeatherList.toString()}")
                    // Emit the filtered weather data

                    _weatherDailyStateFlow.value = ApiStateForcast.Success(filterDailyWeatherList)
                }
        }
    }

    fun isSharedPreferencesContains(key : String,activity: Activity) : Boolean{
        val sharedPreferences = activity.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.contains(key)
    }
    fun getDataFromSharedPref(activity: Activity) : Pair<Double,Double>{
        val sharedPreferences = activity.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        var lon =  sharedPreferences.getFloat("lon", 0.0f)
        var lat =  sharedPreferences.getFloat("lat", 0.0f)
        return Pair(lon.toDouble(),lat.toDouble())
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDateTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return current.format(formatter)
        Log.i("TAG", "getCurrentDateTime: ")
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun areSameDay(dateTimeString1: String, dateTimeString2: String): Boolean {
        // Define the formatter matching your date-time string pattern
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the strings to LocalDateTime objects
        val dateTime1 = LocalDateTime.parse(dateTimeString1, formatter)
        val dateTime2 = LocalDateTime.parse(dateTimeString2, formatter)

        // Compare the dates (ignoring the time part)
        return dateTime1.toLocalDate() == dateTime2.toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isMidnight(dateTimeString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)

        // Check if the time is exactly 00:00:00
        return dateTime.toLocalTime().toString() == "00:00"
    }
}