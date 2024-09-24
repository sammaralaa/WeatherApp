package com.example.weatherproject.model

import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepository(private var remoteDataSource: WeatherRemoteDataSource, private var localDataAource: WeatherLocalDataSource, private  var sharedDataSource: SharedDataSource) {

    companion object{
        var instance : WeatherRepository ? = null

        fun getInstance(remote : WeatherRemoteDataSource, local : WeatherLocalDataSource, sharedDataSource: SharedDataSource) : WeatherRepository{
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
    ////local
    suspend fun getLocalWeathers() : Flow<List<WeatherModel>>{
        return localDataAource.getAllWeathers()
    }
    suspend fun insertWeather(weatherModel: WeatherModel){
        localDataAource.insertWeather(weatherModel)
    }
    suspend fun deleteWeather(weatherModel: WeatherModel){
        localDataAource.deleteWeather(weatherModel)
    }
    }