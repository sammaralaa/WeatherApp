package com.example.weatherproject.view.favorite

import com.example.weatherproject.model.WeatherModel

interface OnRemoveFavClickListener {
    fun removeFromFav(weather : WeatherModel)
}