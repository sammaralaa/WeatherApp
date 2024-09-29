package com.example.weatherproject.view.home.alerts

import androidx.recyclerview.widget.DiffUtil
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.model.WeatherForcastModel

class AlertsDiffutil :  DiffUtil.ItemCallback<AlarmData>() {
    override fun areItemsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
        return oldItem.workerId == newItem.workerId
    }

    override fun areContentsTheSame(oldItem: AlarmData, newItem: AlarmData): Boolean {
        return oldItem == newItem
    }
}