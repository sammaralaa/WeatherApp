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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val  REQUEST_LOCATION_CODE = 2001
class HomeFragmentViewModel(private val repo : WeatherRepository) : ViewModel() {
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> = _weather

    private val _hourlyWeather = MutableLiveData<WeatherResponse?>()
    val hourlyWeather: LiveData<WeatherResponse?> = _hourlyWeather
     var w :WeatherResponse? = null
    var wind : Wind? = null

    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String) {

       viewModelScope.launch(Dispatchers.IO){
            w = repo.getCurrentWeather(lat,lon,lang,unit)
           withContext(Dispatchers.Main){
               _weather.postValue(w)
           }
           Log.i("TAG", "viewModel: ${w?.wind?.deg}")
        }
    }

    fun getHourlyWeather(lat: Double, lon: Double,lang : String,unit:String) {

        viewModelScope.launch(Dispatchers.IO){
            w = repo.getHourlyWeather(lat,lon,lang,unit)
            withContext(Dispatchers.Main){
                _weather.postValue(w)
            }
            Log.i("TAG", "viewModel: ${w?.wind?.deg}")
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
}