package com.example.weatherproject.view.home.alerts

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.databinding.FragmentAlertItemBinding
import com.example.weatherproject.databinding.FragmentAlertsBinding
import com.example.weatherproject.model.AlarmData
import com.example.weatherproject.view.home.daily_forcast.ForcastDiffutil
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AlertsAdapter(var onRemoveAlertListener: OnRemoveAlertListener) : ListAdapter<AlarmData, AlertsAdapter.ViewHolder>(
    AlertsDiffutil()
) {
    lateinit var binding: FragmentAlertItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentAlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = getItem(position)
        holder.binding.alertName.text = "${currentWeather.date}  ${currentWeather.time}"

        holder.binding.removeImg.setOnClickListener{
            onRemoveAlertListener.removeAlert(currentWeather)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayOfWeek(dateTimeString: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val dateTime = LocalDateTime.parse(dateTimeString, formatter)
        val dayOfWeek: DayOfWeek = dateTime.dayOfWeek
        return dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }.substring(0,3)
    }

    class ViewHolder(var binding: FragmentAlertItemBinding): RecyclerView.ViewHolder(binding.root){
    }
}