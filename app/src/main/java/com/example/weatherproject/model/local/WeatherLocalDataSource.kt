package com.example.weatherproject.model.local

import android.util.Log
import com.example.weatherproject.db.WeatherDao
import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(var dao : WeatherDao) {

    fun getAllWeathers() : Flow<List<WeatherModel>>{
        return dao.getAll()
    }
    fun insertWeather(weatherModel: WeatherModel){
        var l = dao.insert(weatherModel)
        Log.i("TAG", "insertWeather: $l from weatherLocalDataSource")
    }
    fun deleteWeather(weatherModel: WeatherModel){
        dao.delete(weatherModel)
    }

}