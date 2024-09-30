package com.example.weatherproject.view.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentFavoriteBinding
import com.example.weatherproject.db.WeatherDataBase
import com.example.weatherproject.isWifiConnected
import com.example.weatherproject.model.WeatherModel
import com.example.weatherproject.model.repo.WeatherRepository
import com.example.weatherproject.model.local.WeatherLocalDataSource
import com.example.weatherproject.model.shared_preferences.SharedDataSource
import com.example.weatherproject.network.RetrofitHelper
import com.example.weatherproject.network.remote.WeatherRemoteDataSource
import com.example.weatherproject.view_model.favorite.FavFragmentViewModel
import com.example.weatherproject.view_model.favorite.FavFragmentViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavoriteFragment : Fragment(),OnFavClickListener,OnRemoveFavClickListener{
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
            WeatherLocalDataSource(WeatherDataBase.getInstance(requireContext()).getWeatherDao(),WeatherDataBase.getInstance(requireContext()).getAlertDao(),WeatherDataBase.getInstance(requireContext()).getOfflineDao()),
            SharedDataSource(requireActivity().getSharedPreferences("MySharedPrefs", Context.MODE_PRIVATE))
        ))
        favViewModel = ViewModelProvider(this, favFactory).get(FavFragmentViewModel::class.java)
        favViewModel.getAllLocalWeather()
        favAdapter = WeatherFavAdapter(this,this)
        mLayoutManager = GridLayoutManager(requireContext(),1)
        recyclerView.apply {
            adapter = favAdapter
            layoutManager = mLayoutManager
        }
        favViewModel.weather.observe(viewLifecycleOwner , Observer { weather ->
            if (weather != null && weather.isNotEmpty()) {
                favAdapter.submitList(weather)
                favAdapter.notifyDataSetChanged()
                binding.emptyFavorits.visibility = View.INVISIBLE
                binding.noFavTxt.visibility = View.INVISIBLE
                binding.favRecycler.visibility = View.VISIBLE
            } else {
                binding.emptyFavorits.visibility = View.VISIBLE
                binding.noFavTxt.visibility = View.VISIBLE
                Log.i("TAG", "FavoriteFragment No weather data available.")
            }
        } )
    }

    override fun showWeather(weather: WeatherModel) {
        Log.i("SharedViewModelTest", "showWeather: $weather")
        if(isWifiConnected(requireContext())){
            val bundle = Bundle().apply {
                putString("unite", weather.unite)
                putString("lang", weather.lang)
                putDouble("lat", weather.lat)
                putDouble("lon", weather.lon)
            }
            findNavController().navigate(R.id.action_favoriteFragment_to_favDetailsFragment,bundle)
        }
        else{
            Toast.makeText(requireContext(), "There is no connection", Toast.LENGTH_LONG).show()
        }

    }

    override fun removeFromFav(weather: WeatherModel) {
        favViewModel.deleteWeather(weather)
        favAdapter.notifyDataSetChanged()
    }
}