package com.example.weatherproject.model.repo

import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Coord
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.OfflineWeather
import com.example.weatherproject.model.Rain
import com.example.weatherproject.model.Sys
import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.model.local.IWeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.ISharedDataSource
import com.example.weatherproject.network.remote.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeRepo () : IWeatherRepository {
    private val weatherData = mutableListOf<WeatherModel>()
    private var shouldReturnError = false

    private var alarmData = mutableListOf<AlarmData>()


    val coord = Coord(lon = 56.78, lat = 12.34)
    val weatherList = listOf(
        Weather(id = 800, main = "Clear", description = "clear sky", icon = "01d")
    )
    val main = Main(temp = 20.0, feels_like = 19.0, temp_min = 18.0, temp_max = 22.0, pressure = 1012, humidity = 60,0,0)
    val wind = Wind(speed = 3.1, deg = 200,0.0)
    val clouds = Clouds(all = 0)
    val sys = Sys(type = 1, id = 1234, country = "US", sunrise = 1618317040, sunset = 1618361500)
    private val fakeWeatherResponse = WeatherResponse(
        coord = coord,
        weather = weatherList,
        base = "stations",
        main = main,
        visibility = 10000,
        wind = wind,
        clouds = clouds,
        dt = 1618317040,
        sys = sys,
        timezone = -14400,
        id = 5128581,
        name = "New York",
        cod = 200
    )
    var fakeForcast = WeatherForcastModel(11,main,weatherList,clouds,wind,122,52.44, rain = Rain(52), dt_txt = "2024-09-29 12:00:00",sys=sys)
    var fakeForcast2 = WeatherForcastModel(11,main,weatherList,clouds,wind,122,52.44, rain = Rain(52), dt_txt = "2024-09-29 00:00:00",sys=sys)
    private val fakeWeatherForecast = listOf(fakeForcast,fakeForcast2)
    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }
    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<WeatherResponse> {
        return if (shouldReturnError) {
            flow { throw Exception("Error fetching weather") }
        } else {
            flowOf(fakeWeatherResponse)
        }
    }

    override suspend fun getForcastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        unit: String
    ): Flow<List<WeatherForcastModel>> {
        return if (shouldReturnError) {
            flow { throw Exception("Error fetching weather") }
        } else {
            flowOf(fakeWeatherForecast)
        }
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

    override fun isisSharedPreferencesContains(key: String): Boolean {
        TODO("Not yet implemented")
    }

    override fun saveData(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun addSelected() {
        TODO("Not yet implemented")
    }

    override suspend fun getAllAlerts(): Flow<List<AlarmData>> {
        if(alarmData == null){
            alarmData = mutableListOf()
        }
        return flowOf( alarmData)
    }

    override suspend fun inserAlerts(alert: AlarmData) {
        alarmData.add(alert)
    }

    override suspend fun deleteAlert(alert: AlarmData) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertById(alertId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertByWorkId(alertId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun getOfflineWeathers(): Flow<List<OfflineWeather>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOffline(weather: OfflineWeather) {
        TODO("Not yet implemented")
    }
}