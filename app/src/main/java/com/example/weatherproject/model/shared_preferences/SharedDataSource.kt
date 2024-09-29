package com.example.weatherproject.model.shared_preferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedDataSource( var sharedPreference: SharedPreferences) : ISharedDataSource {

    override fun getStringFromSharedPref(key : String) : String?{
        return sharedPreference.getString(key,"")
    }
    override fun setStringFromSharedPref(key: String, value: String) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
        Log.i("TAG", "saveData: $key == $value ")
    }

    override fun removeFromSharedPref(key: String) {
        val editor = sharedPreference.edit()
        editor.remove(key).apply()
    }
    override fun getDataFromSharedPref() : Pair<Double,Double>{
        var lon =  sharedPreference.getFloat("lon", 0.0f)
        var lat =  sharedPreference.getFloat("lat", 0.0f)
        return Pair(lon.toDouble(),lat.toDouble())
    }

    override fun addSelected() {
        val editor = sharedPreference.edit()
        editor.putBoolean("selected", true)
        editor.apply()
    }

    override fun saveData(key: String, value: Double) {
        val editor = sharedPreference.edit()
        editor.putFloat(key, value.toFloat())
        editor.apply()
    }

    override fun isSharedPreferencesContains(key: String): Boolean {
        return sharedPreference.contains(key)
    }

}