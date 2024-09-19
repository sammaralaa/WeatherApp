package com.example.weatherproject.network

import com.example.weatherproject.model.WheatherResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String
    ): Response<WheatherResponse>
}
//https://api.openweathermap.org/data/2.5/weather?lat=44.34&lon=10.99&appid=
object RetrofitHelper {
    val BASE_URL : String = "https://api.openweathermap.org/"
    val retrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
    val service = retrofitInstance.create(ApiService::class.java)
}