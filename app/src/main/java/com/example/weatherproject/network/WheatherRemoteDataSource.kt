package com.example.weatherproject.network

import com.example.weatherproject.model.WheatherModel

class WheatherRemoteDataSource( var apiObj : ApiService) {

    suspend fun getCurrentWheather() : List<WheatherModel>{
        val response = apiObj.getCurrentWeather(44.34,10.99,"7f6dd0097b5662feed4455238a1321a5")
        return response.body()?.wheatherModel ?: listOf()
    }
}