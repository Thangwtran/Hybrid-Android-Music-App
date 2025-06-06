package com.example.hybridmusicapp.ui.library.recent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hybridmusicapp.R
import com.example.hybridmusicapp.databinding.FragmentRecentHeardBinding

class RecentHeardFragment : Fragment() {
    private lateinit var binding: FragmentRecentHeardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentRecentHeardBinding.inflate(inflater,container,false)
        return binding.root
    }
}