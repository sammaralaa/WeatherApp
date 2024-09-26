package com.example.weatherproject.view.home

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherproject.databinding.FragmentForcastItemBinding
import com.example.weatherproject.databinding.FragmentWeatherItemLayoutBinding
import com.example.weatherproject.model.WeatherForcastModel
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.view.favorite.WeatherDiffutil
import com.example.weatherproject.view.favorite.WeatherFavAdapter
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ForcastItemAdapter : ListAdapter<WeatherForcastModel, ForcastItemAdapter.ViewHolder>(ForcastDiffutil()) {
    lateinit var binding: FragmentForcastItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentForcastItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = getItem(position)
        holder.binding.forcastTemp.text = currentWeather.main.temp.toString()
        holder.binding.forcastDay.text = getDayOfWeek(currentWeather.dt_txt)
        Glide.with(binding.forcastImg.context)
            .load("https://openweathermap.org/img/wn/${currentWeather.weather.get(0).icon}.png")
            .into(binding.forcastImg)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val dayOfWeek: DayOfWeek = dateTime.dayOfWeek
        return dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0,3)
    }

    class ViewHolder(var binding: FragmentForcastItemBinding): RecyclerView.ViewHolder(binding.root){
    }
}