package com.example.weatherproject

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherNotifyWorker(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val lat = inputData.getDouble("latitude", 0.0)
        val lon = inputData.getDouble("longitude", 0.0)

        return withContext(Dispatchers.IO) {
            try {
                val weatherRepository =
                    WeatherRemoteDataSource(RetrofitHelper.service)

                // Collecting the Flow from the repository
                weatherRepository.getCurrentWeather(lat, lon,"en","metric").collect { weatherData ->
                    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                    when (currentTime) {
                        "08:00" -> showNotification(
                            "Good Morning.",
                            "Current Temperature: ${weatherData.main.temp}°C",
                            R.drawable.cloud
                        )

                        "16:00" -> showNotification(
                            "Good Afternoon.",
                            "Current Temperature: ${weatherData.main.temp}°C",
                            R.drawable.cloud
                        )

                        "22:00" -> showNotification(
                            "Good Evening.",
                            "Current Temperature: ${weatherData.main.temp}°C",
                            R.drawable.cloud
                        )

                        else -> {
                            return@collect
                        }
                    }
                }
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }
    }


    @SuppressLint("LaunchActivityFromNotification")
    private fun showNotification(title: String, content: String, imageRes: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "WeatherChannel",
                "Weather Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Create Notification
        val notification = NotificationCompat.Builder(context, "WeatherChannel")
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentTitle(title)
            .setContentText(content)
            .setLargeIcon(context.getDrawable(imageRes)?.toBitmap())
            .setAutoCancel(true)
            .build()
        notificationManager.notify((0..1000).random(), notification)
    }
}