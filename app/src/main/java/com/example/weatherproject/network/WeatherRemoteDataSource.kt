package com.example.weatherproject.network

import android.util.Log
import com.example.weatherproject.model.Clouds
import com.example.weatherproject.model.CurrentWeatherResponse
import com.example.weatherproject.model.Main
import com.example.weatherproject.model.Weather
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.WheatherModel
import com.example.weatherproject.model.Wind
import retrofit2.Response

class WeatherRemoteDataSource(var apiObj : ApiService) {
    val API_KEY = "7f6dd0097b5662feed4455238a1321a5"

    suspend fun getCurrentWeather(lat: Double, lon: Double): WeatherResponse? {
        val response = apiObj.getCurrentWeather(lat, lon, API_KEY)
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.i("TAG", "getCurrentWeather: onFailure")
            null
        }
    }

    suspend fun getMain(lat: Double, lon: Double) : Main?{
        val response = apiObj.getMain(lat,lon,API_KEY)
        return if (response.isSuccessful) {
            response.body()?.main
        } else {
            Log.i("TAG", "getMain: onFailure")
            null
        }
    }

    suspend fun getWind(lat: Double, lon: Double) : Wind?{
        val response = apiObj.getWind(lat,lon,API_KEY)
        return if (response.isSuccessful) {
            response.body()?.wind
        } else {
            Log.i("TAG", "getWind: onFailure")
            null
        }
    }

    suspend fun getClouds(lat: Double, lon: Double) : Clouds?{
        val response = apiObj.getClouds(lat,lon,API_KEY)
        return if (response.isSuccessful) {
            response.body()?.clouds
        } else {
            Log.i("TAG", "getClouds: onFailure")
            null
        }
    }

    suspend fun getCityName(lat: Double, lon: Double) : String?{
        val response = apiObj.getCityName(lat,lon,API_KEY)
        return if (response.isSuccessful) {
            response.body()
        } else {
            Log.i("TAG", "getCityName: onFailure")
            null
        }
    }

}