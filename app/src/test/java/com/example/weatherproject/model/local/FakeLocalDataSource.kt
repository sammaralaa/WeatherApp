package com.example.weatherproject.model.local

import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeLocalDataSource(var weatherData: MutableList<WeatherModel>? = mutableListOf()) : IWeatherLocalDataSource{
        private val weatherFlow = MutableStateFlow<List<WeatherModel>>(emptyList())

        override suspend fun getAllWeathers(): Flow<List<WeatherModel>> {
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
//class FakeLocalDataSource( var weatherData: MutableList<WeatherModel> = mutableListOf()) : IWeatherLocalDataSource {
//    private val weatherFlow = MutableStateFlow<List<WeatherModel>>(weatherData)
//
//    override suspend fun getAllWeathers(): Flow<List<WeatherModel>> {
//        // Emit the current weather data
//        return weatherFlow
//    }
//
//    override suspend fun insertWeather(weatherModel: WeatherModel) {
//        weatherData.add(weatherModel)
//        // Update the flow to notify observers of the change
//        weatherFlow.value = ArrayList(weatherData)
//    }
//
//    override suspend fun deleteWeather(weatherModel: WeatherModel) {
//        weatherData.remove(weatherModel)
//        // Update the flow to notify observers of the change
//        weatherFlow.value = ArrayList(weatherData)
//    }
//}
