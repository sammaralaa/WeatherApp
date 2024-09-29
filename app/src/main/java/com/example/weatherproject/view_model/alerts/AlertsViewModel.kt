package com.example.weatherproject.view_model.alerts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.repo.IWeatherRepository
import com.example.weatherproject.network.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertsViewModel (var repository: IWeatherRepository) : ViewModel(){

    private var _AlertItem : MutableLiveData<List<AlarmData>> = MutableLiveData<List<AlarmData>>()
    val AlertItem  : LiveData<List<AlarmData>> = _AlertItem

    private val _weatherStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val weatherStateFlow: StateFlow<ApiState> = _weatherStateFlow

    init {
        getAllAlerts()
    }
    fun getAllAlerts(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllAlerts().collect{weathers->
                withContext(Dispatchers.Main){
                    _AlertItem.postValue(weathers)
                }
                Log.i("TAG", "getAllLocalWeather: ")
            }
        }
    }
    fun insertAlert(alarm : AlarmData){
        viewModelScope.launch(Dispatchers.IO){
            repository.inserAlerts(alarm)
            getAllAlerts()
        }
    }
    fun deleteAlert(alert: AlarmData){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlert(alert)
            getAllAlerts()
        }
    }
    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String) {
        viewModelScope.launch {
            repository.getCurrentWeather(lat,lon,lang,unit)
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
    fun getDataFromSharedPref() : Pair<Double,Double>{
        return repository.getCoordFromSharedPref()
    }
    fun getStringFromSharedPref(key : String) : String?{
        return repository.getStringFromSharedPref(key)
    }
}