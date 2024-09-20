package com.example.weatherproject.network

import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.WheatherModel

class WheatherRemoteDataSource( var apiObj : ApiService) {

    suspend fun getCurrentWeather(): WeatherResponse? {
        val response = apiObj.getCurrentWeather(44.34, 10.99, "7f6dd0097b5662feed4455238a1321a5")
        return if (response.isSuccessful) {
            response.body()
        } else {
            null // Handle error case here
        }
    }

}