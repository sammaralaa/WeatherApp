package com.example.weatherproject.view_model.alerts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel

class AlertsViewModelFactory (private val repo: WeatherRepository) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlertsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlertsViewModel(repo) as T
        }else
        {throw IllegalArgumentException("Unknown ViewModel class")}
    }
}