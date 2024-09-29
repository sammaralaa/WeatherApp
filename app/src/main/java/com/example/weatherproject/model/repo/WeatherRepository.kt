package com.example.weatherproject.model.repo

import android.util.Log
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.local.IWeatherLocalDataSource
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.ISharedDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.remote.IWeatherRemoteDataSource
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow



class WeatherRepository(private var remoteDataSource: IWeatherRemoteDataSource, private var localDataAource: IWeatherLocalDataSource, private  var sharedDataSource: ISharedDataSource) :
    IWeatherRepository {

    companion object{
        var instance : WeatherRepository? = null

         fun getInstance(remote : WeatherRemoteDataSource, local : WeatherLocalDataSource, sharedDataSource: SharedDataSource) : WeatherRepository {
            return instance ?: synchronized(this){
                val temp = WeatherRepository(remote,local,sharedDataSource)
                instance =temp
                temp
            }
        }


    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang : String, unit : String) : Flow<WeatherResponse>{
        return remoteDataSource.getCurrentWeather(lat,lon,lang,unit)
    }
    override suspend fun getForcastWeather(lat: Double, lon: Double, lang : String, unit : String) : Flow<List<WeatherForcastModel>>{
        return remoteDataSource.getForcastWeather(lat,lon,lang,unit)
    }
    override fun getStringFromSharedPref(key : String) : String?{
        return sharedDataSource.getStringFromSharedPref(key)
    }
    override fun setStringFromSharedPref(key: String, value: String) {
       sharedDataSource.setStringFromSharedPref(key,value)
    }
    override fun removeFromSharedPref(key : String){
        sharedDataSource.removeFromSharedPref(key)
    }
    override fun getCoordFromSharedPref():Pair<Double,Double>{
        return sharedDataSource.getDataFromSharedPref()
    }
    ////local
    override suspend fun getLocalWeathers() : Flow<List<WeatherModel>>{
        return localDataAource.getAllWeathers()
    }
    override suspend fun insertWeather(weatherModel: WeatherModel){
        localDataAource.insertWeather(weatherModel)
    }
    override suspend fun deleteWeather(weatherModel: WeatherModel){
        localDataAource.deleteWeather(weatherModel)
    }

    override fun isisSharedPreferencesContains(key: String): Boolean {
       return sharedDataSource.isSharedPreferencesContains(key)
    }

    override fun saveData(key: String, value: Double) {
        sharedDataSource.saveData(key,value)
    }

    override fun addSelected() {
        sharedDataSource.addSelected()
    }
    override suspend fun getAllAlerts() : Flow<List<AlarmData>>{
        return localDataAource.getAllAlerts()
    }
    override suspend fun inserAlerts(alert: AlarmData){
        val l = localDataAource.inserAlerts(alert)

        Log.i("TAG", "insertAlert: $l from weatherLocalDataSource")
    }
    override suspend fun deleteAlert(alert: AlarmData){
        localDataAource.deleteAlert(alert)
    }
}