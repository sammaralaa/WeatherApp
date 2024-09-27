package com.example.weatherproject.view.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentWeatherItemLayoutBinding
import com.example.weatherproject.model.WeatherModel

class WeatherFavAdapter(var onFavClickListener: OnFavClickListener,var onRemoveFavClickListener: OnRemoveFavClickListener) : ListAdapter<WeatherModel, WeatherFavAdapter.ViewHolder>(WeatherDiffutil()) {
    lateinit var binding: FragmentWeatherItemLayoutBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = FragmentWeatherItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentWeather = getItem(position)
          holder.binding.cardName.text = currentWeather.name
          holder.binding.cardLat.text = currentWeather.lat.toString()
          holder.binding.cardLon.text = currentWeather.lon.toString()
          holder.binding.card.setOnClickListener{
                onFavClickListener.showWeather(currentWeather.lat,currentWeather.lon)
          }
        holder.binding.removeImg.setOnClickListener{
            onRemoveFavClickListener.removeFromFav(currentWeather)
        }
    }

    class ViewHolder(var binding: FragmentWeatherItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
    }
}