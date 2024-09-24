package com.example.weatherproject.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view_model.home.HomeFragmentViewModelFactory
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentHomeBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.WeatherResponse
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.utilitis
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

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
    //lateinit var temp : TextView
//    lateinit var dt : TextView
//    lateinit var humidity : TextView
//    lateinit var wind : TextView
//    lateinit var cloud : TextView
//    lateinit var pressure : TextView
//    lateinit var city : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allFactory = HomeFragmentViewModelFactory(WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        viewModel = ViewModelProvider(this, allFactory).get(HomeFragmentViewModel::class.java)
        // viewModel.getCurrentWeather(10.99, 44.34)

        updateConfig()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        desc = binding.precipitationText
        //temp = binding.temperatureText
//        dt = binding.maxMinTemperature
//        humidity = binding.humidityValuetxt
//        wind = binding.windValuetxt
//        cloud = binding.cloudValuetxt
//        pressure = binding.pressureValuetxt
//        city = binding.cityNametxt
        val toolbar = (activity as AppCompatActivity).supportActionBar
        val navIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_menu_24)
        navIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white)) // Change color

        // Set the custom icon to toolbar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        // Optional: Handle the drawer icon click
        toolbar?.setHomeButtonEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        super.onStart()
        if(viewModel.isSharedPreferencesContains(KEY,requireActivity())){
            if(viewModel.isSharedPreferencesContains("lon",requireActivity())){
               var lon =  viewModel.getDataFromSharedPref(requireActivity()).first
               var lat =  viewModel.getDataFromSharedPref(requireActivity()).second
                updateConfig()
                viewModel.getCurrentWeather(lat.toDouble(),lon.toDouble(),lang,unite)
                viewModel.weather.observe(viewLifecycleOwner) { w ->
                    updateUI(w)
                }
            }
        }else{
            viewModel.addSelected(requireActivity())
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
                        if(checkPermissions()){
                            Log.i("TAG", "showLocationDialog: checkPermissions")
                            if(isLocationEnabled()){
                                Log.i("TAG", "showLocationDialog: isLocationEnabled")
                                getFreshLocation()
                                Log.i("TAG", "isLocationEnabled: ")
//                                viewModel.getCurrentWeather(lattitudeValue,longituteValue)
//                                viewModel.weather.observe(viewLifecycleOwner) { w ->
//                                    updateUI(w)
//                                }
                            }else{
                                Log.i("TAG", "showLocationDialog: isLocationEnabled else")
                                enableLocationServices()
                                getFreshLocation()
                            }
                        }else{
                            Log.i("TAG", "showLocationDialog: requestPermissions else")
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
                // Permission granted
                if (isLocationEnabled()) {
                    getFreshLocation()
                } else {
                    enableLocationServices()
                    getFreshLocation()
                }
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this.requireContext(), "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun checkPermissions(): Boolean {
        Log.i("TAG", "checkPermissions: ")
        return (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }
    private fun enableLocationServices(){
        Log.i("TAG", "enableLocationServices: ")
        Toast.makeText(this.requireContext(),"Turn on location",Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnabled():Boolean{
        Log.i("TAG", "isLocationEnabled: ")
        //old methode
        val locationManager : LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
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
                        viewModel.saveData("lon", longituteValue, requireActivity())
                        viewModel.saveData("lat", lattitudeValue, requireActivity())

                        // Update weather based on the retrieved location
                        updateConfig()
                        Log.i("TAG", "Location retrieved: lat=$lattitudeValue, lon=$longituteValue")
                        viewModel.getCurrentWeather(lattitudeValue, longituteValue,lang,unite)

                        // Observe the weather data and update the UI
                        viewModel.weather.observe(this@HomeFragment) { weatherResponse ->
                            updateUI(weatherResponse)
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



    @RequiresApi(Build.VERSION_CODES.O)
    fun updateUI(response : WeatherResponse?){
        binding.precipitationText.text = response?.weather?.get(0)?.description
        binding.temperatureText.text = response?.main?.temp?.toString()
        binding.maxMinTemperature.text = viewModel.getCurrentDateTime()
        binding.humidityValuetxt.text = response?.main?.humidity?.toString()
        binding.windValuetxt.text = response?.wind?.speed?.toString()
        binding.cloudValuetxt.text = response?.clouds?.all.toString()
        binding.pressureValuetxt.text = response?.main?.pressure.toString()
        binding.cityNametxt.text = response?.name
    }
    fun updateConfig(){
        if(viewModel.isSharedPreferencesContains("lang",requireActivity())){
            lang = viewModel.getStringFromSharedPref("lang").toString()
            u.setAppLocale(lang,requireContext())
        }else{
            lang = "en"
            u.setAppLocale(lang,requireContext())
        }
        if(viewModel.isSharedPreferencesContains("units",requireActivity())){
            unite = viewModel.getStringFromSharedPref("units").toString()
        }else{
            unite = "standard"
        }
    }
}