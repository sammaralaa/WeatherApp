package com.example.weatherproject.model.local

import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.OfflineWeather
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

    override suspend fun getAllAlerts(): Flow<List<AlarmData>> {
        TODO("Not yet implemented")
    }

    override suspend fun inserAlerts(alert: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertById(alertId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertByWorkId(alertId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllOffline(): Flow<List<OfflineWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun inserOffline(data: OfflineWeather) {
        TODO("Not yet implemented")
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
