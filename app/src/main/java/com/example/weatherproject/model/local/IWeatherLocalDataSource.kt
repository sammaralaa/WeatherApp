package com.example.weatherproject.model.local

import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    fun getAllWeathers(): Flow<List<WeatherModel>>
    fun insertWeather(weatherModel: WeatherModel)
    fun deleteWeather(weatherModel: WeatherModel)
}