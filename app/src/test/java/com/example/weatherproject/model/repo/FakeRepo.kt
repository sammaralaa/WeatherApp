package com.example.weatherproject.model.repo

import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.local.IWeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.ISharedDataSource
import com.example.weatherproject.network.remote.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo () : IWeatherRepository {
    private val weatherData = mutableListOf<WeatherModel>()

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<WeatherResponse> {
        return flowOf()
    }

    override suspend fun getForcastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<List<WeatherForcastModel>> {
        return flowOf(emptyList())
    }

    override fun getStringFromSharedPref(key: String): String? {
        return " "
    }

    override fun setStringFromSharedPref(key: String, value: String) {
        TODO("Not yet implemented")
    }

    override fun removeFromSharedPref(key: String) {
        TODO("Not yet implemented")
    }

    override fun getCoordFromSharedPref(): Pair<Double, Double> {
        TODO("Not yet implemented")
    }

    override suspend fun getLocalWeathers(): Flow<List<WeatherModel>> {
          return flowOf(weatherData)
    }

    override suspend fun insertWeather(weatherModel: WeatherModel) {
       weatherData.add(weatherModel)
    }

    override suspend fun deleteWeather(weatherModel: WeatherModel) {
        weatherData.remove(weatherModel)
    }
}