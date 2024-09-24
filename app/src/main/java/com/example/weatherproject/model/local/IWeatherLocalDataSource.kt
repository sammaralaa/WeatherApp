package com.example.weatherproject.model.local

import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun getAllWeathers(): Flow<List<WeatherModel>>
    suspend fun insertWeather(weatherModel: WeatherModel)
    suspend fun deleteWeather(weatherModel: WeatherModel)
}