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
import com.example.weatherproject.view.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view.view_model.home.HomeFragmentViewModelFactory
import com.example.weatherproject.R
import com.example.weatherproject.view.view_model.home.REQUEST_LOCATION_CODE
import com.example.weatherproject.model.WeatherLocalDataSource
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.WeatherRemoteDataSource
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        desc = view.findViewById(R.id.precipitationText)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        val navIcon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.baseline_menu_24
        ) // Your drawer icon drawable
        navIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white)) // Change color

        // Set the custom icon to toolbar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        // Optional: Handle the drawer icon click
        toolbar?.setHomeButtonEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        allFactory = HomeFragmentViewModelFactory(
            WeatherRepository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.service), WeatherLocalDataSource()
            )
        )
        viewModel = ViewModelProvider(this, allFactory).get(HomeFragmentViewModel::class.java)
       // viewModel.getCurrentWeather(10.99, 44.34)

        viewModel.weather.observe(viewLifecycleOwner) { w ->
            Log.i("TAG", "onHomeFragment: ${w?.wind?.deg}")
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onStart() {
        super.onStart()
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        if(sharedPreferences.contains(KEY)){
            if(sharedPreferences.contains("lon")){
               var lon =  sharedPreferences.getFloat("lon", 0.0f)
               var lat =  sharedPreferences.getFloat("lat", 0.0f)
               // viewModel.getCurrentWeather(lat.toDouble(),lon.toDouble())
            }
        }else{
            val editor = sharedPreferences.edit()
            editor.putBoolean(KEY, true)
            editor.apply()
            showLocationDialog()
        }



    }
    private fun showLocationDialog() {
        val options = arrayOf("Use GPS", "Enter Location Manually")

        AlertDialog.Builder(this.requireContext())
            .setTitle("Choose Location Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Use GPS
                        if(checkPermissions()){
                            if(isLocationEnabled()){
                                getFreshLocation()
                            }else{
                                enableLocationServices()
                            }
                        }else{
                            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
                                REQUEST_LOCATION_CODE
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
    private fun getFreshLocation(){
        Log.i("TAG", "getFreshLocation: ")
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply { setPriority(Priority.PRIORITY_HIGH_ACCURACY) }.build(),
            object : LocationCallback() {
                @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                override fun onLocationResult(p0: LocationResult) {
                    Log.i("TAG", "onLocationResult: ")
                    super.onLocationResult(p0)
                    //Log.i("TAG", "onLocationResult: ${p0.locations.toString()}")
                    longituteValue = p0.locations.get(0).longitude
                    lattitudeValue = p0.locations.get(0).latitude
                    geoCoder = Geocoder(this@HomeFragment.requireContext())
                    Log.i("TAG", "onLocationResult: long = ${longituteValue } --- lat = ${lattitudeValue}")
                }

            }, Looper.myLooper()

        )
    }

}