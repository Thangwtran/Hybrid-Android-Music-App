package com.example.hybridmusicapp.ui.discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentDiscoveryBinding


class DiscoveryFragment : Fragment() {
    private lateinit var binding: FragmentDiscoveryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDiscoveryBinding.inflate(inflater,container,false)
        return binding.root
    }

}