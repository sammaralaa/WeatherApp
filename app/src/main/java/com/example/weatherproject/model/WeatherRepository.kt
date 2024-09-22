package com.example.weatherproject.model

import android.util.Log
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.WeatherRemoteDataSource

class WeatherRepository(private var remoteDataSource: WeatherRemoteDataSource,private var localDataAource: WeatherLocalDataSource,private  var sharedDataSource: SharedDataSource) {

    companion object{
        var instance : WeatherRepository ? = null

        fun getInstance(remote : WeatherRemoteDataSource, local : WeatherLocalDataSource , sharedDataSource: SharedDataSource) : WeatherRepository{
            return instance?: synchronized(this){
                val temp = WeatherRepository(remote,local,sharedDataSource)
                instance=temp
                temp
            }
        }


    }

    suspend fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit : String) : WeatherResponse?{
        return remoteDataSource.getCurrentWeather(lat,lon,lang,unit)
    }
    fun getStringFromSharedPref(key : String) : String?{
        return sharedDataSource.getStringFromSharedPref(key)
    }
    fun setStringFromSharedPref(key: String, value: String) {
       sharedDataSource.setStringFromSharedPref(key,value)
    }

    }