package com.example.weatherproject.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapFragment : Fragment() {
    lateinit var _binding: FragmentMapBinding
    lateinit var mapView: MapView
   // private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding= FragmentMapBinding.inflate(inflater, container, false)
        return _binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize osmdroid configuration
        Configuration.getInstance().load(requireContext(), requireContext().getSharedPreferences("osmdroid", 0))

        // Initialize the MapView
         mapView = _binding.map // Access the map view using view binding
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(8.0)

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
                    saveData("lon",lon)
                    saveData("lat",lat)
                    Log.i("TAG", "onViewCreated: Selected Location: Lat: $lat, Lon: $lon")
                    findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
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
        val marker = Marker(_binding.map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Selected Location"
        marker.setOnMarkerClickListener { m, mapView ->
            // Show marker info when clicked
            InfoWindow.closeAllInfoWindowsOn(mapView)
            m.showInfoWindow()
            true
        }
        _binding.map.overlays.add(marker)
        _binding.map.invalidate() // Refresh the map
    }
    override fun onResume() {
        super.onResume()
        _binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        _binding.map.onPause()
    }
    private fun saveData(key: String, value: Double) {
        val sharedPreferences = requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value.toFloat())
        editor.apply() // or editor.commit() for synchronous saving
        Log.i("TAG", "saveData: $key == $value ")
    }
/*  FavoriteFragmentDirections.ActionFavoriteFragmentToFavMealDetailsFragment action = FavoriteFragmentDirections.actionFavoriteFragmentToFavMealDetailsFragment(meal);
        Navigation.findNavController(this.getView()).navigate(action);*/
}