package com.example.weatherproject.view.home.alerts

import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.weatherproject.R
import com.example.weatherproject.db.WeatherDao
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.local.IWeatherLocalDataSource
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.IWeatherRemoteDataSource
import com.example.weatherproject.network.remote.WeatherRemoteDataSource

class AlertWorker(appContext: Context, workerParams: WorkerParameters) : CoroutineWorker(appContext, workerParams) {

    val context: Context = appContext
    lateinit var message : String
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var repository: WeatherRepository

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            sharedPreferences = context.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)

            repository =
                WeatherRepository.getInstance(
                    WeatherRemoteDataSource(RetrofitHelper.service),
                    WeatherLocalDataSource(WeatherDataBase.getInstance(applicationContext).getWeatherDao(),
                        WeatherDataBase.getInstance(applicationContext).getAlertDao(),WeatherDataBase.getInstance(applicationContext).getOfflineDao()),
                    SharedDataSource(applicationContext.getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)))


            val id  = inputData.getInt("id",0)
            message = inputData.getString("message").toString()
            val type = inputData.getString("type")
            sendNotification(message,type)
            repository.deleteAlertById(id.toString())
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(message : String, type : String?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "12-34"

        val channel =
            NotificationChannel(channelId,"notificationChannel" ,NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

            if(type =="n") {
                val notification = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.weather)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build()
                notificationManager.notify(1, notification)
            } else {
                val intent = Intent(applicationContext, AlarmService::class.java)
                intent.putExtra("message",message)
                context.startService(intent)
            }
    //    }

    }

}