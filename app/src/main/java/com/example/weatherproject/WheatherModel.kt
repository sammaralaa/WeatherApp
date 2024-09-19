package com.example.weatherproject
import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "weather_data")
data class WheatherModel(var lat : Double,
                         var lon :Double,
                         @Embedded var main :Main,
                         @Embedded var wind :Wind,
                         @Embedded var rain :Rain,
                         @Embedded var clouds : Clouds,
                         @Embedded val weather: ArrayList<Weather>,
                         var visibility : Int,
                         var dt : Int,
                         var dt_iso:String,
                         var timezone:Int
    ){}

data class Clouds(var all : Int)

data class Main(var temp:Double,
                var temp_min :Double,
                var temp_max :Double,
                var feels_like :Double,
                var pressure :Int,
                var humidity :Int,
                var dew_point :Double)

data class Rain(var _3h : Int)
data class Weather(var id : Int,var main : String,var description : String,var icon : String)
data class Wind(var speed : Double,var deg : Int,var gust : Double)