package com.example.weatherproject.view.favorite

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentFavoriteBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.model.WeatherRepository
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import com.example.weatherproject.view_model.home.HomeFragmentViewModel
import com.example.weatherproject.view_model.home.HomeFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavoriteFragment : Fragment(),OnFavClickListener{
    lateinit var binding : FragmentFavoriteBinding
    lateinit var fab : FloatingActionButton
    lateinit var recyclerView: RecyclerView
    lateinit var favViewModel : FavFragmentViewModel
    lateinit var favFactory : FavFragmentViewModelFactory
    lateinit var favAdapter: WeatherFavAdapter
    private lateinit var mLayoutManager: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =  FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab = binding.fab
        recyclerView = binding.favRecycler
        fab.setOnClickListener {
           findNavController().navigate(R.id.action_favoriteFragment_to_newMapFragment)
        }
        favFactory = FavFragmentViewModelFactory(
            WeatherRepository.getInstance(
            WeatherRemoteDataSource(RetrofitHelper.service),
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        favViewModel = ViewModelProvider(this, favFactory).get(FavFragmentViewModel::class.java)

        favAdapter = WeatherFavAdapter(this)
        mLayoutManager = GridLayoutManager(requireContext(),2, RecyclerView.VERTICAL, false)
        recyclerView.apply {
            adapter = favAdapter
            layoutManager = mLayoutManager
        }
        favViewModel.weather.observe(viewLifecycleOwner , Observer { weather ->
            favAdapter.submitList(weather)
            favAdapter.notifyDataSetChanged()
        } )
    }

    override fun showWeather(lat: Double, lon: Double) {

    }
}