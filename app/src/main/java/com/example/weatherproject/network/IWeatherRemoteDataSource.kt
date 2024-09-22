package com.example.weatherproject.network

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

    suspend fun getMain(lat: Double, lon: Double): Main?
    suspend fun getWind(lat: Double, lon: Double): Wind?
    suspend fun getClouds(lat: Double, lon: Double): Clouds?
    suspend fun getCityName(lat: Double, lon: Double): String?
}