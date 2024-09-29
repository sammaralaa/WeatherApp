package com.example.weatherproject.view.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherproject.Gpslocation
import com.example.weatherproject.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view_model.home.HomeFragmentViewModelFactory
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentHomeBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.getCurrentDateTime
import com.example.weatherproject.isWifiConnected
import com.example.weatherproject.model.OfflineWeather
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.ApiState
import com.example.weatherproject.network.ApiStateForcast
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.utilitis
import com.example.weatherproject.view.home.daily_forcast.ForcastItemAdapter
import com.example.weatherproject.view.home.hourly_forcast.ForcastHourlyAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class HomeFragment : Fragment() {
    var lattitudeValue : Double = 0.0
    var longituteValue : Double =0.0
    lateinit var geoCoder : Geocoder
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var allFactory: HomeFragmentViewModelFactory
    lateinit var desc : TextView
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val KEY = "selected"
    private var lang : String = ""
    private var unite : String = ""
    private var u = utilitis()
    private lateinit var binding: FragmentHomeBinding

    lateinit var dailyAdapter : ForcastItemAdapter
    lateinit var dailyRecyclerView: RecyclerView
    private lateinit var dailyLayoutManager: LayoutManager

    lateinit var hourlyAdapter : ForcastHourlyAdapter
    lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var hourlyLayoutManager: LayoutManager

    private lateinit var hourlyBar : ProgressBar
    private lateinit var homeBar : ProgressBar

    lateinit var offlineData : OfflineWeather

    lateinit var location : Gpslocation
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allFactory = HomeFragmentViewModelFactory(
            WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao(),WeatherDataBase.getInstance(requireContext()).getAlertDao(),WeatherDataBase.getInstance(requireContext()).getOfflineDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        viewModel = ViewModelProvider(this, allFactory).get(HomeFragmentViewModel::class.java)
        updateConfig()
        location = Gpslocation(requireContext())
        geoCoder = Geocoder(requireContext())
         offlineData = OfflineWeather(1,"",0.0,5,1,"",0.0,11,"",0.0,"","",0.0,"","",0.0,"",
            "",0.0,"","",0.0,"")
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = (activity as AppCompatActivity).supportActionBar
        val navIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_menu_24)
        navIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white)) // Change color

        // Set the custom icon to toolbar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        // Optional: Handle the drawer icon click
        toolbar?.setHomeButtonEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        homeBar = binding.homeBar
        hourlyBar = binding.hourlyBar
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

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        super.onStart()
        if(viewModel.isSharedPreferencesContains(KEY)){
            updateConfig()
            if(viewModel.isSharedPreferencesContains("lon")){
               var lon =  viewModel.getDataFromSharedPref().first
               var lat =  viewModel.getDataFromSharedPref().second
                updateConfig()
                if(isWifiConnected(requireContext())){
                getCurrentWeather(lat,lon,lang,unite)
                getForcastWeather(lat,lon,lang,unite)
                    binding.dailyLinear.visibility = View.VISIBLE
                    binding.hourlyLinear.visibility = View.VISIBLE
                }else{
                    binding.dailyLinear.visibility = View.GONE
                    binding.hourlyLinear.visibility = View.GONE
                    viewModel.getAllOfflineWeather()
                    viewModel.offlineWeather.observe(viewLifecycleOwner , Observer { weather ->
                        Log.i("TAG", "onStart: $weather")
                        UpdateUiFromOffline(weather.get(0))
                    })
                }
            }else{
                if(location.checkPermissions()){
                    if(location.isLocationEnabled()){
                        getFreshLocation()
                        updateConfig()
                        if(isWifiConnected(requireContext())){
                            getCurrentWeather(lattitudeValue,longituteValue,lang,unite)
                            getForcastWeather(lattitudeValue,longituteValue,lang,unite)
                            binding.dailyLinear.visibility = View.VISIBLE
                            binding.hourlyLinear.visibility = View.VISIBLE
                        }else{
                            binding.dailyLinear.visibility = View.GONE
                            binding.hourlyLinear.visibility = View.GONE
                            viewModel.getAllOfflineWeather()
                            viewModel.offlineWeather.observe(viewLifecycleOwner , Observer { weather ->
                                UpdateUiFromOffline(weather.get(0))
                            })
                        }
                    }else{
                        location.enableLocationServices()
                        getFreshLocation()
                    }
                }else{
                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )

                }
            }
        }else{
            viewModel.addSelected()
            showLocationDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showLocationDialog() {
        val options = arrayOf("Use GPS", "Enter Location Manually")
        AlertDialog.Builder(this.requireContext())
            .setTitle("Choose Location Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Use GPS
                        if(location.checkPermissions()){
                            if(location.isLocationEnabled()){
                                getFreshLocation()
                                updateConfig()
//
                            }else{
                                location.enableLocationServices()
                                getFreshLocation()
                            }
                        }else{
                            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                LOCATION_PERMISSION_REQUEST_CODE
                            )

                        }
                    }
                    1 -> {
                        // Enter Location Manually
                        findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
                    }
                }
            }
            .create()
            .show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (location.isLocationEnabled()) {
                    getFreshLocation()
                } else {
                    location.enableLocationServices()
                    if(location.isLocationEnabled()){getFreshLocation()}
                }
            } else {
                Toast.makeText(this.requireContext(), "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }




    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        Log.i("TAG", "getFreshLocation: requesting location update")

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val locationRequest = LocationRequest.Builder(10000) // 10 seconds interval
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
            object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)

                    if (locationResult != null && locationResult.locations.isNotEmpty()) {
                        // Get the last location from the result
                        val location = locationResult.lastLocation
                        lattitudeValue = location?.latitude?:0.0
                        longituteValue = location?.longitude?:0.0

                        // Save the data in shared preferences
                        viewModel.saveData("lon", longituteValue)
                        viewModel.saveData("lat", lattitudeValue)

                        updateConfig()
                        Log.i("TAG", "Location retrieved: lat=$lattitudeValue, lon=$longituteValue")
                        if(isWifiConnected(requireContext())){
                            getCurrentWeather(lattitudeValue,longituteValue,lang,unite)
                            getForcastWeather(lattitudeValue,longituteValue,lang,unite)
                            binding.dailyLinear.visibility = View.VISIBLE
                            binding.hourlyLinear.visibility = View.VISIBLE
                        }else{
                            binding.dailyLinear.visibility = View.GONE
                            binding.hourlyLinear.visibility = View.GONE
                            viewModel.getAllOfflineWeather()
                            viewModel.offlineWeather.observe(viewLifecycleOwner , Observer { weather ->
                                UpdateUiFromOffline(weather.get(0))
                            })
                        }

                    } else {
                        Log.e("TAG", "No location detected")
                        Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    @SuppressLint("NewApi")
    fun getCurrentWeather(lat: Double, lon: Double,lang : String,unit:String){
        lifecycleScope.launch {
            updateConfig()
            viewModel.getCurrentWeather(lat,lon,lang,unit)

            viewModel.weatherStateFlow.collectLatest { state ->
                when (state) {
                    is ApiState.Loading -> {
                        homeBar.visibility = View.VISIBLE
                    }
                    is ApiState.Success -> {
                        homeBar.visibility = View.GONE
                        updateUI(state.data)
                        updateFromCurrent(state.data)
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
    @SuppressLint("NewApi")
    fun getForcastWeather(lat: Double, lon: Double,lang : String,unit:String){
        lifecycleScope.launch {
            updateConfig()
            viewModel.getForcastWeather(lat,lon,lang,unit)
            Log.i("TAG", "getCurrentWeather: frament ")
            viewModel.weatherHourlyStateFlow.collectLatest { state ->
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
            viewModel.weatherDaileStateFlow.collectLatest { state->
                when(state){
                    is ApiStateForcast.Failure -> {

                    }
                    ApiStateForcast.Loading ->{

                    }
                    is ApiStateForcast.Success -> {
                        dailyAdapter.submitList(state.data)
                        dailyAdapter.notifyDataSetChanged()
                        Log.i("TAG", "getForcastWeather: Daily ---> ${state.data.size}")
                    }
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUI(response : WeatherResponse?){
        var temp  = " °C"
        var wind  = " m/s"
        when(unite){
            "metric"->{
                temp=" °C"
                wind = " m/s"
            }
            "standard"->{
                temp=" K"
                wind = " m/s"
            }
            "imperial"->{
                temp=" °F"
                wind = " m/h"
            }
        }
        Log.i("TAG", "updateUI: ${response.toString()}")
        binding.precipitationText.text = response?.weather?.get(0)?.description
        binding.temperatureText.text = "${response?.main?.temp.toString()}$temp"
        binding.maxMinTemperature.text = getCurrentDateTime()
        Log.i("TAG", "updateUI: ${getCurrentDateTime()}")
        binding.humidityValuetxt.text = "${response?.main?.humidity?.toString()} %"
        binding.windValuetxt.text = "${response?.wind?.speed?.toString()}$wind"
        binding.cloudValuetxt.text = response?.clouds?.all.toString()
        binding.pressureValuetxt.text = response?.main?.pressure.toString()
        binding.cityNametxt.text = response?.name
    }
    fun updateConfig(){
        if(viewModel.isSharedPreferencesContains("lang")){
            lang = viewModel.getStringFromSharedPref("lang").toString()
            u.setAppLocale(lang,requireContext())
        }else{
            lang = "en"
            u.setAppLocale(lang,requireContext())
        }
        if(viewModel.isSharedPreferencesContains("units")){
            unite = viewModel.getStringFromSharedPref("units").toString()
        }else{
            unite = "metric"
        }
    }
   fun updateFromCurrent(response : WeatherResponse?){
       offlineData.name = response?.name.toString()
       offlineData.all = response?.clouds?.all?:0
       offlineData.description = response?.weather?.get(0)?.description?:""
       offlineData.temp = response?.main?.temp?:0.0
       offlineData.humidity = response?.main?.humidity?:0
       offlineData.pressure = response?.main?.pressure?:0
       offlineData.speed = response?.wind?.speed?:0.0
       viewModel.insertOfflieneWeather(offlineData)
   }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun UpdateUiFromOffline(w : OfflineWeather){
        var temp  = " °C"
        var wind  = " m/s"
        when(unite){
            "metric"->{
                temp=" °C"
                wind = " m/s"
            }
            "standard"->{
                temp=" K"
                wind = " m/s"
            }
            "imperial"->{
                temp=" °F"
                wind = " m/h"
            }
        }
        binding.precipitationText.text = w.description
        binding.temperatureText.text = "${w.temp.toString()}$temp"
        binding.maxMinTemperature.text = getCurrentDateTime()
        binding.humidityValuetxt.text = "${w.humidity?.toString()} %"
        binding.windValuetxt.text = "${w.speed?.toString()}$wind"
        binding.cloudValuetxt.text = w.all.toString()
        binding.pressureValuetxt.text = w.pressure.toString()
        binding.cityNametxt.text = w.name

    }
}