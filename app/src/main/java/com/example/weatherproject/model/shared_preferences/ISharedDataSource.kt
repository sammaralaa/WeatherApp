package com.example.weatherproject.model.shared_preferences

import android.app.Activity
import android.content.SharedPreferences

interface ISharedDataSource {
    fun getStringFromSharedPref(key: String): String?
    fun setStringFromSharedPref(key: String, value: String)
    fun removeFromSharedPref(key : String)
    fun getDataFromSharedPref(): Pair<Double, Double>
    fun addSelected()
    fun saveData(key: String, value: Double)
    fun isSharedPreferencesContains(key : String) : Boolean
}