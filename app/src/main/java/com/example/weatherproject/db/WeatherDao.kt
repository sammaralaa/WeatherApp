package com.example.weatherproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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