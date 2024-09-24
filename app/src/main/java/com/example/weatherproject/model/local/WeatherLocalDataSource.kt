package com.example.weatherproject.model.local

import android.util.Log
import com.example.weatherproject.db.WeatherDao
import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private var dao : WeatherDao) : IWeatherLocalDataSource {

    override suspend fun getAllWeathers() : Flow<List<WeatherModel>>{
        return dao.getAll()
    }
    override suspend fun insertWeather(weatherModel: WeatherModel){
        var l = dao.insert(weatherModel)
        Log.i("TAG", "insertWeather: $l from weatherLocalDataSource")
    }
    override suspend fun deleteWeather(weatherModel: WeatherModel){
        dao.delete(weatherModel)
    }

}