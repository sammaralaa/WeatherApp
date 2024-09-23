package com.example.weatherproject.model.shared_preferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedDataSource(override var sharedPreference: SharedPreferences) : ISharedDataSource {

    override fun getStringFromSharedPref(key : String) : String?{
        return sharedPreference.getString(key,"")
    }
    override fun setStringFromSharedPref(key: String, value: String) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
        Log.i("TAG", "saveData: $key == $value ")
    }
}