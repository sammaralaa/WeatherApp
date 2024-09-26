package com.example.weatherproject.view.home.hourly_forcast

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherproject.databinding.FragmentForcastHourlyItemBinding
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.view.home.daily_forcast.ForcastDiffutil


class ForcastHourlyAdapter: ListAdapter<WeatherForcastModel, ForcastHourlyAdapter.ViewHolder>(
    ForcastDiffutil()
) {
    lateinit var binding: FragmentForcastHourlyItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentForcastHourlyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = getItem(position)
        holder.binding.forcastTemp.text = currentWeather.main.temp.toString()
        holder.binding.forcastHour.text = currentWeather.dt_txt.substring(11,16)
        Glide.with(binding.forcastImg.context)
            .load("https://openweathermap.org/img/wn/${currentWeather.weather.get(0).icon}.png")
            .into(binding.forcastImg)
    }


    class ViewHolder(var binding: FragmentForcastHourlyItemBinding): RecyclerView.ViewHolder(binding.root){
    }
}