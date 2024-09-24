package com.example.weatherproject.view.favorite

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherproject.model.WeatherModel

class WeatherDiffutil  :  DiffUtil.ItemCallback<WeatherModel>() {
    override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
        return oldItem == newItem
    }
}