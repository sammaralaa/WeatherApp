package com.example.weatherproject.model.shared_preferences

import android.content.SharedPreferences

class FakeSharedDataSource() :ISharedDataSource {
    override fun getStringFromSharedPref(key: String): String? {
       return ""
    }

    override fun setStringFromSharedPref(key: String, value: String) {

    }

    override fun removeFromSharedPref(key: String) {

    }

    override fun getDataFromSharedPref(): Pair<Double, Double> {
       return Pair(0.0,0.0)
    }

    override fun addSelected() {
        TODO("Not yet implemented")
    }

    override fun saveData(key: String, value: Double) {
        TODO("Not yet implemented")
    }

    override fun isSharedPreferencesContains(key: String): Boolean {
        TODO("Not yet implemented")
    }
}