package com.example.weatherproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherproject.model.WheatherModel

@Database(entities = arrayOf(WheatherModel::class), version = 1)
abstract class WeatherDataBase : RoomDatabase() {
        //abstract fun getProductsDao(): ProductsDao
        companion object{
            private var Instance : WeatherDataBase? = null

            fun getInstance (context: Context): WeatherDataBase {
                return  Instance ?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext, WeatherDataBase::class.java,
                        "weather_database"
                    ).build()
                    Instance =instance

                    instance
                }
            }
        }
    }
