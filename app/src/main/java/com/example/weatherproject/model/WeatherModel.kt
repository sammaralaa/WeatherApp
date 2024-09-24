package com.example.weatherproject.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_data")
data class WeatherModel(@PrimaryKey val name: String,
                         val  lat : Double,
                         val  lon : Double,
                         val temp: Double,
                         val pressure: Int,
                         val humidity: Int,
                         var description : String,
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