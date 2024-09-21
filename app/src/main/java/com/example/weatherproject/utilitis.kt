package com.example.weatherproject

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

class utilitis {

    fun setAppLocale(languageCode: String, context: Context) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        // For Android 8.0 (API level 26) and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.createConfigurationContext(config)
        }
    }

}