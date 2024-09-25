package com.example.weatherproject.network

import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherResponse

sealed class ApiState {
    class Success(val data: WeatherResponse): ApiState()
    class Failure(val msg: Throwable): ApiState()
    object Loading: ApiState()
}

sealed class ApiStateForcast {
    class Success(val data: List<WeatherForcastModel>): ApiStateForcast()
    class Failure(val msg: Throwable): ApiStateForcast()
    object Loading: ApiStateForcast()
}