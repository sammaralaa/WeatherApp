package com.example.weatherproject.view

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.example.weatherproject.R
import com.example.weatherproject.WeatherAlertReceiver


class AlertsFragment : Fragment() {
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alerts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val btnSetAlert = view.findViewById<Button>(R.id.btnSetWeatherAlert)
        val btnStopAlert = view.findViewById<Button>(R.id.btnStopAlert)
        val etDuration = view.findViewById<EditText>(R.id.etDuration)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

        btnSetAlert.setOnClickListener {
            val duration = etDuration.text.toString().toLongOrNull() ?: 0L
            val selectedType = when (radioGroup.checkedRadioButtonId) {
                R.id.rbNotification -> "NOTIFICATION"
                R.id.rbAlarmSound -> "ALARM"
                else -> "NOTIFICATION"
            }
            setWeatherAlert(duration, selectedType)
        }

        btnStopAlert.setOnClickListener {
            cancelWeatherAlert()
            stopAlarm(requireContext())
        }
    }

    private fun setWeatherAlert(duration: Long, alertType: String) {
        // Intent to BroadcastReceiver
        val intent = Intent(requireContext(), WeatherAlertReceiver::class.java).apply {
            putExtra("ALERT_TYPE", alertType)
        }

        pendingIntent = PendingIntent.getBroadcast(
            requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Schedule the alarm
        val triggerTime = SystemClock.elapsedRealtime() + duration * 60 * 1000 // Duration in minutes
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, pendingIntent)

        Toast.makeText(requireContext(), "Weather alert set", Toast.LENGTH_SHORT).show()
    }

    private fun cancelWeatherAlert() {
        if (::pendingIntent.isInitialized) {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(requireContext(), "Weather alert canceled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Weather Alerts"
            val descriptionText = "Channel for weather alerts"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("weather_alert_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    private fun stopAlarm(context: Context?) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        if (ringtone.isPlaying) {
            ringtone.stop()
        }
    }
}

