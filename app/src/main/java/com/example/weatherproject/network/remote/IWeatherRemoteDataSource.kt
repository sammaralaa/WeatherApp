package com.example.weatherproject.network.remote

import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherForcastResponse
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String): Flow<WeatherResponse>
    suspend fun getForcastWeather(lat: Double,
                                  lon: Double,
                                  lang : String,
                                  unit :String): Flow<List<WeatherForcastModel>>

}