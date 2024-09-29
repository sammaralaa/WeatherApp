package com.example.weatherproject.view.home.alerts

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.example.weatherproject.R
import com.example.weatherproject.databinding.AlertDialogLayoutBinding
import com.example.weatherproject.databinding.CustomDialogLayoutBinding

class AlarmService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding : AlertDialogLayoutBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()

        mediaPlayer = MediaPlayer.create(this, R.raw.samsung)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        binding = AlertDialogLayoutBinding.inflate(LayoutInflater.from(this))
        overlayView = binding.root

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        )

        layoutParams.y = 8
        layoutParams.gravity = Gravity.TOP

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, layoutParams)

        binding.alertTitle.text = getString(R.string.app_name)
        binding.alertMessage.text = intent?.getStringExtra("message")
//        binding.i.setImageResource(R.drawable.ic_cloud_alarm)

        binding.dismissBtn.setOnClickListener {
            stopSelf()
        }

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "alarm_channel_id",
            "Alarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm notifications"
        }
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        if (::windowManager.isInitialized && ::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}