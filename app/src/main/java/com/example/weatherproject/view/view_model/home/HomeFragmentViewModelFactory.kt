package com.example.weatherproject.view.view_model.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherproject.model.WeatherRepository

class HomeFragmentViewModelFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeFragmentViewModel(repo) as T
        }else
        {throw IllegalArgumentException("Unknown ViewModel class")}
    }
}