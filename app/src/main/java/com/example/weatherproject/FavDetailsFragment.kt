package com.example.weatherproject

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherproject.databinding.FragmentFavDetailsBinding
import com.example.weatherproject.databinding.FragmentHomeBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view.home.daily_forcast.ForcastItemAdapter
import com.example.weatherproject.view.home.hourly_forcast.ForcastHourlyAdapter
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import com.example.weatherproject.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view_model.home.HomeFragmentViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient


class FavDetailsFragment : Fragment() {

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var favViewModel: FavFragmentViewModel
    private lateinit var favFactory: FavFragmentViewModelFactory
    lateinit var desc : TextView
    private lateinit var binding: FragmentFavDetailsBinding

    lateinit var dailyAdapter : ForcastItemAdapter
    lateinit var dailyRecyclerView: RecyclerView
    private lateinit var dailyLayoutManager: LayoutManager

    lateinit var hourlyAdapter : ForcastHourlyAdapter
    lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var hourlyLayoutManager: LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favFactory = FavFragmentViewModelFactory(
            WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        favViewModel = ViewModelProvider(this, favFactory).get(FavFragmentViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dailyRecyclerView = binding.forcastDailyRecycler
        dailyAdapter = ForcastItemAdapter()
        dailyLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        dailyRecyclerView.apply {
            adapter = dailyAdapter
            layoutManager = dailyLayoutManager
        }

        hourlyRecyclerView = binding.hourlyRecycler
        hourlyAdapter = ForcastHourlyAdapter()
        hourlyLayoutManager = LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false)
        hourlyRecyclerView.apply {
            adapter=dailyAdapter
            layoutManager=hourlyLayoutManager
        }
    }
    fun updateUI(response : WeatherResponse?){
        Log.i("TAG", "updateUI: ${response.toString()}")
        binding.precipitationText.text = response?.weather?.get(0)?.description
        binding.temperatureText.text = response?.main?.temp.toString()
//        binding.maxMinTemperature.text = viewModel.getCurrentDateTime()
//        Log.i("TAG", "updateUI: ${viewModel.getCurrentDateTime()}")
        binding.humidityValuetxt.text = response?.main?.humidity?.toString()
        binding.windValuetxt.text = response?.wind?.speed?.toString()
        binding.cloudValuetxt.text = response?.clouds?.all.toString()
        binding.pressureValuetxt.text = response?.main?.pressure.toString()
        binding.cityNametxt.text = response?.name
    }
}