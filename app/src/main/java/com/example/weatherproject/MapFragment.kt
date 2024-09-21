package com.example.weatherproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.weatherproject.databinding.FragmentMapBinding
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

class MapFragment : Fragment() {
    lateinit var _binding: FragmentMapBinding
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
        val mapView = _binding.map // Access the map view using view binding
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(15.0)

//        // Set a default location
//        val startPoint = GeoPoint(44.34, 10.99) // Example coordinates
//        mapView.controller.setCenter(startPoint)

        // Set a touch listener for the map
        mapView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                // Get GeoPoint from the touch location
                val geoPoint: GeoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                val lat = geoPoint.latitude
                val lon = geoPoint.longitude

                // Add a marker at the selected location
                addMarker(geoPoint)

                // Log or use the selected coordinates
                println("Selected Location: Lat: $lat, Lon: $lon")
            }
            false
        }
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

}