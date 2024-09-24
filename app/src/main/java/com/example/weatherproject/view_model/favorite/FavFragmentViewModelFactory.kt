package com.example.weatherproject.view_model.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.view_model.home.HomeFragmentViewModel

class FavFragmentViewModelFactory (private val repo: WeatherRepository) : ViewModelProvider.Factory  {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavFragmentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavFragmentViewModel(repo) as T
            }else
            {throw IllegalArgumentException("Unknown ViewModel class")}
        }
    }
