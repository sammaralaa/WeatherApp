package com.example.weatherproject

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class utilitis {

    fun setAppLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.createConfigurationContext(config)
        }
    }



}
fun isWifiConnected(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentDateTime(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
    Log.i("TAG", "getCurrentDateTime: ")
}
@RequiresApi(Build.VERSION_CODES.O)
fun areSameDay(dateTimeString1: String, dateTimeString2: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime1 = LocalDateTime.parse(dateTimeString1, formatter)
    val dateTime2 = LocalDateTime.parse(dateTimeString2, formatter)
    return dateTime1.toLocalDate() == dateTime2.toLocalDate()
}

@RequiresApi(Build.VERSION_CODES.O)
fun isMidnight(dateTimeString: String): Boolean {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)

    return dateTime.toLocalTime().toString() == "00:00"
}

