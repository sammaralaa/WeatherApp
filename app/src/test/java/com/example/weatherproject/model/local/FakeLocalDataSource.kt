package com.example.weatherproject.model.local

import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(var weatherData: MutableList<WeatherModel>? = mutableListOf()) : IWeatherLocalDataSource{
        private val weatherFlow = MutableStateFlow<List<WeatherModel>>(emptyList())

        override suspend fun getAllWeathers(): Flow<List<WeatherModel>> {
//            weatherData?.let { return flowOf(ArrayList(it)) }
//            return flowOf(emptyList())
            return flow {
                weatherData?.let { emit(ArrayList(it)) } ?: emit(emptyList())
            }
        }

        override suspend fun insertWeather(weatherModel: WeatherModel) {
            weatherData?.add(weatherModel)
        }

        override suspend fun deleteWeather(weatherModel: WeatherModel) {
            weatherData?.remove(weatherModel)
        }
    }