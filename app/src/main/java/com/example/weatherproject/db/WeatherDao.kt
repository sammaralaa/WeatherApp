package com.example.weatherproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.OfflineWeather
import com.example.weatherproject.model.WeatherModel
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather_data")
    fun getAll(): Flow<List<WeatherModel>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(weather: WeatherModel): Long
    @Delete
    fun delete(weather: WeatherModel): Int

}
@Dao
interface AlertDao {
    @Query("SELECT * FROM alert_data")
    fun getAllAlerts(): Flow<List<AlarmData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlert(alarm: AlarmData): Long

    @Query("DELETE FROM alert_data WHERE workerId = :alertWorkId")
    fun deleteAlertByWorkerId(alertWorkId: String?)

    @Delete
    fun deleteAlert(alarm: AlarmData): Int

    @Query("DELETE FROM alert_data WHERE id = :alertId")
    fun deleteAlertById(alertId: String?)
}

@Dao
interface OfflineDao{
    @Query("SELECT * FROM offline_data")
    fun getAllOffData(): Flow<List<OfflineWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlert(data: OfflineWeather): Long
}