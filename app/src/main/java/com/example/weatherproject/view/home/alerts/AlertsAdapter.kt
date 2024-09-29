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
import com.example.weatherproject.model.AlarmData
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

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
        holder.binding.alertName.text = formatTime(currentWeather.date)

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
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertUnixTimestampToDateTime(unixTime: Long): String {
        val instant = Instant.ofEpochSecond(unixTime)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }
    class ViewHolder(var binding: FragmentAlertItemBinding): RecyclerView.ViewHolder(binding.root){
    }

    private fun formatTime(calendar: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.getDefault())
        return sdf.format(calendar)
    }
}