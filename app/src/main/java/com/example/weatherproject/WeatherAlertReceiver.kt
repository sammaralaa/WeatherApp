package com.example.weatherproject

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.weatherproject.view.AlertsFragment

class WeatherAlertReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val alertType = intent?.getStringExtra("ALERT_TYPE")

        if (alertType == "NOTIFICATION") {
            showNotification(context)
        } else if (alertType == "ALARM") {
            playAlarmSound(context)
        }
    }

    private fun showNotification(context: Context?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationIntent = Intent(context, AlertsFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, "weather_alert_channel")
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentTitle("Weather Alert")
            .setContentText("Weather Alert is active!")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun playAlarmSound(context: Context?) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, alarmSound)
        ringtone.play()
    }

}