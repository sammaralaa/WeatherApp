package com.example.weatherproject.network.remote

import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): WeatherResponse?

}