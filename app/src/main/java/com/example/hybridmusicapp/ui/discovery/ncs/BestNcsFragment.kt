package com.example.hybridmusicapp.ui.discovery.ncs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hybridmusicapp.databinding.FragmentBestNcsBinding

class BestNcsFragment : Fragment() {
    private lateinit var binding: FragmentBestNcsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentBestNcsBinding.inflate(inflater, container, false)
        return binding.root
    }
}