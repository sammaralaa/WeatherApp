package com.example.weatherproject.model
import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_data")
data class WheatherModel(  val coord: Coord,
                           val weather: List<Weather>,
                           val base: String,
                           val main: Main,
                           val visibility: Int,
                           val wind: Wind,
                           val rain: Rain?,
                           val clouds: Clouds,
                           val dt: Long,
                           val sys: Sys,
                           val timezone: Int,
                           val id: Int,
                           val name: String,
                           val cod: Int
    ){}

data class Clouds(var all : Int)

data class Main(val temp: Double,
                val feels_like: Double,
                val temp_min: Double,
                val temp_max: Double,
                val pressure: Int,
                val humidity: Int,
                val sea_level: Int?,
                val grnd_level: Int?)

data class Rain(@SerializedName("1h")var oneHour : Int)
data class Weather(var id : Int,
                   var main : String,
                   var description : String,
                   var icon : String)

data class Wind(var speed : Double,
                var deg : Int,
                var gust : Double)
data class Coord(var lon : Double,var lat:Double)
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)