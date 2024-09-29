package com.example.weatherproject.model
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_data")
data class WeatherModel(@PrimaryKey val name: String,
                         val  lat : Double,
                         val  lon : Double,
                         val lang : String,
                         var unite : String,
    )

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

data class City(
    val id : Int,
    val name : String,
    val coord: Coord,
    val country : String,
    val population : Int,
    val timezone : Int,
    val sunrise : Int,
    val sunset : Int
)
data class WeatherForcastModel(
    var dt : Int,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility : Int,
    val pop : Double,
    val rain : Rain,
    val sys: Sys,
    val dt_txt : String
)

@Entity(tableName = "alert_data")
data class AlarmData(
    @PrimaryKey(autoGenerate = true)var id : Int =0,
    var time: Long,
    var date: Long,
    var type : String,
    var message : String,
    var workerId : String
)

@Entity(tableName = "offline_data")
data class OfflineWeather(
    @PrimaryKey var id:Int,
    var description : String,
    var temp: Double,
    var pressure: Int,
    var humidity: Int,
    var name: String,
    var speed : Double,
    var all : Int,
    //days
    var dt_txt1: String,
    var tempD1: Double,
    var iconD1: String,
    var dt_txt2: String,
    var tempD2: Double,
    var iconD2: String,
    var dt_txt3: String,
    var tempD3: Double,
    var iconD3: String,
    var dt_txt4: String,
    var tempD4: Double,
    var iconD4: String,
    var dt_txt5: String,
    var tempD5: Double,
    var iconD5: String,
)