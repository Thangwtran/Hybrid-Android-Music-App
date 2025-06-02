package com.example.hybridmusicapp.ui.home.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.databinding.FragmentArtistDetailBinding


class ArtistDetailFragment : Fragment() {
   private lateinit var binding: FragmentArtistDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentArtistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
}