package com.example.weatherproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable

class HomeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        val navIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_menu_24) // Your drawer icon drawable
        navIcon?.setTint(ContextCompat.getColor(requireContext(), R.color.white)) // Change color

        // Set the custom icon to toolbar
        toolbar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)

        // Optional: Handle the drawer icon click
        toolbar?.setHomeButtonEnabled(true)
        toolbar?.setHomeAsUpIndicator(navIcon)
    }
}