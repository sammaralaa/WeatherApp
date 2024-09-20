package com.example.weatherproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherproject.model.WheatherModel
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.WheatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var animation : LottieAnimationView
    lateinit var remoteDataSource: WheatherRemoteDataSource
   // lateinit var Wheather : List<WheatherModel>
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // animation = findViewById(R.id.lottieBackground)
       // animation.playAnimation()
//        remoteDataSource = WheatherRemoteDataSource(RetrofitHelper.service)
//        GlobalScope.launch(Dispatchers.IO) {
//
//         var  Wheather= remoteDataSource.getCurrentWeather()
//            Log.i("TAG", "onCreate: ${Wheather?.main?.pressure}")
//            Log.i("TAG", "onCreate: ${Wheather?.name}")
//            Log.i("TAG", "onCreate: ${Wheather?.clouds?.all}")
//            Log.i("TAG", "onCreate: ${Wheather?.timezone}")
//            Log.i("TAG", "onCreate: ${Wheather?.wind?.deg}")
//        }
        var intent : Intent = Intent(this@MainActivity,HomeActivity::class.java)
       startActivity(intent)
    }
}