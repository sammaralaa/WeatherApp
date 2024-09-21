package com.example.weatherproject.network

import com.example.weatherproject.model.CloudsResponse
import com.example.weatherproject.model.CurrentWeatherResponse
import com.example.weatherproject.model.MainResponse
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.WheatherModel
import com.example.weatherproject.model.WindResponse
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
        @Query("appid") apiKey: String): Response<WeatherResponse>

    @GET("main")
    suspend fun getMain(@Query("lat") latitude: Double,
                        @Query("lon") longitude: Double,

                        @Query("appid") apiKey: String):Response<MainResponse>

    @GET("wind")
    suspend fun getWind(@Query("lat") latitude: Double,
                        @Query("lon") longitude: Double,
                        @Query("appid") apiKey: String):Response<WindResponse>

    @GET("clouds")
    suspend fun getClouds(@Query("lat") latitude: Double,
                        @Query("lon") longitude: Double,
                        @Query("appid") apiKey: String):Response<CloudsResponse>

    @GET("name")
    suspend fun getCityName(@Query("lat") latitude: Double,
                        @Query("lon") longitude: Double,
                        @Query("appid") apiKey: String):Response<String>
}
object RetrofitHelper {

    val BASE_URL: String = "https://api.openweathermap.org/data/2.5/"

    // Create OkHttpClient to add logging or other interceptors
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)  // Add the OkHttp client here
        .baseUrl(BASE_URL)
        .build()

    val service = retrofitInstance.create(ApiService::class.java)
}