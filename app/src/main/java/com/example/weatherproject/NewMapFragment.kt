package com.example.weatherproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherproject.databinding.FragmentMapBinding
import com.example.weatherproject.databinding.FragmentNewMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.widget.Autocomplete


class NewMapFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentNewMapBinding
    private lateinit var map: GoogleMap
    private val AUTOCOMPLETE_REQUEST_CODE = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewMapBinding.inflate(inflater, container, false)

        // Find the map fragment and set the map ready callback
        val mapFragment = childFragmentManager.findFragmentById(R.id.new_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize the Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "YOUR_GOOGLE_MAPS_API_KEY")
        }

        // Return the binding root
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Move the camera to an initial location (e.g., New York City)
        val initialLocation = LatLng(40.7128, -74.0060)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(data!!)
            place.latLng?.let {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
                map.addMarker(MarkerOptions().position(it).title(place.name))
            }

            Toast.makeText(requireContext(), "Place: ${place.name}", Toast.LENGTH_LONG).show()
        }
    }
}

