package com.example.weatherproject

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherproject.databinding.FragmentFavDetailsBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.ApiStateForcast
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view.home.daily_forcast.ForcastItemAdapter
import com.example.weatherproject.view.home.hourly_forcast.ForcastHourlyAdapter
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavDetailsFragment : Fragment() {

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

    private lateinit var hourlyBar : ProgressBar
    private lateinit var homeBar : ProgressBar
    private lateinit var dailyBar : ProgressBar

    var lat : Double = 0.0
    var lon : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favFactory = FavFragmentViewModelFactory(
            WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao(),WeatherDataBase.getInstance(requireContext()).getAlertDao(),WeatherDataBase.getInstance(requireContext()).getOfflineDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        favViewModel = ViewModelProvider(this, favFactory).get(FavFragmentViewModel::class.java)
//        lat = arguments?.getDouble("lat") ?: 0.0
//        lon lon= arguments?.getDouble("lon") ?: 0.0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            adapter=hourlyAdapter
            layoutManager=hourlyLayoutManager
        }
        homeBar = binding.homeBar
        hourlyBar = binding.hourlyBar
        dailyBar = binding.dailyBar
        val unite = arguments?.getString("unite")
        val lang = arguments?.getString("lang")
        val lon = arguments?.getDouble("lon")
        val lat = arguments?.getDouble("lat")
        getCurrentWeather(lat?:0.0, lon?:0.0, lang?:"en", unite?:"metric")
        getForcastWeather(lat?:0.0, lon?:0.0, lang?:"en", unite?:"metric")
//                getForcastWeather(weather.lat, weather.lon, weather.lang, "metric")
//        Log.i("SharedViewModelTest", "onViewCreated: ${favViewModel.FavItemweather.value?.name}")
//        favViewModel.FavItemweather.observe(viewLifecycleOwner , Observer { weather ->
//            if (weather != null) {
//                Log.i("SharedViewModelTest", "onViewCreated: favDetails ${weather}")
//
//            } else {
//                Log.i("SharedViewModelTest", "onViewCreated: No weather data available")
//            }
//
//        })
    }
    @SuppressLint("NewApi")
    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String){
        lifecycleScope.launch {
            favViewModel.getCurrentWeather(lat,lon,lang,unit)
            Log.i("TAG", "getCurrentWeather: frament ")

            favViewModel.weatherStateFlowFav.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> {
                        homeBar.visibility = View.VISIBLE
                        Log.i("TAG", "Loading products...")
                    }
                    is ApiState.Success -> {
                        homeBar.visibility = View.GONE
                        updateUI(state.data)
                    }
                    is ApiState.Failure -> {
                        homeBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Failed to load weather, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", "Error loading weather: ${state.msg}")
                    }

                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NewApi")
    fun getForcastWeather(lat: Double, lon: Double,lang : String,unit:String){
        lifecycleScope.launch {
            favViewModel.getForcastWeather(lat,lon,lang,unit)
            Log.i("TAG", "getCurrentWeather: frament ")
            favViewModel.weatherHourlyStateFlow.collectLatest { state ->
                when (state) {
                    is ApiStateForcast.Loading -> {
                        hourlyBar.visibility = View.VISIBLE
                        Log.i("TAG", "Loading products...")
                    }
                    is ApiStateForcast.Success -> {
                        hourlyBar.visibility = View.GONE
                        hourlyAdapter.submitList(state.data)
                        Log.i("TAG", "getForcastWeather: Hourly ---> ${state.data.size}")
                    }
                    is ApiStateForcast.Failure -> {
                        hourlyBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Failed to load weather, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("TAG", "Error loading weather: ${state.msg}")
                    }

                }
            }

        }
        lifecycleScope.launch {
            favViewModel.weatherDaileStateFlow.collectLatest { state->
                when(state){
                    is ApiStateForcast.Failure -> {
                        dailyBar.visibility = View.GONE
                    }
                    ApiStateForcast.Loading ->{
                        dailyBar.visibility = View.VISIBLE
                    }
                    is ApiStateForcast.Success -> {
                        dailyBar.visibility = View.GONE
                        dailyAdapter.submitList(state.data)
                        dailyAdapter.notifyDataSetChanged()
                        Log.i("TAG", "getForcastWeather: Daily ---> ${state.data.size}")
                    }
                }
            }
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