package com.example.weatherproject.model.shared_preferences

import android.content.SharedPreferences

interface ISharedDataSource {
    var sharedPreference: SharedPreferences
    fun getStringFromSharedPref(key: String): String?
    fun setStringFromSharedPref(key: String, value: String)
}