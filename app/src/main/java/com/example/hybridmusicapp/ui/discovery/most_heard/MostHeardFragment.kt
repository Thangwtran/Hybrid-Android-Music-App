package com.example.hybridmusicapp.ui.discovery.most_heard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentMostHeardBinding

class MostHeardFragment : Fragment() {
    private lateinit var binding: FragmentMostHeardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMostHeardBinding.inflate(inflater,container,false)
        return binding.root
    }

}