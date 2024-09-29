package com.example.weatherproject.view.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentNewMapBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow
import java.net.URL


class NewMapFragment : Fragment() {

    lateinit var binding: FragmentNewMapBinding
    lateinit var mapView: MapView
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var viewModel: FavFragmentViewModel
    private lateinit var favFactory: FavFragmentViewModelFactory
    lateinit var geocoder: Geocoder

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewMapBinding.inflate(inflater, container, false)
        favFactory = FavFragmentViewModelFactory(
            WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao(),WeatherDataBase.getInstance(requireContext()).getAlertDao(),WeatherDataBase.getInstance(requireContext()).getOfflineDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        viewModel = ViewModelProvider(this, favFactory).get(FavFragmentViewModel::class.java)
        return binding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize osmdroid configuration
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))
        geocoder = Geocoder(requireContext())
        // Initialize the MapView
        mapView = binding.newMap // Access the map view using view binding
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(8.0)

        autoCompleteTextView = binding.newAutoCompleteSearch
        setupAutoCompleteSearch()

//        // Set a default location
//        val startPoint = GeoPoint(44.34, 10.99) // Example coordinates
//        mapView.controller.setCenter(startPoint)

        addMapClickListener()
    }
    private fun addMapClickListener() {
        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                // Handle single tap here
                p?.let {
                    // val geoPoint: GeoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                    val lat = p.latitude
                    val lon = p.longitude

                    // Add a marker at the selected location
                    addMarker(p)
                    Log.i("TAG", "onViewCreated: Selected Location: Lat: $lat, Lon: $lon")
                    showDialog(p)
                }
                return true // return true if event is handled
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        // Create an overlay to handle map events
        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(overlayEvents)
    }
    private fun addMarker(point: GeoPoint) {
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Selected Location"
        marker.setOnMarkerClickListener { m, mapView ->
            // Show marker info when clicked
            InfoWindow.closeAllInfoWindowsOn(mapView)
            showDialog(point)
            true
        }
        mapView.overlays.add(marker)
        mapView.invalidate() // Refresh the map
    }
    private fun removeMarker(point : GeoPoint){
        val marker = Marker(mapView)
        marker.position = point
        mapView.overlays.remove(marker)
    }
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    @SuppressLint("MissingInflatedId")
    private fun showDialog(p: GeoPoint){
        val option = false
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog_layout, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        val alertDialog = builder.create()
        var yes = dialogView.findViewById<Button>(R.id.yes_btn)
        var no = dialogView.findViewById<Button>(R.id.no_btn)
        dialogView.findViewById<TextView>(R.id.dialogMessage).text= getString(R.string.favConf)
        yes.setOnClickListener{
           // findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
            addToFav(p)
            alertDialog.dismiss()
            findNavController().navigate(R.id.action_newMapFragment_to_favoriteFragment)
        }
        no.setOnClickListener{
            Toast.makeText(requireContext(), "You clicked No", Toast.LENGTH_SHORT).show()
            alertDialog.dismiss()
        }
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
    private fun setupAutoCompleteSearch() {
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        autoCompleteTextView.setAdapter(adapter)

        // Listen for text changes
        autoCompleteTextView.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                if (!s.isNullOrEmpty()) {
                    fetchLocationSuggestions(s.toString()) { suggestions ->
                        adapter.clear()
                        adapter.addAll(suggestions)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if (!s.isNullOrEmpty()) {
                    fetchLocationSuggestions(s.toString()) { suggestions ->
                        adapter.clear()
                        adapter.addAll(suggestions)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    fetchLocationSuggestions(s.toString()) { suggestions ->
                        adapter.clear()
                        adapter.addAll(suggestions)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })

        // Listen for item selection
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedLocation = parent.getItemAtPosition(position).toString()
            fetchCoordinatesForLocation(selectedLocation) { lat, lon ->
                // Move the map to the selected location
                val geoPoint = GeoPoint(lat, lon)
                mapView.controller.setCenter(geoPoint)
                mapView.controller.setZoom(15.0)

                // Optionally, add a marker at the selected location
                addMarker(geoPoint)
            }
        }
    }
    private fun fetchLocationSuggestions(query: String, callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            try {
                val url = "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=5"
                val response = URL(url).readText()
                val jsonArray = JSONArray(response)

                val suggestions = mutableListOf<String>()
                for (i in 0 until jsonArray.length()) {
                    val location = jsonArray.getJSONObject(i)
                    val displayName = location.getString("display_name")
                    suggestions.add(displayName)
                }

                // Call the callback function with the suggestions
                withContext(Dispatchers.Main) {
                    callback(suggestions)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Function to fetch coordinates for a selected location
    private fun fetchCoordinatesForLocation(query: String, callback: (Double, Double) -> Unit) {
        GlobalScope.launch {
            try {
                val url = "https://nominatim.openstreetmap.org/search?q=$query&format=json&limit=10"
                val response = URL(url).readText()
                val jsonArray = JSONArray(response)

                if (jsonArray.length() > 0) {
                    val location = jsonArray.getJSONObject(0)
                    val lat = location.getDouble("lat")
                    val lon = location.getDouble("lon")

                    // Call the callback function with the coordinates
                    withContext(Dispatchers.Main){
                        callback(lat, lon)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun addToFav(p: GeoPoint){
       var name =  geocoder.getFromLocation(p.latitude,p.longitude,5)?.get(0)?.subAdminArea
        viewModel.insertWeather(p.latitude,p.longitude,name?:"")
    }

}

