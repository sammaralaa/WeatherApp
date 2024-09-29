package com.example.weatherproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherModel

@Database(entities = arrayOf(WeatherModel::class, AlarmData::class), version = 5)
abstract class WeatherDataBase : RoomDatabase() {
        abstract fun getWeatherDao(): WeatherDao
        abstract fun getAlertDao() : AlertDao
        companion object{
            private var Instance : WeatherDataBase? = null

            fun getInstance (context: Context): WeatherDataBase {
                return  Instance ?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext, WeatherDataBase::class.java,
                        "weather_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    Instance =instance

                    instance
                }
            }
        }
    }
