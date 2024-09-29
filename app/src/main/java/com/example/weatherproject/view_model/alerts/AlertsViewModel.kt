package com.example.weatherproject.view_model.alerts

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.repo.IWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertsViewModel (var repository: IWeatherRepository) : ViewModel(){

    private var _AlertItem : MutableLiveData<List<AlarmData>> = MutableLiveData<List<AlarmData>>()
    val AlertItem  : LiveData<List<AlarmData>> = _AlertItem


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
}