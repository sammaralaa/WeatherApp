package com.example.weatherproject.model.local

import android.util.Log
import com.example.weatherproject.db.AlertDao
import com.example.weatherproject.db.OfflineDao
import com.example.weatherproject.db.WeatherDao
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.OfflineWeather
import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private var dao : WeatherDao,private var alertDao : AlertDao,private var offlineDao : OfflineDao) : IWeatherLocalDataSource {

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

    override suspend fun getAllAlerts() : Flow<List<AlarmData>>{
        return alertDao.getAllAlerts()
    }
    override suspend fun inserAlerts(alert: AlarmData){
        var l = alertDao.insertAlert(alert)
        Log.i("TAG", "insertAlert: $l from weatherLocalDataSource")
    }

    override suspend fun deleteAlertById(alertId: String?) {
        alertDao.deleteAlertById(alertId)
    }

    override suspend fun deleteAlert(alert: AlarmData){
        alertDao.deleteAlert(alert)
    }

    override suspend fun deleteAlertByWorkId(alertId: String?) {
        alertDao.deleteAlertByWorkerId(alertId)
    }
    override suspend fun getAllOffline() : Flow<List<OfflineWeather>>{
        return offlineDao.getAllOffData()
    }
    override suspend fun inserOffline(data: OfflineWeather){
        var l = offlineDao.insertAlert(data)
    }
}