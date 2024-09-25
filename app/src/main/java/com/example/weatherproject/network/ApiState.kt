package com.example.weatherproject.network

import com.example.weatherproject.model.WeatherModel

sealed class ApiState {
    class Success(val data: List<WeatherModel>): ApiState()
    class Failure(val msg: Throwable): ApiState()
    object Loading: ApiState()
}