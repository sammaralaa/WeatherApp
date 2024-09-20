package com.example.weatherproject.model

import com.example.weatherproject.network.WeatherRemoteDataSource

class WeatherRepository(private var remoteDataSource: WeatherRemoteDataSource,private var localDataAource: WeatherLocalDataSource) {

    companion object{
        var instance : WeatherRepository ? = null

        fun getInstance(remote : WeatherRemoteDataSource, local : WeatherLocalDataSource) : WeatherRepository{
            return instance?: synchronized(this){
                val temp = WeatherRepository(remote,local)
                instance=temp
                temp
            }
        }


    }

    suspend fun getCurrentWeather(lat: Double, lon: Double) : List<Weather>?{
        return remoteDataSource.getCurrentWeather(lat,lon)
    }
    }