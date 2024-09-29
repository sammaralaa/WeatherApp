package com.example.weatherproject.network.remote

import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource(private var weatherResponse: WeatherResponse? = null,
                           private var forecastResponse: List<WeatherForcastModel>? = null) : IWeatherRemoteDataSource {
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<WeatherResponse> {
        return flow {
            weatherResponse?.let {
                emit(it)
            } ?: throw Exception("No weather data available")
        }.catch { e ->
            throw e
        }
    }

    override suspend fun getForcastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<List<WeatherForcastModel>> {
        return flow {
            forecastResponse?.let {
                emit(it)
            } ?: throw Exception("No forecast data available")
        }.catch { e ->
            throw e
        }
    }
}