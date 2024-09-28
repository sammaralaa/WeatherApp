package com.example.weatherproject.view.favorite

import com.example.weatherproject.model.WeatherModel

interface OnFavClickListener {
    fun showWeather(weatherModel: WeatherModel)
}