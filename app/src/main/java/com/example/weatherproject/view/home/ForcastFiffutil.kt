package com.example.weatherproject.view.home

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherproject.model.WeatherForcastModel

class ForcastFiffutil :  DiffUtil.ItemCallback<WeatherForcastModel>() {
    override fun areItemsTheSame(oldItem: WeatherForcastModel, newItem: WeatherForcastModel): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(oldItem: WeatherForcastModel, newItem: WeatherForcastModel): Boolean {
        return oldItem == newItem
    }
}