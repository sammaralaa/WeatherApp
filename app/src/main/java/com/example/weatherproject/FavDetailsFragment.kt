package com.example.weatherproject

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.weatherproject.databinding.FragmentFavDetailsBinding
import com.example.weatherproject.databinding.FragmentHomeBinding
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_details, container, false)
    }


}