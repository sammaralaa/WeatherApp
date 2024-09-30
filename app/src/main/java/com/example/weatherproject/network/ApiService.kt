package com.example.weatherproject.network
import com.example.weatherproject.model.WeatherForcastResponse
import com.example.weatherproject.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") lang:String,
        @Query("units") unit : String,
        @Query("appid") apiKey: String): WeatherResponse

    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") lang: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): WeatherForcastResponse


    @GET("forecast")
    suspend fun getDailyForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("lang") lang: String,
        @Query("cnt") count: Int,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>

}
object RetrofitHelper {

    val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    val service = retrofitInstance.create(ApiService::class.java)
}