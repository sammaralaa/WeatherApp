package com.example.weatherproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.WeatherLocalDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var animation : LottieAnimationView
    lateinit var remoteDataSource: WeatherRemoteDataSource
    lateinit var repo : WeatherRepository
   // lateinit var Wheather : List<WheatherModel>
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // animation = findViewById(R.id.lottieBackground)
       // animation.playAnimation()
//        remoteDataSource = WheatherRemoteDataSource(RetrofitHelper.service)
            repo = WeatherRepository(WeatherRemoteDataSource(RetrofitHelper.service),
                WeatherLocalDataSource()
            )
//        GlobalScope.launch(Dispatchers.IO) {
////
//         var  Wheather= repo.getCurrentWeather(44.34,10.99)
//            Log.i("TAG", "onCreate: ${Wheather?.get(0)?.toString()}")
////            Log.i("TAG", "onCreate: ${Wheather?.name}")
////            Log.i("TAG", "onCreate: ${Wheather?.clouds?.all}")
////            Log.i("TAG", "onCreate: ${Wheather?.timezone}")
////            Log.i("TAG", "onCreate: ${Wheather?.wind?.deg}")
//        }
        var intent : Intent = Intent(this@MainActivity,HomeActivity::class.java)
       startActivity(intent)
    }
}