package com.example.weatherproject.model.repo

import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface IWeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double, lang : String, unit : String) : Flow<WeatherResponse>
    suspend fun getForcastWeather(lat: Double, lon: Double, lang : String, unit : String) : Flow<List<WeatherForcastModel>>
    fun getStringFromSharedPref(key : String) : String?
    fun setStringFromSharedPref(key: String, value: String)
    fun removeFromSharedPref(key : String)
    fun getCoordFromSharedPref(): Pair<Double, Double>

    ////local
    suspend fun getLocalWeathers() : Flow<List<WeatherModel>>
    suspend fun insertWeather(weatherModel: WeatherModel)
    suspend fun deleteWeather(weatherModel: WeatherModel)


}