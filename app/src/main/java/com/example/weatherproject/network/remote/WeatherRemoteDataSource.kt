package com.example.weatherproject.network.remote

import android.util.Log
import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherForcastResponse
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class WeatherRemoteDataSource(var apiObj : ApiService) : IWeatherRemoteDataSource {
    val API_KEY = "7f6dd0097b5662feed4455238a1321a5"

    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang : String, unit :String): Flow<WeatherResponse> = flow {
        val response = apiObj.getCurrentWeather(lat, lon, lang ,unit,API_KEY)
        emit(response)
    }.catch { e ->
        throw e
    }

    override suspend fun getForcastWeather(lat: Double, lon: Double, lang : String, unit :String)
    : Flow<List<WeatherForcastModel>> = flow {
        val response = apiObj.getForecastWeather(lat, lon, lang ,unit,API_KEY).list
        Log.i("TAG", "getForcastWeather: Remote $response")
        emit(response)
    }.catch { e ->
        throw e
    }





}