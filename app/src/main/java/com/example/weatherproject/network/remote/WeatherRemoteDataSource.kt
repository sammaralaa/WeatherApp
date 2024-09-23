package com.example.weatherproject.network.remote

import android.util.Log
import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.Wind
import com.example.weatherproject.network.ApiService

class WeatherRemoteDataSource(var apiObj : ApiService) : IWeatherRemoteDataSource {
    val API_KEY = "7f6dd0097b5662feed4455238a1321a5"
    //var loc : String = ""


    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang : String, unit :String): WeatherResponse? {
        val response = apiObj.getCurrentWeather(lat, lon, lang ,unit,API_KEY)
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.i("TAG", "getCurrentWeather: onFailure")
            null
        }
    }


}