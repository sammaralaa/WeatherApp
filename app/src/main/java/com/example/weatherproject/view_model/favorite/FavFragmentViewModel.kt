package com.example.weatherproject.view_model.favorite

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.areSameDay
import com.example.weatherproject.getCurrentDateTime
import com.example.weatherproject.isMidnight
import com.example.weatherproject.model.WeatherModel
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
import kotlinx.coroutines.withContext

class FavFragmentViewModel(var repository: IWeatherRepository) : ViewModel(){

    private var _weather : MutableLiveData<List<WeatherModel>> = MutableLiveData<List<WeatherModel>>()
    val weather  : LiveData<List<WeatherModel>> = _weather

    private var _FavItemweather : MutableLiveData<WeatherModel> = MutableLiveData<WeatherModel>()
    val FavItemweather  : LiveData<WeatherModel>get() = _FavItemweather

    private val _weatherStateFlowFav = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherStateFlowFav: StateFlow<ApiState> = _weatherStateFlowFav

    private val _weatherHourlyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherHourlyStateFlow: StateFlow<ApiStateForcast> = _weatherHourlyStateFlow

    private val _weatherDailyStateFlow = MutableStateFlow<ApiStateForcast>(ApiStateForcast.Loading)
    val weatherDaileStateFlow: StateFlow<ApiStateForcast> = _weatherDailyStateFlow

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
            val formattedNumber = String.format("%.2f", lat)
            val lat = formattedNumber.toDouble()

            val formattedNumber2 = String.format("%.2f", lon)
            val lon = formattedNumber2.toDouble()

            var lang = repository.getStringFromSharedPref("lang")
            var unite = repository.getStringFromSharedPref("units")
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
    fun setSharedData(weatherModel: WeatherModel){
        _FavItemweather.postValue(weatherModel)
        _FavItemweather.apply {
            value = weatherModel
        }
//        Log.i("SharedViewModelTest", "setSharedData: ${_FavItemweather.value?.name}")
    }


    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String) {
        viewModelScope.launch {
            repository.getCurrentWeather(lat,lon,lang,unit)
                .onStart {
                    _weatherStateFlowFav.value = ApiState.Loading
                }

                .catch { e ->
                    _weatherStateFlowFav.value = ApiState.Failure(e)
                }

                .collect { weather ->
                    _weatherStateFlowFav.value = ApiState.Success(weather)

//                    Log.i("TAG", "getCurrentWeather: viewModel success ${weather.toString()}")
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForcastWeather(lat: Double, lon: Double, lang : String, unit:String) {
        viewModelScope.launch(Dispatchers.IO){
            repository.getForcastWeather(lat,lon,lang,unit)
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
//                    Log.i("TAG", "getForcastWeather: hourly ${filteredWeatherList.toString()}")
//                    Log.i("TAG", "getForcastWeather: Daily ${filterDailyWeatherList.toString()}")
                    // Emit the filtered weather data
                    _weatherHourlyStateFlow.value = ApiStateForcast.Success(filteredWeatherList)
                    _weatherDailyStateFlow.value = ApiStateForcast.Success(filterDailyWeatherList)
                }
        }
    }



}