package com.example.weatherproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherproject.R
import com.example.weatherproject.databinding.FragmentFavoriteBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FavoriteFragment : Fragment() {
    lateinit var binding : FragmentFavoriteBinding
    lateinit var fab : FloatingActionButton
    lateinit var recyclerView: RecyclerView
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
        fab.setOnClickListener {
           findNavController().navigate(R.id.action_favoriteFragment_to_newMapFragment)
        }
    }
}