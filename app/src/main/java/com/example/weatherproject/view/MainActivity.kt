package com.example.weatherproject.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherproject.R
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var intent : Intent = Intent(this@MainActivity, HomeActivity::class.java)
       startActivity(intent)
    }
}