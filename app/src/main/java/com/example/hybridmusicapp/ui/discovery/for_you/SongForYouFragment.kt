package com.example.hybridmusicapp.ui.discovery.for_you

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentSongForYouBinding

class SongForYouFragment : Fragment() {
    private lateinit var binding: FragmentSongForYouBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSongForYouBinding.inflate(inflater,container,false)
        return binding.root
    }
}