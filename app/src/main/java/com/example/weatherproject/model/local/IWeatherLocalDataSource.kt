package com.example.weatherproject.model.local

import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun getAllWeathers(): Flow<List<WeatherModel>>
    suspend fun insertWeather(weatherModel: WeatherModel)
    suspend fun deleteWeather(weatherModel: WeatherModel)
    suspend fun getAllAlerts(): Flow<List<AlarmData>>
    suspend fun inserAlerts(alert: AlarmData)
    suspend fun deleteAlert(alert: AlarmData)
}